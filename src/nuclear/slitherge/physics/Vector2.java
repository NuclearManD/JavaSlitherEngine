package nuclear.slitherge.physics;

import nuclear.slitherge.top.Position;

public class Vector2 implements Vector{
	public double x;
	public double y;
	public Vector2() {
		x=0;
		y=0;
	}
	public Vector2(Position s){
		x=s.x;
		y=s.y;
	}
	public Vector2(double x, double y){
		this.x=x;
		this.y=y;
	}
	public Vector2 normalize(){
		Vector2 out=new Vector2();
		double l=magnitude();
		out.x=x/l;
		out.y=y/l;
		return out;
	}
	public double magnitude() {
		return Math.sqrt(x*x+y*y);
	}
	public Vector2 add(Vector2 b) {
		return new Vector2(x+b.x,y+b.y);
	}
	public Vector2 mult(double multiplier) {
		return new Vector2(x*multiplier,y*multiplier);
	}
	public Vector2 divide(double d) {
		return new Vector2(x/d,y/d);
	}
	public Vector2 neg() {
		return new Vector2(-x,-y);
	}
	public double Distance(Vector2 b){
		return new Vector2(x-b.x, y-b.y).magnitude();
	}
	public String toString(){
		return "Vector2("+x+','+y+")";
	}
}
