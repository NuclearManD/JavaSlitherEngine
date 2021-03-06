package nuclear.slithernet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class USReq extends Client {
	public static final byte[] LOCALHOST={0x7F,0x4E,0x00,0x00,0x7F,0x00,0x00,0x01};
	private static RoutingTable router=new RoutingTable();
	protected byte[] endpoint;
	public USReq(byte[] endpoint) {
		super(1153, "68.4.23.94");
		this.endpoint=endpoint;
	}
	public String get(String q, byte[] adr){
		byte[] request=Arrays.copyOf(endpoint, q.length()+16);
		for(int i=0;i<8;i++){
			request[i+8]=adr[i];
		}
		byte[] data=q.getBytes();
		for(int i=0;i<q.length();i++){
			request[i+16]=data[i];
		}
		byte[] retdat;
		try {
			retdat=poll(request);
			if(retdat==null)throw new IOException();
		} catch (IOException e) {
			return null;
		}
		data=Arrays.copyOfRange(retdat, 16, retdat.length);
		return new String(data,StandardCharsets.UTF_8);
	}
	public String get(String q){
		return get(q,endpoint);
	}
}
