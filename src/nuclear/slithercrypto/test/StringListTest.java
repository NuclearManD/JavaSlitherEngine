package nuclear.slithercrypto.test;

import nuclear.blocks.node.SavedList;
import nuclear.slitherge.top.io;

public class StringListTest {

	public static void main(String[] args) {
		SavedList list=new SavedList("test.lua");
		list.add("Hello World!");
		io.println(list.length());
		io.println(list.get(0));
	}

}
