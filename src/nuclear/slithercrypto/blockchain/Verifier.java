package nuclear.slithercrypto.blockchain;

import java.util.Arrays;

public class Verifier {
	protected int lastBlockChecked=-1;
	protected int firstBlockVerified=-1;
	protected int nearBlockVerified=-1;
	protected BlockchainBase bc;
	protected byte[] address;
	protected int blocksVerified=0;
	protected int nearBlocksVerified=0;
	protected boolean registered=false;
	public Verifier(byte[] adr, BlockchainBase b){
		address=adr;
		bc=b;
		update();
	}
	public boolean equals(byte[] adr){
		return Arrays.equals(adr, address);
	}
	public void update(){
		int len=bc.length();
		if(len-172800>firstBlockVerified){
			for(int i=firstBlockVerified+1;i<=len-172800;i++){
				if(equals(bc.getBlockByIndex(i).getMiner())){
					blocksVerified--;
				}
			}
			if(blocksVerified==0){
				registered=false;
			}
		}
		if(blocksVerified>0)
			registered=true;
		if(len-1024>nearBlockVerified){
			for(int i=firstBlockVerified+1;i<=len-1024;i++){
				if(equals(bc.getBlockByIndex(i).getMiner())){
					nearBlocksVerified--;
				}
			}
		}
		for(int i=lastBlockChecked+1;i<len;i++){
			Block q=bc.getBlockByIndex(i);
			if(equals(q.getMiner())){
				blocksVerified++;
				if(i>len-1024)
					nearBlocksVerified++;
				registered=true;
			}else if(!registered){
				for(int j=0;j<q.numTransactions();j++){
					Transaction t=q.getTransaction(j);
					if(t.type==Transaction.TRANSACTION_REGISTER&&t.verify()&&Arrays.equals(t.getSender(), address)){
						registered=true;
					}
				}
			}
		}
		lastBlockChecked=len-1;
		firstBlockVerified=len-172800;
		if(firstBlockVerified<-1)
			firstBlockVerified=-1;
		nearBlockVerified=len-1024;
		if(nearBlockVerified<-1)
			nearBlockVerified=-1;
	}
	public int getL(){
		return nearBlocksVerified;
	}
	public int getV(){
		return blocksVerified;
	}
	public boolean registered(){
		return registered;
	}
	public double getV(int index) {
		int o=0;
		for(int i=bc.length()-172800;i<bc.length();i++){
			Block q=bc.getBlockByIndex(i);
			if(equals(q.getMiner())){
				o++;
			}
		}
		return o;
	}
	public double getL(int index) {
		int o=0;
		for(int i=bc.length()-1024;i<bc.length();i++){
			Block q=bc.getBlockByIndex(i);
			if(equals(q.getMiner())){
				o++;
			}
		}
		return o;
	}
}
