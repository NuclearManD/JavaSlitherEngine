package nuclear.blocks.test;

import java.util.Arrays;

import nuclear.slithercrypto.blockchain.Block;
import nuclear.slithercrypto.blockchain.BlockChainManager;
import nuclear.slithercrypto.blockchain.BlockListFile;
import nuclear.slitherge.top.io;

public class BlockListFileTest {

	public static void main(String[] args) {
		BlockListFile list=new BlockListFile("C:\\Users\\dylanbrophy\\Desktop\\tmp\\blk");
		io.println("There are already "+list.length()+" blocks in the list.");
		Block b;
		try {
			io.println("Writing block...");
			int index=list.addBlock(BlockChainManager.genesis);
			io.println("Reading Block...");
			b=list.get(index);
			if(Arrays.equals(b.getHash(), BlockChainManager.genesis.getHash()))
				io.println("Success!");
			else
				io.println("Error: hashes DO NOT match!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
