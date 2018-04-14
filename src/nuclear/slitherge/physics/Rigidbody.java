package nuclear.slitherge.physics;

import nuclear.slitherge.top.io;

public class Rigidbody{
	public Vector3 velocity, position;
	public double mass;
	private double dt=1.0/60.0;
	public Rigidbody(Vector3 position) {
		velocity=new Vector3(0, 0, 0);
		this.position=position;
	}

	public void update(World3D world) {
		position=position.add(velocity.mult(dt));
		fixedUpdate(world);
	}
	protected void fixedUpdate(World3D world){}

	public void addForce(Vector3 force){
		velocity=velocity.add(force.mult(dt).divide(mass));
	}
}
