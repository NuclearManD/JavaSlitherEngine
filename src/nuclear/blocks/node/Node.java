package nuclear.blocks.node;

import java.io.File;
import java.util.List;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slitherge.top.io;

public class Node {
	NodeServer server;
	List<ExternalNode> nodes;
	ECDSAKey key;
	String keypath=System.getProperty("user.home")+"/AppData/Roaming/NuclearBlocks/keys/main.key";
	public Node() {
		if(new File(keypath).exists())
			key=new ECDSAKey(keypath);
		else{
			new File(keypath).getParentFile().mkdirs();
			key=new ECDSAKey();
			key.save(keypath);
		}
		server=new NodeServer(key.getPublicKey());
	}

	public static void main(String[] args) {
		io.println("Node preinit...");
		new Node();
	}

}
