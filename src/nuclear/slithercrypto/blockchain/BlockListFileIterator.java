package nuclear.slithercrypto.blockchain;

import java.util.Iterator;

public class BlockListFileIterator implements Iterator<Block>{
	int index=0;
	BlockListFile l;
	public BlockListFileIterator(BlockListFile list) {
		l=list;
	}
	public boolean hasNext() {
		return index<l.length();
	}

	public Block next() {
		index++;
		return l.get(index-1);
	}

}
