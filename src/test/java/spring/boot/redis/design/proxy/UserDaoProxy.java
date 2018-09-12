package spring.boot.redis.design.proxy;

/**
 * 代理对象,静态代理
 * 
 * 静态代理总结:
 * 1.优点：
 * 		可以做到在不修改目标对象的功能前提下,对目标功能扩展.
 * 2.缺点:
 * 		因为代理对象需要与目标对象实现一样的接口,所以会有很多代理类,类太多.同时,一旦接口增加方法,目标对象与代理对象都要维护.
 * 
 * 
 * @author zhengjiaxing
 * @date 2018年9月9日
 */
public class UserDaoProxy implements IUserDao {

	private IUserDao target;

	public UserDaoProxy() {
	}

	public UserDaoProxy(IUserDao target) {
		this.target = target;
	}

	@Override
	public void save() {
		System.out.println("开始事务...");
		target.save();// 执行目标对象的方法
		System.out.println("提交事务...");
	}

}
