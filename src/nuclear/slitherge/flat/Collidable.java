package nuclear.slitherge.flat;

import nuclear.slitherge.top.Position;

public interface Collidable {
	//public boolean collides(Entity e);
	public boolean collides(double xi, double yi);
	public boolean collides(Position p);
	public boolean collides(Rectangle r);
}
