package nuclear.slithercrypto.blockchain;

import java.util.Arrays;
import java.util.Base64;

import nuclear.slitherio.SlitherS;

public class Transaction {

	public static final int TRANSACTION_LENGTH = 1536;
	public static final byte TRANSACTION_ARBITRARY=0;
	public static final byte TRANSACTION_SEND_COIN=1;
	public static final byte TRANSACTION_SEND_GAS=2;
	public static final byte TRANSACTION_MAKE_PROGRAM=3;
	public static final byte TRANSACTION_EXECUTE_PROGRAM=4;
	public static final byte TRANSACTION_STORE_MESSAGE=5;
	public byte[] pubKey;
	public byte[] descriptor;
	public byte type;
	public Transaction(byte[] packed) {
		pubKey=Arrays.copyOfRange(packed, 0, 512);
		descriptor=Arrays.copyOfRange(packed, 512, 1535);
		type=packed[1535];
	}
	public Transaction(byte[] publicKey, byte[] descriptr, byte t) {
		pubKey=Arrays.copyOf(publicKey, 512);
		descriptor=Arrays.copyOf(descriptr, 1023);
		type=t;
	}

	public byte[] pack() {
		byte[] out = new byte[1536];
		int n=0;
		for(byte i:pubKey) {
			out[n]=i;
			n++;
		}
		for(byte i:descriptor) {
			out[n]=i;
			n++;
		}
		out[1535]=type;
		return out;
	}
	public byte[] getSender() {
		return pubKey;
	}
	public String toString() {
		String o="Transaction: ";
		if(type==this.TRANSACTION_ARBITRARY) {
			o+="Arbitrary";
		}if(type==this.TRANSACTION_SEND_COIN) {
			o+="Send "+SlitherS.bytesToLong(Arrays.copyOfRange(descriptor, 512, TRANSACTION_LENGTH-1))+" Coins to "+Base64.getEncoder().encodeToString(Arrays.copyOf(descriptor, 512));
		}if(type==this.TRANSACTION_SEND_GAS) {
			o+="Send "+SlitherS.bytesToLong(Arrays.copyOfRange(descriptor, 512, TRANSACTION_LENGTH-1))+" Gas to "+Base64.getEncoder().encodeToString(Arrays.copyOf(descriptor, 512));
		}if(type==this.TRANSACTION_MAKE_PROGRAM) {
			o+="Upload Program\n";
			o+="Daughter block hash: "+Base64.getEncoder().encodeToString(Arrays.copyOf(descriptor, 32));
		}else
			o+="Unknown Type";
		o+="\n Transaction created by "+Base64.getEncoder().encodeToString(pubKey);
		return o;
	}
}
