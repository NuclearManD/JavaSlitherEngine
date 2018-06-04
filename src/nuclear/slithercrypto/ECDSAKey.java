package nuclear.slithercrypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

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
	public ECDSAKey(String path) {
		try {
			FileInputStream stream = new FileInputStream(path);
			byte[] pub=new byte[91];
			for(int i=0;i<91;i++) {
				pub[i]=(byte)stream.read();
			}
			byte[] pri=new byte[(int) (stream.getChannel().size()-91)];
			for(int i=0;i<pri.length;i++) {
				pri[i]=(byte)stream.read();
			}
			stream.close();
			KeyFactory kf;
			kf = KeyFactory.getInstance("EC");
			key=new KeyPair(kf.generatePublic(new X509EncodedKeySpec(pub)), kf.generatePrivate(new PKCS8EncodedKeySpec(pri)));
		}catch(Exception e) {
			key=null;
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
	        byte[] der=sig.sign();
	        byte[] out=Arrays.copyOf(der, 74);
	        out[73]=(byte)der.length;
			return out;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public byte[] sign(String data) {
		return sign(data.getBytes(StandardCharsets.UTF_8));
	}
	public static boolean verify(byte[] sig,byte[] data, byte[] pubKey) {
		KeyFactory kf;
        try {
			kf = KeyFactory.getInstance("EC");
    		Signature sigg = Signature.getInstance("SHA1withECDSA");
            sigg.initVerify(kf.generatePublic(new X509EncodedKeySpec(pubKey)));
            sigg.update(data);
            sig=Arrays.copyOf(sig, sig[73]);
			return sigg.verify(sig);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
	public boolean save(String path) {
		File gashdg=new File(path);
		if(gashdg.exists())
			return false;
		try {
			FileOutputStream stream=new FileOutputStream(path);
			stream.write(Arrays.copyOf(getPublicKey(),91));
			stream.write(getPrivateKey());
			stream.close();
			return true;
		} catch (Exception e) {}
		return false;
	}
}
