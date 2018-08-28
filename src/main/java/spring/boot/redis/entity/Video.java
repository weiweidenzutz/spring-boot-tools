package spring.boot.redis.entity;

/**
 * 微信视频类型
 * 
 * @author zhengjiaxing
 * @date 2018年8月27日
 */
public class Video {

	private String mediaId;
	private String title;
	private String description;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

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

}
