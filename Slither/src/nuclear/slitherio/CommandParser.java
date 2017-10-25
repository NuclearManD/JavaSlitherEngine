package nuclear.slitherio;

public class CommandParser {
	public static String[] parse(String inp){
		String[] tokens=new String[256];
		String tmp="";
		int cnt=0;
		for(int i=0;i<inp.length();i++){
			char c=inp.charAt(i);
			if(c==' '){
				tokens[cnt]=tmp;
				tmp="";
				cnt++;
			}else{
				tmp+=inp.charAt(i);
			}
		}
		tokens[cnt]=tmp;
		cnt++;
		String out[]=new String[cnt];
		for(int i=0;i<cnt;i++)
			out[i]=tokens[i];
		return out;
	}
}
