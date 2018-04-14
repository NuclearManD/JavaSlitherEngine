package nuclear.slitherge.physics;

import java.util.ArrayList;

import nuclear.slitherge.physics.Rigidbody;

public class World3D {
	public ArrayList<Rigidbody> objects= new ArrayList<Rigidbody>();
	public void update(){
		for(int x=0;x<objects.size();x++){
			Rigidbody e =objects.get(x);
			e.update(this);
		}
	}
}
