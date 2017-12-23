package nuclear.slitherge.top;

import java.util.ArrayList;

//import com.sun.xml.internal.fastinfoset.util.PrefixArray;

import nuclear.slitherge.physics.Vector2;

public abstract class Entity {
	protected String name="";
	protected int health=1; // at least 1 starting health
	protected int armor;
	protected int attack;
	protected Thing[] inv;
	protected int objs=0;
	protected int dimension;
	protected double x, y;
	public int team=0;
	public boolean isShielded=false;
	public double range=10.0;
	private boolean isDead=false;
	protected String prefix="a ";
	public Entity(int dimt,double x2, double y2){
		x=x2;
		y=y2;
		dimension=dimt;
	}
	public String getPrefix(){
		return prefix;
	}
	public void doDamage(int damage){
		health-=damage;
		if(health<=0){
			die();
		}
	}
	protected void die(){
		getDimension().removeEntity(this);
		isDead=true;
		onDeath();
	}
	public int getHealth(){
		return health;
	}
	public abstract void update();
	public void move(double xi, double yi){
		Position tmp=new Position(dimension,x,y);
		x=xi;
		y=yi;
		onMove(tmp);
	}
	public Position getPos(){
		return new Position(dimension,x,y);
	}
	public Vector2 getVector(){
		return new Vector2(x,y);
	}
	public double getX() {
		return x;
	}
	protected Dimension getDimension() {
		return Universe.dimensions.get(dimension);
	}
	public double getY() {
		return y;
	}
	protected void onMove(Position old) {
		
	}
	public void move(int dimension2, double x2, double y2) {
		Position tmp=new Position(dimension,x,y);
		if(dimension2==-1)
			io.println("ERROR");
		Universe.teleportEntity(this, dimension2);
		x=x2;
		y=y2;
		onMove(tmp);
	}
	protected void sendMessage(String msg){
		ArrayList<Entity> e=getDimension().getEntities(this);
		for (Entity entity : e) {
			entity.onReceiveMessage(msg,this);
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription(){
		return "There is "+prefix+getName();
	}
	public String look(){
		String out="";
		for (Entity e : getDimension().getEntities(this)) {
			String tmp=e.getDescription();
			if(tmp != null)
				out+=tmp+'\n';
		}
		for (Thing e : getDimension().getThings(this)) {
			String tmp=e.getDescription();
			if(tmp != null)
				out+=tmp+'\n';
		}
		return out;
	}
	protected void onDeath(){
		
	}
	public void onReceiveMessage(String msg,Entity src){
		
	}
	public void onNotify(String string, Entity e) {
		
	}
	public boolean isDead(){
		return isDead;
	}
}
