package nuclear.slitherge.computers;

import nuclear.slitherge.top.Thing;

public abstract class Motherboard extends Thing{
	public CPU CPU;
	public Hardware modules[]={};
	public Motherboard(double x, double y) {
		super(x, y);
		modules= new Hardware[0];
	}
	public void installModule(Hardware module){
		Hardware mnova[]=new Hardware[modules.length+1];
		for(int i=0;i<modules.length;i++){
			mnova[i]=modules[i];
		}
		mnova[modules.length]=module;
		modules=mnova;
	}
	public abstract void update();
}
