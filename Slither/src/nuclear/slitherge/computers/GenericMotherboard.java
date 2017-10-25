package nuclear.slitherge.computers;

import nuclear.slitherge.computers.CPU_BIS.CPU_BIS;
import nuclear.slitherge.top.Position;

public class GenericMotherboard extends Motherboard {

	public GenericMotherboard(double x, double y, ComputerInterfaceHandler ihandler) {
		super(x, y, ihandler);
		CPU=new CPU_BIS(ihandler);
	}

	@Override
	public void update() {
		CPU.run();
	}

	@Override
	public int use(String[] args, Position pos, boolean print) {
		return -3;
	}

}
