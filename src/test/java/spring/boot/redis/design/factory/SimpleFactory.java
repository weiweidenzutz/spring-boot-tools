package spring.boot.redis.design.factory;

/**
 * 简单工厂模式:
 * 		工厂模式的目的在于程序的可扩展性。
 * 		而对于简单工厂模式来说，它是为了让程序有一个更好地封装，降低程序模块之间的耦合程度。
 * 		也可以将其理解成为一个创建对象的工具类
 * 
 * @author zhengjiaxing
 * @date 2018年9月7日
 */
public class SimpleFactory {
	
	public Object create(Class<?> clazz) {
		if (clazz.getName().equals(Plane.class.getName())) {
			return createPlane();
		} else if (clazz.getName().equals(Broom.class.getName())) {
			return createBroom();
		}
		return null;
	}

	private Broom createBroom() {
		System.out.println("创建Broom");
		return new Broom();
	}

	private Plane createPlane() {
		System.out.println("创建Plane");
		return new Plane();
	}

}
