package nuclear.slitherge.flat;


public abstract class AdvEntity extends Entity {
	public AdvEntity(double x2, double y2) {
		super(x2, y2);
	}
	public void move(double xi, double yi){
		if(xi<0 || yi<0) return;
		Rectangle hitbox=getHitbox();
		hitbox.x=xi-getWidth()/2;
		hitbox.y=yi-getHeight()/2;
		for(Thing i:getDimension().getThings((int)Math.round(xi),(int)Math.round(yi))){
			if(i instanceof Collidable){
				if(((Collidable) i).collidable())
					return;
			}
		}
		super.move(xi, yi);
	}
	// duh.
	//public abstract double getMovementSpeed();
	public double getWidth(){return 1;};
	public double getHeight(){return 1;};
	public abstract void doDamage(int damage, Entity e);
	public Rectangle getHitbox(){
		return new Rectangle(x-getWidth()/2,y-getHeight()/2,getWidth(),getHeight());
	}
}
