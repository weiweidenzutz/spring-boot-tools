package spring.boot.redis.entity;

/**
 * 微信图文信息类
 * 
 * @author zhengjiaxing
 * @date 2018年8月27日
 */
public class NewsItem {

	private String title; // 标题
	private String description; // 描述
	private String picUrl; // 图片url
	private String url; // 图文链接

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
