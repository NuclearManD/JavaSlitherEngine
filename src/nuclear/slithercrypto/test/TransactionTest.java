package nuclear.slithercrypto.test;

import nuclear.slithercrypto.Crypt;
import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;

public class TransactionTest {
	public static void main(String[] args) {
		io.println("Making key...");
		ECDSAKey key=new ECDSAKey();
		byte[] lastBlockHash=Crypt.SHA256("lol get rekt");
		byte[] program_data={1,2,3,4,5,6,7,8,9};
		io.println("Making Daughter/Transaction pair...");
		DaughterPair pair=Transaction.makeFile(key.getPublicKey(), key.getPrivateKey(), program_data, lastBlockHash, "Drink Weed");
		io.println("Transaction:\t"+pair.tr.toString());
		io.println("Daughter:\t\t"+pair.block.toString());
		io.println("Valid: "+pair.tr.verify());
		key.prnt(key.getPrivateKey());
	}

}
