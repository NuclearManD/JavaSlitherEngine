package nuclear.slitherge.flat;
import java.util.ArrayList;

import nuclear.slitherge.top.Position;


public class Universe {
	public static final double max_pos = 1000000000000.0;
	public static final double tick_len = 600000;
	public static ArrayList<Dimension> dimensions= new ArrayList<Dimension>();
	public static Entity player;
	protected static int time=0;
	public static int conversion=1000; // units/km
	private static int dimCurrent;
	public static void update() {
		for(byte i=0;i<dimensions.size();i++){
			if(dimensions.get(i)!=null){
				dimCurrent = i;
				dimensions.get(i).update();
			}
		}
		time++;
	}
	public static void addPlayer(Entity e, int dimension) {
		player=e;
		dimensions.get(dimension).addEntity(e);
	}
	public static String getTime(){
		return (time/6)%24+":"+10*(time%6)+", Stardate "+(time/43200+2984)+
				"/"+((time/4320)%10+1)+"/"+((time/144)%30+1);
	}
	public static int addDim(Dimension dimension) {
		dimensions.add(dimension);
		dimension.id=dimensions.indexOf(dimension);
		return dimension.id;
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
	public static Dimension currentDimension() {
		return dimensions.get(dimCurrent);
	}

	public static Position getThisPosition() {
		return new Position(dimCurrent, currentDimension().current_x, currentDimension().current_y);
	}
	public static int currentDimensionId() {
		return dimCurrent;
	}
}
