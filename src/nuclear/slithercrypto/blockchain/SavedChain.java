package nuclear.slithercrypto.blockchain;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SavedChain extends BlockchainBase{
	protected BlockListFile chain;
	BlockListFile daughters;
	private Block current;
	public SavedChain(String storeDir) {
		chain=new BlockListFile(storeDir+"/chain");
		daughters=new BlockListFile(storeDir+"/daugt");
		setup(chain,daughters);
		setCurrent(new Block(new byte[32],new byte[32],new byte[0]));
	}

	synchronized public void addPair(DaughterPair pair) {
		addTransaction(pair.tr);
		daughters.addBlock(pair.block);
	}
	synchronized public byte[] readFile(String meta,byte[] pubAdr) {
		for(int j=chain.length()-1;j>=0;j--) {
			Block block=chain.get(j);
			for(int i=block.numTransactions()-1;i>=0;i--) {
				Transaction t=block.getTransaction(i);
				String tmeta=new String(t.getMeta(),StandardCharsets.UTF_8);
				if(t.type==Transaction.TRANSACTION_STORE_FILE&&Arrays.equals(t.pubKey, pubAdr)&&tmeta.equals(meta))
					return getDaughter(Arrays.copyOf(t.descriptor,32)).getData();
			}
		}
		return null;
	}
	synchronized public Block getDaughter(byte[] hash) {
		daughters.update(); // in case another program added more
		for(Block i:daughters) {
			if(Arrays.equals(i.getHash(),hash))return i;
		}
		return null;
	}
	synchronized public boolean addBlock(Block block){
		chain.update(); // in case some other program added to the blockchain first
		if(block==null)
			return false;
		if(isNext(block))
			chain.addBlock(block);
		else {
			return false;
		}
		getCurrent().setLastBlockHash(block.getHash());
		return true;
	}
	synchronized public int length() {
		return chain.length();
	}
	synchronized public Block getBlockByIndex(int index) {
		return chain.get(index);
	}
	synchronized public boolean commit() {
		if(addBlock(getCurrent()))
			setCurrent(new Block(new byte[32],chain.get(chain.length()-1).getHash(),new byte[0]));
		else
			return false;
		return true;
	}
	synchronized public Block getCurrent() {
		return current;
	}
	synchronized public void setCurrent(Block current) {
		this.current = current;
	}
	public void update(){
		chain.update();
		daughters.update();
	}
}
