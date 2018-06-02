package nuclear.blocks.node;

import java.util.Base64;

import nuclear.slithercrypto.blockchain.Block;
import nuclear.slithercrypto.blockchain.BlockchainBase;
import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherLog;
import nuclear.slitherio.uint256_t;

public class NodeMiner implements Runnable {
	public BlockchainBase man;
	protected SlitherLog logger;
	protected boolean solo;
	private byte[] pubKey;
	private int blocktime=30000*60;
	public NodeMiner(BlockchainBase man,SlitherLog log, boolean SoloNetwork, byte[] pubKey) {
		this.man=man;
		logger=log;
		solo=SoloNetwork;
		this.pubKey=pubKey;
		setup();
	}
	public NodeMiner(BlockchainBase man, SlitherLog log, boolean SoloNetwork, byte[] pubKey, int bt) {
		this.man=man;
		logger=log;
		solo=SoloNetwork;
		this.pubKey=pubKey;
		this.blocktime=bt;
		setup();
	}
	uint256_t difficultyConstant=new uint256_t("1157920892373161954235709850086879078532699846656405640394575840079131296399364");
	private uint256_t hashrate_256;
	private uint256_t difficulty_raw;
	private void setup() {
		logger.println("Finding hashrate for 2048 byte Blocks...");
		long time=System.currentTimeMillis();
		// set difficulty to zero so it is never mined
		Block tmp=new Block(new byte[32], new uint256_t("0"), new byte[2048]);
		for(int i=0;i<200000;i++) 
			tmp.mineOnce(new byte[91]);
		time=System.currentTimeMillis()-time;
		double hashrate=100000.0/(double)time;
		logger.println("In "+time+"ms 200.000,0 hashes were executed: "+(int)hashrate+" kH/s");
		hashrate_256=uint256_t.fromBigInt(uint256_t.valueOf((long)hashrate*1000));// in Hashes/s, not kH/s
		difficulty_raw=uint256_t.fromBigInt(difficultyConstant.divide(hashrate_256.multiply(new uint256_t(Integer.toString(blocktime)))));
		logger.println("Raw Difficulty: "+difficulty_raw);
	}
	private boolean started=false;
	public void start(int numThreads){
		if(started==true)
			return;
		for(int i=0;i<numThreads;i++)
			new Thread(this).start();
		started=true;
	}
	
	void configNextBlock(){
		logger.println("Setting up next block...");
		man.getCurrent().setDifficulty(difficulty_raw);
		byte[] lasthash=new byte[32];
		if(man.length()>0)
			lasthash=man.getBlockByIndex(man.length()-1).getHash();
		man.getCurrent().setLastBlockHash(lasthash);
	}
	long hashes=0;
	int threads=0;
	private boolean job=false;
	//@Override
	public void run() {
		int thread=threads;
		threads++;
		if(solo) {
			while(true){
				if(thread>0)
					while(!job){
						try{
							Thread.sleep(100);
						}catch(Exception e){}
					}
				else if(man.getCurrent().numTransactions()==0){
					logger.println("Waiting for transaction block...");
					while(man.getCurrent().numTransactions()==0){
						try{
							Thread.sleep(50);
						}catch(Exception e){}
					}
				}
				if(thread==0){
					configNextBlock();
					logger.println("Mining...");
					job=true;
				}
				long time=System.currentTimeMillis();
				long mil=System.currentTimeMillis();
				hashes=0;
				Block tmp=man.getCurrent();
				while((!tmp.mineOnce(pubKey))&&job) {
					hashes++;
					if(thread==0&&System.currentTimeMillis()-mil>2000) {
						io.println(hashes/(System.currentTimeMillis()-mil)+" KH/s...");
						hashes=0;
						mil=System.currentTimeMillis();
					}
				}
				if(thread==0)
					io.println(hashes/(System.currentTimeMillis()-mil)+" KH/s...");
				if(job){
					time=((System.currentTimeMillis()-time)/60000);
					logger.println("[Thread "+thread+"] Mined block "+Base64.getEncoder().encodeToString(man.getCurrent().getHash())+" in "+time+" minutes.");
					job=false;
					man.commit();
				}
			}
		}
	}

}
