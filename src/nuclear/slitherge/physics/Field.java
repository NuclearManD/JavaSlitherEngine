package nuclear.slitherge.physics;

import nuclear.slitherge.top.io;

public abstract class Field {
	private double strength=0.1;
	// field force equation: s*c/sq(r)  where:
	//   s is the strength constant for the field type
	//   c is the result of getConditional()
	//   r is the distance from the center of the field
	
	protected Rigidbody self;
	
	public Field(Rigidbody r, double s){
		strength=s;
		self=r;
	}
	public abstract Vector3 getConditional(Rigidbody r);
	public void applyForce(Rigidbody object){
		Vector3 dV=self.position.neg().add(object.position);
		double a=strength/(dV.x*dV.x+dV.y*dV.y+dV.z*dV.z);
		object.addForce(getConditional(object).mult(a));
	}
}
