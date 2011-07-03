package net.code.btalk.ui;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.TreeField;
import net.rim.device.api.ui.component.TreeFieldCallback;

public class BuddyTreeFieldCallback implements TreeFieldCallback {

	public void drawTreeItem(TreeField treeField, Graphics graphics, int node,
			int y, int width, int indent) {
		// TODO Auto-generated method stub
		TreeItem itm = (TreeItem) treeField.getCookie(node);
		if (itm != null) {
			switch (itm.type) {
			case TreeItem.TYPE_SYS_GROUP:
				break;
				
			case TreeItem.TYPE_GROUP:
				break;
				
			case TreeItem.TYPE_LEAF:
				break;
			}
		}
	}

}
