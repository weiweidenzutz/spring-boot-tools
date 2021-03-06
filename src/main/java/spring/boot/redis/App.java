package spring.boot.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
//@EnableCaching // 启动缓存
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
