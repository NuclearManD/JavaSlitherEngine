package nuclear.blocks.node;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import nuclear.slithercrypto.Crypt;
import nuclear.slithercrypto.ECDSAKey;
import nuclear.slithercrypto.blockchain.Block;
import nuclear.slithercrypto.blockchain.BlockChainManager;
import nuclear.slithercrypto.blockchain.DaughterPair;
import nuclear.slithercrypto.blockchain.Transaction;
import nuclear.slitherge.top.io;
import nuclear.slithernet.Server;

public class NodeServer extends Server {
	public static final byte CMD_ADD_PAIR = 5;
	public static final byte CMD_ADD_TRANS = 1;
	public static final byte[] RESULT_SUCCESS = "OK".getBytes(StandardCharsets.UTF_8);
	BlockChainManager blockchain;
	byte[] pubkey;
	public NodeServer(byte[] Key) {
		super(1152);
		blockchain=new BlockChainManager();
		pubkey=Key;
		io.println("Starting...");
		try {
			start();
		} catch (IOException e) {
			io.println("Could not bind port");
		}
	}
	public byte[] easyServe(byte[] in) {
		byte cmd=in[0];
		byte[] response="OK".getBytes(StandardCharsets.UTF_8);
		byte data[]=Arrays.copyOfRange(in,1,in.length);
		io.println(in[0]);
		io.println(Arrays.toString(in));
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
		}else
			response="UREC".getBytes(StandardCharsets.UTF_8);
		return response;
	}
	private void log(String string, String string2) {
		io.println(string+"  : "+string2);
	}
	private Block mineBlockForChain(Block b) {
		b.CPUmine(pubkey);
		return b;
	}
}
