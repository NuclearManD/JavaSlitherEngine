package nuclear.slithernet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import nuclear.slitherge.top.io;

@SuppressWarnings("unused")
public abstract class Server implements Runnable{
	protected Thread serverThread;
	protected ServerSocket sok;
	protected int port;
	protected Socket tmpsok;
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
			onError(e);
		}
	}
	protected void onError(Exception e) {
		e.printStackTrace();
	}
	protected void serve(Socket client) throws Exception{
		DataInputStream is=new DataInputStream(client.getInputStream());
		DataOutputStream os=new DataOutputStream(client.getOutputStream());
		while(true){
			try{
				long length=is.readLong();
				byte in[]=new byte[(int)length];
				if(length>0) {
					is.readFully(in, 0, (int)length);
				}
				byte[] o=easyServe(in);
				os.writeLong(o.length);
				os.write(o);
				os.flush();
				os.close();
				is.close();
			}catch(Exception e){
				break;
			}
		}
		client.close();
	}
	protected byte[] easyServe(byte[] in) {
		return easyServe(new String(in,StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
	}
	protected String easyServe(String in) {
		return "SlitherError";
	}
}
