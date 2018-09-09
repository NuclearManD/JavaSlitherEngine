package nuclear.nn;

import java.util.Arrays;

public class Neuron {
	private ActivationFunction activator;
	private double[] coefs;
	private double k;
	public Neuron(int inputs){
		coefs=new double[inputs];
		k=0;
	}
	public Neuron(int inputs, ActivationFunction activ){
		coefs=new double[inputs];
		k=0;
		activator=activ;
	}
	public ActivationFunction getActivator(){
		return activator;
	}
	public double run(double[] input){
		input=Arrays.copyOf(input, coefs.length);
		double q=0;
		for(int i=0;i<coefs.length;i++){
			q+=input[i]*coefs[i];
		}
		return activator.f(q+k);
	}
}
