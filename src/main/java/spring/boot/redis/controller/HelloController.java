package spring.boot.redis.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.boot.redis.config.MyException;

/**
 * 统一异常测试
 * @author zhengjiaxing
 * @date 2018年8月20日
 */
@Controller
public class HelloController {

	@RequestMapping("/hello")
	@ResponseBody
	public String hello() throws Exception{
//		throw new Exception("异常！！！");
		throw new MyException("我的异常！");
	}
	
	@RequestMapping("/sayHi")
	public String sayHi(HttpServletRequest request, @RequestParam("name") String name){
		request.setAttribute("name", name);
		return "hello";
	}
}
