package spring.boot.redis.design.factory;
/**
 * 工厂模式
 * @author zhengjiaxing
 * @date 2018年9月9日
 */
public class BroomFactory extends VehicleFactory {
	 
    @Override
    public Moveable create() {
        return new BroomImpl();
    }
 
}