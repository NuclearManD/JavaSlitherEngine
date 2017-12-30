package nuclear.slithercrypto.blockchain;

import java.util.ArrayList;
import java.util.Arrays;

import nuclear.slitherio.uint256_t;

public abstract class BlockchainBase {
	private Block current;
	public static final Block genesis = new Block(new byte[91], new byte[32], new uint256_t("771947261582107967251640281103336579920368336826869405186543784860581888"), new byte[0]);
	public abstract void addPair(DaughterPair pair);
	public abstract byte[] readFile(String meta,byte[] pubAdr);
	public abstract Block getDaughter(byte[] hash);
	public abstract void commit(byte[] key);
	Iterable<Block> chain,daughters;
	protected void setup(Iterable<Block> chain,Iterable<Block> daughters){
		this.chain=chain;
		this.daughters=daughters;
	}
	synchronized public void addTransaction(Transaction t) {
		getCurrent().addTransaction(t);
	}
	public abstract boolean addBlock(Block block);
	public abstract int length();
	public abstract Block getBlockByIndex(int index);
	public abstract void commit();
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
}
