package spring.boot.redis.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import spring.boot.redis.common.Constant;
import spring.boot.redis.entity.Articles;

/**
 * 微信工具类
 * 
 * @author zhengjiaxing
 * @date 2018年8月27日
 */
@Component
public class WechatUtil {

	@Autowired
	private HttpUtil httpUtil;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${news.path}")
	private String newsPath;

	/**
	 * 上传永久图文素材
	 * 
	 * @param articles
	 * @param accessToken
	 * @return 图片mediaId
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String getThumbMediaId(MultipartFile media, String accessToken, String type)
			throws IllegalStateException, IOException {
		// 保存文件到本地
		File filePath = new File(newsPath);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		File destFile = new File(newsPath + media.getOriginalFilename());
		media.transferTo(destFile);

		// 获取thumb_media_id
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = MediaType.parseMediaType("text/plain; charset=UTF-8");
		headers.setContentType(mediaType);
		headers.setConnection("keep-alive");
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());

		HttpEntity<Object> formEntity = new HttpEntity<Object>(null, headers);

		String url = Constant.THUMB_MEDIA_ID_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
		JSONObject jsonObj = restTemplate.postForObject(url, formEntity, JSONObject.class);

		String mediaId = type.equalsIgnoreCase("thumb") ? jsonObj.getString("thumb_media_id")
				: jsonObj.getString("media_id");
		return mediaId;
	}

	/**
	 * 新增永久图文素材
	 * 
	 * @param articles
	 * @param accessToken
	 * @return 图片mediaId
	 */
	public String addForeverPic(Articles articles, String accessToken) {
		JSONObject jsonObj = httpUtil
				.postForObject(Constant.FOREVER_PIC_URL.replace(Constant.ACCESS_TOKEN, accessToken), articles);
		return jsonObj.getString("media_id");
	}

	
	public String createMenu(String accessToken, Map<String, Object> map) {
		JSONObject jsonObj = httpUtil.postForObject(Constant.CREATE_MENU_URL.replace(Constant.ACCESS_TOKEN, accessToken), map);
		System.out.println(jsonObj.get("errcode"));
		System.out.println(jsonObj.get("errmsg"));
		return (String) jsonObj.get("errmsg");
	}
	
	
}
