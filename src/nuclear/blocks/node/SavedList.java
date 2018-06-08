package nuclear.blocks.node;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import nuclear.slitherge.top.io;
import nuclear.slitherio.SlitherS;

public class SavedList {
	private String fn;
	public SavedList(String filename){
		fn=filename;
	}
	public String get(int idx) {
		try{
			FileInputStream file=new FileInputStream(fn);
			for(long i=0;i<idx-1;i++){
				while(file.read()!=13);
			}
			String out="";
			while(true){
				char c=(char)file.read();
				if(c==13)break;
				out+=c;
			}
			file.close();
			return out;
		}catch(Exception e){
			return null;
		}
	}
	
	public int length(){
		try{
			FileInputStream file=new FileInputStream(fn);
			int o=0;
			while(file.available()>0){
				char c=(char)file.read();
				if(c==13)o++;
			}
			file.close();
			return o;
		}catch(Exception e){
			return 0;
		}
	}
	public void add(String i){
		try {
			FileOutputStream file=new FileOutputStream(fn);
			file.write(i.getBytes());
			file.write(13);
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void remove(int i) throws Exception{
		throw new Exception("Not a Feature!");
	}
	public byte[] serialize() {
		try{
			FileInputStream file=new FileInputStream(fn);
			byte[] o=new byte[file.available()];
			file.read(o, 0, o.length);
			file.close();
			return o;
		}catch(Exception e){
			return new byte[0];
		}
	}

}
