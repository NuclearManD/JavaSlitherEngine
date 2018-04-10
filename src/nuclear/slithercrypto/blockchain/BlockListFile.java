package nuclear.slithercrypto.blockchain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Iterator;

import nuclear.slitherio.SlitherS;

public class BlockListFile implements Iterable<Block>{
	protected long[] blockStarts;
	protected long[] blockLengths;
	protected String dir;
	// pathname MUST end in '/'
	public BlockListFile(String pathname) {
		dir=pathname;
		try {
			File a=new File(dir+".lk");
			File b=new File(dir+".dat");
			if(b.isFile()&&a.isFile()) {
				readDirectory();
			}else{
				blockStarts=new long[0];
				blockLengths=new long[0];
				a.getParentFile().mkdirs();
				a.createNewFile();
				b.createNewFile();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	synchronized private void readDirectory() throws Exception {
		FileInputStream chainData=new FileInputStream(dir+".lk");
		long len=chainData.getChannel().size();
		blockStarts=new long[(int) (len/16)];
		blockLengths=new long[(int) (len/16)];
		for(int i=0;i<len/16;i++) {
			byte[] b=new byte[8];
			for(int x=0;x<8;x++) {
				b[x]=(byte)chainData.read();
			}
			blockStarts[i]=SlitherS.bytesToLong(b);
			for(int x=0;x<8;x++) {
				b[x]=(byte)chainData.read();
			}
			blockLengths[i]=SlitherS.bytesToLong(b);
		}
		chainData.close();
	}
	synchronized public int addBlock(Block block){
		byte[] packed=block.pack();
		try {
			FileOutputStream chainData=new FileOutputStream(dir+".lk",true);
			FileOutputStream chainBlocks=new FileOutputStream(dir+".dat",true);
			chainData.write(SlitherS.longToBytes(chainBlocks.getChannel().size()));
			chainData.write(SlitherS.longToBytes(packed.length));
			long[] tbs=blockStarts;
			long[] tbl=blockLengths;
			blockStarts=Arrays.copyOf(tbs, tbs.length+1);
			blockLengths=Arrays.copyOf(tbl, tbl.length+1);
			blockStarts[tbs.length]=chainBlocks.getChannel().size();
			blockLengths[tbl.length]=packed.length;
			chainData.close();
			chainBlocks.write(packed);
			chainBlocks.close();
			return tbs.length;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	synchronized public Block get(int index) {
		try {
			long start=blockStarts[index];
			int length=(int)blockLengths[index];
			FileInputStream chainBlocks=new FileInputStream(dir+".dat");
			chainBlocks.skip(start);
			byte[] data=new byte[length];
			for(int i=0;i<length;i++) {
				int q=chainBlocks.read();
				if(q==-1)
					i--;
				else
					data[i]=(byte)q;
			}
			chainBlocks.close();
			return new Block(data);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public int length() {
		return blockStarts.length;
	}
	public Iterator<Block> iterator() {
		return new BlockListFileIterator(this);
	}
	public void update() {
		try {
			readDirectory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
