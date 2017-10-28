package nuclear.slitherge.computers;

import nuclear.slitherge.top.Position;
import nuclear.slitherge.top.Thing;
import nuclear.slitherge.top.io;

public abstract class CPU extends Thing {
	private int tick_count;
	private ComputerInterfaceHandler iface;
	public CPU(ComputerInterfaceHandler i, int ticks_per_update){
		super(0, 0);
		tick_count=ticks_per_update;
		name=getName();
		iface=i;
	}
	public CPU(ComputerInterfaceHandler i, double x, double y, int ticks_per_update){
		super(x, y);
		tick_count=ticks_per_update;
		name=getName();
		iface=i;
	}
	public CPU(ComputerInterfaceHandler i, double x, double y){
		super(x, y);
		name=getName();
		tick_count=100;
		iface=i;
	}
	public CPU(ComputerInterfaceHandler i){
		super(0, 0);
		tick_count=100;
		name=getName();
		iface=i;
	}
	public abstract void reset();
	public abstract void tick();
	public abstract String getName();
	public void run(){
		for(int i=0;i<tick_count;i++)
			tick();
	}
	public void setTicksPerUpdate(int q){
		tick_count=q;
	}
	public int use(String[] args, Position pos, boolean print) {
		if(print)io.println("You can't use a "+name);
		return -3;
	}
	protected void ioWrite(byte data, int adr){
		iface.ioWrite(data, adr);
	}
	protected byte ioRead(int adr){
		return iface.ioRead(adr);
	}
	protected byte memRead(long adr){
		return iface.memRead(adr);
	}
	protected void memWrite(byte data, long adr){
		iface.memWrite(data, adr);
	}
}
