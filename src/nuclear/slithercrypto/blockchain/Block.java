package nuclear.slithercrypto.blockchain;

import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import nuclear.slithercrypto.Crypt;
import nuclear.slithercrypto.ECDSAKey;
import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherS;
import nuclear.slitherio.uint256_t;

public class Block {
	public static final int HEADER_LENGTH = 217;
	private static final byte[] CURRENT_VERSION = {1,0,0,0};
	public static final double BYTE_COST = 1.0/1024.0;
	private byte[] miner;			// 91 bytes
	private byte[] lsblock;			// 32 bytes
	private byte[] version;			// 4 bytes
	private long blockLen;			// 8 bytes
	private long timestamp;			// 8 bytes
	protected byte[] data;			// ? bytes
	private byte[] signature;		// 74 bytes
	Random r=new Random();
	/*
	 *  Creates a Block from raw bytes
	 *  @param packed bytes to be unpacked
	 */
	public Block(byte[] packed) {
		if(packed.length<HEADER_LENGTH){
			io.println("ERROR : block size error");
		}
		miner=Arrays.copyOfRange(packed, 0, 91);
		lsblock=Arrays.copyOfRange(packed, 91, 123);
		version=Arrays.copyOfRange(packed, 123, 127);
		blockLen=SlitherS.bytesToLong(Arrays.copyOfRange(packed, 127, 135));
		timestamp=SlitherS.bytesToLong(Arrays.copyOfRange(packed, 135, 143));
		signature=Arrays.copyOfRange(packed, 143, 217);
		data=Arrays.copyOfRange(packed, HEADER_LENGTH, packed.length);
	}
	public Block(byte[] miner, byte[] lastblock, byte[] data) {
		this.data=data;
		this.miner=miner;
		lsblock=lastblock;
		timestamp=System.currentTimeMillis() / 1000L;
		version=CURRENT_VERSION;
		blockLen=HEADER_LENGTH+data.length;
		signature=new byte[74];
	}
	public Block(byte[] lastblock, byte[] data) {
		this.data=data;
		this.miner=new byte[91];
		lsblock=lastblock;
		timestamp=System.currentTimeMillis() / 1000L;
		version=CURRENT_VERSION;
		blockLen=HEADER_LENGTH+data.length;
		signature=new byte[74];
	}
	/*
	 *  Checks if the block is surface valid. (IE, the block alone looks good,
	 *  			but still needs to be checked with the rest of the chain)
	 *  @return true if block is surface valid, otherwise false
	 */
	public boolean verify(){
      return ECDSAKey.verify(signature, packNoSig(), miner);
	}
	public void sign(ECDSAKey key){
		timestamp=System.currentTimeMillis() / 1000L;
		miner=key.getPublicKey();
		signature=key.sign(packNoSig());
	}
	public byte[] packNoSig() {
		int length=HEADER_LENGTH+data.length;
		byte[] out = new byte[length];
		int n=0;
		for(byte i:miner) {
			out[n]=i;
			n++;
		}
		for(byte i:lsblock) {
			out[n]=i;
			n++;
		}
		for(byte i:version) {
			out[n]=i;
			n++;
		}
		for(byte i:SlitherS.longToBytes(blockLen)) {
			out[n]=i;
			n++;
		}
		for(byte i:SlitherS.longToBytes(timestamp)) {
			out[n]=i;
			n++;
		}
		for(byte i:data) {
			out[n]=i;
			n++;
		}
		return out;
	}
	public byte[] pack() {
		int length=HEADER_LENGTH+data.length;
		byte[] out = new byte[length];
		int n=0;
		for(byte i:miner) {
			out[n]=i;
			n++;
		}
		for(byte i:lsblock) {
			out[n]=i;
			n++;
		}
		for(byte i:version) {
			out[n]=i;
			n++;
		}
		for(byte i:SlitherS.longToBytes(blockLen)) {
			out[n]=i;
			n++;
		}
		for(byte i:SlitherS.longToBytes(timestamp)) {
			out[n]=i;
			n++;
		}
		for(byte i:signature) {
			out[n]=i;
			n++;
		}
		for(byte i:data) {
			out[n]=i;
			n++;
		}
		return out;
	}
	public byte[] getData() {
		return data;
	}
	synchronized public void setData(byte[] data) {
		this.data=data;
		blockLen=HEADER_LENGTH+data.length;
	}
	public byte[] getVersion() {
		return version;
	}
	public boolean isComplete() {
		byte[] packed=pack();
		return blockLen==packed.length;
	}
	public Transaction getTransaction(int index){
		index=index*Transaction.PACKED_LEN;
		if(index>data.length)
			return null;
		return new Transaction(Arrays.copyOfRange(data,index,index+Transaction.PACKED_LEN));
	}
	synchronized public void addTransaction(Transaction t){
		int index=data.length;
		byte[] tmp=new byte[index+Transaction.PACKED_LEN];
		for(int i=0;i<data.length;i++) {
			tmp[i]=data[i];
		}
		byte[] packed=t.pack();
		for(int i=0;i<Transaction.PACKED_LEN;i++) {
			tmp[i+index]=packed[i];
		}
		data=tmp;
	}
	public byte[] getHash() {
		return Crypt.SHA256(pack());
	}
	public int numTransactions() {
		return data.length/Transaction.PACKED_LEN;
	}
	public String toString(){
		return "Block:"+
				"\n  Hash   =   "+Base64.getEncoder().encodeToString(getHash())+
				"\n  dataLength="+data.length+
				"\n  valid   =  "+verify();
	}
	synchronized public void setLastBlockHash(byte[] lsblockhash) {
		lsblock=lsblockhash;
	}
	public byte[] getMiner() {
		return this.miner;
	}
	public double getCost() {
		return BYTE_COST*(this.data.length+HEADER_LENGTH);
	}
	public byte[] getLastHash() {
		return lsblock;
	}
	public long getTimestamp() {
		return timestamp;
	}
}
