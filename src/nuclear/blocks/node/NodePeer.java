package nuclear.blocks.node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import nuclear.slitherge.top.io;

public class NodePeer implements Runnable {
	Socket socket;
	Thread thread;
	NodeServer s;
	public NodePeer(Socket sok, NodeServer nodeServer) {
		thread=new Thread(this);
		thread.start();
		s=nodeServer;
		socket=sok;
	}

	@Override
	public void run() {
		try {
			//socket.setSoTimeout(timeout);
			DataInputStream is=new DataInputStream(socket.getInputStream());
			DataOutputStream os=new DataOutputStream(socket.getOutputStream());
			long length=is.readLong();
			io.println("Receiving "+length+" bytes of data...");
			byte in[]=new byte[(int)length];
			if(length>0) {
				is.readFully(in, 0, (int)length);
			}
			is.close();
			io.println("Processing Request...");
			byte[] o=s.easyServe(in);
			os.writeLong(o.length);
			os.write(o);
			os.flush();
			os.close();
			socket.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
