package nuclear.blocks.test;

import java.util.Arrays;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.Block;
import nuclear.slitherge.top.io;

public class GenesisTest {

	public static void main(String[] args) {
		ECDSAKey key=new ECDSAKey(System.getProperty("user.home")+"/AppData/Roaming/NuclearBlocks/keys/main.key");
		Block genesis=new Block(new byte[91], new byte[32], new byte[0]);
		genesis.sign(key);
		io.println("Generated: "+Arrays.toString(genesis.pack()));
	}

}
