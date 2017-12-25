package nuclear.blocks.node;

import java.util.Base64;

import nuclear.slithercrypto.Crypt;
import nuclear.slithercrypto.blockchain.Block;
import nuclear.slithercrypto.blockchain.BlockChainManager;
import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherLog;
import nuclear.slitherio.uint256_t;

public class Miner implements Runnable {
	public BlockChainManager man;
	protected SlitherLog logger;
	protected boolean solo;
	private byte[] pubKey;
	public Miner(BlockChainManager man,SlitherLog log, boolean SoloNetwork, byte[] pubKey) {
		this.man=man;
		logger=log;
		solo=SoloNetwork;
		this.pubKey=pubKey;
	}
	//@Override
	public void run() {
		if(solo) {
			logger.println("Finding hashrate for 2048 byte Blocks...");
			long time=System.currentTimeMillis();
			// set difficulty to zero so it is never mined
			Block tmp=new Block(new byte[32], new uint256_t("0"), new byte[2048]);
			for(int i=0;i<200000;i++) 
				tmp.mineOnce(new byte[91]);
			time=System.currentTimeMillis()-time;
			double hashrate=200000.0/(double)time;
			logger.println("In "+time+"ms 200.000,0 hashes were executed: "+(int)hashrate+" kH/s");
			uint256_t difficultyConstant=new uint256_t("7067388259113537318333190002971674063309935587502475832486424805170479104");
			uint256_t hashrate_256=uint256_t.fromBigInt(uint256_t.valueOf((long)hashrate*1000));// in Hashes/s, not kH/s
			uint256_t difficulty_raw=uint256_t.fromBigInt(difficultyConstant.divide(hashrate_256));
			logger.println("Raw Difficulty: "+difficulty_raw);
			while(true){
				if(man.getCurrent().numTransactions()==0){
					logger.println("Waiting for transaction block...");
					while(man.getCurrent().numTransactions()==0);
				}
				logger.println("Setting up next block...");
				man.getCurrent().setDifficulty(difficulty_raw);
				logger.println("Mining...");
				time=System.currentTimeMillis();
				long mil=System.currentTimeMillis();
				long hashes=0;
				tmp=man.getCurrent();
				while(!tmp.mineOnce(pubKey)) {
					hashes++;
					if(System.currentTimeMillis()-mil>30000) {
						io.println(hashes/(System.currentTimeMillis()-mil)+" KH/s...");
						hashes=0;
						mil=System.currentTimeMillis();
					}
				}
				time=((System.currentTimeMillis()-time)/60000);
				logger.println("Mined block "+Base64.getEncoder().encodeToString(man.getCurrent().getHash())+" in "+time+" minutes.");
				man.commit();
			}
		}
	}

}
