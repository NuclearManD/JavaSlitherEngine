package nuclear.slithernet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import nuclear.slitherge.top.io;

public class Client {
	protected DataInputStream inStream;
	protected DataOutputStream outStream;
	protected Socket socket;
	public Client(int port, String host) throws IOException{
		socket=new Socket(host, port);
		inStream=new DataInputStream(socket.getInputStream());
		outStream=new DataOutputStream(socket.getOutputStream());
	}
	public static String ezPoll(int port, String in,String host) throws IOException {
		return new String(bytePoll(port,in.getBytes(StandardCharsets.UTF_8),host),StandardCharsets.UTF_8);
	}
	public static byte[] bytePoll(int port, byte[] in, String host) throws IOException{
		Client tmp=new Client(port,host);
		tmp.outStream.writeLong(in.length);
		tmp.outStream.write(in);
		long len=tmp.inStream.readLong();
		byte out[]=new byte[(int)len];
		int i=0;
		while(len>i&&tmp.inStream.available()>0) {
			out[i]=tmp.inStream.readByte();
			i++;
		}
		return out;
	}
}
