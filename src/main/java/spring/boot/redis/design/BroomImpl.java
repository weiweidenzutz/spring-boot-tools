package spring.boot.redis.design;

public class BroomImpl implements Moveable {

	@Override
	public void run() {
		System.out.println("我是Broom.我在飞...");
	}

}