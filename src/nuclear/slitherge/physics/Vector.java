package nuclear.slitherge.physics;

public interface Vector {
	public Vector normalize();
	public Vector neg();
	public double magnitude();
	public Vector mult(double x);
	public Vector divide(double x);
}
