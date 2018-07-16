package nuclear.slithernet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import nuclear.slithercrypto.Crypt;

public class USReqAES extends USReq {
	private String pass;
	public USReqAES(byte[] endpoint,String p) {
		super(endpoint);
		pass=p;
	}
	public String get(String q, byte[] adr){
		byte[] data=Crypt.EncryptAES(q.getBytes(),pass);
		byte[] request=Arrays.copyOf(endpoint, data.length+16);
		for(int i=0;i<8;i++){
			request[i+8]=adr[i];
		}
		for(int i=0;i<data.length;i++){
			request[i+16]=data[i];
		}
		byte[] retdat;
		try {
			retdat=poll(request);
			if(retdat==null)throw new IOException();
		} catch (IOException e) {
			return null;
		}
		if(retdat.length==0){
			return null;
		}
		data=Arrays.copyOfRange(retdat, 16, retdat.length);
		return new String(Crypt.DecryptAES(data,pass),StandardCharsets.UTF_8);
	}
	public String get(String q){
		return get(q,endpoint);
	}
}
