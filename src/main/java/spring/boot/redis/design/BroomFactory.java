package spring.boot.redis.design;

public class BroomFactory extends VehicleFactory {
	 
    @Override
    public Moveable create() {
        return new BroomImpl();
    }
 
}