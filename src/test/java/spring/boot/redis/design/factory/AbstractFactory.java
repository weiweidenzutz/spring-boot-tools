package spring.boot.redis.design.factory;

/**
 * 抽象工厂模式
 * @author zhengjiaxing
 * @date 2018年9月9日
 */
public abstract class AbstractFactory {

	public abstract Flyable createFlyable();

	public abstract Moveable createMoveable();

	public abstract Writeable createWriteable();
}
