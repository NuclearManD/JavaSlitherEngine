package nuclear.slitherge.top;

import java.awt.Dimension;
import java.awt.Point;

public class Rectangle extends java.awt.Rectangle {
	public Rectangle(java.awt.Rectangle r) {
		super(r);
	}

	public Rectangle(Point p) {
		super(p);
	}

	public Rectangle(Dimension d) {
		super(d);
	}

	public Rectangle(int width, int height) {
		super(width, height);
	}

	public Rectangle(Point p, Dimension d) {
		super(p, d);
	}

	public Rectangle(int x, int y, int width, int height) {
		super(x, y, width, height);
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
