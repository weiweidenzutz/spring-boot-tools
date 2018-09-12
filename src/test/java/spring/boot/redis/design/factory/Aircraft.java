package spring.boot.redis.design.factory;
/**
 * 工厂模式
 * @author zhengjiaxing
 * @date 2018年9月9日
 */
public class Aircraft implements Flyable {

	@Override
	public void fly(int height) {
		System.out.println("我是一架客运机，我目前的飞行高度为：" + height + "千米。");
	}

}
