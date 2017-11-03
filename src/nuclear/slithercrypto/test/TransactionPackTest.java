package nuclear.slithercrypto.test;

import java.nio.charset.StandardCharsets;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;

public class TransactionPackTest {
	public static void main(String[] args) {
		ECDSAKey key=new ECDSAKey();
		byte[] pgm={1,2,43,4,5,6,7,87,8,9,9,0,0};
		Transaction t=Transaction.makeFile(key.getPublicKey(), key.getPrivateKey(), pgm, new byte[32], "Test").tr;
		Transaction t2=new Transaction(t.pack());
		io.println(t2.toString());
		io.println("Meta: "+new String(t2.getMeta(),StandardCharsets.UTF_8));
		if(t2.type!=t.type)
			io.println("Error: types don't match!");
	}

}
