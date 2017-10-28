package nuclear.slitherge.top;

public class Player extends Entity {

	public Player(int dimt,double xc, double yc) {
		super(dimt,xc, yc);
	}
	@Override
	public void update() {
		Dimension dim=getDimension();
		io.print(">");
		String inp=io.getStr();
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
		String cmd=tokens[0];
		if(cmd.equals("look")){
			lookP();
		}else if(cmd.equals("n")){
			if(y>=Universe.max_pos)
				io.println("You cannot go that way; you have reached the edge of the world.");
			else
				y++;
			lookP();
		}else if(cmd.equals("s")){
			if(y<1)
				io.println("You cannot go that way; you have reached the edge of the world.");
			else
				y--;
			lookP();
		}else if(cmd.equals("e")){
			if(x>=Universe.max_pos)
				io.println("You cannot go that way; you have reached the edge of the world.");
			else
				x++;
			lookP();
		}else if(cmd.equals("w")){
			if(x<1)
				io.println("You cannot go that way; you have reached the edge of the world.");
			else
				x--;
			lookP();
		}else if(cmd.equals("take")){
			if(cnt<1){
				io.println("take what?");
			}else{
				if(objs==inv.length)
					io.print("Not enough room.");
				else{
					String x=tokens[1];
					for(int i=2;i<cnt+1;i++)
						x+=' '+tokens[i];
					inv[objs]=dim.removeObject(x);
					if(inv[objs]!=null)
						objs++;
					else
						io.println("There's no "+tokens[1]+" around here!");
				}
			}
		}else if(cmd.equals("inv")){
			tmp="You have ";
			for(int i=0;i<objs-1;i++){
				tmp+="a "+inv[i]+", ";
				if(i==objs-2)
					tmp+="and ";
			}
			tmp+="a "+inv[objs-1].name+'.';
			io.println(tmp);
		}else if(cmd.equals("use")){
			if(cnt>0){
				boolean done=false;
				for(int i=0;i<objs;i++){
					if(inv[i].name.replace(" ", "").equals(tokens[1])){
						done=true;
						inv[i].use(tokens, new Position(dimension, x, y),true);
						break;
					}
				}
				if(!done){
					io.println("You don't have that! (Or you inserted an extra space...)");
				}
			}else
				io.println("Usage: use [item WITHOUT spaces] [args]");
		}else{
			io.println("How do you '"+cmd+"'?");
		}
	}
	void lookP(){
		Dimension dim=Universe.dimensions.get(dimension);
		io.println("You are "+dim.name+" at coordinates "+x+", "+y);
		io.println(look());
	}
}
