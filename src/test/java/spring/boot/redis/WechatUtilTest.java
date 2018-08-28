package spring.boot.redis;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import spring.boot.redis.base.BaseJunit4Test;
import spring.boot.redis.entity.AccessToken;
import spring.boot.redis.entity.Articles;
import spring.boot.redis.util.TokenUtil;
import spring.boot.redis.util.WechatUtil;

public class WechatUtilTest extends BaseJunit4Test {

	@Autowired
	private WechatUtil wxUtil;

	@Autowired
	private TokenUtil tokenUtil;

	@Value("${wechat.appid}")
	private String appId;

	@Value("${wechat.appsecret}")
	private String appSecret;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	/**
	 * 在每次测试执行前构建mvc环境
	 */
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void getThumbMediaIdTest() {
		try {
			MockMultipartFile mockFile = new MockMultipartFile("file", "C:\\Users\\111512180\\Desktop\\aa.jpg", "multipart/form-data", "hello upload".getBytes("UTF-8"));
			
			String result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/file")
				.file(
					mockFile
				)
			).andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn().getResponse().getContentAsString();
			
			System.out.println("结果：" + result);
			
			AccessToken accessToken = tokenUtil.getAccessToken(appId, appSecret);
			wxUtil.getThumbMediaId(mockFile, accessToken.getAccessToken(), "image");
		} catch (Exception e) {
		}
		
	}
}
