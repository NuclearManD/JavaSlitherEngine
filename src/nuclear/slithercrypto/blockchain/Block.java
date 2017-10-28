package nuclear.slithercrypto.blockchain;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import nuclear.slithercrypto.Crypt;
import nuclear.slitherio.SlitherS;
import nuclear.slitherio.uint256_t;

public class Block {
	public static final int HEADER_LENGTH = 652;
	private byte[] hash;
	private byte[] key;
	private byte[] miner;
	private byte[] lsblock;
	private uint256_t difficulty;
	private byte[] version;// 4 bytes
	private long blockLen;
	private byte[] data;
	private boolean valid=false;
	/*
	 *  Creates a Block from raw bytes
	 *  @param packed bytes to be unpacked
	 */
	public Block(byte[] packed) {
		hash=Arrays.copyOfRange(packed, 0, 32);
		key=Arrays.copyOfRange(packed, 32, 64);
		miner=Arrays.copyOfRange(packed, 64, 576);
		lsblock=Arrays.copyOfRange(packed, 576, 608);
		difficulty=new uint256_t(Arrays.copyOfRange(packed, 608, 640));
		version=Arrays.copyOfRange(packed, 640, 644);
		blockLen=SlitherS.bytesToLong(Arrays.copyOfRange(packed, 644, 652));
		data=Arrays.copyOfRange(packed, 652, packed.length);
	}
	/*
	 *  Checks if the block is valid
	 *  @return true if block is valid, otherwise false
	 */
	public boolean verify() {
		byte[] packed=pack();
		valid=Crypt.SHA256(Arrays.copyOfRange(packed,256/8,packed.length)).equals(hash);
		if(valid)
			valid=difficulty.compareTo(new BigInteger(hash))>0;
		return valid;
	}
	public byte[] pack() {
		int length=5120/8+data.length;
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
		for(byte i:data) {
			out[n]=i;
			n++;
		}
		return out;
	}
	public boolean mineOnce(byte[] publicKey) {
		if(!valid) {
			miner=publicKey;
			new Random().nextBytes(key);
			return verify();
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
}
