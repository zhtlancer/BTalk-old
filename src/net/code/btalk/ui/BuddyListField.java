package net.code.btalk.ui;

import java.util.Vector;


import net.code.btalk.BTalk;
import net.code.btalk.Buddy;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ObjectListField;

public class BuddyListField extends ObjectListField {
	public static final Bitmap offlineIcon = Bitmap.getBitmapResource("img/offline.png");
	public static final Bitmap awayIcon = Bitmap.getBitmapResource("img/away.png");
	public static final Bitmap busyIcon = Bitmap.getBitmapResource("img/busy.png");
	public static final Bitmap onlineIcon = Bitmap.getBitmapResource("img/online.png");
	public static final Bitmap unreadIcon = Bitmap.getBitmapResource("img/unread.png");
	
	public static final Bitmap[] statusIcon = new Bitmap[]{offlineIcon, awayIcon, busyIcon, onlineIcon};
	
	public Vector buddyVector;
	private final BTalk btalk;
	
	public BuddyListField(BTalk b) {
		btalk = b;
		buddyVector = new Vector();
	}
	
	public Buddy getBuddyAt(int index) {
		return (Buddy)buddyVector.elementAt(index);		
	}
	
	public int getBuddyIndex(Buddy b) {
		return buddyVector.indexOf(b);
	}
	
//	protected int moveFocus(int amount, int status, int time) {
//		
//		return super.moveFocus(amount, status, time);
//		
//	}
//	
//	protected boolean navigationMoment(int dx, int dy, int status, int time) {
//		return super.navigationMovement(dx, dy, status, time);
//	}
	
	protected boolean keyChar(char key, int status, int time) {
		int idx;
		switch(key) {
		//#KEYMAP
		case 'd':	//qw
		case 'o':	//qw
/*
		case 'g':	//st
		case 'h':	//st
 */
		case Keypad.KEY_ENTER:
			idx = this.getSelectedIndex();
			if (idx >= 0) {
				btalk.currentBuddy = (Buddy)buddyVector.elementAt(idx);
				btalk.openBuddy(btalk.currentBuddy);
			}
			return true;
			
		//#KEYMAP
		case 'k':	//qw
		case 'e':	//qw
/*
		case 't':	//st
		case 'y':	//st
 */
			idx = this.getSelectedIndex()-1;
			if (idx >= 0) {
				this.setSelectedIndex(idx);
			}
			return true;
		
		//#KEYMAP

		case 'j':	//qw
		case 'x':	//qw
/*		
		case 'b':	//st
		case 'n':	//st
*/
			idx = this.getSelectedIndex()+1;
			if (idx > 0 && idx < this.getSize()) {
				this.setSelectedIndex(idx);
			}
			return true;
		
		//#KEYMAP
		case 't':	//qw
/*
		case 'e':	//st
		case 'r':	//st
*/
			if (this.getSize() > 0) {
				this.setSelectedIndex(0);
			}
			return true;
		
		//#KEYMAP
		case 'b':	//qw
/*
		case 'c':	//st
		case 'v':	//st
*/
			if (this.getSize() > 0) {
				this.setSelectedIndex(this.getSize()-1);
			}
			return true;
		
		// page down
		//#KEYMAP
		case 'n':	//qw
		case 'f':  //qw
/*
		case 'm':	//st
*/
		case Keypad.KEY_SPACE:
			this.btalk.buddyscreen.pageDown(1, KeypadListener.STATUS_ALT, time);
			return true;
			
		// page up
		//#KEYMAP
		case 'p':	//qw
		case 's':  //qw
/*
		case 'u':	//st
*/
			this.btalk.buddyscreen.pageUp(-1, KeypadListener.STATUS_ALT, time);
			return true;
			
		}
		
		return false;
	}
	
	public void buddyReposition(Buddy b) {
		int index = buddyVector.indexOf(b);
		buddyReposition(index);
	}
	
