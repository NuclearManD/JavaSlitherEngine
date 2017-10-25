package nuclear.slitherge.physics;

public class NullCollider extends Collider{
	@Override
	public Vector2 collide(Collider collider) {
		return null; // don't collide with anything
	}
	
}
