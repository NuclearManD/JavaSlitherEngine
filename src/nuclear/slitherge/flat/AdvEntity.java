package nuclear.slitherge.flat;


public abstract class AdvEntity extends Entity {
	public AdvEntity(double x2, double y2) {
		super(x2, y2);
	}
	public boolean move(double xi, double yi){
		if(xi<0 || yi<0) return false;
		for(Thing i:getDimension().getThings((int)Math.round(xi+getWidth()/2),(int)Math.round(yi+getHeight()/2))){
			if(i instanceof Collidable){
				if(((Collidable) i).collidable())
					return false;
			}
		}
		for(Thing i:getDimension().getThings((int)Math.round(xi-getWidth()/2),(int)Math.round(yi+getHeight()/2))){
			if(i instanceof Collidable){
				if(((Collidable) i).collidable())
					return false;
			}
		}
		for(Thing i:getDimension().getThings((int)Math.round(xi+getWidth()/2),(int)Math.round(yi-getHeight()/2))){
			if(i instanceof Collidable){
				if(((Collidable) i).collidable())
					return false;
			}
		}
		for(Thing i:getDimension().getThings((int)Math.round(xi-getWidth()/2),(int)Math.round(yi-getHeight()/2))){
			if(i instanceof Collidable){
				if(((Collidable) i).collidable())
					return false;
			}
		}
		return super.move(xi, yi);
	}
	public double getWidth(){return 1;};
	public double getHeight(){return 1;};
	public abstract void doDamage(double damage, Entity e);
}
