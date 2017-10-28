package nuclear.slithercrypto;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAKey {
	/*
	 * Generate new keypair
	 */
	KeyPair key;
	public RSAKey() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(4096);
			key = keyGen.generateKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
			key=null;
		}
	}
	public RSAKey(byte[] pri, byte[] pub) {
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
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
			sig = Signature.getInstance("SHA1WithRSA");
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
	public boolean verify(byte[] sig,byte[] data) {
        try {
    		Signature sigg = Signature.getInstance("SHA1withRSA");
            sigg.initVerify(key.getPublic());
            sigg.update(data);
			return sigg.verify(sig);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
}
