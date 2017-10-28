package nuclear.slitherio;

import java.util.Scanner;

public class SlitherS {
	public static final boolean LEAST_TO_GREATEST = true;
	public static final boolean GREATEST_TO_LEAST = false;
	private static Scanner iport;
	public static void setupIport(){
		iport=new Scanner(System.in);
	}
	public static String  in(){
		return iport.next();
	}
	public static int getInt(){
		return iport.nextInt();
	}
	public static void print(String a){
		System.out.print(a);
	}
	public static void println(String a){
		System.out.println(a);
	}
	public static void println(int a) {
		System.out.println(a);
	}
	public static void println(long a) {
		System.out.println(a);
	}
	public static double getDouble() {
		return iport.nextDouble();
	}
	public static boolean Yn() {
		// TODO Auto-generated method stub
		String a=iport.next();
		if(a.charAt(0)=='Y')
			return true;
		return false;
	}
	public static long getLong() {
		// TODO Auto-generated method stub
		return iport.nextLong();
	}
	public static byte getByte() {
		// TODO Auto-generated method stub
		return iport.nextByte();
	}
	public static long randInt(long limit){
		return (long)(Math.random()*limit);
	}
	public static double randFloat(double limit){
		return Math.random()*limit;
	}
	public static int[] sortInts(int[] intz, boolean dir) {
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
	public static int[] sortInts(int[] intz) {
		return sortInts(intz, LEAST_TO_GREATEST);
	}
	public static String[] sortStrings(String[] intz, boolean dir) {
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
	public static String[] sortStrings(String[] intz) {
		return sortStrings(intz, LEAST_TO_GREATEST);
	}
	public static byte[] reverse(byte[] val) {
		byte[] out=new byte[val.length];
		for(int i=0;i<val.length;i++)
			out[i]=val[val.length-i-1];
		return out;
	}
	public static byte[] longToBytes(long in) {
		byte[] o = new byte[8];
		for (int i=7; i>=0; i--){
	        o[i] = (byte) (in >> i*8);
	    }
		return o;
	}
	public static long bytesToLong(byte[] in) {
		long o=0;
		for (int i=7; i>=0; i--){
	        o|=in[i]<<(i*8);
	    }
		return o;
	}
}
