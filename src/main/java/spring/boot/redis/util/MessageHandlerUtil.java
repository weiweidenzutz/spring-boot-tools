package spring.boot.redis.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import spring.boot.redis.common.Constant;
import spring.boot.redis.entity.MessageType;
import spring.boot.redis.entity.Music;
import spring.boot.redis.entity.NewsItem;
import spring.boot.redis.entity.Video;

/**
 * 消息处理工具类
 * @author zhengjiaxing
 * @date 2018年8月27日
 */
public class MessageHandlerUtil {

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return map
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws DocumentException, IOException {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();
		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		Iterator<Element> itor = elementList.iterator();
		while (itor.hasNext()) {
			Element e = itor.next();
			System.out.println(e.getName() + "|" + e.getText());
			map.put(e.getName(), e.getText());
		}
		// 释放资源
		inputStream.close();
		inputStream = null;
		return map;
	}

	/**
	 * 根据消息类型 构造返回消息
	 * 
	 * @param map
	 * @return
	 */
	public static String buildResponseMessage(Map<String, String> map) {
		String responseMessage = "";
		String msgType = map.get("MsgType").toString();
		System.out.println("MsgType:" + msgType);
		// 消息类型
		MessageType messageEnumType = MessageType.valueOf(MessageType.class, msgType.toUpperCase());
		switch (messageEnumType) {
		case TEXT:
			// 处理文本消息
			responseMessage = handleTextMessage(map);
			break;
		case IMAGE:
			// 处理图片消息
			responseMessage = handleImageMessage(map);
			break;
		case VOICE:
			// 处理语音消息
			responseMessage = handleVoiceMessage(map);
			break;
		case VIDEO:
			// 处理视频消息
			responseMessage = handleVideoMessage(map);
			break;
		case SHORTVIDEO:
			// 处理小视频消息
			responseMessage = handleSmallVideoMessage(map);
			break;
		case LOCATION:
			// 处理位置消息
			responseMessage = handleLocationMessage(map);
			break;
		case LINK:
			// 处理链接消息
			responseMessage = handleLinkMessage(map);
			break;
		case EVENT:
			// 处理事件消息,用户在关注与取消关注公众号时，微信会向我们的公众号服务器发送事件消息,开发者接收到事件消息后就可以给用户下发欢迎消息
			responseMessage = handleEventMessage(map);
		default:
			break;
		}
		// 返回响应消息
		return responseMessage;
	}
	
