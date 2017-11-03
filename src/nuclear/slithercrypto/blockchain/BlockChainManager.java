package nuclear.slithercrypto.blockchain;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import nuclear.slitherge.top.io;
import nuclear.slitherio.uint256_t;

public class BlockChainManager {
	Block current;
	ArrayList<Block> blocks=new ArrayList<Block>();
	ArrayList<Block> daughters=new ArrayList<Block>();
	public BlockChainManager(Block genesis) {
		blocks.add(genesis);
		current=new Block(new byte[32],blocks.get(blocks.size()-1).getHash(),new uint256_t("771947261582107967251640281103336579920368336826869405186543784860581888"),new byte[0]);
	}
	public void addPair(DaughterPair pair) {
		current.addTransaction(pair.tr);
		daughters.add(pair.block);
	}
	public byte[] readFile(String meta,byte[] pubAdr) {
		for(Block block:blocks) {
			for(int i=0;i<block.numTransactions();i++) {
				Transaction t=block.getTransaction(i);
				io.println(t.toString());
				if(t.type==Transaction.TRANSACTION_STORE_FILE&&t.pubKey.equals(pubAdr)&&new String(t.getMeta(),StandardCharsets.UTF_8).equals(meta))
					return getDaughter(Arrays.copyOf(t.descriptor,32)).getData();
			}
		}
		return null;
	}
	public Block getDaughter(byte[] hash) {
		for(Block i:daughters) {
			if(i.getHash().equals(hash))return i;
		}
		return null;
	}
	public void commit(byte[] key) {
		long hashes=0;
		long mil=System.currentTimeMillis();
		while(!current.mineOnce(key)) {
			hashes++;
			if(System.currentTimeMillis()-mil>3000) {
				io.println(hashes/(System.currentTimeMillis()-mil)+" KH/s...");
				hashes=0;
				mil=System.currentTimeMillis();
			}
		}
		blocks.add(current);
		current=new Block(new byte[32],blocks.get(blocks.size()-1).getHash(),new uint256_t("771947261582107967251640281103336579920368336826869405186543784860581888"),new byte[0]);
	}
}
