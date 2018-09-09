package nuclear.nn;

public abstract class ActivationFunction {
	public abstract double f(double a);
	public abstract double err(double a);
}
