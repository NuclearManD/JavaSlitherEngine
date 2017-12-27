package nuclear.slithercrypto.blockchain;

import nuclear.slitherio.uint256_t;

public abstract class BlockchainBase {
	private Block current;
	public static final Block genesis = new Block(new byte[91], new byte[32], new uint256_t("771947261582107967251640281103336579920368336826869405186543784860581888"), new byte[0]);
	public abstract void addPair(DaughterPair pair);
	public abstract byte[] readFile(String meta,byte[] pubAdr);
	public abstract Block getDaughter(byte[] hash);
	public abstract void commit(byte[] key);
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
}
