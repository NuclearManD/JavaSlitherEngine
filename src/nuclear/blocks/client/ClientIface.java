package nuclear.blocks.client;

import java.io.IOException;
import java.util.Arrays;

import nuclear.blocks.node.NodeServer;
import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;
import nuclear.slithernet.Client;

public class ClientIface {
	protected Client client;
	public ClientIface(String host) throws IOException{
		client=new Client(1152, host);
	}
	public boolean uploadPair(DaughterPair pair){
		byte[] packed=pair.serialize();
		byte[] request=new byte[packed.length+1];
		int n=1;
		for(byte i:packed){
			request[n]=i;
			n++;
		}
		request[0]=NodeServer.CMD_ADD_PAIR;
		byte[] result;
		try {
			result = client.poll(request);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		io.println(Arrays.toString(result));
		return Arrays.equals(result, NodeServer.RESULT_SUCCESS);
	}
	public boolean uploadTransaction(Transaction t){
		byte[] packed=t.pack();
		byte[] request=new byte[packed.length+1];
		int n=1;
		for(byte i:packed){
			request[n]=i;
			n++;
		}
		request[0]=NodeServer.CMD_ADD_TRANS;
		byte[] result;
		try {
			result = client.poll(request);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		io.println(Arrays.toString(result));
		return Arrays.equals(result, NodeServer.RESULT_SUCCESS);
	}
}
