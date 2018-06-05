package nuclear.blocks.client;

import java.io.IOException;

import nuclear.blocks.node.NodeServer;
import nuclear.slithercrypto.blockchain.Block;
import nuclear.slithercrypto.blockchain.BlockchainBase;
import nuclear.slithercrypto.blockchain.Transaction;

public class NodeIface extends MultiClientIface{
	int lastBlock=-1;
	public NodeIface(String[] hosts) {
		super(hosts);
		reportNode();
	}

	public void reportNode() {
		byte[] message={NodeServer.CMD_ADD_NODE,-1};
		for(ClientIface i:ifaces){
			try {
				i.client.poll(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sync(BlockchainBase bc) {
		downloadBlockchain(bc);
		for(int i=lastBlock+1;i<bc.length();i++){
			Block b=bc.getBlockByIndex(i);
			for(int j=0;j<b.numTransactions();j++){
				Transaction t=b.getTransaction(j);
				if(t.type==Transaction.TRANSACTION_STORE_ENCRYPTED||t.type==Transaction.TRANSACTION_STORE_FILE||t.type==Transaction.TRANSACTION_STORE_PAGE){
					if(bc.getDaughter(t.getDaughterHash())==null){
						Block d=downloadDaughter(t.getDaughterHash());
						bc.addDaughter(d);
					}
				}
			}
		}
		lastBlock=bc.length()-1;
	}

}
