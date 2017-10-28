package nuclear.slitherge.physics;

import nuclear.slitherge.top.Entity;

public abstract class Collidable extends Entity {
	protected Collider collider;
	public Collidable(int dimt, double x2, double y2) {
		super(dimt, x2, y2);
	}
	public boolean collidesWith(Collider c, Vector2 offset) {
		return collider.collidesWith(c, offset);
	}
	public Collider getCollider(){
		return collider;
	}

}
