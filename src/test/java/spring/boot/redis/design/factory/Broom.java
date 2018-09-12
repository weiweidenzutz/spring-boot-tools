package spring.boot.redis.design.factory;
/**
 * 工厂模式
 * @author zhengjiaxing
 * @date 2018年9月9日
 */
public class Broom {

	private String name;
	private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
