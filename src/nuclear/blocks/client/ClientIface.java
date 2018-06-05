package nuclear.blocks.client;

import java.io.IOException;
import java.util.ArrayList;
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
	public ClientIface(String host){
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
			e.printStackTrace();
		} catch (Exception e) {
			String message="";
			for(StackTraceElement i:e.getStackTrace())
				message+=i.toString()+'\n';
			JOptionPane.showMessageDialog(null, message, e.toString(), JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}
	public ArrayList<Block> getBlocks(int index){
		byte[] request=new byte[9];
		request[0]=NodeServer.CMD_GET_BLOCKS;
		int n=1;
		for(byte i:SlitherS.longToBytes(index)){
			request[n]=i;
			n++;
		}
		try {
			byte[] data=client.poll(request);
			if(data==null)
				return null;
			ArrayList<Block> out=new ArrayList<Block>();
			if(data[0]!=0x55||data.length!=1){
				for(int i=0;i<data.length;){
					long len=SlitherS.bytesToLong(Arrays.copyOfRange(data, i, i+8));
					i+=8;
					Block b=new Block(Arrays.copyOfRange(data, i, (int)(i+len)));
					out.add(b);
					i+=len;
				}
			}
			return out;
		} catch (IOException e) {
			setNetErr(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
			ArrayList<Block> blocks=getBlocks(i);
			if(blocks==null)
				return -1;
			if(blocks.isEmpty())
				break;
			if(isNetErr()){
				setNetErr(false);
				return -1;
			}
			for(Block block:blocks){
				if(manager.addBlock(block)){
					n++;
					i++;
				}else
					break;
			}
			int s=blocks.size();
			if(s<32)
				break;
		}
		return n;
	}
	public boolean isNetErr() {
		return netErr;
	}
	public void setNetErr(boolean netErr) {
		this.netErr = netErr;
	}
	public void close() {
		client.disconnect();
	}
	public String[] getNodes(){
		 byte[] req={NodeServer.CMD_GET_NODES,-1}; // pad request with extra byte.
		 try{
			 byte[] raw=client.poll(req);
			 int cnt=0;
			 for(byte i:raw){
				 if(i==13)
					 cnt++;
			 }
			 String[] out=new String[cnt];
			 for(int i=0;i<cnt;i++){
				 out[i]=new String();
			 }
			 cnt=0;
			 for(byte i:raw){
				 if(i==13)
					 cnt++;
				 else
					 out[cnt]+=(char)i;
			 }
			 return out;
		 }catch(Exception e){
			 return new String[0];
		 }
	}
}
