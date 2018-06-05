package nuclear.slithercrypto.blockchain;

import java.util.Arrays;

public class BalanceCache {
	private byte[] adr;
	private BlockchainBase bc;
	private int lastBlock=-1;
	private double bal=0;
	private boolean wasVerifier=false;
	public BalanceCache(byte[] a, BlockchainBase b){
		adr=a;
		bc=b;
	}
	public double getBalance(){
		for(int i=lastBlock+1;i<bc.length();i++){
			Block b=bc.getBlockByIndex(i);
			for(int x=0;x<b.numTransactions();x++){
				Transaction t=b.getTransaction(x);
				if(!t.verify())
					continue;
				if(t.type==Transaction.TRANSACTION_SEND_COIN){
					if(Arrays.equals(t.pubKey,adr)){
						double cn=t.getCoinsSent();
						bal-=cn;
					}if(Arrays.equals(t.getReceiver(), adr)){
						bal+=t.getCoinsSent();
					}
				}
				if(Arrays.equals(t.pubKey,adr))
					bal-=t.getTransactionCost();
			}
			if(Arrays.equals(adr,b.getMiner())){
				bal+=b.getCost();
				if(i==0)
					bal+=1000;
			}
		}
		int p=bc.getPriority(adr);
		if((p!=100)!=wasVerifier){
			if(p==100)
				bal+=1000;
			else
				bal-=1000;
			wasVerifier=(p!=100);
		}
		lastBlock=bc.length()-1;
		return bal;
	}
	

	public boolean equals(byte[] address){
		return Arrays.equals(adr, address);
	}
}
