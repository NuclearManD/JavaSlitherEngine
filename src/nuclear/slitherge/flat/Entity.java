package nuclear.slitherge.flat;

import java.util.ArrayList;

//import com.sun.xml.internal.fastinfoset.util.PrefixArray;

import nuclear.slitherge.physics.Vector2;
import nuclear.slitherge.top.Position;

public abstract class Entity {
	protected String name="";
	protected double health=1; // at least 1 starting health
	protected double x, y;
	protected double audioRange = 15;
	Dimension our_dim;
	int our_dim_id = -1;
	public Entity(double x2, double y2){
		x=x2;
		y=y2;
	}
	public void doDamage(int damage){
		health-=damage;
		if(health<=0){
			onDeath();
		}
	}
	public double getHealth(){
		return health;
	}
	public abstract void update();
	public boolean move(double xi, double yi){
		x=xi;
		y=yi;
		onMove();
		return true;
	}
	public Position getPos(){
		return new Position(our_dim_id,x,y);
	}
	public Vector2 getVector(){
		return new Vector2(x,y);
	}
	public double getX() {
		return x;
	}
	public Dimension getDimension() {
		if(our_dim==null)return Universe.currentDimension();
		return our_dim;
	}
	public double getY() {
		return y;
	}
	protected void onMove() {
		
	}
	public boolean move(int dim, double x2, double y2) {
		for(Dimension i:Universe.dimensions){
			if(i.entities.contains(this))i.removeEntity(this);
		}
		Universe.dimensions.get(dim).addEntity(this);
		
		x=x2;
		y=y2;
		onMove();
		return true;
	}
	protected void sendMessage(String msg){
		ArrayList<Entity> e=getDimension().allEntities();
		for (Entity entity : e) {
			if(entity.getDistance(this)<audioRange)
				entity.onReceiveMessage(msg,this);
		}
	}

	protected void sendMessageAnon(String msg) {
		ArrayList<Entity> e=getDimension().allEntities();
		for (Entity entity : e) {
			if(entity.getDistance(this)<audioRange)
				entity.onReceiveMessage(msg,null);
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	protected void onDeath(){
		
	}
	public void onReceiveMessage(String msg,Entity src){
		
	}
	public void onNotify(String string, Entity e) {
		
	}
	public boolean isDead(){
		return health<=0;
	}
	public double getDistance(Entity e){
		return Math.sqrt(Math.pow(e.x-x,2)+Math.pow(e.y-y,2));
	}
}
