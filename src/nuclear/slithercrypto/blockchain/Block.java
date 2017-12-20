package nuclear.slithercrypto.blockchain;

import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import nuclear.slithercrypto.Crypt;
import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherS;
import nuclear.slitherio.uint256_t;

public class Block {
	public static final int HEADER_LENGTH = 239;
	private static final byte[] CURRENT_VERSION = {1,0,0,0};
	public static final int TRANSACTION_LIMIT = 64;
	private byte[] hash;			// 32 bytes
	private byte[] key;				// 32 bytes
	private byte[] miner;			// 91 bytes
	private byte[] lsblock;			// 32 bytes
	private uint256_t difficulty;	// 32 bytes
	private byte[] version;			// 4 bytes
	private long blockLen;			// 8 bytes
	private long timestamp;			// 8 bytes
	private byte[] data;			// ? bytes
	private boolean valid=false;
	Random r=new Random();
	/*
	 *  Creates a Block from raw bytes
	 *  @param packed bytes to be unpacked
	 */
	public Block(byte[] packed) {
		hash=Arrays.copyOfRange(packed, 0, 32);
		key=Arrays.copyOfRange(packed, 32, 64);
		miner=Arrays.copyOfRange(packed, 64, 155);
		lsblock=Arrays.copyOfRange(packed, 155, 187);
		difficulty=new uint256_t(Arrays.copyOfRange(packed, 187, 219));
		version=Arrays.copyOfRange(packed, 219, 223);
		blockLen=SlitherS.bytesToLong(Arrays.copyOfRange(packed, 223, 231));
		timestamp=SlitherS.bytesToLong(Arrays.copyOfRange(packed, 231, 239));
		data=Arrays.copyOfRange(packed, HEADER_LENGTH, packed.length);
	}
	public Block(byte[] miner, byte[] lastblock, uint256_t diff, byte[] data) {
		this.data=data;
		difficulty=diff;
		this.miner=miner;
		lsblock=lastblock;
		timestamp=System.currentTimeMillis() / 1000L;
		key=new byte[32];
		version=CURRENT_VERSION;
		blockLen=HEADER_LENGTH+data.length;
	}
	public Block(byte[] lastblock, uint256_t diff, byte[] data) {
		this.data=data;
		difficulty=diff;
		this.miner=new byte[91];
		lsblock=lastblock;
		timestamp=System.currentTimeMillis() / 1000L;
		key=new byte[32];
		version=CURRENT_VERSION;
		blockLen=HEADER_LENGTH+data.length;
	}
	/*
	 *  Checks if the block is valid
	 *  @return true if block is valid, otherwise false
	 */
	public boolean verify() {
		byte[] packed=packNoHash();
		valid=Arrays.equals(Crypt.SHA256(packed),hash);
		valid=valid&&(difficulty.compareTo(new uint256_t(hash))>0);
		return valid;
	}
	public boolean mineOnce(byte[] publicKey) {
		if(!valid) {
			miner=publicKey;
			r.nextBytes(key);
			timestamp=System.currentTimeMillis() / 1000L;
			reHash();
			return verify();
		}
		return true;
	}
	public void reHash() {
		byte[] packed=packNoHash();
		hash=Crypt.SHA256(packed);
	}
	private byte[] packNoHash() {
		int length=HEADER_LENGTH+data.length-32;
		byte[] out = new byte[length];
		int n=0;
		for(byte i:key) {
			out[n]=i;
			n++;
		}
		for(byte i:miner) {
			out[n]=i;
			n++;
		}
		for(byte i:lsblock) {
			out[n]=i;
			n++;
		}
		for(byte i:difficulty.littleEndian()) {
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
		for(byte i:hash) {
			out[n]=i;
			n++;
		}
		for(byte i:key) {
			out[n]=i;
			n++;
		}
		for(byte i:miner) {
			out[n]=i;
			n++;
		}
		for(byte i:lsblock) {
			out[n]=i;
			n++;
		}
		for(byte i:difficulty.littleEndian()) {
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
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data=data;
		valid=false;
		blockLen=HEADER_LENGTH+data.length;
	}
	public void setDifficulty(byte[] data) {
		this.difficulty=new uint256_t(data);
		valid=false;
	}
	public uint256_t getDifficulty() {
		return difficulty;
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
	public void addTransaction(Transaction t){
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
		if(hash==null)
			reHash();
		return hash;
	}
	public int numTransactions() {
		return data.length/Transaction.PACKED_LEN;
	}
	public String toString(){
		return "Block:"+
				"\n  StoredHash="+Base64.getEncoder().encodeToString(hash)+
				"\n  actualHash="+Base64.getEncoder().encodeToString(Crypt.SHA256(packNoHash()))+
				"\n  dataLength="+data.length+
				"\n  valid   =  "+verify();
	}
	public void CPUmine(byte[] pubKey) {
		long hashes=0;
		long mil=System.currentTimeMillis();
		while(!mineOnce(pubKey)) {
			hashes++;
			if(System.currentTimeMillis()-mil>3000) {
				io.println(hashes/(System.currentTimeMillis()-mil)+" KH/s...");
				hashes=0;
				mil=System.currentTimeMillis();
			}
		}
	}
}
