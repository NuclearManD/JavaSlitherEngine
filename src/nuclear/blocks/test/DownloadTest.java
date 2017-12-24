package nuclear.blocks.test;

import java.io.IOException;

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
		byte[] program_data={1,2,3,4,5,6,7,8,9};
		io.println("Uploading a pair...");
		DaughterPair pair=Transaction.makeFile(key.getPublicKey(), key.getPrivateKey(), program_data, lastBlockHash, "Drink Weed");
		try {
			ClientIface iface=new ClientIface("192.168.1.132");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
