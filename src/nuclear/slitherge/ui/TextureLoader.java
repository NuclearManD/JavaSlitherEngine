package nuclear.slitherge.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TextureLoader {
	private static ArrayList<BufferedImage> images=new ArrayList<BufferedImage>();
	private static ArrayList<String> indexes=new ArrayList<String>();
	public static synchronized BufferedImage getImage(String name){
		int i;
		for(i=0;i<indexes.size();i++){
			if(indexes.get(i).equals(name))
				break;
		}
		if(i==indexes.size()){
			try {
				images.add(ImageIO.read(new File(name)));
			} catch (IOException e) {
				indexes.add(name);
				images.add(null);
				return null;
			}
			indexes.add(name);
		}
		return images.get(i);
	}
}