	public void buddyReposition(int oldIndex) {
		Buddy b = (Buddy)buddyVector.elementAt(oldIndex);
		int newIndex = 0;
		
		if (b.unread) {
			newIndex = 0;
		} else {
			while (newIndex < buddyVector.size() &&
					((b == (Buddy)buddyVector.elementAt(newIndex)) || 
					((Buddy)buddyVector.elementAt(newIndex)).unread || 
					(b.status < ((Buddy)buddyVector.elementAt(newIndex)).status)))
				++newIndex;
		}
		
		newIndex = (oldIndex < newIndex) ? (newIndex-1) : newIndex;
		
		if (oldIndex != newIndex) {
			buddyVector.removeElementAt(oldIndex);
			buddyVector.insertElementAt(b, newIndex);
		}
		
//		synchronized (BTalk.getEventLock()) {
//			this.invalidate();				
//		}
		this.invalidate();
	}
	
	public int findBuddyIndex(String jid) {
		for (int i = buddyVector.size()-1; i >= 0; i--) {
			if (((Buddy)buddyVector.elementAt(i)).jid.equalsIgnoreCase(jid))
				return i;
		}
		
		return -1;
	}
	
	public Buddy findBuddy(String jid) {
		for (int i = buddyVector.size()-1; i >= 0; i--) {
			if (((Buddy)buddyVector.elementAt(i)).jid.equalsIgnoreCase(jid))
				return (Buddy)buddyVector.elementAt(i);
		}
		return null;
	}
	
	public void addBuddy(Buddy b) {
		buddyVector.addElement(b);
		this.insert(buddyVector.indexOf(b));
	}
	
	public boolean deleteBuddy(String jid) {
		int idx;
		for (idx = buddyVector.size()-1; idx >= 0; idx--) {
			if (((Buddy)buddyVector.elementAt(idx)).jid.equalsIgnoreCase(jid))
				break;
		}
		
		if (idx >= 0) {
			buddyVector.removeElementAt(idx);
			this.delete(idx);
			this.getScreen().invalidate();
			return true;
		} else {
			return false;
		}
	}
	
	public void drawListRow(ListField listField, Graphics graphics, int index, int y, int width) {
		// TODO finish drawListRow
		// NOTICE 14 would be consist the icon size
		Buddy b = (Buddy)buddyVector.elementAt(index);
		if (b.unread) {
			//graphics.drawBitmap(1, y+((this.getRowHeight()-icon.getWidth())>>1), icon.getWidth(), icon.getWidth(), unreadIcon, 0, 0);
			graphics.drawBitmap(1, y+((this.getRowHeight()-14)>>1), 14, 14, unreadIcon, 0, 0);
		} else {
			//graphics.drawBitmap(1, y+((this.getRowHeight()-icon.getWidth())>>1), icon.getWidth(), icon.getWidth(), statusIcon[b.status], 0, 0);
			graphics.drawBitmap(1, y+((this.getRowHeight()-14)>>1), 14, 14, statusIcon[b.status], 0, 0);
		}
		//graphics.drawText(b.name, icon.getWidth()+2, y, DrawStyle.ELLIPSIS, width-icon.getWidth()-2);
		graphics.drawText(b.name, 16, y, DrawStyle.ELLIPSIS, width-16);
	}

	public void clearBuddies() {
		// TODO Auto-generated method stub
		if (btalk.buddyList != null) {
			int i = buddyVector.size();
			while (i-- > 0)
				this.delete(0);
			btalk.buddyscreen.delete(btalk.buddyList);
			btalk.buddyList = null;
		}
	}
	
	public void invalBuddies() {
		if (btalk.buddyList != null) {
			for (int i = buddyVector.size()-1; i >= 0; i--) {
				Buddy b = (Buddy) buddyVector.elementAt(i);
				b.status = Buddy.STATUS_OFFLINE;
			}
			
			this.invalidate();
		}
	}
}
