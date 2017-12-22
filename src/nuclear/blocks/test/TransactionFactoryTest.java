package nuclear.blocks.test;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import nuclear.slithercrypto.Crypt;
import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;

public class TransactionFactoryTest {
	public static void main(String[] args) {
		io.println("Makaing ECDSA key...");
		ECDSAKey key=new ECDSAKey();
		byte[] pgm= {1,2,3,4,5,6,7,78,9};
		byte[] lastBlockHash=Crypt.SHA256("Khalo le Monden");
		io.println("Making file transaction...");
		DaughterPair t=Transaction.makeFile(key.getPublicKey(), key.getPrivateKey(), pgm, lastBlockHash, "GetRektBadKidz");
		io.println(t.tr.toString());
		io.println(key.getPrivateKey().length+"");
		io.println(key.getPublicKey().length+"");
		if(!t.tr.verify())
			io.println("Error: SlitherCrypto broken: Transaction invalidly invalid.");
	}
	public static void showECKeyEncodings() {

	    try {
	        KeyPairGenerator kp = KeyPairGenerator.getInstance("EC");
	        kp.initialize(256,SecureRandom.getInstance("SHA1PRNG"));
	        KeyPair keyPair = kp.generateKeyPair();

	        PrivateKey privKey = keyPair.getPrivate();
	        byte[] encodedPrivKey = privKey.getEncoded();
	        //System.out.println(toHex(encodedPrivKey));

	        PublicKey pubKey = keyPair.getPublic();
	        byte[] encodedPubKey = pubKey.getEncoded();
	        //System.out.println(toHex(encodedPubKey));

	        KeyFactory kf = KeyFactory.getInstance("EC");
	        PublicKey pubKey2 = kf.generatePublic(new X509EncodedKeySpec(encodedPubKey));
	        if (Arrays.equals(pubKey2.getEncoded(), encodedPubKey)) {
	            System.out.println("That worked for the public key");
	        }

	        PrivateKey privKey2 = kf.generatePrivate(new PKCS8EncodedKeySpec(encodedPrivKey));
	        if (Arrays.equals(privKey2.getEncoded(), encodedPrivKey)) {
	            System.out.println("That worked for the private key");
	        }

	    } catch (Exception e) {
	        throw new IllegalStateException(e);
	    }

	}
}
