package nuclear.slithercrypto.test;

import java.io.File;
import java.util.Base64;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slitherge.top.io;

public class KeySaveTest {

	public static void main(String[] args) {
		String path=System.getProperty("user.home")+"/AppData/tmp/kf.key";
		File q=new File(path);
		if(q.exists())
			q.delete();
		else
			q.getParentFile().mkdirs();
		ECDSAKey key=new ECDSAKey();
		key.save(path);
		ECDSAKey key2=new ECDSAKey(path);
		io.println(Base64.getEncoder().encodeToString(key.getPrivateKey()));
		io.println(Base64.getEncoder().encodeToString(key2.getPrivateKey()));
		io.println(Base64.getEncoder().encodeToString(key.getPublicKey()));
		io.println(Base64.getEncoder().encodeToString(key2.getPublicKey()));
	}

}
