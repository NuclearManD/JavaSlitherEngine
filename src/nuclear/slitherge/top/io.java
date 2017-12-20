package nuclear.slitherge.top;
import java.util.Scanner;

public class io {
	private static Scanner sc=new Scanner(System.in);
	public static  String getStr(){
		return sc.nextLine();
	}
	public static int getInt(){
		return sc.nextInt();
	}
	public static void print(String a){
		System.out.print(a);
	}
	public static void println(String a){
		System.out.println(a);
	}
	public static double getDouble() {
		return sc.nextDouble();
	}
	public static boolean Yn() {
		String a=sc.next();
		if(a.charAt(0)=='Y')
			return true;
		return false;
	}
	public static long getLong() {
		return sc.nextLong();
	}
	public static byte getByte() {
		return sc.nextByte();
	}
	public static void println() {
		System.out.println();
	}
	public static void println(int a) {
		System.out.println(a);
		
	}
	public static void print(char i) {
		System.out.print(i);
	}
}
