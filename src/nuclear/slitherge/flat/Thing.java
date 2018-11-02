package nuclear.slitherge.flat;

public abstract class Thing {
	public String name="UNDEFINED";
	public abstract void update();
	public abstract int use(String[] args, Entity entity, Thing t, double x, double y);
	public String toString(){
		return name;
	}
}
