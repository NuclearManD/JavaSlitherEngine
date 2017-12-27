package nuclear.blocks.test;

import java.io.IOException;
import java.util.Arrays;

import nuclear.blocks.client.ClientIface;
import nuclear.slithercrypto.blockchain.Block;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;

public class DaughterDownloadTest {

	public static void main(String[] args) {
		io.println("getting second block from node...");
		try {
			ClientIface iface=new ClientIface("192.168.1.150");
			Block s=iface.downloadByIndex(0);
			Transaction t = null;
			for(int i=0;i<s.numTransactions();i++) {
				t=s.getTransaction(i);
				if(t.type==Transaction.TRANSACTION_STORE_FILE) {
					break;
				}
			}
			if(t==null) {
				io.println("Error: no suitable transaction found in block two.");
				return;
			}
			io.println("downloading daughter block...");
			Block d=iface.downloadDaughter(t.getDaughterHash());
			io.println("Contents of file (as array) : "+Arrays.toString(d.getData()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
