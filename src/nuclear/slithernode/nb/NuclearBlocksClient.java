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
			byte[] resp=Client.bytePoll(1152, "LSHASH".getBytes(StandardCharsets.UTF_8), "localhost");
			if(new String(resp,StandardCharsets.UTF_8).equals("CNFE")){
				io.println("Server did not recognise 'LSHASH'!");
				return;
			}else if(resp.length!=32)
				io.println("Warn: "+resp.length+" byte hash.");
			lshash=resp;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		DaughterPair pair=Transaction.makeFile(key.getPublicKey(), key.getPrivateKey(), data.getBytes(StandardCharsets.UTF_8), lshash, "~~~");
		if(!pair.verify()) {
			io.println("Error: invalid pair!");
			return;
		}
		io.println("made pair...");
		byte[] out;
		try {
			byte[] serial=pair.serialize();
			byte[] command=new byte[serial.length+6];
			int n=0;
			for(byte i:"ADPAIR".getBytes(StandardCharsets.UTF_8)) {
				command[n]=i;
				n++;
			}
			n=6;
			for(byte i:serial) {
				command[n]=i;
				n++;
			}
			out=Client.bytePoll(1152, command, "localhost");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if(new String(out,StandardCharsets.UTF_8).equals("OK"))
			io.println("uploaded!");
		else
			io.println("Server Error!");
	}

}
