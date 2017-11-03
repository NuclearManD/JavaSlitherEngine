package nuclear.slithernet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
	protected DataInputStream inStream;
	protected DataOutputStream outStream;
	protected Socket socket;
	public Client(int port, String name) throws IOException{
		socket=new Socket(name, port);
		inStream=new DataInputStream(socket.getInputStream());
		outStream=new DataOutputStream(socket.getOutputStream());
	}
	public Client(int port) throws IOException {
		socket=new Socket("SlitherClient", port);
		inStream=new DataInputStream(socket.getInputStream());
		outStream=new DataOutputStream(socket.getOutputStream());
	}
	public static String ezPoll(int port, String in) throws IOException {
		Client tmp=new Client(port);
		tmp.outStream.writeLong(in.length());
		tmp.outStream.write(in.getBytes(StandardCharsets.UTF_8));
		long len=tmp.inStream.readLong();
		byte out[]=new byte[(int)len];
		int i=0;
		while(len>i&&tmp.inStream.available()>0) {
			out[i]=tmp.inStream.readByte();
			i++;
		}
		return new String(out,StandardCharsets.UTF_8);
	}
}
