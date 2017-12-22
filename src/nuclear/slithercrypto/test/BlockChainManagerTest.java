package nuclear.slithercrypto.test;

import java.nio.charset.StandardCharsets;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.*;
import nuclear.slitherge.top.io;
import nuclear.slitherio.uint256_t;

public class BlockChainManagerTest {

	public static void main(String[] args) {
		ECDSAKey key=new ECDSAKey();
		//Block genesis=new Block(new byte[32],new byte[32],new uint256_t("2315841784746324097914350074143783609629524485720183718758238994082824192"),new byte[0]);
		long hashes=0;
		long mil=System.currentTimeMillis();
		/*while(!genesis.mineOnce(key.getPublicKey())) {
			hashes++;
			if(System.currentTimeMillis()-mil>3000) {
				io.println(hashes/(System.currentTimeMillis()-mil)+" KH/s...");
				hashes=0;
				mil=System.currentTimeMillis();
			}
		}*/
		io.println("Creating file...");
		BlockChainManager man=new BlockChainManager();
		byte[] program_data="Hello World!".getBytes(StandardCharsets.UTF_8);
		DaughterPair pairTmp = Transaction.makeFile(key.getPublicKey(), key.getPrivateKey(), program_data, man.genesis.getHash(), "Troll");
		man.addPair(pairTmp);
		io.println("man.commit(); ...");
		man.commit(key.getPublicKey());
		io.println("Getting file 'Troll'...");
		io.println("FILE CONTENTS:\n");
		io.println(new String(man.readFile("Troll", key.getPublicKey()),StandardCharsets.UTF_8));
	}

}
