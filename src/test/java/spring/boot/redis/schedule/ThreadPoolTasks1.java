package spring.boot.redis.schedule;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolTasks1 {
	
	private Logger logger = LoggerFactory.getLogger(ThreadPoolTasks1.class);
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public static Random random = new Random();

	@Async("taskExecutor")
	public void doTaskOne() throws Exception {
		logger.info("开始做任务一");
		long start = System.currentTimeMillis();
		logger.info(stringRedisTemplate.randomKey());
		long end = System.currentTimeMillis();
		logger.info("完成任务一，耗时：" + (end - start) + "毫秒");
	}

	@Async("taskExecutor")
	public void doTaskTwo() throws Exception {
		logger.info("开始做任务二");
		long start = System.currentTimeMillis();
		logger.info(stringRedisTemplate.randomKey());
		long end = System.currentTimeMillis();
		logger.info("完成任务二，耗时：" + (end - start) + "毫秒");
	}

	@Async("taskExecutor")
	public void doTaskThree() throws Exception {
		logger.info("开始做任务三");
		long start = System.currentTimeMillis();
		logger.info(stringRedisTemplate.randomKey());
		long end = System.currentTimeMillis();
		logger.info("完成任务三，耗时：" + (end - start) + "毫秒");
	}
}
