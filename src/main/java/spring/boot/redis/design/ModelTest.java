package spring.boot.redis.design;

import org.junit.Test;

public class ModelTest {

	/**
	 * 单例模式测试
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void simpletonTest() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			System.out.println("序号：" + i);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Simpleton simpleton = Simpleton.getInstance();
					System.out.println(simpleton.toString());
				}
			}).start();
			Thread.sleep(0);
		}
	}

	/**
	 * 简单工厂模式测试
	 */
	@Test
	public void simpleFactoryTest() {
		SimpleFactory simpleFactory = new SimpleFactory();

		Plane plane = (Plane) simpleFactory.create(Plane.class);
		System.out.println(plane.toString());
	}

	/**
	 * 抽象工厂方法测试
	 */
	@Test
	public void cxFactoryFunTest() {
		VehicleFactory factory = new BroomFactory();
		Moveable moveable = factory.create();
		moveable.run();
	}

	/**
	 * 抽象工厂模式测试
	 */
	@Test
	public void cxFactoryTest() {
		AbstractFactory factory = new Factory1();
		Flyable flyable = factory.createFlyable();
		flyable.fly(1589);

		Moveable moveable = factory.createMoveable();
		moveable.run();

		Writeable writeable = factory.createWriteable();
		writeable.run();
	}
}
