package nuclear.slitherge.physics;

public class CircleCollider extends Collider {
	double radius;
	public CircleCollider(double r) {
		radius=r;
	}

	@Override
	public Vector2 collide(Collider collider,Vector2 offset) {
		double bound=collider.getBound(offset);
		if(bound!=-1&&offset.magnitude()<(bound+radius)) {
			return ;
		}else
			return null;
	}

	@Override
	public double getBound(Vector2 a) {
		return radius;
	}

}
