package nuclear.slitherge.physics;

import nuclear.slitherge.top.Entity;
import nuclear.slitherge.top.Universe;
import nuclear.slitherge.top.io;

public abstract class Rigidbody2D extends Collidable{
	public Vector2 velocity;
	private double tick_len=Universe.tick_len;
	public Rigidbody2D(int dimt, double x, double y) {
		super(dimt, x, y);
		collider=new NullCollider();
		velocity=new Vector2(0, 0);
	}

	@Override
	public void update() {
		for (Entity e : Universe.myDim(this).getEntities(this)) {
			if(e instanceof Rigidbody2D){
				Vector2 collisionForce=((Rigidbody2D)e).getCollider().collide(collider,getVector());
				if(collisionForce!=null){
					addForce(collisionForce);
					((Rigidbody2D)e).addForce(collisionForce.neg());
				}
			}
		}
		Vector2 pos=new Vector2(x,y);
		pos=pos.add(velocity.mult(tick_len));
		x=pos.x;
		y=pos.y;
		fixedUpdate();
	}
	protected abstract void fixedUpdate();

	public void addForce(Vector2 force){
		io.println("Adding force "+force.toString()+" to velocity "+velocity.toString());
		velocity=velocity.add(force.mult(tick_len));
		io.println("New velocity: "+velocity.toString());
	}
}
