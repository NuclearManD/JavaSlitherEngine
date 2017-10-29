package nuclear.slithercrypto.blockchain;

import java.util.Arrays;

public class DaughterPair {
	public Transaction tr;
	public Block block;
	public DaughterPair(Transaction t, Block d) {
		tr=t;
		block=d;
	}
	public boolean verify() {
		if(block.verify())
			if(Arrays.copyOf(tr.descriptor,32).equals(block.getHash()))
				return true;
		return false;
	}
}
