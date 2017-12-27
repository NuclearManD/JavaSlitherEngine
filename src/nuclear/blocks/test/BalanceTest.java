package nuclear.blocks.test;

import java.io.IOException;
import java.util.Base64;

import nuclear.blocks.client.ClientIface;
import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.BlockChainManager;
import nuclear.slitherge.top.io;

public class BalanceTest {

	public static void main(String[] args) {
		ECDSAKey key=new ECDSAKey();
		BlockChainManager man=new BlockChainManager();
		try {
			io.println("Downloading blockchain...");
			ClientIface iface=new ClientIface("192.168.1.150");
			iface.downloadBlockchain(man);
			io.println("Downloaded.");
			io.println("Our Balance: "+man.getCoinBalance(key.getPublicKey()));
			io.println("Our address: "+Base64.getEncoder().encodeToString(key.getPublicKey()));
			io.println("Block 0 miner: "+Base64.getEncoder().encodeToString(man.getBlockByIndex(0).getMiner()));
			io.println("Block 0 miner balance: "+man.getCoinBalance(man.getBlockByIndex(0).getMiner()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
