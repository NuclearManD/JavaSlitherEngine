package nuclear.slitherge.computers;

public class RTC extends Hardware {
	int time[]={0,0,0,0,0,0,0,0};
	public RTC() {
		super("Generic RTC");
		// TODO Auto-generated constructor stub
	}

	@Override
	public byte ioRead(int adr) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void ioWrite(int adr, byte data) {
		// TODO Auto-generated method stub
		
	}

}
