package nuclear.slitherge.physics.test;

import nuclear.slitherge.physics.Rigidbody;
import nuclear.slitherge.physics.Vector3;
import nuclear.slitherge.physics.World3D;
import nuclear.slitherge.top.io;

public class GravityTest {
	public static final int LEN=10;
	public static void main(String[] args) {
		Earth a=new Earth(new Vector3(0,0,0));
		Rigidbody b=new Rigidbody(new Vector3(384.4E6,0,0));
		b.velocity=new Vector3(0,1022,0);
		b.mass=7.34767309E22; // moon mass
		World3D world=new World3D();
		world.objects.add(a);
		world.objects.add(b);
		
		io.println("field: "+a.field.getConditional(b).toString());
		
		double[] data=new double[LEN];
		
		for(int j=0;j<LEN;j++){ // simulate 10 orbits
			for(int i=0;b.position.x>=0;i++){
				world.update();
				//if(i%216000==0)
					//io.println(b.position.toString());
			}
			for(int i=0;b.position.x<=0;i++){
				world.update();
				//if(i%216000==0)
					//io.println(b.position.toString());
			}
			io.println(b.position.toString());
			io.println("Magnitude: "+b.position.magnitude());
			data[j]=b.position.magnitude();
		}
		io.println(b.position.toString());
		io.println("Magnitude: "+b.position.magnitude());
		for(double i:data){
			io.print(i+", ");
		}
	}

}
