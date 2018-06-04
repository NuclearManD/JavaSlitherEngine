package nuclear.slithercrypto.blockchain;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import nuclear.slitherge.top.io;
import nuclear.slitherio.uint256_t;

public abstract class BlockchainBase {
	private Block current;
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
		if(length()==0)
			return 0;
		double out=0;
		if(Arrays.equals(getBlockByIndex(0).getMiner(),adr))
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
	public ArrayList<Transaction> getPagesOf(byte[] adr){
		ArrayList<Transaction> out=new ArrayList<Transaction>();
		for(Block i:chain) {
			for(int j=0;j<i.numTransactions();j++) {
				Transaction t=i.getTransaction(j);
				if(t.type==Transaction.TRANSACTION_STORE_PAGE&&Arrays.equals(t.pubKey,adr))
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
		if(Arrays.equals(lsh, empty)&&length()==0)
			return true;
		Block last=getBlockByIndex(length()-1);
		if(!Arrays.equals(lsh, last.getHash())){
			io.println("Hash error: hashes of blocks DO NOT match.");
			return false;
		}
		int blockTime=(int) (block.getTimestamp()-last.getTimestamp());
		if(blockTime<15){
			io.println("Blocktime error: blocktime is less than 15.");
			return false;
		}
		byte[] miner=block.getMiner();
		int priority=getPriority(miner);
		if(priority>blockTime||priority==100){
			io.println("Priority Error: miner priority is "+priority+" and block time is "+blockTime);
			return false;  // Not registered or priority incorrect.
		}
		return true;
	}
	
	// returns priority between 15 and 60.  If the address is not registered then returns 100.
	public int getPriority(byte[] miner) {
		double V=0,L=0;
		boolean registered=false;
		int n=0;
		for(int i=length()-1;i>=0&&i>length()-172800;i--){
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
	public byte[] readPage(String path, byte[] address) {
		for(int j=length()-1;j>=0;j--) {
			Block block=getBlockByIndex(j);
			for(int i=block.numTransactions()-1;i>=0;i--) {
				Transaction t=block.getTransaction(i);
				String tmeta=new String(t.getMeta(),StandardCharsets.UTF_8);
				if(t.type==Transaction.TRANSACTION_STORE_PAGE&&Arrays.equals(t.pubKey, address)&&tmeta.equals(path))
					return getDaughter(Arrays.copyOf(t.descriptor,32)).getData();
			}
		}
		return null;
	}
}
