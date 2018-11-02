package nuclear.slitherge.flat;

import java.util.ArrayList;

import nuclear.slitherge.top.Position;

public class Dimension {
	
	private int x_size = -1;
	private int y_size = -1;
	
	protected ArrayList<Entity> entities= new ArrayList<Entity>();
	protected ArrayList<Thing>[][] things;

	protected ArrayList<Entity> entities_nadd= new ArrayList<Entity>();
	protected ArrayList<Thing>[][] things_nadd;
	protected ArrayList<Entity> entities_nrm= new ArrayList<Entity>();
	protected ArrayList<Thing>[][] things_nrm;
	
	private String name="in the unknown";
	protected int id=-1;
	public Dimension(String n) {
		name=n;
	}
	public void setId(){
		id=Universe.dimensions.indexOf(this);
	}
	private synchronized void update_lists(){
		for(Entity e:entities_nadd)
			entities.add(e);
		for(int x=0;x<x_size;x++){
			for(int y=0;y<y_size;y++){
				for(Thing e:things_nadd[x][y])
					things[x][y].add(e);
				things_nadd[x][y].clear();
			}
		}
		for(Entity e:entities_nrm)
			entities.remove(e);
		for(int x=0;x<x_size;x++){
			for(int y=0;y<y_size;y++){
				for(Thing e:things_nrm[x][y])
					things[x][y].remove(e);
				things_nrm[x][y].clear();
			}
		}
		entities_nadd.clear();
		entities_nrm.clear();
	}
	public void update(){
		if(id==-1)
			setId();
		for(int x=0;x<entities.size();x++){
			Entity e =entities.get(x);
			e.update();
		}
		for(int x=0;x<x_size;x++){
			for(int y=0;y<y_size;y++){
				for(Thing e:things[x][y])
					e.update();
			}
		}
		safeUpdate();
		update_lists();
	}
	protected void safeUpdate(){}
	public void insertObject(int x, int y, Thing w) {
		things_nadd[x][y].add(w);
	}

	public void addEntity(Entity e) {
		entities_nadd.add(e);
	}

	public Entity find(int dim, String s) {
		for(int i=0;i<entities.size();i++){
			Entity e=entities.get(i);
			if(e.getName().equals(s))
				return entities.get(i);
		}
		return null;
	}
	public ArrayList<Thing> getThings(int x, int y){
		return things[x][y];
	}
	public Thing removeObject(int x, int y, String name){
		for(int i=0;i<things[x][y].size();i++){
			if(things[x][y].get(i).name.equals(name)){
				Thing tmp=things[x][y].get(i);
				things[x][y].remove(i);
				return tmp;
			}
		}
		return null;
	}
	public Thing removeObject(int x, int y, Thing ssc) {
		things_nrm[x][y].add(ssc);
		return ssc;
	}
	public Entity removeEntity(Entity entity) {
		entities_nrm.add(entity);
		return entity;
	}
	public int getId() {
		return id;
	}
	public ArrayList<Entity> allEntities() {
		return entities;
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
	public ArrayList<Entity> getEntities(double x, double y,double range) {
		ArrayList<Entity> tmp = new ArrayList<Entity>();
		for(Entity e : entities){
			if(e.getPos().sub(new Position(0,x,y)).magnitude()<range)tmp.add(e);
		}
		return tmp;
	}
}
