package nuclear.slithercrypto.blockchain;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import nuclear.slitherge.top.io;
import nuclear.slitherio.uint256_t;

public class SavedChain extends BlockchainBase{
	BlockListFile chain;
	BlockListFile daughters;
	private Block current;
	public static final Block genesis = new Block(new byte[91], new byte[32], new uint256_t("771947261582107967251640281103336579920368336826869405186543784860581888"), new byte[0]);
	public SavedChain(String storeDir) {
		chain=new BlockListFile(storeDir+"/chain");
		daughters=new BlockListFile(storeDir+"/daugt");
		setup(chain,daughters);
		setCurrent(new Block(new byte[32],new byte[32],new uint256_t("771947261582107967251640281103336579920368336826869405186543784860581888"),new byte[0]));
	}

	synchronized public void addPair(DaughterPair pair) {
		addTransaction(pair.tr);
		daughters.addBlock(pair.block);
	}
	synchronized public byte[] readFile(String meta,byte[] pubAdr) {
		for(Block block:chain) {
			for(int i=0;i<block.numTransactions();i++) {
				Transaction t=block.getTransaction(i);
				String tmeta=new String(t.getMeta(),StandardCharsets.UTF_8);
				if(t.type==Transaction.TRANSACTION_STORE_FILE&&Arrays.equals(t.pubKey, pubAdr)&&tmeta.equals(meta))
					return getDaughter(Arrays.copyOf(t.descriptor,32)).getData();
			}
		}
		return null;
	}
	synchronized public Block getDaughter(byte[] hash) {
		for(Block i:daughters) {
			if(Arrays.equals(i.getHash(),hash))return i;
		}
		return null;
	}
	synchronized public void commit(byte[] key) {
		long hashes=0;
		long mil=System.currentTimeMillis();
		while(!getCurrent().mineOnce(key)) {
			hashes++;
			if(System.currentTimeMillis()-mil>3000) {
				io.println(hashes/(System.currentTimeMillis()-mil)+" KH/s...");
				hashes=0;
				mil=System.currentTimeMillis();
			}
		}
		commit();
	}
	synchronized public void addTransaction(Transaction t) {
		getCurrent().addTransaction(t);
	}
	synchronized public boolean addBlock(Block block){
		if(block==null)
			return false;
		if(block.verify()&&(chain.length()==0||Arrays.equals(chain.get(chain.length()-1).getHash(),block.getHash())))
			chain.addBlock(block);
		else
			return false;
		getCurrent().setLastBlockHash(block.getHash());
		return true;
	}
	synchronized public int length() {
		return chain.length();
	}
	synchronized public Block getBlockByIndex(int index) {
		return chain.get(index);
	}
	synchronized public void commit() {
		chain.addBlock(getCurrent());
		setCurrent(new Block(new byte[32],chain.get(chain.length()-1).getHash(),new uint256_t("771947261582107967251640281103336579920368336826869405186543784860581888"),new byte[0]));
	}
	synchronized public Block getCurrent() {
		return current;
	}
	synchronized public void setCurrent(Block current) {
		this.current = current;
	}
}
