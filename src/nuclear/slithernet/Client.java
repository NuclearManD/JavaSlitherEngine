package nuclear.slithernet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class Client {
	protected DataInputStream inStream;
	protected DataOutputStream outStream;
	protected Socket socket;
	private String host;
	private int port;
	public int timeout=5000;
	private boolean busy=false;
	public Client(int port, String host){
		this.host=host;
		this.port=port;
		connect();
	}
	public void connect() {
		if(socket==null||(!socket.isConnected())||socket.isClosed()){
			socket=new Socket();
			try {
				socket.connect(new InetSocketAddress(host, port),timeout);
				inStream=new DataInputStream(socket.getInputStream());
				outStream=new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
			}
		}
	}
	public void disconnect(){
		if(socket.isConnected()){
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
	public String ezPoll(String in) throws IOException {
		return new String(poll(in.getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8);
	}
	public byte[] poll(byte[] in) throws IOException {
		connect();
		if(!socket.isConnected()){
			return null;
		}
		try{
			outStream.writeLong(in.length);
		}catch(IOException e){
			socket.close();
			connect();
			outStream.writeLong(in.length);
		}
		while(busy){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		};
		busy=true;
		inStream.skip(inStream.available());
		outStream.write(in);
		outStream.flush();
		long timer=System.currentTimeMillis()+timeout;
		while(inStream.available()<4){
			if(timer<System.currentTimeMillis()){
				// connection error
				disconnect();
				busy=false;
				return null;
			}
				
		}
		long len=inStream.readLong();
		byte out[]=new byte[(int)len];
		if(len>0) {
			inStream.readFully(out, 0, (int)len);
		}
		busy=false;
		return out;
	}
	public String getAddress() {
		return host+":"+port;
	}
}
