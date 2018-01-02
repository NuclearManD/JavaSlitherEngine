package nuclear.slithernet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import nuclear.slitherge.top.io;

public class Client {
	protected DataInputStream inStream;
	protected DataOutputStream outStream;
	protected Socket socket;
	private String host;
	private int port;
	public int timeout=150000;
	public Client(int port, String host) throws IOException{
		this.host=host;
		this.port=port;
	}
	public String ezPoll(String in) throws IOException {
		return new String(poll(in.getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8);
	}
	public synchronized byte[] poll(byte[] in) throws IOException {
		io.println("\nConnecting...\n");
		socket=new Socket();
		socket.connect(new InetSocketAddress(host,port), timeout);
		io.println("Connected...");
		inStream=new DataInputStream(socket.getInputStream());
		outStream=new DataOutputStream(socket.getOutputStream());
		outStream.writeLong(in.length);
		outStream.write(in);
		outStream.flush();
		io.println("Data sent, awaiting response...");
		long len=inStream.readLong();
		byte out[]=new byte[(int)len];
		if(len>0) {
			inStream.readFully(out, 0, (int)len);
		}
		socket.close();
		return out;
	}
}
