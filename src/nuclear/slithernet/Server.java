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
				tmpsok.setTcpNoDelay(true);
				io.println("New incoming connection from "+tmpsok.getInetAddress().getHostAddress());
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
			e.printStackTrace();
		}
	}
	protected synchronized void serve(Socket client) throws Exception{
		io.println("Stage One");
		DataInputStream is=new DataInputStream(client.getInputStream());
		DataOutputStream os=new DataOutputStream(client.getOutputStream());
		long length=is.readLong();
		io.println("Receiving "+length+" bytes of data...");
		byte in[]=new byte[(int)length];
		if(length>0) {
			is.readFully(in, 0, (int)length);
		}
		is.close();
		io.println("Processing Request...");
		byte[] o=easyServe(in);
		os.writeLong(o.length);
		os.write(o);
		os.flush();
		os.close();
		client.close();
	}
	protected byte[] easyServe(byte[] in) {
		return easyServe(new String(in,StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
	}
	protected String easyServe(String in) {
		return "SlitherError";
	}
}
