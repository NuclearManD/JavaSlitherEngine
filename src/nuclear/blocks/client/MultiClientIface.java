package nuclear.blocks.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import nuclear.blocks.node.NodeServer;
import nuclear.blocks.node.SavedList;
import nuclear.slithercrypto.blockchain.Block;
import nuclear.slithercrypto.blockchain.BlockchainBase;
import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithercrypto.blockchain.Transaction;

public class MultiClientIface{
	protected ArrayList<ClientIface> ifaces=new ArrayList<ClientIface>();
	protected int seekLimit=3;
	public MultiClientIface(String[] hosts) {
		for(String i:hosts)
			ifaces.add(new ClientIface(i));
	}
	@Deprecated
	public MultiClientIface(SavedList src){
		for(int i=0;i<src.length();i++){
			ifaces.add(new ClientIface(src.get(i)));
		}
	}
	public boolean uploadPair(DaughterPair pair){
		
	}
	public boolean uploadTransaction(Transaction t){
		
	}
	public Block downloadByIndex(int x){
		
	}
	public ArrayList<Block> getBlocks(int index){
		ArrayList<Block> out=new ArrayList<Block>();
		for(ClientIface i:ifaces){
			ArrayList<Block> d=i.getBlocks(index);
			if(d!=null){
				out.addAll(d);
				index+=d.size();
			}
		}
		return out;
	}
	public Block downloadDaughter(byte[] hash){
		for(ClientIface i:ifaces){
			Block d=i.downloadDaughter(hash);
			if(d!=null&&d.verify())
				return d;
		}
		return null;
	}
	public int downloadBlockchain(BlockchainBase manager){
		int n=0;
		for(ClientIface i:ifaces){
			n+=i.downloadBlockchain(manager);
		}
		return n;
	}
	public boolean isNetErr() {
		return false;
	}
	public void close() {
		for(ClientIface i:ifaces)
			i.close();
	}

}
