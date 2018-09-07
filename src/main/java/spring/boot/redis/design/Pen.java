package spring.boot.redis.design;

public class Pen implements Writeable {

	@Override
	public void run() {
		System.out.println("一支笔在写....");
	}

}
