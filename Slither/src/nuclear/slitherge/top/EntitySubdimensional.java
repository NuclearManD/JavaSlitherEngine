package nuclear.slitherge.top;

import java.util.ArrayList;

public abstract class EntitySubdimensional extends Entity {
	protected Dimension subdim;
	protected int dim;
	public EntitySubdimensional(int dimt,double x, double y, String n) {
		super(dimt,x, y);
		setName(n);
		subdim=new Dimension(n);
		Universe.dimensions.add(subdim);
		dim=Universe.dimensions.indexOf(subdim);
		if(dim==-1)
			io.println("ERROR: -1");
	}
	public Dimension getSubDimension(){
		return subdim;
	}
	protected void onDeath(){
		ArrayList<Entity> els=getSubDimension().allEntities();
		while(!els.isEmpty()) {
			Entity e=els.get(0);
			e.onReceiveMessage(name+" foundered!", this);
			e.move(dimension, x, y);
			e.doDamage(300);
		}
		Universe.removeDimension(getSubDimension());
	}
}
