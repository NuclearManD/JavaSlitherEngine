package nuclear.slitherge.flat;

public class AreaEventListener extends Entity {
	protected Entity sendTo;
	public AreaEventListener(double x2, double y2, Entity owner) {
		super(x2, y2);
		sendTo=owner;
	}
	
	public void update() {
		;
	}
	protected void onDeath(){
		sendTo.onNotify("AEL DEAD",this);
	}
	public void onReceiveMessage(String msg,Entity src){
		sendTo.onReceiveMessage(msg, src);
	}
}
