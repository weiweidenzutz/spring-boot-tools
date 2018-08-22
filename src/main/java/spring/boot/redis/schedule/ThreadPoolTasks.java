package spring.boot.redis.schedule;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolTasks {
	
	private Logger logger = LoggerFactory.getLogger(ThreadPoolTasks.class);

	public static Random random = new Random();

	@Async("taskExecutor")
	public void doTaskOne() throws Exception {
		logger.info("开始做任务一");
		long start = System.currentTimeMillis();
		Thread.sleep(random.nextInt(10000));
		long end = System.currentTimeMillis();
		logger.info("完成任务一，耗时：" + (end - start) + "毫秒");
	}

	@Async("taskExecutor")
	public void doTaskTwo() throws Exception {
		logger.info("开始做任务二");
		long start = System.currentTimeMillis();
		Thread.sleep(random.nextInt(10000));
		long end = System.currentTimeMillis();
		logger.info("完成任务二，耗时：" + (end - start) + "毫秒");
	}

	@Async("taskExecutor")
	public void doTaskThree() throws Exception {
		logger.info("开始做任务三");
		long start = System.currentTimeMillis();
		Thread.sleep(random.nextInt(10000));
		long end = System.currentTimeMillis();
		logger.info("完成任务三，耗时：" + (end - start) + "毫秒");
	}
}
