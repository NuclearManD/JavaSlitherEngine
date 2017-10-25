package nuclear.slitherge.computers;

public abstract class ComputerInterfaceHandler {
	public abstract void ioWrite(byte data, int adr);
	public abstract byte ioRead(int adr);
	public abstract void memWrite(byte data, long adr);
	public abstract byte memRead(long adr);
}
