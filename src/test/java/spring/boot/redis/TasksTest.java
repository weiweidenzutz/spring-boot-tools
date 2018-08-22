package spring.boot.redis;

import java.util.concurrent.Future;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import spring.boot.redis.base.BaseJunit4Test;
import spring.boot.redis.schedule.AsyncTasks;
import spring.boot.redis.schedule.SyncTasks;
import spring.boot.redis.schedule.ThreadPoolTasks;
import spring.boot.redis.schedule.ThreadPoolTasks1;

public class TasksTest extends BaseJunit4Test {

	@Autowired
	private SyncTasks syncTasks;

	@Autowired
	private AsyncTasks asyncTasks;

	@Autowired
	private ThreadPoolTasks threadTask;

	@Autowired
	private ThreadPoolTasks1 threadTask1;

	/**
	 * 同步执行
	 * 
	 * @throws Exception
	 */
	@Test
	public void syncTaskTest() throws Exception {
		syncTasks.doTaskOne();
		syncTasks.doTaskTwo();
		syncTasks.doTaskThree();

	}

	/**
	 * 异步执行
	 * 
	 * @throws Exception
	 */
	@Test
	public void AsyncTaskTest() throws Exception {
		long start = System.currentTimeMillis();
		Future<String> task_1 = asyncTasks.doTaskOne();
		Future<String> task_2 = asyncTasks.doTaskTwo();
		Future<String> task_3 = asyncTasks.doTaskThree();

		while (true) {
			if (task_1.isDone() && task_2.isDone() && task_3.isDone()) {
				break;
			}
			Thread.sleep(1000);
		}
		long end = System.currentTimeMillis();
		System.out.println("任务全部完成，总耗时：" + (end - start) + "毫秒");
	}

	/**
	 * 异步线程池测试
	 * 
	 * @throws Exception
	 */
	@Test
	public void threadTaskTest() throws Exception {
		threadTask.doTaskOne();
		threadTask.doTaskTwo();
		threadTask.doTaskThree();

		Thread.currentThread().join();
	}

	/**
	 * 异步线程池测试1 (+ Redis)
	 * 
	 * @throws Exception
	 */
	@Test
	public void threadTask1Test() throws Exception {
		for (int i = 0; i < 10000; i++) {
			threadTask1.doTaskOne();
			threadTask1.doTaskTwo();
			threadTask1.doTaskThree();

			if (i == 9999) {
				System.exit(0);
			}
		}
	}
}
