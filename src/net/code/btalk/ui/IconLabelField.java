package net.code.btalk.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

public class IconLabelField extends LabelField {
	private Bitmap _icon;
	private String _text;
	
	public IconLabelField(Bitmap icon, String text) {
		super("                                  ");
		_icon = icon;
		_text = text;
	}
	
	public void paint(Graphics g) {
		g.drawBitmap(this.getLeft()+1, this.getTop()+((this.getHeight()-14)>>1), 14, 14, _icon, 0, 0);
		g.drawText(_text, 16, this.getTop(), DrawStyle.ELLIPSIS, this.getWidth()-16);
	}
	
	public void setIcon(Bitmap icon) {
		_icon = icon;
	}
	
	public void setText(String text) {
		_text = text;
	}
}
