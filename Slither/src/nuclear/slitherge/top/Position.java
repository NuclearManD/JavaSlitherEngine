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
}
