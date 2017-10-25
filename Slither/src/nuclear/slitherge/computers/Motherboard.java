package nuclear.slitherge.computers;

import nuclear.slitherge.computers.CPU_BIS.CPU_BIS;
import nuclear.slitherge.top.Thing;

public abstract class Motherboard extends Thing{
	int clockspeed=4000000;
	CPU CPU;
	Hardware modules[]={};
	public Motherboard(double x, double y, ComputerInterfaceHandler ihandler) {
		super(x, y);
		CPU=new CPU_BIS(ihandler);
		modules= new Hardware[3];
		modules[0]=new RTC();
		modules[1]=new NGT20();
		modules[2]=new Keyboard();
	}
	public Motherboard(double x, double y, CPU cpu, int clock) {
		super(x, y);
		modules= new Hardware[3];
		modules[0]=new RTC();
		modules[1]=new NGT20();
		modules[2]=new Keyboard();
		clockspeed=clock;
		CPU=cpu;
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
	public String toString(){
		String o= "Computer; Specs:\n"+
				CPU+" running at "+((float)(clockspeed)/1000000)+"MHz\nInstalled Modules:\n";
		for(int i=0;i<modules.length;i++){
			o+="  "+modules[i].getName()+'\n';
		}
		return o;
	}
}
