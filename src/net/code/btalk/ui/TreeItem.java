package net.code.btalk.ui;

public class TreeItem {
	static final int TYPE_LEAF = 1;
	static final int TYPE_GROUP = 2;
	static final int TYPE_SYS = 4;
	static final int TYPE_SYS_GROUP = 6; // TYPE_SYS | TYPE_GROUP
	
	public int tid;		//node id in the TreeField
	public int type;
	public Object obj;
}
