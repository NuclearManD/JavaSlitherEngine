package nuclear.slitherge.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TextureLoader {
	private static ArrayList<BufferedImage> images=new ArrayList<BufferedImage>();
	private static ArrayList<String> indexes=new ArrayList<String>();
	private static ArrayList<BufferedImage>[] rots=null;
	private static ArrayList<String>[] roti=null;
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
	public static synchronized BufferedImage getImageRotated(String name,int degrees){
		if(rots==null){
			rots=new ArrayList[40];
			roti=new ArrayList[40];
			for(int i=0;i<40;i++){
				rots[i]=new ArrayList<BufferedImage>();
				roti[i]=new ArrayList<String>();
			}
		}
		int idx=(degrees%360)/9;
		if(idx<0)idx+=40;
		if(idx==0)
			return getImage(name);
		int i;
		for(i=0;i<roti[idx].size();i++){
			if(roti[idx].get(i).equals(name))
				break;
		}
		if(i==roti[idx].size()){
			roti[idx].add(name);
			BufferedImage q=getImage(name);
			int w = q.getWidth();  
			int h = q.getHeight();
			BufferedImage result = new BufferedImage(w, h, q.getType());
			Graphics2D g2 = result.createGraphics();
			g2.rotate(Math.toRadians(degrees), w/2, h/2);  
			g2.drawImage(q,null,0,0);
			rots[idx].add(result);
		}
		return rots[idx].get(i);
	}
	public static synchronized int loadImage(String name){
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
				return images.size()-1;
			}
			indexes.add(name);
		}
		return i;
	}
	public static synchronized BufferedImage getImage(int id){
		if(id>=images.size())
			return null;
		return images.get(id);
	}
}
