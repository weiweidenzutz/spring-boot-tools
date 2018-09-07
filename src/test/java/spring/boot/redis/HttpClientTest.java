package spring.boot.redis;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class HttpClientTest {

	@Test
	public void test() throws HttpException, IOException {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod("http://qiaoliqiang.cn/");
		// 防止中文乱码
		postMethod.getParams().setContentCharset("utf-8");
		// 3.设置请求参数
		postMethod.setParameter("mobileCode", "13834786998");
		postMethod.setParameter("userID", "");
		// 4.执行请求 ,结果码
		int code = httpClient.executeMethod(postMethod);
		// 5. 获取结果
        String result = postMethod.getResponseBodyAsString();
        System.out.println("状态吗：" + code);
        System.out.println("Post请求的结果：" + result);
	}
	
	@Test
	public void testJsoup() throws IOException {
        String url = "https://user.qzone.qq.com/389394744";
        //直接获取DOM树
        Document document = Jsoup.connect(url).get();
        System.out.println(document.toString());
    } 
	
	@Test
	public void paChong() throws IOException {    
        URL url= new URL("http://www.cnblogs.com/qlqwjy/p/8721867.html");    
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();  
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");  
        InputStream is = conn.getInputStream();    
        
        byte[] bytes = new byte[1024];    
        int byteLength = 0;    
        StringBuffer sb = new StringBuffer();    
        while((byteLength = is.read(bytes)) != -1) {    
            sb.append(new String(bytes, 0, byteLength, "UTF-8"));    
        }    
        is.close();  
        System.out.println(sb.toString());  
     }  
}
