package spring.boot.redis.design.simpleton;

/**
 * 单例模式
 * 
 * @author zhengjiaxing
 * @date 2018年9月7日
 */
public class Simpleton {

	private volatile static Simpleton simpleton;

	private Simpleton() {
	}

	public static Simpleton getInstance() {
		if (null == simpleton) {
			synchronized (Simpleton.class) {
				simpleton = new Simpleton();
			}
		}
		return simpleton;
	}
}
