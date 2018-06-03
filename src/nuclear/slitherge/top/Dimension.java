package nuclear.slitherge.top;

import java.util.ArrayList;

public class Dimension {
	protected ArrayList<Entity> entities= new ArrayList<Entity>();
	protected ArrayList<Thing> things= new ArrayList<Thing>();
	private String name="in the unknown";
	protected int id=-1;
	public Dimension(String n) {
		name=n;
	}
	public void setId(){
		id=Universe.dimensions.indexOf(this);
	}
	public void update(){
		if(id==-1)
			setId();
		for(int x=0;x<entities.size();x++){
			Entity e =entities.get(x);
			e.dimension=id;
			e.update();
		}
		for(int x=0;x<things.size();x++){
			things.get(x).update();
		}
		safeUpdate();
	}
	protected void safeUpdate(){}
	public void insertObject(Thing w) {
		things.add(w);
	}

	public void addEntity(Entity e) {
		entities.add(e);
		e.dimension=id;
	}

	public Entity find(int dim, String s) {
		for(int i=0;i<entities.size();i++){
			Entity e=entities.get(i);
			if(e.getName().equals(s)&&(dim==e.dimension||dim==-1))
				return entities.get(i);
		}
		return null;
	}
	public ArrayList<Entity> getEntities(Entity c){
		ArrayList<Entity> o=new ArrayList<Entity>();
		for(Entity e : entities){
			if(e!=c&&Position.Len(e.x,e.y,c.x,c.y)<c.range){
				o.add(e);
			}
		}
		return o;
	}
	public ArrayList<Thing> getThings(Entity c){
		ArrayList<Thing> o=new ArrayList<Thing>();
		for(Thing e : things){
			if(Position.Len(e.x,e.y,c.x,c.y)<c.range){
				o.add(e);
			}
		}
		return o;
	}
	@Deprecated
	public String getDescription(Entity c){
		String o="You can see ";
		for(int i=0;i<things.size();i++){
			Thing e=things.get(i);
			if(Position.Len(e.x,e.y,c.x,c.y)<c.range){
				o+="a "+things.get(i).name+", ";
			}
		}
		String q="  There is also ";
		for(int i=0;i<entities.size();i++){
			Entity e=entities.get(i);
			if(e!=c&&Position.Len(e.x,e.y,c.x,c.y)<c.range){
				q+=/*"a "+*/e.getName()+", ";
			}
		}
		if(!q.equals("  There is also "))
			o+=q;
		if(o.isEmpty()) {
			o="There is nothing around you.  Absolutely nothing.";
		}
		return o;
	}
	public Thing removeObject(String name){
		for(int i=0;i<things.size();i++){
			if(things.get(i).name.equals(name)){
				Thing tmp=things.get(i);
				things.remove(i);
				return tmp;
			}
		}
		return null;
	}
	public boolean containsObject(Thing ssc) {
		return things.contains(ssc);
	}
	public Thing removeObject(Thing ssc) {
		things.remove(ssc);
		return ssc;
	}
	public Entity removeEntity(Entity entity) {
		entities.remove(entity);
		return entity;
	}
	//public boolean objectPresent(Thing ssc, Entity c) {
		// TODO make this function
		//return false;
	//}
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}
	public ArrayList<Entity> allEntities() {
		return entities;
	}
	public ArrayList<Thing> allThings() {
		return things;
	}
	public String toString(){
		return "Dimension '"+name+"'";
	}
	public String getName(Entity e){
		return name;
	}
	public String getName(){
		return name;
	}
	public void setName(String n){
		name=n;
	}
}
