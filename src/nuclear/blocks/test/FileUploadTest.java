package nuclear.blocks.test;

import java.io.IOException;

import nuclear.blocks.client.ClientIface;
import nuclear.slithercrypto.Crypt;
import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;

public class FileUploadTest {
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
		try {
			ClientIface iface=new ClientIface("192.168.1.150");
			io.print("SUCCESS: "+iface.uploadPair(pair));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
