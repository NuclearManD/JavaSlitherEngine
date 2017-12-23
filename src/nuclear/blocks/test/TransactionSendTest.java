package nuclear.blocks.test;

import java.io.IOException;

import nuclear.blocks.client.ClientIface;
import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;

public class TransactionSendTest {
	public static void main(String[] args) {
		io.println("Making key...");
		ECDSAKey key=new ECDSAKey();
		io.println("Making Transaction...");
		Transaction t=Transaction.sendCoins(key.getPublicKey(), key.getPublicKey(), key.getPrivateKey(), 100000);
		io.println("Transaction:\t"+t.toString());
		io.println("Valid: "+t.verify());
		try {
			ClientIface iface=new ClientIface("192.168.1.132");
			io.print("SUCCESS: "+iface.uploadTransaction(t));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
