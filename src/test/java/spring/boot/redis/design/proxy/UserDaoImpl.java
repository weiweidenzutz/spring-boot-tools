package spring.boot.redis.design.proxy;

public class UserDaoImpl implements IUserDao {

	@Override
	public void save() {
		System.out.println("保存用户成功！");
	}

}
