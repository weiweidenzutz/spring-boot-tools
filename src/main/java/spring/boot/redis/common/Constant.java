package spring.boot.redis.common;

public class Constant {

	/**返回的错误状态码**/
	public static final String ERROR_CODE = "500";
	/**返回的成功状态码**/
	public static final String SUCCESS_CODE = "200";

	/**分页，每页10条**/
	public static final int PAGE_SIZE = 5;
	
	public static final String APPID = "APPID";
	public static final String APPSECRET = "APPSECRET";
	public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	
	/**Token可由开发者可以任意填写，用作生成签名（该Token会和接口URL中包含的Token进行比对，从而验证安全性）**/
	public static final String TOKEN = "zjx";
	
	/**access token url**/
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	/**临时统一url**/
	public static final String NATAPP_URL = "http://dp9sp8.natappfree.cc"; 
	
	/**新增永久素材url**/
	public static final String FOREVER_PIC_URL = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";
	
	/**获取thumb_media_id**/
	//public static final String THUMB_MEDIA_ID_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	public static final String THUMB_MEDIA_ID_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	
	/**创建菜单**/
	public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	
}
