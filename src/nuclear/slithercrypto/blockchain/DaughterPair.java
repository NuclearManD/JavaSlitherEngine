package nuclear.slithercrypto.blockchain;

import java.util.Arrays;

import nuclear.slitherge.top.io;

public class DaughterPair {
	public Transaction tr;
	public Block block;
	public DaughterPair(Transaction t, Block d) {
		tr=t;
		block=d;
	}
	public boolean verify() {
		if(block.verify()&&tr.verify())
			if(Arrays.copyOf(tr.descriptor,32).equals(block.getHash()))
				return true;
		return false;
	}
	public byte[] serialize() {
		byte trp[]=tr.pack();
		byte bkp[]=block.pack();
		byte out[]=new byte[Transaction.PACKED_LEN+bkp.length];
		for(int i=0;i<Transaction.PACKED_LEN;i++)
			out[i]=trp[i];
		for(int i=0;i<bkp.length;i++)
			out[i+Transaction.PACKED_LEN]=bkp[i];
		io.println(""+out.length);
		return out;
	}
	public static DaughterPair deserialize(byte[] data) {
		byte trp[]=Arrays.copyOf(data, Transaction.PACKED_LEN);
		byte bkp[]=Arrays.copyOfRange(data, Transaction.PACKED_LEN,data.length);
		io.println(""+data.length);
		return new DaughterPair(new Transaction(trp),new Block(bkp));
	}
}
