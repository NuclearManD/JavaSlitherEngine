package nuclear.slithernet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import nuclear.slitherge.top.io;

public abstract class Server implements Runnable{
	protected Thread serverThread;
	protected ServerSocket sok;
	protected int port;
	private Socket tmpsok;
	public Server(int port){
		this.port=port;
	}
	public void start() throws IOException{
		sok = new ServerSocket(port);
		while(true) {
			try {
				tmpsok = sok.accept();
				new Thread(this).start();
			}   
			catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	public void run() {
		try {
			serve(tmpsok);
		} catch (Exception e) {
		}
	}
	protected void serve(Socket client) throws Exception{
		DataInputStream is=new DataInputStream(client.getInputStream());
		DataOutputStream os=new DataOutputStream(client.getOutputStream());
		long length=is.readLong();
		io.println("Length: "+length);
		byte in[]=new byte[(int)length];
		int i=0;
		while(length>i&&is.available()>0) {
			in[i]=is.readByte();
			i++;
		}
		byte[] o=easyServe(in);
		io.println("sending "+o.length+" byte reply...");
		os.writeLong(o.length);
		os.write(o);
		os.close();
		is.close();
		client.close();
	}
	protected byte[] easyServe(byte[] in) {
		return easyServe(new String(in,StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
	}
	protected String easyServe(String in) {
		return "SlitherError";
	}
}
