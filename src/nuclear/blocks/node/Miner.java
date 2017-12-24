package nuclear.blocks.node;

import nuclear.slithercrypto.Crypt;
import nuclear.slithercrypto.blockchain.BlockChainManager;
import nuclear.slitherio.SlitherLog;

public class Miner implements Runnable {
	protected BlockChainManager man;
	protected SlitherLog logger;
	protected boolean solo;
	public Miner(BlockChainManager man,SlitherLog log, boolean SoloNetwork) {
		this.man=man;
		logger=log;
		solo=SoloNetwork;
	}
	@Override
	public void run() {
		if(solo) {
			logger.println("Finding hashrate for 512 byte objects...");
			long time=System.currentTimeMillis();
			for(int i=0;i<200000;i++) 
				Crypt.SHA256(new byte[512]);
			time=System.currentTimeMillis()-time;
			double hashrate=200000.0/(double)time;
			logger.println("In "+time+"ms 200.000,0 hashes were executed: "+(int)hashrate+" kH/s");
		}
	}

}
