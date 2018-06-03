package nuclear.slithercrypto.blockchain;

import java.util.ArrayList;
import java.util.Arrays;

import nuclear.slitherio.uint256_t;

public abstract class BlockchainBase {
	private static final byte[] genesis_bytecode = {48, 89, 48, 19, 6, 7, 42, -122, 72, -50, 61, 2, 1, 6, 8, 42, -122, 72, -50, 61, 3, 1, 7, 3, 66, 0, 4, 49, -76, -21, 50, -53, 90, 29, -49, -65, 97, -72, -81, 90, -17, 35, 57, -33, -101, 57, -84, 125, 51, -53, -115, 56, 101, 36, -49, 60, 18, 115, -88, 16, 31, -97, -81, -114, 95, 124, 15, -101, -14, 35, 63, -33, -89, -97, 82, -55, 101, -118, -51, -48, 82, 7, -68, 38, 73, -54, 31, -25, -15, 13, 116, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -39, 0, 0, 0, 0, 91, 19, -61, -83, 48, 68, 2, 32, 112, 60, -97, -4, 102, -17, 51, 67, -55, 3, -83, -17, -18, -27, 112, 87, -93, -121, 96, 87, -80, -38, 58, 66, 125, -81, 121, 125, -52, 43, 97, -84, 2, 32, 81, -80, 84, 50, -58, 7, 101, 9, -123, 121, -9, 113, 118, 107, 91, 98, 9, -83, 56, -125, -53, 0, 46, 105, -35, -22, -56, -13, -4, 33, -34, -26, 0, 0, 0, 70};
	private Block current;
	public static final Block genesis = new Block(genesis_bytecode);
	public abstract void addPair(DaughterPair pair);
	public abstract byte[] readFile(String meta,byte[] pubAdr);
	public abstract Block getDaughter(byte[] hash);
	Iterable<Block> chain,daughters;
	protected void setup(Iterable<Block> chain,Iterable<Block> daughters){
		this.chain=chain;
		this.daughters=daughters;
	}
	synchronized public boolean addTransaction(Transaction t) {
		if(!t.verify())
			return false;
		double bal=getCoinBalance(t.pubKey);
		if(t.type==Transaction.TRANSACTION_REGISTER)
			bal-=1000;
		bal-=t.getTransactionCost();
		if(t.type==Transaction.TRANSACTION_SEND_COIN)
			bal-=t.getCoinsSent();
		if(bal<0)
			return false; // insufficient funds.
		getCurrent().addTransaction(t);
		return true;
	}
	public abstract boolean addBlock(Block block);
	public abstract int length();
	public abstract Block getBlockByIndex(int index);
	public abstract boolean commit();
	synchronized public Block getCurrent() {
		return current;
	}
	synchronized public void setCurrent(Block current) {
		this.current = current;
	}
	public double getCoinBalance(byte[] adr){
		if(adr.length!=91)
			return 0;
		double out=0;
		if(Arrays.equals(genesis.getMiner(),adr))
			out=1000;
		for(Block i:chain){
			if(!i.verify())
				continue;
			
			for(int x=0;x<i.numTransactions();x++){
				Transaction t=i.getTransaction(x);
				if(!t.verify())
					continue;
				if(t.type==Transaction.TRANSACTION_SEND_COIN){
					if(Arrays.equals(t.pubKey,adr))
						out-=t.getCoinsSent();
					if(Arrays.equals(t.getReceiver(), adr))
						out+=t.getCoinsSent();
				}
				if(Arrays.equals(t.pubKey,adr))
					out-=t.getTransactionCost();
			}
			if(Arrays.equals(adr,i.getMiner()))
				out+=i.getCost();
		}
		if(getPriority(adr)!=100)
			out-=1000;
		return out;
	}
	public boolean isActive(byte[] adr) {
		for(Block i:chain) {
			if(Arrays.equals(adr, i.getMiner()))
				return true;
			for(int j=0;j<i.numTransactions();j++) {
				Transaction t=i.getTransaction(j);
				if(Arrays.equals(t.pubKey,adr))
					return true;
			}
		}
		return false;
	}
	public void update() {
		
	}
	public ArrayList<Transaction> getFilesOf(byte[] adr){
		ArrayList<Transaction> out=new ArrayList<Transaction>();
		for(Block i:chain) {
			for(int j=0;j<i.numTransactions();j++) {
				Transaction t=i.getTransaction(j);
				if(t.type==Transaction.TRANSACTION_STORE_FILE&&Arrays.equals(t.pubKey,adr))
					out.add(t);
			}
		}
		return out;
	}
	public Block getBlockByHash(byte[] hash){
		for(int i=0;i<length();i++){
			Block b=getBlockByIndex(i);
			if(Arrays.equals(b.getHash(),hash))
				return b;
		}
		return null;
	}
	public boolean isNext(Block block){
		if(!block.verify())
			return false;
		byte[] lsh=block.getLastHash();
		byte[] empty=new byte[32];
		Arrays.fill(empty, (byte)0);
		Block last=getBlockByIndex(length()-1);
		if(!Arrays.equals(lsh, last.getHash()))
			return false;
		int blockTime=(int) (block.getTimestamp()-last.getTimestamp());
		if(blockTime<15){
			return false;
		}
		byte[] miner=block.getMiner();
		if(getPriority(miner)<blockTime&&getPriority(miner)<100)
			return false;  // Not registered or priority incorrect.
		return true;
	}
	
	// returns priority between 15 and 60.  If the address is not registered then returns 100.
	public int getPriority(byte[] miner) {
		double V=0,L=0;
		boolean registered=false;
		int n=0;
		for(int i=length()-1;i>0&&i>length()-172800;i++){
			Block q=getBlockByIndex(i);
			if(Arrays.equals(q.getMiner(), miner)){
				registered=true;
				V++;
				if(n<1024)
					L++;
				n++;
			}
			else
				for(int j=0;j<q.numTransactions();j++){
					Transaction t=q.getTransaction(j);
					if(t.type==Transaction.TRANSACTION_REGISTER&&t.verify()&&Arrays.equals(t.getSender(), miner)){
						registered=true;
					}
				}
		}
		if(!registered)
			return 100;
		Block q=getBlockByIndex(length()-1);
		double a=V*q.numTransactions()/8640000.0;
		double r=-4/Math.pow(2, a*2.0)+4/Math.pow(2, a)+1/(L/1024.0+1);
		return (int)(30.0/r);
	}
}
