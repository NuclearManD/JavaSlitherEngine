package nuclear.blocks.node;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithercrypto.blockchain.SavedChain;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;
import nuclear.slitherio.CLILogger;
import nuclear.slitherio.SlitherS;
import nuclear.slithernet.Server;

public class NodeServer extends Server {
	public static final byte CMD_ADD_PAIR = 5;
	public static final byte CMD_ADD_TRANS = 1;
	public static final byte CMD_GET_BLOCK = 2;
	public static final byte[] RESULT_SUCCESS = "OK".getBytes(StandardCharsets.UTF_8);
	SavedChain blockchain;
	byte[] pubkey;
	protected Thread minerThread;
	protected NodeMiner minerObject;
	public NodeServer(byte[] Key) {
		super(1152);
		blockchain=new SavedChain("C:/Node/blockchain");
		pubkey=Key;
		io.println("Starting...");
		minerObject=new NodeMiner(blockchain,new CLILogger(),true,pubkey);
		minerThread=new Thread(minerObject);
		try {
			start();
			minerThread.start();
		} catch (IOException e) {
			io.println("Could not bind port");
		}
	}
	public byte[] easyServe(byte[] in) {
		byte cmd=in[0];
		byte[] response="OK".getBytes(StandardCharsets.UTF_8);
		byte data[]=Arrays.copyOfRange(in,1,in.length);
		if(cmd==CMD_ADD_PAIR) {
			DaughterPair pair=DaughterPair.deserialize(data);
			if(pair.verify()) {
				blockchain.addPair(pair);
				io.println("Added Daughter Pair:");
				io.println(pair.toString());
			}else {
				response="INVALID".getBytes(StandardCharsets.UTF_8);
				log("CLIENT_ERR","Invalid Pair Submission for daughter "+pair.block.toString());
				log(" > ",pair.tr.toString());
			}
		}else if(cmd==CMD_ADD_TRANS) {
			Transaction t=new Transaction(data);
			if(t.verify()) {
				blockchain.addTransaction(t);
				io.println("Added Transaction:");
				io.println(t.toString());
			}else {
				response="INVALID".getBytes(StandardCharsets.UTF_8);
				log("CLIENT_ERR","Invalid Transaction: "+t.toString());
				log(" > ",t.toString());
			}
		}else if(cmd==CMD_GET_BLOCK){
			int index=(int)SlitherS.bytesToLong(data);
			io.println("Sending block "+index+" to client...");
			if(index<blockchain.length())
				return blockchain.getBlockByIndex(index).pack();
			else
				io.println("Blockchain is not as long as "+index+" blocks.");
		}else
			response="UREC".getBytes(StandardCharsets.UTF_8);
		return response;
	}
	private void log(String string, String string2) {
		io.println(string+"  : "+string2);
	}
}
