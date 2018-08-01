package nuclear.slitherge.top;

public class Rectangle extends java.awt.Rectangle {
	public double width, height, x,y;
	public Rectangle(java.awt.Rectangle r) {
		width=r.getWidth();
		height=r.getHeight();
		x=r.getX();
		y=r.getY();
	}
	public Rectangle(double width, double height) {
		this.width=width;
		this.height=height;
	}

	public Rectangle(double x, double y, double width, double height) {
		this.width=width;
		this.height=height;
		this.x=x;
		this.y=y;
	}
	public boolean inRect(double degrees, Position p){
		p=p.sub(new Position(p.dim,x,y)).rotate(-degrees).add(new Position(p.dim,x,y));
		return inRect(p);
	}
	public boolean inRect(Position p){
		p=p.sub(new Position(p.dim,x,y));
		return p.x<=width&&p.x>=0&&p.y<=height&&p.y>=0;
	}
}
