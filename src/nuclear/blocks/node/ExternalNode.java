package nuclear.blocks.node;


import java.nio.charset.StandardCharsets;

import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithernet.Client;

public class ExternalNode{
	private String address;
	public ExternalNode(String host) {
		address=host;
	}
	public boolean submitPair(DaughterPair pair) {
		try {
			if(Client.ezPoll(1152, new String(pair.serialize(),StandardCharsets.UTF_8), address).equals("OK")) return true;
		}catch(Exception e) {}
		return false;
	}
}
