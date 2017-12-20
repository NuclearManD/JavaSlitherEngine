package nuclear.slithercrypto.test;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slitherge.top.io;

public class ToFrom64Test {

	public static void main(String[] args) {
		io.println("Making key...");
		ECDSAKey key=new ECDSAKey();
		byte data[]={1,2,3,4,5,6,7,8,9,0};
		try {
			io.println(ECDSAKey.verify(ECDSAKey.sigFrom64(ECDSAKey.sigTo64(key.sign(data))), data, key.getPublicKey())+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
