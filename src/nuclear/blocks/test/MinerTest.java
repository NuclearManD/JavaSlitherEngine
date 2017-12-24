package nuclear.blocks.test;

import nuclear.blocks.node.Miner;
import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.BlockChainManager;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;
import nuclear.slitherio.CLILogger;

public class MinerTest {
	public static void main(String[] args) {
		ECDSAKey key=new ECDSAKey();
		BlockChainManager man=new BlockChainManager();
		Miner miner=new Miner(man,new CLILogger(),true,key.getPublicKey());
		Thread t=new Thread(miner);
		t.start();
		io.println("Miner started...");
		ECDSAKey key2=new ECDSAKey();// burn some time for the miner to start
		long time=System.currentTimeMillis()+2000;
		while(time>System.currentTimeMillis());
		man.addTransaction(Transaction.sendCoins(key.getPublicKey(), key2.getPublicKey(), key.getPrivateKey(), 10000));
		io.println("Transaction created. Block data size: "+man.getCurrent().getData().length);
		io.println("Block has "+man.getCurrent().numTransactions()+" transactions");
		while(true);
	}

}
