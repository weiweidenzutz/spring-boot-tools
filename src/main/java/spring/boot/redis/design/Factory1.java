package spring.boot.redis.design;

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
