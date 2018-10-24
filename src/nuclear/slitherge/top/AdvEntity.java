package nuclear.slitherge.top;


public abstract class AdvEntity extends Entity {
	// this class basically handles collisions right now.  More may come later.
	protected boolean pathneeded = false;
	protected Thing collidedwith;
	public AdvEntity(int dimt, double x2, double y2) {
		super(dimt, x2, y2);
	}
	public void move(double xi, double yi){
		//Rectangle hitbox=getHitbox();
		if(this.dimension>-1)
			for(Thing i:getDimension().allThings()){
				if(i instanceof Collidable){
					if(((Collidable) i).collides(xi,yi))
						return;
				}
			}
		super.move(xi, yi);
	}
	public void move(Position n, Position predict){
		//Rectangle hitbox=getHitbox();
		for(Thing i:getDimension().allThings()){
			if(i instanceof Collidable){
				if(((Collidable) i).collides(n))
				{
					collidedwith = i;
					pathneeded = true;
					return;
				}
				if(((Collidable) i).collides(predict))
				{
					collidedwith = i;
					pathneeded = true;
				}
			}
		}
		super.move(n);
	}
	public void move(Position n){
		//Rectangle hitbox=getHitbox();
		for(Thing i:getDimension().allThings()){
			if(i instanceof Collidable){
				if(((Collidable) i).collides(n))
				{
					collidedwith = i;
					pathneeded = true;
					return;
				}
			}
		}
		super.move(n);
	}
	// duh.
	//public abstract double getMovementSpeed();
	public double getWidth(){return -1;};
	public double getHeight(){return -1;};
	public double getDistance(Thing t)
	{
		
		return Math.sqrt(Math.pow(t.x-x,2)+Math.pow(t.y-y,2));
	}
	public abstract void doDamage(int damage, Entity e);
	/*public Rectangle getHitbox(){
		return new Rectangle(1,1);
	}*/
}
