package nuclear.slithernet;

import java.util.Arrays;

import nuclear.slitherge.top.io;

public class RoutingTable {
	public static final byte[] ROUTE_PUBLIC_DATABASE={0x7F,0x00,0x00,0x00,0x00,0x00,0x00,0x3E};
	public static final byte ROUTE_IPV4=0x4E;
	public static final byte ROUTE_IPV4PORT=0x4D;
	public String next(byte[] endpoint) {
		if(Arrays.equals(endpoint, ROUTE_PUBLIC_DATABASE)){
			return "localhost:1152";
		}else if(endpoint[0]==0x7F&&endpoint[1]==ROUTE_IPV4){
			String result="";
			for(int i=4;i<7;i++){
				int q=endpoint[i];
				if(q<0)
					q+=256;
				result+=q+".";
			}
			return result+endpoint[7];
		}else if(endpoint[0]==0x7F&&endpoint[1]==ROUTE_IPV4PORT){
			String result="";
			for(int i=4;i<7;i++){
				int q=endpoint[i];
				if(q<0)
					q+=256;
				result+=q+".";
			}
			int port=endpoint[2]|(endpoint[3]<<8);
			return result+endpoint[7]+":"+port;
		}else if(endpoint[0]==0&&endpoint[1]==0&&endpoint[2]==0&&endpoint[3]==0&&endpoint[4]==1){
			// Route to London's Node
			return ":8081";
		}else if(endpoint[0]==0&&endpoint[1]==0&&endpoint[2]==0&&endpoint[3]==0&&endpoint[4]==0){
			return "68.4.23.94:"+(8000+endpoint[7]);
		}
		return null;
	}
	public boolean convert(byte[] endpoint){
		if(Arrays.equals(endpoint, ROUTE_PUBLIC_DATABASE)){
			return true;
		}else if(endpoint[0]==0x7F&&endpoint[1]==ROUTE_IPV4){
			return false;
		}else if(endpoint[0]==0x7F&&endpoint[1]==ROUTE_IPV4PORT){
			return true;
		}
		return false;
	}
	public static byte[] fromStr(String s){
		String[] q=s.split(":");
		byte[] r=new byte[8];
		for(int i=0;i<8;i++){
			r[i]=Byte.parseByte(q[i]);
		}
		return r;
	}
	public static String toStr(byte[] s){
		String o="";
		for(int i=0;i<7;i++){
			o+=s[i]+':';
		}
		o+=s[7];
		return o;
	}
}
