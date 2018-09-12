package spring.boot.redis.mq;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spring.boot.redis.mq.jms.Producer;
import spring.boot.redis.mq.jms.Producer2;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JmsTest {

	@Autowired
	private Producer producer;
	
	@Autowired
	private Producer2 producer2;

	@Test
	public void contextLoads() throws InterruptedException {
		Destination destination = new ActiveMQQueue("mytest.queue");

		for (int i = 0; i < 100; i++) {
			producer.sendMessage(destination, "myname is chhliu!!! >>> " + i);
		}
	}
	
	@Test
	public void contextLoads2() throws InterruptedException {
		Destination destination = new ActiveMQQueue("mytest.queue");

		for (int i = 0; i < 100; i++) {
			producer2.sendMessage(destination, "myname is chhliu!!! >>> " + i);
		}
	}
}
