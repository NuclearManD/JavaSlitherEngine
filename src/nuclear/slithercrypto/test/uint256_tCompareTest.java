package nuclear.slithercrypto.test;

import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherS;
import nuclear.slitherio.uint256_t;

public class uint256_tCompareTest {

	public static void main(String[] args) {
		uint256_t u1=uint256_t.make(SlitherS.longToBytes(-115200));
		io.println("115200="+u1);
		
	}

}
