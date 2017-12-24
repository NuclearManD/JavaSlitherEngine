package nuclear.slitherio;

public abstract class SlitherLog {
	public abstract void print(String s);
	public void println() {
		print("\n");
	}
	public void println(String s) {
		print(s+"\n");
	}
}
