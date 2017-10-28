package nuclear.slitherge.computers;

public abstract class Hardware {
	String name="";
	public Hardware(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public abstract byte ioRead(int adr);
	public abstract void ioWrite(int adr, byte data);
}
