package nuclear.slitherge.physics;

import nuclear.slitherge.top.Position;

public class Vector3 implements Vector{
	public double x,y,z;
	public Vector3() {
		x=0;
		y=0;
		z=0;
	}
	public Vector3(Position s){
		x=s.x;
		y=s.y;
		z=s.dim;
	}
	public Vector3(double x, double y, double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public Vector3 normalize(){
		Vector3 out=new Vector3();
		double l=magnitude();
		out.x=x/l;
		out.y=y/l;
		out.z=z/l;
		return out;
	}
	public double magnitude() {
		return Math.sqrt(x*x+y*y+z*z);
	}
	public Vector3 add(Vector3 b) {
		return new Vector3(x+b.x,y+b.y, z+b.z);
	}
	public Vector3 mult(double multiplier) {
		return new Vector3(x*multiplier,y*multiplier, z*multiplier);
	}
	public Vector3 divide(double d) {
		return new Vector3(x/d,y/d, z/d);
	}
	public Vector3 neg() {
		return new Vector3(-x,-y, -z);
	}
	public double Distance(Vector3 b){
		return new Vector3(x-b.x, y-b.y, z-b.z).magnitude();
	}
	public String toString(){
		return "Vector3("+x+','+y+','+z+")";
	}

}
