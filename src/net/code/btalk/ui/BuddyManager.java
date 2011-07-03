package net.code.btalk.ui;

import net.code.btalk.BTalk;

public class BuddyManager {
	public static BTalk btalk;
	BuddyTreeFiled _treeField;
	
	public BuddyManager(BTalk b) {
		btalk = b;
		this._treeField = new BuddyTreeFiled(new BuddyTreeFieldCallback(), 0);
	}
	
	
}
