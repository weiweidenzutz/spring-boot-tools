package spring.boot.redis.design.factory;
/**
 * 工厂模式
 * @author zhengjiaxing
 * @date 2018年9月9日
 */
public class Factory1 extends AbstractFactory {

	@Override
	public Flyable createFlyable() {
		return new Aircraft();
	}

	@Override
	public Moveable createMoveable() {
		return new Car();
	}

	@Override
	public Writeable createWriteable() {
		return new Pen();
	}

}
