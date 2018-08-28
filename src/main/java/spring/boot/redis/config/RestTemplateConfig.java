package spring.boot.redis.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Configuration
@ConditionalOnClass(value = { RestTemplate.class })
public class RestTemplateConfig {

	// 初始化RestTemplate,并加入spring的Bean工厂，由spring统一管理
	@Bean
	@ConditionalOnMissingBean(RestTemplate.class)
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
		
		configureMessageConverters(converterList);
		// 重新设置StringHttpMessageConverter字符集为UTF-8，解决中文乱码问题
		HttpMessageConverter<?> converterTarget = null;
		for (HttpMessageConverter<?> item : converterList) {
			if (StringHttpMessageConverter.class == item.getClass()) {
				converterTarget = item;
				break;
			}
		}
		if (null != converterTarget) {
			converterList.remove(converterTarget);
		}
		converterList.add(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		// 加入FastJson转换器 根据使用情况进行操作，此段注释，默认使用jackson
		// converterList.add(new FastJsonHttpMessageConverter4());
		return restTemplate;
	}
	
	 public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

	        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
	        FastJsonConfig fastJsonConfig = new FastJsonConfig();
	        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue,
	                SerializerFeature.QuoteFieldNames, SerializerFeature.DisableCircularReferenceDetect);
	        fastConverter.setFastJsonConfig(fastJsonConfig);

	        List<MediaType> fastMediaTypes = new ArrayList<>();
	        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
	        fastMediaTypes.add(MediaType.parseMediaType(MediaType.TEXT_PLAIN + ";charset=UTF-8"));
	        fastConverter.setSupportedMediaTypes(fastMediaTypes);
	        converters.add(fastConverter);
	    }

}
