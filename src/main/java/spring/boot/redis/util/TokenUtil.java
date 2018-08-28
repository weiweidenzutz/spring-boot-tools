package spring.boot.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import spring.boot.redis.common.Constant;
import spring.boot.redis.entity.AccessToken;
import spring.boot.redis.entity.AccessTokenInfo;

/**
 * Token工具类
 * 
 * @author zhengjiaxing
 * @date 2018年8月27日
 */
@Component
public class TokenUtil implements Runnable {

	@Value("${wechat.appid}")
	private String appId;

	@Value("${wechat.appsecret}")
	private String appSecret;

	@Autowired
	private HttpUtil httpUtil;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public void run() {
		try {
			while (true) {
				// 获取accessToken
				AccessTokenInfo.accessToken = getAccessToken(appId, appSecret);
				// 获取成功
				if (null != AccessTokenInfo.accessToken) {
					// 获取到access_token 休眠7000秒,大约2个小时左右
					Thread.sleep(7000 * 1000);
					System.out.println("获取accessToken成功：" + AccessTokenInfo.accessToken.toString());
					// Thread.sleep(10 * 1000);//10秒钟获取一次
				} else {
					// 获取失败
					Thread.sleep(1000 * 3); // 获取的access_token为空 休眠3秒
				}
			}
		} catch (InterruptedException e) {
			System.out.println("发生异常：" + e.getMessage());
			e.printStackTrace();
			try {
				//发生异常休眠10秒
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} 
		}
	}

	/**
	 * 获取access token
	 * 
	 * @param appId
	 * @param appSecret
	 * @return
	 */
	public AccessToken getAccessToken(String appId, String appSecret) {
		// 从redis获取
		AccessToken accToken = (AccessToken) redisUtil.get(Constant.ACCESS_TOKEN + "_" + Constant.APPID + "_" + Constant.APPSECRET);
		if (null != accToken) {
			return accToken;
		}
		String url = Constant.ACCESS_TOKEN_URL.replace(Constant.APPID, appId).replace(Constant.APPSECRET, appSecret);
		JSONObject jsonObj = httpUtil.getForObject(url);
		String accTokenStr = jsonObj.getString("access_token");
		int expiresIn = jsonObj.getIntValue("expires_in");
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken(accTokenStr);
		accessToken.setExpiresIn(expiresIn);
		// 保存到redis
		redisUtil.set(Constant.ACCESS_TOKEN + "_" + Constant.APPID + "_" + Constant.APPSECRET, accessToken, 7200L);
		return accessToken;
	}

}
