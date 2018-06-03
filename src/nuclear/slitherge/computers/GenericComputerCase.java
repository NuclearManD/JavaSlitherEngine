package nuclear.slitherge.computers;

import nuclear.slitherge.top.Entity;

public class GenericComputerCase extends ComputerCase {

	public GenericComputerCase(int dimt,double x, double y) {
		super(dimt,x, y,new GenericMotherboard(x, y, new GenericCPUInterface(new byte[4096], new Hardware[0])));
	}
	public String getName(){
		if(isOn)
			return "computer (ON)";
		return "computer";
	}
	public String getDescription(){
		if(isOn)
			return "A fan whirs in the background near the eerie glow of a computer.";
		else
			return "A computer sits nearby.";
	}
	@Override
	public void onReceiveMessage(String msg,Entity src) {
		if(msg.equalsIgnoreCase("computer on")){
			isOn=true;
			sendMessage("A computer turns on nearby.");
		}
	}

}
