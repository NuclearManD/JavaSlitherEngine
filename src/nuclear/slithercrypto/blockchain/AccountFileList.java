package nuclear.slithercrypto.blockchain;

import java.util.ArrayList;
import java.util.Arrays;

public class AccountFileList {
	private byte[] address;
	private ArrayList<String> fileNames=new ArrayList<String>();
	private ArrayList<byte[]> fileHashes=new ArrayList<byte[]>();
	private ArrayList<String> pageNames=new ArrayList<String>();
	private ArrayList<byte[]> pageHashes=new ArrayList<byte[]>();
	private ArrayList<String> encNames=new ArrayList<String>();
	private ArrayList<byte[]> encHashes=new ArrayList<byte[]>();
	private BlockchainBase bc;
	private int nextToCheck=0;
	public AccountFileList(byte[] a, BlockchainBase b){
		address=a;
		bc=b;
	}
	public void update(){
		for(;nextToCheck<bc.length();nextToCheck++){
			Block b=bc.getBlockByIndex(nextToCheck);
			for(int i=0;i<b.numTransactions();i++){
				Transaction t=b.getTransaction(i);
				int u=t.type;
				if((u==Transaction.TRANSACTION_STORE_FILE||u==Transaction.TRANSACTION_STORE_PAGE||u==Transaction.TRANSACTION_STORE_ENCRYPTED)&&t.verify()){
					if(equals(t.pubKey)){
						byte[] hash=t.getDaughterHash();
						String name=new String(t.getMeta());
						if(u==Transaction.TRANSACTION_STORE_FILE){
							fileNames.add(0,name);
							fileHashes.add(0,hash);
						}else if(u==Transaction.TRANSACTION_STORE_PAGE){
							pageNames.add(0,name);
							pageHashes.add(0,hash);
						}else if(u==Transaction.TRANSACTION_STORE_ENCRYPTED){
							encNames.add(0,name);
							encHashes.add(0,hash);
						}
					}
				}
			}
		}
	}
	public boolean equals(byte[] q){
		return Arrays.equals(address, q);
	}
	public byte[] getFileHash(String name){
		update();
		for(int i=0;i<fileNames.size();i++){
			if(fileNames.get(i).equals(name))
				return fileHashes.get(i);
		}
		return null;
	}
	public byte[] getPageHash(String name){
		update();
		for(int i=0;i<pageNames.size();i++){
			if(pageNames.get(i).equals(name))
				return pageHashes.get(i);
		}
		return null;
	}
	public byte[] getEncryptedHash(String name){
		update();
		for(int i=0;i<encNames.size();i++){
			if(encNames.get(i).equals(name))
				return encHashes.get(i);
		}
		return null;
	}
	public ArrayList<String> getFileNames(){
		return fileNames;
	}
	public ArrayList<String> getPageNames(){
		return pageNames;
	}
	public ArrayList<String> getEncryptedNames(){
		return encNames;
	}
}
