package nuclear.slithercrypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypt {
	public static byte[] SHA256(String a) {
		return SHA256(a.getBytes(StandardCharsets.UTF_8));
	}
	public static byte[] SHA256(byte[] a) {
		try {
			return MessageDigest.getInstance("SHA-256").digest(a);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
