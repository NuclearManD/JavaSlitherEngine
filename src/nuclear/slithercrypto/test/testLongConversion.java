package nuclear.slithercrypto.test;


import nuclear.slithercrypto.Crypt;
import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.Block;
import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherS;
import nuclear.slitherio.uint256_t;

public class testLongConversion {
	public static void main(String[] args) {
		io.println(""+SlitherS.bytesToLong(SlitherS.longToBytes(21455643)));
		Block a=new Block(Crypt.SHA256("11567"), new uint256_t("5763342"), SlitherS.longToBytes(21455643));
		a.CPUmine(Crypt.SHA256("11567"));
		Block b=new Block(a.pack());
		io.println("1 valid:"+b.verify());
		ECDSAKey key=new ECDSAKey();
		byte data[]={1,2,3,4,5,6,7,8,9,0};
		DaughterPair a2=Transaction.makeFile(key.getPublicKey(), key.getPrivateKey(), data, a.getHash(), "AARRG!");
		io.println("a2 valid:"+a2.verify());
		//io.println("a2s: "+Arrays.toString(a2.tr.))
		io.println("b2 valid:"+DaughterPair.deserialize(a2.serialize()).verify());
	}

}