	 /**
     * 处理接收到图片消息
     *
     * @param map
     * @return
     */
    private static String handleImageMessage(Map<String, String> map) {
        String picUrl = map.get("PicUrl");
        String mediaId = map.get("MediaId");
        System.out.print("picUrl:" + picUrl);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的图片，图片Url为：%s\n图片素材Id为：%s", picUrl, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * 处理接收到语音消息
     * @param map
     * @return
     */
    private static String handleVoiceMessage(Map<String, String> map) {
        String format = map.get("Format");
        String mediaId = map.get("MediaId");
        System.out.print("format:" + format);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的语音，语音格式为：%s\n语音素材Id为：%s", format, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * 处理接收到的视频消息
     * @param map
     * @return
     */
    private static String handleVideoMessage(Map<String, String> map) {
        String thumbMediaId = map.get("ThumbMediaId");
        String mediaId = map.get("MediaId");
        System.out.print("thumbMediaId:" + thumbMediaId);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的视频，视频中的素材ID为：%s\n视频Id为：%s", thumbMediaId, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * 处理接收到的小视频消息
     * @param map
     * @return
     */
    private static String handleSmallVideoMessage(Map<String, String> map) {
        String thumbMediaId = map.get("ThumbMediaId");
        String mediaId = map.get("MediaId");
        System.out.print("thumbMediaId:" + thumbMediaId);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的小视频，小视频中素材ID为：%s,\n小视频Id为：%s", thumbMediaId, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * 处理接收到的地理位置消息
     * @param map
     * @return
     */
    private static String handleLocationMessage(Map<String, String> map) {
        String latitude = map.get("Location_X");  //纬度
        String longitude = map.get("Location_Y");  //经度
        String label = map.get("Label");  //地理位置精度
        String result = String.format("纬度：%s\n经度：%s\n地理位置：%s", latitude, longitude, label);
        return buildTextMessage(map, result);
    }

    /**
     * 处理接收到的链接消息
     * @param map
     * @return
     */
    private static String handleLinkMessage(Map<String, String> map) {
        String title = map.get("Title");
        String description = map.get("Description");
        String url = map.get("Url");
        String result = String.format("已收到您发来的链接，链接标题为：%s,\n描述为：%s\n,链接地址为：%s", title, description, url);
        return buildTextMessage(map, result);
    }
    

    /**
     * 处理消息Message
     * @param map 封装了解析结果的Map
     * @return
     */
    private static String handleEventMessage(Map<String, String> map) {
        String responseMessage = buildWelcomeTextMessage(map);
        return responseMessage;
    }

	/**
	 * 测试：根据消息类型 构造返回消息
	 * 
	 * @param map
	 * @return
	 */
	public static String buildXml(Map<String, String> map) {
		String result;
		String msgType = map.get("MsgType").toString();
		System.out.println("MsgType:" + msgType);
		if (msgType.toUpperCase().equals("TEXT")) {
			result = buildTextMessage(map, "欢迎关注夹心码农公众号");
		} else {
			String fromUserName = map.get("FromUserName");
			// 开发者微信号
			String toUserName = map.get("ToUserName");
			result = String.format(
					"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
							+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
							+ "<Content><![CDATA[%s]]></Content>" + "</xml>",
					fromUserName, toUserName, getMsgCreateTime(), "请回复如下关键词：\n文本\n图片\n语音\n视频\n音乐\n图文");
		}

		return result;
	}

	/**
	 * 构造文本消息
	 *
	 * @param map
	 * @param content
	 * @return
	 */
	private static String buildTextMessage(Map<String, String> map, String content) {
		// 发送方帐号
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		/**
		 * 文本消息XML数据格式 <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
		 * <FromUserName><![CDATA[fromUser]]></FromUserName>
		 * <CreateTime>1348831860</CreateTime>
		 * <MsgType><![CDATA[text]]></MsgType> <Content><![CDATA[this is a
		 * test]]></Content> <MsgId>1234567890123456</MsgId> </xml>
		 */
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
						+ "<Content><![CDATA[%s]]></Content>" + "</xml>",
				fromUserName, toUserName, getMsgCreateTime(), content);
	}

	/**
	 * 构造图片消息
	 * 
	 * @param map
	 *            封装了解析结果的Map
	 * @param mediaId
	 *            通过素材管理接口上传多媒体文件得到的id
	 * @return 图片消息XML字符串
	 */
	private static String buildImageMessage(Map<String, String> map, String mediaId) {
		// 发送方帐号
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		/**
		 * 图片消息XML数据格式 <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
		 * <FromUserName><![CDATA[fromUser]]></FromUserName>
		 * <CreateTime>12345678</CreateTime>
		 * <MsgType><![CDATA[image]]></MsgType>
		 * <Image> <MediaId><![CDATA[media_id]]></MediaId> </Image> </xml>
		 */
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[image]]></MsgType>" + "<Image>"
						+ "   <MediaId><![CDATA[%s]]></MediaId>" + "</Image>" + "</xml>",
				fromUserName, toUserName, getMsgCreateTime(), mediaId);
	}

	/**
	 * 构造音乐消息
	 * 
	 * @param map
	 *            封装了解析结果的Map
	 * @param music
	 *            封装好的音乐消息内容
	 * @return 音乐消息XML字符串
	 */
	private static String buildMusicMessage(Map<String, String> map, Music music) {
		// 发送方帐号
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		/**
		 * 音乐消息XML数据格式 <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
		 * <FromUserName><![CDATA[fromUser]]></FromUserName>
		 * <CreateTime>12345678</CreateTime>
		 * <MsgType><![CDATA[music]]></MsgType>
		 * <Music> <Title><![CDATA[TITLE]]></Title>
		 * <Description><![CDATA[DESCRIPTION]]></Description>
		 * <MusicUrl><![CDATA[MUSIC_Url]]></MusicUrl>
		 * <HQMusicUrl><![CDATA[HQ_MUSIC_Url]]></HQMusicUrl>
		 * <ThumbMediaId><![CDATA[media_id]]></ThumbMediaId> </Music> </xml>
		 */
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[music]]></MsgType>" + "<Music>"
						+ "   <Title><![CDATA[%s]]></Title>" + "   <Description><![CDATA[%s]]></Description>"
						+ "   <MusicUrl><![CDATA[%s]]></MusicUrl>" + "   <HQMusicUrl><![CDATA[%s]]></HQMusicUrl>"
						+ "</Music>" + "</xml>",
				fromUserName, toUserName, getMsgCreateTime(), music.getTitle(), music.getDescription(),
				music.getMusicUrl(), music.getHqMusicUrl());
	}

	/**
	 * 构造视频消息
	 * 
	 * @param map
	 *            封装了解析结果的Map
	 * @param video
	 *            封装好的视频消息内容
	 * @return 视频消息XML字符串
	 */
	private static String buildVideoMessage(Map<String, String> map, Video video) {
		// 发送方帐号
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		/**
		 * 音乐消息XML数据格式 <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
		 * <FromUserName><![CDATA[fromUser]]></FromUserName>
		 * <CreateTime>12345678</CreateTime>
		 * <MsgType><![CDATA[video]]></MsgType>
		 * <Video> <MediaId><![CDATA[media_id]]></MediaId>
		 * <Title><![CDATA[title]]></Title>
		 * <Description><![CDATA[description]]></Description> </Video> </xml>
		 */
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[video]]></MsgType>" + "<Video>"
						+ "   <MediaId><![CDATA[%s]]></MediaId>" + "   <Title><![CDATA[%s]]></Title>"
						+ "   <Description><![CDATA[%s]]></Description>" + "</Video>" + "</xml>",
				fromUserName, toUserName, getMsgCreateTime(), video.getMediaId(), video.getTitle(),
				video.getDescription());
	}

	/**
	 * 构造语音消息
	 * 
	 * @param map
	 *            封装了解析结果的Map
	 * @param mediaId
	 *            通过素材管理接口上传多媒体文件得到的id
	 * @return 语音消息XML字符串
	 */
	private static String buildVoiceMessage(Map<String, String> map, String mediaId) {
		// 发送方帐号
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		/**
		 * 语音消息XML数据格式 <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
		 * <FromUserName><![CDATA[fromUser]]></FromUserName>
		 * <CreateTime>12345678</CreateTime>
		 * <MsgType><![CDATA[voice]]></MsgType>
		 * <Voice> <MediaId><![CDATA[media_id]]></MediaId> </Voice> </xml>
		 */
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[voice]]></MsgType>" + "<Voice>"
						+ "   <MediaId><![CDATA[%s]]></MediaId>" + "</Voice>" + "</xml>",
				fromUserName, toUserName, getMsgCreateTime(), mediaId);
	}

	/**
	 * 构造图文消息
	 * 
	 * @param map
	 *            封装了解析结果的Map
	 * @return 图文消息XML字符串
	 */
	private static String buildNewsMessage(Map<String, String> map) {
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		NewsItem item = new NewsItem();
		item.setTitle("微信开发学习总结（一）——微信开发环境搭建");
		String desc = "工欲善其事，必先利其器。要做微信公众号开发，那么要先准备好两样必不可少的东西：\n" + "\n" + "　　1、要有一个用来测试的公众号。\n" + "\n"
				+ "　　2、用来调式代码的开发环境";
		item.setDescription(desc);
		item.setPicUrl("http://images2015.cnblogs.com/blog/289233/201601/289233-20160121164317343-2145023644.png");
		item.setUrl("http://www.cnblogs.com/xdp-gacl/p/5149171.html");
		String itemContent1 = buildSingleItem(item);

		NewsItem item2 = new NewsItem();
		item2.setTitle("微信开发学习总结（二）——微信开发入门");
		item2.setDescription(
				"微信服务器就相当于一个转发服务器，终端（手机、Pad等）发起请求至微信服务器，微信服务器然后将请求转发给我们的应用服务器。应用服务器处理完毕后，将响应数据回发给微信服务器，微信服务器再将具体响应信息回复到微信App终端。");
		item2.setPicUrl("");
		item2.setUrl("http://www.cnblogs.com/xdp-gacl/p/5151857.html");
		String itemContent2 = buildSingleItem(item2);

		String content = String.format(
				"<xml>\n" + "<ToUserName><![CDATA[%s]]></ToUserName>\n"
						+ "<FromUserName><![CDATA[%s]]></FromUserName>\n" + "<CreateTime>%s</CreateTime>\n"
						+ "<MsgType><![CDATA[news]]></MsgType>\n" + "<ArticleCount>%s</ArticleCount>\n" + "<Articles>\n"
						+ "%s" + "</Articles>\n" + "</xml> ",
				fromUserName, toUserName, getMsgCreateTime(), 2, itemContent1 + itemContent2);
		return content;

	}

	/**
	 * 生成图文消息的一条记录
	 *
	 * @param item
	 * @return
	 */
	private static String buildSingleItem(NewsItem item) {
		String itemContent = String.format(
				"<item>\n" + "<Title><![CDATA[%s]]></Title> \n" + "<Description><![CDATA[%s]]></Description>\n"
						+ "<PicUrl><![CDATA[%s]]></PicUrl>\n" + "<Url><![CDATA[%s]]></Url>\n" + "</item>",
				item.getTitle(), item.getDescription(), item.getPicUrl(), item.getUrl());
		return itemContent;
	}

	/**
	 * 接收到文本消息后处理
	 * 
	 * @param map
	 *            封装了解析结果的Map
	 * @return
	 */
	private static String handleTextMessage(Map<String, String> map) {
		// 响应消息
		String responseMessage;
		// 消息内容
		String content = map.get("Content");
		switch (content) {
		case "文本":
			String msgText = "孤傲苍狼又要开始写博客总结了,欢迎朋友们访问我在博客园上面写的博客\n"
					+ "<a href=\"http://www.cnblogs.com/xdp-gacl\">孤傲苍狼的博客</a>";
			responseMessage = buildTextMessage(map, msgText);
			break;
		case "图片":
			// 通过素材管理接口上传图片时得到的media_id
			String imgMediaId = "RozUE66ghlOdzy_UYlDFNeCqYJdcLjf8XFL0iNjtSkiPIgu3pb7iLvls_BDSjOmS";
			responseMessage = buildImageMessage(map, imgMediaId);
			break;
		case "语音":
			// 通过素材管理接口上传语音文件时得到的media_id
			String voiceMediaId = "0uUhFgvB16LorY-iikxxgdIRx6c2QRhMRjhx8qthtAvGhjAKxeysJ7BjMEYjz92D";
			responseMessage = buildVoiceMessage(map, voiceMediaId);
			break;
		case "图文":
			responseMessage = buildNewsMessage(map);
			break;
		case "音乐":
			Music music = new Music();
			music.setTitle("赵丽颖、许志安 - 乱世俱灭");
			music.setDescription("电视剧《蜀山战纪》插曲");
			music.setMusicUrl(Constant.NATAPP_URL + "/music/music.mp3");
			music.setHqMusicUrl(Constant.NATAPP_URL + "/music/music.mp3");
			responseMessage = buildMusicMessage(map, music);
			break;
		case "视频":
			Video video = new Video();
			video.setMediaId("8ifXYGeAf9L64aPc2ywKBPg2JHVOdb34_zEtKwsz8qxebnEf1tq5_rOAY5V4IPib");
			video.setTitle("小苹果");
			video.setDescription("小苹果搞笑视频");
			responseMessage = buildVideoMessage(map, video);
			break;
		default:
			responseMessage = buildWelcomeTextMessage(map);
			break;

		}
		// 返回响应消息
		return responseMessage;
	}

	/**
	 * 欢迎消息
	 * @param map
	 * @return
	 */
	private static String buildWelcomeTextMessage(Map<String, String> map) {
		String content = "欢迎关注夹心码农公众号！";
		// 发送方帐号
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		/**
		 * 文本消息XML数据格式 <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
		 * <FromUserName><![CDATA[fromUser]]></FromUserName>
		 * <CreateTime>1348831860</CreateTime>
		 * <MsgType><![CDATA[text]]></MsgType> <Content><![CDATA[this is a
		 * test]]></Content> <MsgId>1234567890123456</MsgId> </xml>
		 */
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
						+ "<Content><![CDATA[%s]]></Content>" + "</xml>",
				fromUserName, toUserName, getMsgCreateTime(), content);
	}

	private static String getMsgCreateTime() {
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");// 设置显示格式
		String nowTime = df.format(dt);
		long dd = (long) 0;
		try {
			dd = df.parse(nowTime).getTime();
		} catch (Exception e) {

		}
		return String.valueOf(dd);
	}
}