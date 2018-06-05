package nuclear.blocks.client;

import java.io.IOException;

import nuclear.blocks.node.NodeServer;
import nuclear.slithercrypto.blockchain.Block;
import nuclear.slithercrypto.blockchain.BlockchainBase;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherLog;

public class NodeIface extends MultiClientIface implements Runnable{
	int lastBlock=-1;
	BlockchainBase bc;
	SlitherLog log;
	public NodeIface(String[] hosts, BlockchainBase b, SlitherLog l) {
		super(hosts);
		reportNode();
		bc=b;
		log=l;
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

	public void run() {
		while(true){
			io.waitMillis(5000);
			log.println("Syncing with the remote(s)...");
			sync(bc);
			log.println("Done.");
		}
	}

}
