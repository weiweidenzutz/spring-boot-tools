package spring.boot.redis.design;

/**
 * 抽象工厂
 * @author zhengjiaxing
 * @date 2018年9月7日
 */
public abstract class VehicleFactory {

	public abstract Moveable create();
}
