package nuclear.slitherge.top;

public class AreaEventListener extends Entity {
	protected Entity sendTo;
	public AreaEventListener(int dimt, double x2, double y2, Entity owner) {
		super(dimt, x2, y2);
		sendTo=owner;
		range=Double.MAX_VALUE;
	}
	@Deprecated
	public AreaEventListener(int dimt, double x2, double y2) {
		super(dimt, x2, y2);
	}

	@Override
	public void update() {
		;
	}
	protected void onDeath(){
		sendTo.onNotify("AEL DEAD",this);
	}
	public void onReceiveMessage(String msg,Entity src){
		sendTo.onReceiveMessage(msg, src);
	}
	public String getDescription(){
		return "";
	}
}
