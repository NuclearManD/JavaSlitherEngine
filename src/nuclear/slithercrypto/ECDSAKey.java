package nuclear.slithercrypto;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ECDSAKey {
	/*
	 * Generate new keypair
	 */
	KeyPair key;
	public ECDSAKey() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			keyGen.initialize(256,random);
			key = keyGen.generateKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
			key=null;
		}
	}
	public ECDSAKey( byte[] pub,byte[] pri) {
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("EC");
			key=new KeyPair(kf.generatePublic(new X509EncodedKeySpec(pub)), kf.generatePrivate(new PKCS8EncodedKeySpec(pri)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public byte[] getPublicKey() {
		return key.getPublic().getEncoded();                    
	}

	public byte[] getPrivateKey() {
		return key.getPrivate().getEncoded();
	}
	public byte[] sign(byte[] data) {
		Signature sig = null;
		try {
			sig = Signature.getInstance("SHA1WithECDSA");
	        sig.initSign(key.getPrivate());
	        sig.update(data);
			return sig.sign();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public byte[] sign(String data) {
		return sign(data.getBytes(StandardCharsets.UTF_8));
	}
	public static boolean verify(byte[] sig,byte[] data, byte[] pubKey) {
        try {
    		Signature sigg = Signature.getInstance("SHA1withRSA");
            sigg.initVerify(KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(pubKey)));
            sigg.update(data);
			return sigg.verify(sig);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
}
