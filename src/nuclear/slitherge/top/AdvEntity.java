package nuclear.slitherge.top;


public abstract class AdvEntity extends Entity {
	// this class basically handles collisions right now.  More may come later.
	protected boolean pathneeded = false;
	protected Thing collidedwith;
	public AdvEntity(int dimt, double x2, double y2) {
		super(dimt, x2, y2);
	}
	public void move(double xi, double yi){
		Rectangle hitbox=getHitbox();
		hitbox.x=xi-getWidth()/2;
		hitbox.y=yi-getHeight()/2;
		if(this.dimension>-1)
			for(Thing i:getDimension().allThings()){
				if(i instanceof Collidable){
					if(((Collidable) i).collides(hitbox))
						return;
				}
			}
		super.move(xi, yi);
	}
	public void move(Position n, Position predict){
		Rectangle hitbox=getHitbox();
		hitbox.x=n.x-getWidth()/2;
		hitbox.y=n.y-getHeight()/2;
		for(Thing i:getDimension().allThings()){
			if(i instanceof Collidable){
				if(((Collidable) i).collides(n))
				{
					collidedwith = i;
					//pathneeded = true;
					return;
				}
				if(((Collidable) i).collides(predict))
				{
					collidedwith = i;
					//pathneeded = true;
				}
			}
		}
		super.move(n);
	}
	public void move(Position n){
		Rectangle hitbox=getHitbox();
		hitbox.x=n.x-getWidth()/2;
		hitbox.y=n.y-getHeight()/2;
		for(Thing i:getDimension().allThings()){
			if(i instanceof Collidable){
				if(((Collidable) i).collides(hitbox))
				{
					collidedwith = i;
					//pathneeded = true;
					return;
				}
			}
		}
		super.move(n);
	}
	// duh.
	//public abstract double getMovementSpeed();
	public double getWidth(){return 1;};
	public double getHeight(){return 1;};
	public double getDistance(Thing t){
		return Math.sqrt(Math.pow(t.x-x,2)+Math.pow(t.y-y,2));
	}
	public abstract void doDamage(int damage, Entity e);
	public Rectangle getHitbox(){
		return new Rectangle(x-getWidth()/2,y-getHeight()/2,getWidth(),getHeight());
	}
}
