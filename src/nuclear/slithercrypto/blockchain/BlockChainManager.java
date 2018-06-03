package nuclear.slithercrypto.blockchain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import nuclear.slithercrypto.ECDSAKey;
import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherS;
import nuclear.slitherio.uint256_t;

public class BlockChainManager extends BlockchainBase {
	private Block current;
	ArrayList<Block> blocks=new ArrayList<Block>();
	ArrayList<Block> daughters=new ArrayList<Block>();
	public BlockChainManager() {
		setup(blocks,daughters);
		setCurrent(new Block(new byte[32],new byte[32],new byte[0]));
	}
	synchronized public void addPair(DaughterPair pair) {
		addTransaction(pair.tr);
		daughters.add(pair.block);
	}
	synchronized public byte[] readFile(String meta,byte[] pubAdr) {
		for(Block block:blocks) {
			for(int i=0;i<block.numTransactions();i++) {
				Transaction t=block.getTransaction(i);
				String tmeta=new String(t.getMeta(),StandardCharsets.UTF_8);
				if(t.type==Transaction.TRANSACTION_STORE_FILE&&Arrays.equals(t.pubKey, pubAdr)&&tmeta.equals(meta))
					return getDaughter(Arrays.copyOf(t.descriptor,32)).getData();
			}
		}
		return null;
	}
	synchronized public Block getDaughter(byte[] hash) {
		for(Block i:daughters) {
			if(Arrays.equals(i.getHash(),hash))return i;
		}
		return null;
	}
	synchronized public void commit(ECDSAKey key) {
		getCurrent().sign(key);
		commit();
	}
	synchronized public boolean addBlock(Block block){
		if(block==null)
			return false;
		if(isNext(block))
			blocks.add(block);
		else
			return false;
		getCurrent().setLastBlockHash(block.getHash());
		return true;
	}
	synchronized public int length() {
		return blocks.size();// subtract one to only count valid blocks
	}
	synchronized public Block getBlockByIndex(int index) {
		return blocks.get(index);
	}
	synchronized public boolean commit() {
		if(addBlock(getCurrent()))
				setCurrent(new Block(new byte[32],blocks.get(blocks.size()-1).getHash(),new byte[0]));
		else
			return false;
		return true;
	}
	synchronized public Block getCurrent() {
		return current;
	}
	synchronized public void setCurrent(Block current) {
		this.current = current;
	}
	synchronized public void writeChain(OutputStream out){
		try {
			for(Block i:blocks) {
				byte[] data=i.pack();
				out.write(SlitherS.longToBytes(data.length));
				out.write(data);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	synchronized public void writeDaughters(OutputStream out){
		try {
			for(Block i:daughters) {
				byte[] data=i.pack();
				out.write(SlitherS.longToBytes(data.length));
				out.write(data);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static BlockChainManager readFiles(InputStream chain,InputStream daughter) {
		BlockChainManager out=new BlockChainManager();
		byte[] ls=new byte[8];
		try {
			while(chain.available()>0) {
				int tmp=0;
				while(tmp<8) {
					ls[tmp]=(byte) chain.read();
					tmp++;
				}
				byte[] buf=new byte[(int) SlitherS.bytesToLong(ls)];
				tmp=0;
				while(tmp<buf.length-1) {
					tmp+=chain.read(buf,tmp,buf.length-tmp);
				}
				out.addBlock(new Block(buf));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			while(daughter.available()>0) {
				int tmp=0;
				while(tmp<8) {
					ls[tmp]=(byte) daughter.read();
					tmp++;
				}
				byte[] buf=new byte[(int) SlitherS.bytesToLong(ls)];
				tmp=0;
				while(tmp<buf.length-1) {
					tmp+=daughter.read(buf,tmp,buf.length-tmp);
				}
				out.daughters.add(new Block(buf));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
}
