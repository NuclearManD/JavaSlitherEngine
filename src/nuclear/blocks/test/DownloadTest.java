package nuclear.blocks.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import nuclear.blocks.client.ClientIface;
import nuclear.slithercrypto.Crypt;
import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.BlockChainManager;
import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;

public class DownloadTest {

	public static void main(String[] args) {
		io.println("Making key...");
		ECDSAKey key=new ECDSAKey();
		byte[] lastBlockHash=Crypt.SHA256("lol get rekt");
		byte[] program_data="Hey, it worked!!!!!!!".getBytes(StandardCharsets.UTF_8);
		io.println("Uploading a pair...");
		DaughterPair pair=Transaction.makeFile(key.getPublicKey(), key.getPrivateKey(), program_data, lastBlockHash, "Drink Weed");
		try {
			ClientIface iface=new ClientIface("192.168.1.150");
			if(iface.uploadPair(pair))
				io.print("SUCCESS...");
			else{
				io.println("ERROR.");
				return;
			}
			io.println("Creating blockchain manager...");
			BlockChainManager man=new BlockChainManager();
			io.println("Downloading blocks...");
			io.println("Downloaded "+iface.downloadBlockchain(man)+" new blocks.");
			io.println(new String(man.readFile("Drink Weed", key.getPublicKey())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
