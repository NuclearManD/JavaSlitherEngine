package nuclaer.slither3d;

import java.util.ArrayList;

public class Multiverse {
	ArrayList<Universe> universes=new ArrayList<Universe>();
	public void update(){
		for(Universe i:universes){
			i.update();
		}
	}
	public void addUniverse(Universe i){
		universes.add(i);
	}
}
