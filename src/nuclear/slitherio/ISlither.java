package nuclear.slitherio;

import java.util.Scanner;
@Deprecated
public class ISlither {
	public static final boolean LEAST_TO_GREATEST = true;
	public static final boolean GREATEST_TO_LEAST = false;
	private Scanner iport;
	public ISlither(){
		iport=new Scanner(System.in);
	}
	public String  in(){
		return iport.next();
	}
	public int getInt(){
		return iport.nextInt();
	}
	public void print(String a){
		System.out.print(a);
	}
	public void println(String a){
		System.out.println(a);
	}
	public void println(int a) {
		System.out.println(a);
	}
	public void println(long a) {
		System.out.println(a);
	}
	public double getDouble() {
		return iport.nextDouble();
	}
	public boolean Yn() {
		// TODO Auto-generated method stub
		String a=iport.next();
		if(a.charAt(0)=='Y')
			return true;
		return false;
	}
	public long getLong() {
		// TODO Auto-generated method stub
		return iport.nextLong();
	}
	public byte getByte() {
		// TODO Auto-generated method stub
		return iport.nextByte();
	}
	public long randInt(long limit){
		return (long)(Math.random()*limit);
	}
	public double randFloat(double limit){
		return Math.random()*limit;
	}
	public int[] sortInts(int[] intz, boolean dir) {
		// TODO Auto-generated method stub
		//int[] out= new int[l];
		boolean dirty=true;
		int ticking=10000;
		int tries = intz.length*4+1000;
		while(dirty&&tries>0){
			dirty=false;
			for(int i=0;i<intz.length-1;i++){
				if((intz[i]<intz[i+1])^dir){
					int tmp=intz[i+1];
					intz[i+1]=intz[i];
					intz[i]=tmp;
					dirty=true;
				}
			}
			//intz=out;
			ticking--;
			tries--;
			if(ticking==0){
				println("SLITHER WARN:  Sorter ticking!");
				ticking=10000;
			}
		}
		if(tries==0){
			println("SLITHER WARN:  Sorter termiated!");
		}
		return intz;
	}
	public int[] sortInts(int[] intz) {
		return sortInts(intz, LEAST_TO_GREATEST);
	}
	public String[] sortStrings(String[] intz, boolean dir) {
		// TODO Auto-generated method stub
		//int[] out= new int[l];
		boolean dirty=true;
		int ticking=10000;
		while(dirty&&ticking>0){
			dirty=false;
			for(int i=0;i<intz.length-1;i++){
				if((intz[i].compareTo(intz[i+1])<0)^dir){
					String tmp=intz[i+1];
					intz[i+1]=intz[i];
					intz[i]=tmp;
					dirty=true;
				}
			}
			//intz=out;
			ticking--;
		}
		if(ticking==0){
			println("SLITHER WARN:  Sorter started ticking!");
		}
		return intz;
	}
	public String[] sortStrings(String[] intz) {
		return sortStrings(intz, LEAST_TO_GREATEST);
	}
}