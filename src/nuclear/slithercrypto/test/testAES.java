package nuclear.slithercrypto.test;

import nuclear.slithercrypto.Crypt;
import nuclear.slitherge.top.io;

public class testAES {

	public static void main(String[] args){
		io.println("Encrypting 'Hello World'...");
		byte[] enc=Crypt.EncryptAES("Hello World".getBytes(), "test");
		io.println(new String(Crypt.DecryptAES(enc, "test")));
		io.println(new String(enc));

	}

}
