package nuclear.slitherge.computers;

public class GenericCPUInterface extends ComputerInterfaceHandler{

	private byte[] mem;
	private Hardware[] hardware;

	public GenericCPUInterface(byte[] RAM,Hardware hardwares[]) {
		mem=RAM;
		hardware=hardwares;
	}

	@Override
	public void ioWrite(byte data, int adr) {
		hardware[adr>>4].ioWrite(adr&15, data);
	}

	@Override
	public byte ioRead(int adr) {
		return hardware[adr>>4].ioRead(adr&15);
	}

	@Override
	public void memWrite(byte data, long adr) {
		if(adr<mem.length)
			mem[(int) adr]=data;
	}

	@Override
	public byte memRead(long adr) {
		if(adr<mem.length)
			return mem[(int) adr];
		else
			return (byte) (Math.random()*256);
	}

}
