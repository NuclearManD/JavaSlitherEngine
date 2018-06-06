package nuclear.slitherge.top;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

public class TextureLoader {
	private static ArrayList<Image> images=new ArrayList<Image>();
	private static ArrayList<String> indexes=new ArrayList<String>();
	public static synchronized Image getImage(String name){
		int i;
		for(i=0;i<indexes.size();i++){
			if(indexes.get(i).equals(name))
				break;
		}
		if(i==indexes.size()){
			images.add(Toolkit.getDefaultToolkit().getImage(name));
			indexes.add(name);
		}
		return images.get(i);
	}
}
