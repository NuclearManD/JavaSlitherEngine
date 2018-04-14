package nuclear.slitherge.physics.test;

import nuclear.slitherge.physics.FieldGravitational;
import nuclear.slitherge.physics.Rigidbody;
import nuclear.slitherge.physics.Vector3;
import nuclear.slitherge.physics.World3D;

public class Earth extends Rigidbody {
	public FieldGravitational field;
	public Earth(Vector3 position) {
		super(position);
		field=new FieldGravitational(this);
		mass=5.97219E24;
	}

	public void fixedUpdate(World3D world){
		for(Rigidbody i:world.objects)
			if(i!=this)
				field.applyForce(i);
	}
}
