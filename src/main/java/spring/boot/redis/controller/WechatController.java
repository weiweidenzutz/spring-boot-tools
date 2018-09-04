package spring.boot.redis.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import spring.boot.redis.common.Constant;
import spring.boot.redis.entity.AccessToken;
import spring.boot.redis.util.HttpUtil;
import spring.boot.redis.util.MessageHandlerUtil;
import spring.boot.redis.util.TokenUtil;
import spring.boot.redis.util.WechatUtil;

/**
 * 微信公众号测试
 * 
 * @author zhengjiaxing
 * @date 2018年8月27日
 */
@RestController
public class WechatController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private HttpUtil httpUtil;
	
	@Autowired
	private WechatUtil wxUtil;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Value("${wechat.appid}")
	private String appId;

	@Value("${wechat.appsecret}")
	private String appSecret;

	/**
	 * 获取AccessToken
	 * 
	 * @return
	 */
	@RequestMapping("/access-token")
	public AccessToken getAccessToken() {
		String url = Constant.ACCESS_TOKEN_URL.replace(Constant.APPID, appId).replace(Constant.APPSECRET, appSecret);
		JSONObject jsonObj = httpUtil.getForObject(url);
		String accTokenStr = jsonObj.getString("access_token");
		int expiresIn = jsonObj.getIntValue("expires_in");
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken(accTokenStr);
		accessToken.setExpiresIn(expiresIn);
		return accessToken;
	}

	/**
	 * 处理微信服务器发来的消息
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/wxmsg")
	public String dealWxMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO 接收、处理、响应由微信服务器转发的用户发送给公众帐号的消息
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		logger.info("请求进入");
		String result = "";
		try {
			Map<String, String> map = MessageHandlerUtil.parseXml(request);
			logger.info("开始构造消息");
			result = MessageHandlerUtil.buildXml(map);
			logger.info(result);
			if (result.equals("")) {
				result = "未正确响应";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("发生异常：" + e.getMessage());
		}
		return result;
	}

	/**
	 * 微信签名验证
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/sign")
	public String signValidate(HttpServletRequest request, HttpServletResponse response) {
		logger.info("开始验证签名");
		/* 接收微信服务器发送请求时传递过来的4个参数 */
		String signature = request.getParameter("signature");// 微信加密签名signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String timestamp = request.getParameter("timestamp");// 时间戳
		String nonce = request.getParameter("nonce");// 随机数
		String echostr = request.getParameter("echostr");// 随机字符串
		logger.info(echostr);
		// 排序
		String sortString = sort(Constant.TOKEN, timestamp, nonce);
		// 加密
		String mySignature = sha1(sortString);

		String result = null;
		// 校验签名
		if (mySignature != null && mySignature != "" && mySignature.equals(signature)) {
			logger.info("签名校验通过。");
			//result = dealWxMsg(request, response);
			// 消息处理
			Map<String, String> map = null;
			try {
				map = MessageHandlerUtil.parseXml(request);
				logger.info("开始构造消息");
				result = MessageHandlerUtil.buildResponseMessage(map);
			} catch (DocumentException | IOException e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			logger.info("签名校验失败.");
		}
		return result;
	}

	/**
	 * 排序方法
	 * 
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	private String sort(String token, String timestamp, String nonce) {
		String[] strArray = { token, timestamp, nonce };
		Arrays.sort(strArray);
		StringBuilder sb = new StringBuilder();
		for (String str : strArray) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 将字符串进行sha1加密
	 * 
	 * @param str
	 *            需要加密的字符串
	 * @return 加密后的内容
	 */
	public String sha1(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(str.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
	
	/**
	 * 上传素材到本地，获取thumb_media_id
	 * @return
	 */
	@RequestMapping(value = "/thumb-media-id", method = RequestMethod.POST, produces="text/plain")
	public String getThumbMediaId(@RequestBody MultipartFile media, @RequestParam("type") String type){
		String thumbMediaId = null;
		try {
			AccessToken accToken = tokenUtil.getAccessToken(appId, appSecret);
			thumbMediaId = wxUtil.getThumbMediaId(media, accToken.getAccessToken(), type);
		} catch (IllegalStateException | IOException e) {
			logger.error(e.getMessage(), e);
		}
		return thumbMediaId;
	}
	
	/**
	 * 创建菜单
	 */
	@RequestMapping(value = "/menu", method = RequestMethod.POST)
	public String createMenu(@RequestBody Map<String, Object> map){
		AccessToken accToken = tokenUtil.getAccessToken(appId, appSecret);
		return wxUtil.createMenu(accToken.getAccessToken(), map);
	}
}
