package nuclear.nn;

public class SigmoidActivation extends ActivationFunction {

	@Override
	public double f(double a) {
		return 1.0/(1.0+Math.pow(Math.E,-a));
	}

	@Override
	public double err(double a) {
		return 0;
	}

}
