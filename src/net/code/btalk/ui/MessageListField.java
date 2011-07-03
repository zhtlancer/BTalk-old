package net.code.btalk.ui;

import java.util.Vector;


import net.code.btalk.Buddy;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ObjectListField;

public class MessageListField extends ObjectListField {
	public static final Bitmap inIcon = Bitmap.getBitmapResource("img/arrow_down.png");
	public static final Bitmap outIcon = Bitmap.getBitmapResource("img/arrow_up.png");
	
	private Vector messageVector;
	
	public MessageListField() {
		messageVector = new Vector();
	}
	
	public void receiveMsg(String msg) {
		Message m = new Message(Message.TYPE_IN, msg);
		messageVector.addElement(m);
		this.insert(this.getSize());
	}
	
	public void sendMsg(String msg) {
		Message m = new Message(Message.TYPE_OUT, msg);
		messageVector.addElement(m);
		this.insert(this.getSize());
	}
	
	public void drawListRow(ListField listField, Graphics graphics, int index, int y, int width) {
		// TODO finish drawListRow
		// NOTICE 14 would be consist the icon size
		Message msg = (Message)messageVector.elementAt(index);
		if (msg.type == Message.TYPE_IN) {
			//graphics.drawBitmap(1, y+((this.getRowHeight()-icon.getWidth())>>1), icon.getWidth(), icon.getWidth(), unreadIcon, 0, 0);
			graphics.drawBitmap(1, y+((this.getRowHeight()-14)>>1), 14, 14, inIcon, 0, 0);
		} else {
			//graphics.drawBitmap(1, y+((this.getRowHeight()-icon.getWidth())>>1), icon.getWidth(), icon.getWidth(), statusIcon[b.status], 0, 0);
			graphics.drawBitmap(1, y+((this.getRowHeight()-14)>>1), 14, 14, outIcon, 0, 0);
		}
		//graphics.drawText(b.name, icon.getWidth()+2, y, DrawStyle.ELLIPSIS, width-icon.getWidth()+2);
		// FIXME: DEAL WITH LONGER TEXT
		graphics.drawText(msg.message, 16, y, DrawStyle.ELLIPSIS, width-12);
	}
	
	private class Message {
		public static final int TYPE_IN = 0;
		public static final int TYPE_OUT = 1;
		int type;
		String message;
		
		public Message(int t, String msg) {
			type = t;
			message = msg;
		}
	}
}
