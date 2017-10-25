package nuclear.slitherge.top;

public abstract class Thing {
	public String name="air";
	public Thing(double x, double y) {
		this.x=x;
		this.y=y;
	}
	public double x;
	public double y;
	public abstract void update();
	public abstract int use(String[] args, Position pos, boolean print);
	public String getDescription() {
		return "There is a "+name;
	}
}
