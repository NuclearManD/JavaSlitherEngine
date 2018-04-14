package nuclear.slitherge.physics;

public class FieldGravitational extends Field {

	public FieldGravitational(Rigidbody s) {
		super(s,6.67408E-11);
	}

	@Override
	public Vector3 getConditional(Rigidbody r) {
		return r.position.neg().add(self.position).normalize().mult(r.mass*self.mass);
	}

}
