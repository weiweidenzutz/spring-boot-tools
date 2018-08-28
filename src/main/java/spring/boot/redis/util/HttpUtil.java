package spring.boot.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/**
 * Http工具类
 * @author zhengjiaxing
 * @date 2018年8月27日
 */
@Component
public class HttpUtil {

	@Autowired
	private RestTemplate restTemplate;
	
	public JSONObject getForObject(String url){
		return restTemplate.getForObject(url, JSONObject.class);
	}
	
	public JSONObject postForObject(String url, Object obj){
		HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        
        HttpEntity<Object> formEntity = new HttpEntity<Object>(obj, headers);
		return restTemplate.postForObject(url, formEntity, JSONObject.class);
	}
	
	public String getForEntity(String url){
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
	    String body = responseEntity.getBody();
	    HttpStatus statusCode = responseEntity.getStatusCode();
	    int statusCodeValue = responseEntity.getStatusCodeValue();
	    HttpHeaders headers = responseEntity.getHeaders();
	    StringBuffer result = new StringBuffer();
	    result.append("responseEntity.getBody()：").append(body).append("<hr>")
	            .append("responseEntity.getStatusCode()：").append(statusCode).append("<hr>")
	            .append("responseEntity.getStatusCodeValue()：").append(statusCodeValue).append("<hr>")
	            .append("responseEntity.getHeaders()：").append(headers).append("<hr>");
	    return result.toString();
	}
}
