package nuclear.slithercrypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypt {
	public static byte[] SHA256(String a) {
		return a.getBytes(StandardCharsets.UTF_8);
	}
	public static byte[] SHA256(byte[] a) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return digest.digest(a);
	}
}
