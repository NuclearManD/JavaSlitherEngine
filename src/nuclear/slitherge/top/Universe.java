package nuclear.slitherge.top;
import java.util.ArrayList;


public class Universe {
	public static final double max_pos = 1000000000000.0;
	public static final double tick_len = 600000;
	public static ArrayList<Dimension> dimensions= new ArrayList<Dimension>();
	public static Entity player;
	private static int time=0;
	public static int conversion=1000; // units/km
	public static void update() {
		for(byte i=0;i<dimensions.size();i++){
			if(dimensions.get(i)!=null)
				dimensions.get(i).update();
		}
		time++;
	}
	public static void addPlayer(Entity e, int dimension) {
		player=e;
		dimensions.get(e.dimension).addEntity(e);
	}
	public static String getTime(){
		return (time/6)%24+":"+10*(time%6)+", Stardate "+(time/43200+2984)+
				"/"+((time/4320)%10+1)+"/"+((time/144)%30+1);
	}
	public static int addDim(Dimension dimension) {
		dimensions.add(dimension);
		dimension.id=dimensions.indexOf(dimension);
		io.println(dimension.id);
		return dimension.id;
	}
	public static void teleportEntity(Entity c, int dimt) {
		if(dimensions.get(dimt)==null)
			return;
		dimensions.get(dimt).addEntity(dimensions.get(c.dimension).removeEntity(c));
		c.dimension=dimt;
	}
	public static Dimension myDim(Entity entity) {
		return dimensions.get(entity.dimension);
	}
	public static void removeDimension(Dimension d) {
		dimensions.remove(d);
		for (Dimension d2 : dimensions) {
			d2.getId();
		}
	}
	public static int getTimeStamp(){
		return time;
	}
	public static void resetTime() {
		time=0;
	}
}
