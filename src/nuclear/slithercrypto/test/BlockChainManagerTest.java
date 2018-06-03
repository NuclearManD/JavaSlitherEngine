package nuclear.slithercrypto.test;

import java.nio.charset.StandardCharsets;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.*;
import nuclear.slitherge.top.io;

public class BlockChainManagerTest {

	public static void main(String[] args) {
		ECDSAKey key=new ECDSAKey();
		io.println("Creating file...");
		BlockChainManager man=new BlockChainManager();
		byte[] program_data="Hello World!".getBytes(StandardCharsets.UTF_8);
		DaughterPair pairTmp = Transaction.makeFile(key.getPublicKey(), key.getPrivateKey(), program_data, BlockChainManager.genesis.getHash(), "Troll");
		man.addPair(pairTmp);
		io.println("man.commit(); ...");
		man.commit(key);
		io.println("Getting file 'Troll'...");
		io.println("FILE CONTENTS:\n");
		io.println(new String(man.readFile("Troll", key.getPublicKey()),StandardCharsets.UTF_8));
	}

}
