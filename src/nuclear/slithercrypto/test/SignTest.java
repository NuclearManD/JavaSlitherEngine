package nuclear.slithercrypto.test;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slitherge.top.io;

public class SignTest {
	public static void main(String[] args) {
		io.println("Making key...");
		ECDSAKey key=new ECDSAKey();
		byte data[]={1,2,3,4,5,6,7,8,9,0};
		io.println(ECDSAKey.verify(key.sign(data), data, key.getPublicKey())+"");
	}

}
