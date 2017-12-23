package nuclear.blocks.node;


import java.io.IOException;

import nuclear.blocks.client.ClientIface;
import nuclear.slithercrypto.blockchain.DaughterPair;

public class ExternalNode{
	private ClientIface client;
	public ExternalNode(String host) {
		try {
			client=new ClientIface(host);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean submitPair(DaughterPair pair) {
		return client.uploadPair(pair);
	}
}
