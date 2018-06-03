package nuclear.slitherbio;

import java.util.ArrayList;

public class RNA {
	private String sequence;
	public RNA(String s){
		sequence=s;
	}
	public RNA(ArrayList<String> a){
		sequence="";
		for(String i:a){
			sequence+=i;
		}
			
	}
	public String get(){
		return new String(sequence);
	}

}
