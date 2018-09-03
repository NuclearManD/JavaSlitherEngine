package nuclaer.slither3d;

public abstract class GameObject {
	public Vector3 position;
	public Vector3 rotation;
	public GameObject(){
		position=new Vector3();
		rotation=new Vector3();
	}
	final void _upd(){
		update();
	}
	protected abstract void update();
}
