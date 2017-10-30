package nuclear.slithercrypto.blockchain;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import nuclear.slithercrypto.Crypt;
import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherS;
import nuclear.slitherio.uint256_t;

public class Block {
	public static final int HEADER_LENGTH = 239;
	private static final byte[] CURRENT_VERSION = {1,0,0,0};
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
	/*
	 *  Checks if the block is valid
	 *  @return true if block is valid, otherwise false
	 */
	public boolean verify() {
		byte[] packed=packNoHash();
		valid=Crypt.SHA256(packed).equals(hash);
		if(valid)
			valid=difficulty.compareTo(new uint256_t(hash))>0;
		return valid;
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
	public boolean mineOnce(byte[] publicKey) {
		if(!valid) {
			miner=publicKey;
			r.nextBytes(key);
			reHash();
			valid=difficulty.compareTo(new uint256_t(hash))>0;
			return valid;
		}
		return true;
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
	public Transaction getTransaction(int index) throws TransactionIndexError{
		index=index*Transaction.TRANSACTION_LENGTH;
		if(index>data.length)
			throw new TransactionIndexError("Transaction Out Of Bounds.");
		return new Transaction(Arrays.copyOfRange(data,index,index+Transaction.TRANSACTION_LENGTH));
	}
	public void addTransaction(Transaction t){
		int index=data.length;
		byte[] tmp=new byte[index+Transaction.TRANSACTION_LENGTH];
		for(int i=0;i<data.length;i++) {
			tmp[i]=data[i];
		}
		byte[] packed=t.pack();
		for(int i=0;i<Transaction.TRANSACTION_LENGTH;i++) {
			tmp[i+index]=packed[i];
		}
		data=tmp;
	}
	public byte[] getHash() {
		if(hash==null)
			reHash();
		return hash;
	}
	public void reHash() {
		byte[] packed=packNoHash();
		hash=Crypt.SHA256(packed);
	}
	private byte[] packNoHash() {
		int length=HEADER_LENGTH+data.length-32;
		byte[] out = new byte[length];
		int n=0;/*
		io.println(""+key.length);
		io.println(""+miner.length);
		io.println(""+lsblock.length);
		io.println(""+difficulty.littleEndian().length);
		io.println(""+version.length);*/
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
}
