package spring.boot.redis.design.factory;
/**
 * 工厂模式
 * @author zhengjiaxing
 * @date 2018年9月9日
 */
public class Car implements Moveable {

	@Override
	public void run() {
		System.out.println("一辆卡车在跑....");
	}

}
