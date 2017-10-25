package nuclear.slitherge.physics;

public abstract class Collider {

	public abstract Vector2 collide(Collider collider, Vector2 offset);
	public abstract double getBound(Vector2 a);
}
