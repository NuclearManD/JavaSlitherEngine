package nuclear.slitherge.computers;

import nuclear.slitherge.top.Entity;

public abstract class ComputerCase extends Entity {
	protected Motherboard mobo;
	protected boolean isOn=false;
	public ComputerCase(int dimt,double x2, double y2, Motherboard m) {
		super(dimt,x2, y2);
		mobo=m;
	}

	@Override
	public void update() {
		if(isOn)
			mobo.update();
	}
	public void installModule(Hardware module){
		mobo.installModule(module);
	}

}
