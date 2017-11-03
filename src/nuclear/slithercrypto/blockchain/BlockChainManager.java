package nuclear.slithercrypto.blockchain;

import java.util.ArrayList;

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

}
