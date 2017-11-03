package nuclear.slithernode.nb;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;
import nuclear.slithernet.Client;

public class NuclearBlocksClient{
	public static void main(String[] args) {
		io.println("Please wait...");
		ECDSAKey key=new ECDSAKey();
		io.print("data:");
		String data=io.getStr();
		byte[] lshash;
		io.println("Making pair...");
		try {
			lshash=Client.ezPoll(1152, "LSHASH", "localhost").getBytes(StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		DaughterPair pair=Transaction.makeFile(key.getPublicKey(), key.getPrivateKey(), data.getBytes(StandardCharsets.UTF_8), lshash, "~~~");
		String out;
		try {
			out=Client.ezPoll(1152, "ADPAIR"+new String(pair.serialize(),StandardCharsets.UTF_8), "localhost");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if(out.equals("OK"))
			io.println("uploaded!");
		else
			io.println("Server Error!");
	}

}
