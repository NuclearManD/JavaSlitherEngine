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
	
	// TODO: Make ifaces get removed when they do not connect often
	
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
		boolean goon=false;
		for(ClientIface i:ifaces){
			goon|=i.uploadPair(pair);
		}
		return goon;
	}
	public boolean uploadTransaction(Transaction t){
		boolean goon=false;
		for(ClientIface i:ifaces){
			goon|=i.uploadTransaction(t);
		}
		return goon;
	}
	public Block downloadByIndex(int x){
		for(ClientIface i:ifaces){
			Block b=i.downloadByIndex(x);
			if(b!=null&&b.verify()){
				return b;
			}
		}
		return null;
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
	public String[] getNodes(){
		ArrayList<String> out=new ArrayList<String>();
		for(ClientIface i:ifaces){
			String[] q=i.getNodes();
			for(String j:q){
				boolean g=true;
				for(String k:out){
					if(k.equals(j)){
						g=false;
						break;
					}
				}
				if(g)out.add(j);
			}
		}
		return (String[])out.toArray();
	}
}
