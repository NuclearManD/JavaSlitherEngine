package nuclear.slitherge.physics;

public abstract class Collider {

	public abstract Vector2 collide(Collider collider, Vector2 offset);

	public abstract double getBound(Vector2 a);
	public boolean collidesWith(Collider c,Vector2 offset){
		return collide(c, offset)!=null;
	}
}
