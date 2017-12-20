package nuclear.slithernode.nb;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.Block;
import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slitherge.top.io;
import nuclear.slitherio.uint256_t;
import nuclear.slithernet.Server;

public class Node extends Server {
	private static final uint256_t diff = new uint256_t("385973630791053983625820140551668289960184168413434702593271892430290944");
	ArrayList<Block> blocks=new ArrayList<Block>();
	ArrayList<Block> daughters=new ArrayList<Block>();
	Block current;
	private ECDSAKey key;
	public Node() {
		super(1152);
		key=new ECDSAKey();
		blocks.add(new Block(new byte[32], diff, new byte[0]));
		blocks.get(0).CPUmine(key.getPublicKey());
		newCurrent();
		try{
			start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void newCurrent() {
		if(current!=null){
			current.CPUmine(key.getPublicKey());
			blocks.add(current);
		}
		current=new Block(blocks.get(blocks.size()-1).getHash(), diff, new byte[0]);
	}
	public static void main(String[] args) {
		new Node();
	}
	protected byte[] easyServe(byte[] in){
		String cmd=new String(Arrays.copyOf(in, 6),StandardCharsets.UTF_8);
		String out="OK";
		byte[] data=Arrays.copyOfRange(in, 6, in.length);
		io.println("Request for "+cmd);
		io.println("Data: "+Arrays.toString(data));
		if(cmd.equals("ADPAIR")){
			DaughterPair pair=DaughterPair.deserialize(data);
			if(!pair.verify())
				out="INVALID";
			else{
				current.addTransaction(pair.tr);
				daughters.add(pair.block);
			}
		}else if(cmd.equals("LSHASH")){
			io.println("Sending last hash: "+lastBlock().getHash().length);
			return lastBlock().getHash();
		}else {
			out="CNFE";
		}
		checkup();
		io.println("Sending resp:"+out);
		return out.getBytes(StandardCharsets.UTF_8);
	}
	private Block lastBlock() {
		return blocks.get(blocks.size()-1);
	}
	private void checkup() {
		if(current.numTransactions()>=Block.TRANSACTION_LIMIT)
			newCurrent();
	}
}
