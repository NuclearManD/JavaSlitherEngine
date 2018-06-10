package nuclear.slithercrypto.blockchain;

import java.util.ArrayList;
import java.util.Arrays;

import nuclear.slithercrypto.Crypt;
import nuclear.slitherge.top.io;

public abstract class BlockchainBase {
	private Block current;
	public abstract void addPair(DaughterPair pair);
	public abstract Block getDaughter(byte[] hash);
	public abstract void addDaughter(Block d);
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
	ArrayList<BalanceCache> balCache=new ArrayList<BalanceCache>();
	public double getCoinBalance(byte[] adr){
		for(BalanceCache i:balCache){
			if(i.equals(adr)){
				return i.getBalance();
			}
		}
		BalanceCache v=new BalanceCache(adr, this);
		balCache.add(v);
		return v.getBalance();
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
	ArrayList<AccountFileList> fsCache=new ArrayList<AccountFileList>();
	public ArrayList<String> getFilesOf(byte[] adr){
		for(AccountFileList i:fsCache){
			if(i.equals(adr))
				return i.getFileNames();
		}
		AccountFileList q=new AccountFileList(adr,this);
		fsCache.add(q);
		return q.getFileNames();
	}
	public ArrayList<String> getPagesOf(byte[] adr){
		for(AccountFileList i:fsCache){
			if(i.equals(adr))
				return i.getPageNames();
		}
		AccountFileList q=new AccountFileList(adr,this);
		fsCache.add(q);
		return q.getPageNames();
	}
	public ArrayList<String> getEncryptsOf(byte[] adr){
		for(AccountFileList i:fsCache){
			if(i.equals(adr))
				return i.getEncryptedNames();
		}
		AccountFileList q=new AccountFileList(adr,this);
		fsCache.add(q);
		return q.getEncryptedNames();
	}
	public byte[] readPage(String path, byte[] adr) {
		byte[] hash=null;
		for(AccountFileList i:fsCache){
			if(i.equals(adr)){
				hash=i.getPageHash(path);
				break;
			}
		}
		if(hash==null){
			AccountFileList q=new AccountFileList(adr,this);
			fsCache.add(q);
			hash=q.getPageHash(path);
		}
		return getDaughter(hash).getData();
	}
	public byte[] readFile(String path, byte[] adr) {
		byte[] hash=null;
		for(AccountFileList i:fsCache){
			if(i.equals(adr)){
				hash=i.getFileHash(path);
				break;
			}
		}
		if(hash==null){
			AccountFileList q=new AccountFileList(adr,this);
			fsCache.add(q);
			hash=q.getFileHash(path);
		}
		return getDaughter(hash).getData();
	}
	public byte[] readEncryptedFile(String path, byte[] adr,String password) {
		byte[] hash=null;
		for(AccountFileList i:fsCache){
			if(i.equals(adr)){
				hash=i.getEncryptedHash(path);
				break;
			}
		}
		if(hash==null){
			AccountFileList q=new AccountFileList(adr,this);
			fsCache.add(q);
			hash=q.getEncryptedHash(path);
		}
		return Crypt.DecryptAES(getDaughter(hash).getData(),password);
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

	public boolean isNext(Block block, int index){
		if(!block.verify())
			return false;
		byte[] lsh=block.getLastHash();
		byte[] empty=new byte[32];
		Arrays.fill(empty, (byte)0);
		if(Arrays.equals(lsh, empty)&&index==-1)
			return true;
		Block last=getBlockByIndex(index);
		if(!Arrays.equals(lsh, last.getHash())){
			return false;
		}
		int blockTime=(int) (block.getTimestamp()-last.getTimestamp());
		if(blockTime<15){
			return false;
		}
		byte[] miner=block.getMiner();
		int priority=getPriorityAt(miner,index);
		if(priority>blockTime||priority==100){
			io.println("Priority Error: miner priority is "+priority+" and block time is "+blockTime);
			return false;  // Not registered or priority incorrect.
		}
		return true;
	}
	
	ArrayList<Verifier> verifierCache=new ArrayList<Verifier>();
	
	// returns priority between 15 and 60.  If the address is not registered then returns 100.
	public int getPriority(byte[] miner) {
		double V=0,L=0;
		Verifier v=null;
		for(Verifier i:verifierCache){
			if(i.equals(miner)){
				v=i;
				break;
			}
		}
		if(v==null){
			v=new Verifier(miner, this);
			if(!v.registered())
				return 100;
			verifierCache.add(v);
		}
		V=v.getV();
		L=v.getL();
		Block q=getBlockByIndex(length()-1);
		double a=V*q.numTransactions()/8640000.0;
		double r=-4/Math.pow(2, a*2.0)+4/Math.pow(2, a)+1/(L/1024.0+1);
		return (int)(30.0/r);
	}
	public int getPriorityAt(byte[] miner, int index) {
		double V=0,L=0;
		Verifier v=null;
		for(Verifier i:verifierCache){
			if(i.equals(miner)){
				v=i;
				break;
			}
		}
		if(v==null){
			v=new Verifier(miner, this);
			if(!v.registered())
				return 100;
			verifierCache.add(v);
		}
		V=v.getV(index);
		L=v.getL(index);
		Block q=getBlockByIndex(length()-1);
		double a=V*q.numTransactions()/8640000.0;
		double r=-4/Math.pow(2, a*2.0)+4/Math.pow(2, a)+1/(L/1024.0+1);
		return (int)(30.0/r);
	}
}
