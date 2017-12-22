package nuclear.blocks.node;

import java.util.List;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slitherge.top.io;

public class Node implements Runnable {
	NodeServer server;
	List<ExternalNode> nodes;
	public Node() {
		ECDSAKey key=new ECDSAKey();
		server=new NodeServer(key.getPublicKey());
	}
	@Override
	public void run() {
		
	}

	public static void main(String[] args) {
		io.println("Node preinit...");
		new Node();
	}

}
