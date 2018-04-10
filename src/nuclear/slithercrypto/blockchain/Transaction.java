package nuclear.slithercrypto.blockchain;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import nuclear.slithercrypto.ECDSAKey;
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
	public static final int KEY_LEN = 91;
	public static final int SIG_LEN = 74;
	public static final int PACKED_LEN = 1116;//1024(data) + 91(key) + 1(type)
	public byte[] pubKey;
	public byte[] descriptor;
	public byte type;
	
	
	public Transaction(byte[] packed) {
		pubKey=Arrays.copyOf(packed, KEY_LEN);
		descriptor=Arrays.copyOfRange(packed, KEY_LEN, PACKED_LEN-1);
		type=packed[PACKED_LEN-1];
	}
	public Transaction(byte[] publicKey, byte[] descriptr, byte t) {
		pubKey=Arrays.copyOf(publicKey, KEY_LEN);
		descriptor=Arrays.copyOf(descriptr, TRANSACTION_LENGTH);
		type=t;
	}
	public static DaughterPair makeFile(byte[] publickey,byte[] priKey, byte[] program_data,byte[] lastBlockHash,String meta) {
		ECDSAKey key=new ECDSAKey(publickey,priKey);
		byte data[]=new byte[TRANSACTION_LENGTH];
		Block tmp=new Block(publickey,lastBlockHash,new uint256_t("771947261582107967251640281103336579920368336826869405186543784860581888"),program_data);
		tmp.CPUmine(publickey);
		int n=0;
		for(byte i:tmp.getHash()) {
			data[n]=i;
			n++;
		}
		byte[] byte_meta=meta.getBytes(StandardCharsets.UTF_8);
		data[32]=(byte) byte_meta.length;
		n=33;
		for(byte i:byte_meta){
			data[n]=i;
			n++;
		}
		n=data.length-SIG_LEN;
		byte[] sig=key.sign(Arrays.copyOf(data, TRANSACTION_LENGTH-SIG_LEN));
		for(byte i:sig) {
			data[n]=i;
			n++;
		}
		return new DaughterPair(new Transaction(publickey,data,TRANSACTION_STORE_FILE),tmp);
	}
	public static Transaction sendCoins(byte[] sender, byte[] receiver, byte[] priKey,double amount) {
		byte data[]=new byte[TRANSACTION_LENGTH];
		int n=0;
		for(byte i:receiver) {
			data[n]=i;
			n++;
		}
		n=91;
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
		n=KEY_LEN;
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
	public boolean verify() {
		return ECDSAKey.verify(Arrays.copyOfRange(descriptor, descriptor.length-SIG_LEN, descriptor.length), Arrays.copyOf(descriptor, descriptor.length-SIG_LEN), pubKey);
	}
	public byte[] getMeta() {
		return Arrays.copyOfRange(descriptor, 33, 33+descriptor[32]);
	}
	public void setMeta(byte[] meta){
		descriptor[32]=(byte) meta.length;
		int n=33;
		for(byte i:meta){
			descriptor[n]=i;
			n++;
		}
	}
	public String toString() {
		String o="Transaction: ";
		if(type==TRANSACTION_ARBITRARY) {
			o+="Arbitrary";
		}else if(type==TRANSACTION_SEND_COIN) {
			o+="Send "+(double)SlitherS.bytesToLong(Arrays.copyOfRange(descriptor, KEY_LEN, 520))/1024+" Coins to "+Base64.getEncoder().encodeToString(Arrays.copyOf(descriptor, KEY_LEN));
		}else if(type==TRANSACTION_STORE_FILE) {
			o+="Store File With Meta '";
			o+=new String(getMeta(),StandardCharsets.UTF_8)+"";
			o+="'; Daughter has hash "+Base64.getEncoder().encodeToString(Arrays.copyOf(descriptor, 32));
		}else
			o+="Unknown Type";
		o+="\n > Transaction created by "+Base64.getEncoder().encodeToString(pubKey);
		return o;
	}
	public byte[] getDaughterHash() {
		return Arrays.copyOf(descriptor, 32);
	}
	public double getCoinsSent(){
		if(type!=TRANSACTION_SEND_COIN)
			return 0;
		return (double)SlitherS.bytesToLong(Arrays.copyOfRange(descriptor, 91, 99))/1024;
	}
	public double getTransactionCost(){
		return (double)TRANSACTION_LENGTH*Block.BYTE_COST;
	}
	public byte[] getReceiver() {
		return Arrays.copyOf(descriptor, KEY_LEN);
	}
}
