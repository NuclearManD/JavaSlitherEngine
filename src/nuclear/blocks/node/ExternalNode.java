package nuclear.blocks.node;



import nuclear.blocks.client.ClientIface;
import nuclear.slithercrypto.blockchain.DaughterPair;

public class ExternalNode{
	private ClientIface client;
	public ExternalNode(String host) {
		client=new ClientIface(host);
	}
	public boolean submitPair(DaughterPair pair) {
		return client.uploadPair(pair);
	}
}
