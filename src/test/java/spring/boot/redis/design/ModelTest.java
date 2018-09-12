package spring.boot.redis.design;

import org.junit.Test;

import spring.boot.redis.design.factory.AbstractFactory;
import spring.boot.redis.design.factory.BroomFactory;
import spring.boot.redis.design.factory.Factory1;
import spring.boot.redis.design.factory.Flyable;
import spring.boot.redis.design.factory.Moveable;
import spring.boot.redis.design.factory.Plane;
import spring.boot.redis.design.factory.SimpleFactory;
import spring.boot.redis.design.factory.VehicleFactory;
import spring.boot.redis.design.factory.Writeable;
import spring.boot.redis.design.proxy.IUserDao;
import spring.boot.redis.design.proxy.ProxyFactory;
import spring.boot.redis.design.proxy.UserDaoImpl;
import spring.boot.redis.design.proxy.UserDaoProxy;
import spring.boot.redis.design.simpleton.Simpleton;

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

	/**
	 * 静态代理模式测试
	 */
	@Test
	public void staticProxyTest() {
		// 目标对象
		IUserDao target = new UserDaoImpl();
		// 代理对象,把目标对象传给代理对象,建立代理关系
		UserDaoProxy proxy = new UserDaoProxy(target);
		proxy.save();// 执行的是代理的方法
	}

	/**
	 * 动态代理模式测试
	 */
	@Test
	public void proxyTest() {
		// 目标对象
		IUserDao target = new UserDaoImpl();
		// 【原始的类型 class cn.itcast.b_dynamic.UserDao】
		System.out.println(target.getClass());
		// 给目标对象，创建代理对象
		IUserDao proxy = (IUserDao) new ProxyFactory(target).getProxyInstance();
		// class $Proxy0 内存中动态生成的代理对象
		System.out.println(proxy.getClass());
		// 执行方法 【代理对象】
		proxy.save();
	}
}
