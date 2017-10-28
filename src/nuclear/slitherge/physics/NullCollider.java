package nuclear.slitherge.physics;

public class NullCollider extends Collider{
	@Override
	public Vector2 collide(Collider collider, Vector2 offset) {
		return null; // don't collide with anything
	}

	@Override
	public double getBound(Vector2 a) {
		return -1;
	}
	
}
