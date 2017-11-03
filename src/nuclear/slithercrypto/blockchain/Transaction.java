package nuclear.slithercrypto.blockchain;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherS;
import nuclear.slitherio.uint256_t;

public class Transaction {

	public static final int TRANSACTION_LENGTH = 1024;
	public static final byte TRANSACTION_ARBITRARY=0;
	public static final byte TRANSACTION_SEND_COIN=1;
	public static final byte TRANSACTION_SEND_GAS=2;
	//public static final byte TRANSACTION_MAKE_PROGRAM=3;
	//public static final byte TRANSACTION_EXECUTE_PROGRAM=4;
	//public static final byte TRANSACTION_STORE_MESSAGE=5;
	public static final byte TRANSACTION_STORE_FILE=6;
	private static final int KEY_LEN = 91;
	private static final int SIG_LEN = 74;
	public static final int PACKED_LEN = 1116;//1024(data) + 91(key) + 1(type)
	public byte[] pubKey;
	public byte[] descriptor;
	public byte type;
	public Transaction(byte[] packed) {
		pubKey=Arrays.copyOfRange(packed, 0, KEY_LEN);
		descriptor=Arrays.copyOfRange(packed, KEY_LEN, TRANSACTION_LENGTH-1);
		type=packed[TRANSACTION_LENGTH-1];
	}
	public Transaction(byte[] publicKey, byte[] descriptr, byte t) {
		pubKey=Arrays.copyOf(publicKey, KEY_LEN);
		descriptor=Arrays.copyOf(descriptr, TRANSACTION_LENGTH);
		type=t;
	}
	public static DaughterPair makeFile(byte[] publickey,byte[] priKey, byte[] program_data,byte[] lastBlockHash,String meta) {
		byte data[]=new byte[TRANSACTION_LENGTH];
		Block tmp=new Block(publickey,lastBlockHash,new uint256_t("771947261582107967251640281103336579920368336826869405186543784860581888"),program_data);
		long hashes=0;
		long mil=System.currentTimeMillis();
		while(!tmp.mineOnce(publickey)) {
			hashes++;
			if(System.currentTimeMillis()-mil>3000) {
				io.println(hashes/(System.currentTimeMillis()-mil)+" KH/s...");
				hashes=0;
				mil=System.currentTimeMillis();
			}
		}
		int n=0;
		for(byte i:tmp.getHash()) {
			data[n]=i;
			n++;
		}
		n=32;
		for(byte i:meta.getBytes(StandardCharsets.UTF_8)) {
			data[n]=i;
			n++;
		}
		n=data.length-SIG_LEN;
		ECDSAKey key=new ECDSAKey(publickey,priKey);
		byte[] sig=key.sign(Arrays.copyOf(data, TRANSACTION_LENGTH-SIG_LEN));
		for(byte i=0;i<sig.length;i++) {
			data[i+TRANSACTION_LENGTH-SIG_LEN]=sig[i];
		}
		return new DaughterPair(new Transaction(publickey,data,TRANSACTION_STORE_FILE),tmp);
	}
	public static Transaction sendGas(byte[] sender, byte[] receiver, byte[] priKey,int amount) {
		byte data[]=new byte[TRANSACTION_LENGTH];
		int n=0;
		for(byte i:receiver) {
			data[n]=i;
			n++;
		}
		for(byte i:SlitherS.longToBytes(amount)) {
			data[n]=i;
			n++;
		}
		n=data.length-SIG_LEN;
		ECDSAKey key=new ECDSAKey(sender,priKey);
		byte[] sig=key.sign(Arrays.copyOf(data, TRANSACTION_LENGTH-SIG_LEN));
		for(byte i:sig) {
			data[n]=i;
			n++;
		}
		return new Transaction(sender,data,TRANSACTION_SEND_GAS);
	}
	public static Transaction sendCoins(byte[] sender, byte[] receiver, byte[] priKey,double amount) {
		byte data[]=new byte[TRANSACTION_LENGTH];
		int n=0;
		for(byte i:receiver) {
			data[n]=i;
			n++;
		}
		for(byte i:SlitherS.longToBytes((long)(amount*1024))) {
			data[n]=i;
			n++;
		}
		n=data.length-SIG_LEN;
		ECDSAKey key=new ECDSAKey(sender,priKey);
		byte[] sig=key.sign(Arrays.copyOf(data, TRANSACTION_LENGTH-SIG_LEN));
		for(byte i:sig) {
			data[n]=i;
			n++;
		}
		return new Transaction(sender,data,TRANSACTION_SEND_COIN);
	}

	public byte[] pack() {
		byte[] out = new byte[PACKED_LEN];
		int n=0;
		for(byte i:pubKey) {
			out[n]=i;
			n++;
		}
		for(byte i:descriptor) {
			out[n]=i;
			n++;
		}
		out[PACKED_LEN-1]=type;
		return out;
	}
	public byte[] getSender() {
		return pubKey;
	}
	public String toString() {
		String o="Transaction: ";
		if(type==TRANSACTION_ARBITRARY) {
			o+="Arbitrary";
		}else if(type==TRANSACTION_SEND_COIN) {
			o+="Send "+(double)SlitherS.bytesToLong(Arrays.copyOfRange(descriptor, KEY_LEN, 520))/1024+" Coins to "+Base64.getEncoder().encodeToString(Arrays.copyOf(descriptor, KEY_LEN));
		}else if(type==TRANSACTION_SEND_GAS) {
			o+="Send "+SlitherS.bytesToLong(Arrays.copyOfRange(descriptor, KEY_LEN, 520))+" Gas to "+Base64.getEncoder().encodeToString(Arrays.copyOf(descriptor, KEY_LEN));
		/*}if(type==TRANSACTION_MAKE_PROGRAM) {
			o+="Upload Program\n";
			o+="Daughter block hash: "+Base64.getEncoder().encodeToString(Arrays.copyOf(descriptor, 32));*/
		}else if(type==TRANSACTION_STORE_FILE) {
			o+="Store File With Meta '";
			o+=new String(getMeta(),StandardCharsets.UTF_8)+"";
			o+="'; Daughter has hash "+Base64.getEncoder().encodeToString(Arrays.copyOf(descriptor, 32));
		}else
			o+="Unknown Type";
		o+="\n > Transaction created by "+Base64.getEncoder().encodeToString(pubKey);
		return o;
	}
	public boolean verify() {
		return ECDSAKey.verify(Arrays.copyOfRange(descriptor, descriptor.length-SIG_LEN, descriptor.length), Arrays.copyOf(descriptor, descriptor.length-SIG_LEN), pubKey);
	}
	public byte[] getMeta() {
		return Arrays.copyOfRange(descriptor, 32, descriptor.length-SIG_LEN);
	}
}
