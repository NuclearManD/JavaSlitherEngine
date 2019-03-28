package nuclear.slitherge.flat;

import java.util.ArrayList;

import nuclear.slitherge.top.Position;

public class Dimension {
	
	public int current_y, current_x;
	private int x_size = -1;
	private int y_size = -1;
	
	protected ArrayList<Entity> entities= new ArrayList<Entity>();
	protected ArrayList<Thing>[][] things;

	private ArrayList<Entity> entities_nadd= new ArrayList<Entity>();
	protected ArrayList<Thing>[][] things_nadd;
	private ArrayList<Entity> entities_nrm= new ArrayList<Entity>();
	protected ArrayList<Thing>[][] things_nrm;
	
	private String name="in the unknown";
	protected int id=-1;
	public Dimension(String n, int xs, int ys) {
		x_size=xs;
		y_size=ys;
		things_nrm = new ArrayList[xs][ys];
		things_nadd = new ArrayList[xs][ys];
		things = new ArrayList[xs][ys];
		for(int x=0;x<x_size;x++){
			for(int y=0;y<y_size;y++){
				things_nadd[x][y]=new ArrayList<Thing>();
				things_nrm[x][y]=new ArrayList<Thing>();
				things[x][y]=new ArrayList<Thing>();
			}
		}
		name=n;
	}
	public void setId(){
		id=Universe.dimensions.indexOf(this);
	}
	private boolean isUpdatingLists = false;
	private boolean entityListDirty = false;
	private boolean thingListDirty = false;
	private boolean isListLocked = false;
	public boolean isUpdatingLists() {
		return isUpdatingLists;
	}
	protected synchronized void update_lists(){
		while(isListLocked)Thread.yield();
		if(entityListDirty) {
			isUpdatingLists = true;
			for(Entity e:entities_nadd)
				entities.add(e);
			for(Entity e:entities_nrm)
				entities.remove(e);
			entities_nadd.clear();
			entities_nrm.clear();
			entityListDirty = false;
		}
		if(thingListDirty) {
			isUpdatingLists = true;
			for(int x=0;x<x_size;x++){
				for(int y=0;y<y_size;y++){
					for(Thing e:things_nrm[x][y])
						things[x][y].remove(e);
					things_nrm[x][y].clear();
				}
			}
			for(int x=0;x<x_size;x++){
				for(int y=0;y<y_size;y++){
					for(Thing e:things_nadd[x][y])
						things[x][y].add(e);
					things_nadd[x][y].clear();
				}
			}
			thingListDirty  = false;
		}
		isUpdatingLists = false;
	}
	public void update(){
		while(isUpdatingLists)Thread.yield();
		if(id==-1)
			setId();
		for(int x=0;x<entities.size();x++){
			Entity e =entities.get(x);
			e.our_dim=this;
			e.our_dim_id = Universe.dimCurrent;
			e.update();
		}
		for(int x=0;x<x_size;x++){
			current_x=x;
			for(int y=0;y<y_size;y++){
				current_y=y;
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
		thingListDirty = true;
	}

	public void addEntity(Entity e) {
		entities_nadd.add(e);
		e.our_dim=this;
		entityListDirty=true;
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
		if(x<0 || y<0 || x>=x_size || y>=y_size)return new ArrayList<Thing>();
		return things[x][y];
	}
	public Thing removeObject(int x, int y, String name){
		for(int i=0;i<things[x][y].size();i++){
			if(things[x][y].get(i).name.equals(name)){
				Thing tmp=things[x][y].get(i);
				things[x][y].remove(i);
				thingListDirty = true;
				return tmp;
			}
		}
		return null;
	}
	public Thing removeObject(int x, int y, Thing ssc) {
		things_nrm[x][y].add(ssc);
		thingListDirty = true;
		return ssc;
	}
	public Entity removeEntity(Entity entity) {
		entities_nrm.add(entity);
		entityListDirty=true;
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
	public ArrayList<Thing> allThings() {
		ArrayList<Thing> output = new ArrayList<Thing>();
		for(int x=0;x<x_size;x++){
			for(int y=0;y<y_size;y++){
				for(Thing e:things[x][y])output.add(e);
			}
		}
		return output;
	}
	public void lockLists() {
		isListLocked = true;
	}
	public void unlockLists() {
		isListLocked = false;
	}
	public int getYSize() {
		return y_size;
	}
	public int getXSize() {
		return x_size;
	}
}
