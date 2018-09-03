package nuclaer.slither3d;

import java.util.ArrayList;

public class Universe {
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	public Universe(){
		
	}
	public void update(){
		for(GameObject i:objects){
			i._upd();
		}
	}
	public void addObject(GameObject i){
		objects.add(i);
	}
	public void destroyObject(GameObject i){
		objects.remove(i);
	}
}
