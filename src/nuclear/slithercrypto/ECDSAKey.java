package nuclear.slithercrypto;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import nuclear.slitherge.top.io;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class ECDSAKey {
	/*
	 * Generate new keypair
	 */
	private static byte[] P256_HEAD = Base64.getDecoder().decode("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE");
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
	/*private PublicKey convertP256Key(byte[] w) throws InvalidKeySpecException {
	    byte[] encodedKey = new byte[P256_HEAD.length + w.length];
	    System.arraycopy(P256_HEAD, 0, encodedKey, 0, P256_HEAD.length);
	    System.arraycopy(w, 0, encodedKey, P256_HEAD.length, w.length);
	    KeyFactory eckf;
	    try {
	        eckf = KeyFactory.getInstance("EC");
	    } catch (NoSuchAlgorithmException e) {
	        throw new IllegalStateException("EC key factory not present in runtime");
	    }
	    X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
	    return (ECPublicKey) eckf.generatePublic(ecpks);
	}*/
	public static void prnt(byte[] a) {
		for(byte i:a)
			io.print(""+(char)i);
	}
	public static byte[] sigTo64(byte[] sign) throws Exception{
		DerInputStream derInputStream = new DerInputStream(sign);
		DerValue[] values = derInputStream.getSequence(2);
	    byte[] random = values[0].getPositiveBigInteger().toByteArray();
	    byte[] signature = values[1].getPositiveBigInteger().toByteArray();
	    // r and s each occupy half the array
	    // Remove padding bytes
	    byte[] tokenSignature = new byte[64];
	    System.arraycopy(random, random.length > 32 ? 1 : 0, tokenSignature, random.length < 32 ? 1 : 0, random.length > 32 ? 32 : random.length);
	    System.arraycopy(signature, signature.length > 32 ? 1 : 0, tokenSignature, signature.length < 32 ? 33 : 32, signature.length > 32 ? 32 : signature.length);
	    System.out.println("Full Signature length: "+tokenSignature.length+" r length: "+random.length+" s length"+signature.length);
	    return tokenSignature;
	}
	public static byte[] sigFrom64(byte[] b64) throws Exception{
	    
	    // r and s each occupy half the array
	    // Remove padding bytes
	    byte[] signature = Arrays.copyOfRange(b64, 32,64);
	    byte[] random = Arrays.copyOfRange(b64, 0,32);
		DerValue[] values = {new DerValue(random),new DerValue(signature)};
		DerOutputStream ostream = new DerOutputStream();
		
		ostream.putSequence(values);
		byte[] d=ostream.toByteArray();
		io.println("Full Signature length: "+d.length+" r length: "+random.length+" s length"+signature.length);
		return d;
	}
}
