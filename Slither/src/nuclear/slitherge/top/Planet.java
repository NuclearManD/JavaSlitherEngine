package nuclear.slitherge.top;

import java.util.ArrayList;

public class Planet extends EntitySubdimensional {
	public Planet(int dimt,double x, double y, String n) {
		super(dimt,x, y,"on "+n);
		range=5;
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		ArrayList<Entity> entities = Universe.dimensions.get(dimension).getEntities(this);
		for(int i=0;i<entities.size();i++){
			Entity e=entities.get(i);
			e.dimension=dim;
			e.x=(int)Math.random()*Universe.max_pos;
			e.y=(int)Math.random()*Universe.max_pos;
			if(!e.isShielded){
				e.doDamage(150);
				if(e==Universe.player){
					io.println("You find yourself falling, while burning up, into the planet "+getName()+".");
					io.println("Perhaps you should use a star ship next time.");
				}
			}
			if(e==Universe.player)
				((Player)e).look();
		}
	}

}
