package nuclear.blocks.client;

import java.io.IOException;
import java.util.Arrays;

import javax.swing.JOptionPane;

import nuclear.blocks.node.NodeServer;
import nuclear.slithercrypto.blockchain.Block;
import nuclear.slithercrypto.blockchain.BlockchainBase;
import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherS;
import nuclear.slithernet.Client;

public class ClientIface {
	protected Client client;
	private boolean netErr;
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
	public Block downloadByIndex(int x){
		Block result=null;
		byte[] request=new byte[9];
		request[0]=NodeServer.CMD_GET_BLOCK;
		int n=1;
		for(byte i:SlitherS.longToBytes(x)){
			request[n]=i;
			n++;
		}
		try {
			byte[] data=client.poll(request);
			if(data[0]==0x55&&data.length==1)
				return null;
			result = new Block(data);
			if(!result.verify()&&x>0)
				result=null;
		} catch (IOException e) {
			setNetErr(true);
			JOptionPane.showMessageDialog(null, "Unable to download block #"+x+" due to a connection issue.\n", "Network Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			String message="";
			for(StackTraceElement i:e.getStackTrace())
				message+=i.toString()+'\n';
			JOptionPane.showMessageDialog(null, message, e.toString(), JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}
	public Block downloadDaughter(byte[] hash){
		Block result=null;
		byte[] request=new byte[33];
		request[0]=NodeServer.CMD_GET_DAUGHTER;
		int n=1;
		for(byte i:hash){
			request[n]=i;
			n++;
		}
		try {
			result = new Block(client.poll(request));
			io.println(result.toString());
			if(!result.verify()){
				result=null;
				io.println("Error verifying block");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public int downloadBlockchain(BlockchainBase manager){
		setNetErr(false);
		manager.update();
		int i=manager.length();
		int n=0;
		while(true){
			Block block=downloadByIndex(i);
			if(isNetErr()){
				setNetErr(false);
				return -1;
			}
			i++;
			if(manager.addBlock(block))
				n++;
			else
				return n;
		}
	}
	public boolean isNetErr() {
		return netErr;
	}
	public void setNetErr(boolean netErr) {
		this.netErr = netErr;
	}
}
