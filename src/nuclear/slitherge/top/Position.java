package nuclear.slitherge.top;

public class Position {

	public int dim;
	public double y, x;
	public Position(int a, double b, double c){
		dim=a;
		x=b;
		y=c;
	}
	public static double Len(double x1, double y1, double x2, double y2){
		double a=x1-x2;
		double b=y1-y2;
		return Math.sqrt(a*a+b*b);
	}
	public static String toCoords(double x, double y) {
		long ix, iy;
		ix=(long)(x/1e8);
		iy=(long)(y/1e8);
		return ix+":"+((long)x%100000000)+", "+iy+":"+((long)y%100000000);
	}
	public String toCoords() {
		return toCoords(x,y);
	}
	public double magnitude(){
		return Math.sqrt(x*x+y*y);
	}
	public Position normalized(){
		return div(magnitude());
	}
	public Position div(double a) {
		return new Position(dim,x/a,y/a);
	}
	public Position mul(double a) {
		return new Position(dim,x*a,y*a);
	}
	public Position sub(double a) {
		return new Position(dim,x-a,y-a);
	}
	public Position add(double a) {
		return new Position(dim,x+a,y+a);
	}
	public Position sub(Position a) {
		return new Position(dim,x-a.x,y-a.y);
	}
	public Position add(Position a) {
		return new Position(dim,x+a.x,y+a.y);
	}
	public double dp(Position a){
		return x*a.x+y*a.y;
	}
	public Position rotate(double degrees){
		degrees=degrees*Math.PI/180;
		return new Position(dim,x*Math.cos(degrees)+y*Math.sin(degrees),y*Math.cos(degrees)+x*Math.sin(degrees));
	}
}
