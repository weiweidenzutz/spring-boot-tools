package spring.boot.redis.design;

public class Car implements Moveable {

	@Override
	public void run() {
		System.out.println("一辆卡车在跑....");
	}

}
