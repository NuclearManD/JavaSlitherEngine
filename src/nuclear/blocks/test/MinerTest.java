package nuclear.blocks.test;

import nuclear.blocks.node.Miner;
import nuclear.slithercrypto.blockchain.BlockChainManager;
import nuclear.slitherio.CLILogger;

public class MinerTest {

	public static void main(String[] args) {
		BlockChainManager man=new BlockChainManager();
		Miner miner=new Miner(man,new CLILogger(),true);
		miner.run();
	}

}
