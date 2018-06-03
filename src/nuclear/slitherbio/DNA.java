package nuclear.slitherbio;

import java.util.ArrayList;

public class DNA {
	private ArrayList<String> sequence = new ArrayList<String>();
	public DNA(char[] s){
		for(char i:s)
			sequence.add(""+i);
	}
	public DNA(ArrayList<String> a){
		sequence=a;
	}
	public RNA readRandom(){
		int start=0,end=0, i=0;
		for(i=0;i<sequence.size()-3;i++){
			if(sequence.get(i).equals("A")&&sequence.get(i+1).equals("U")&&sequence.get(i+2).equals("G")){
				start=i;
			}
		}
		for(;i<sequence.size()-3;i++){
			if(sequence.get(i).equals("T")&&sequence.get(i+1).equals("A")&&sequence.get(i+2).equals("A")){
				end=i;
			}
			if(sequence.get(i).equals("T")&&sequence.get(i+1).equals("A")&&sequence.get(i+2).equals("G")){
				end=i;
			}
			if(sequence.get(i).equals("T")&&sequence.get(i+1).equals("G")&&sequence.get(i+2).equals("A")){
				end=i;
			}
		}
		i+=3;
		String o="";
		for(i=start;i<end;i++){
			o+=sequence.get(i);
		}
		return new RNA(o);
	}
}
