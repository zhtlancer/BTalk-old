package net.code.btalk.ui;


import net.code.btalk.BTalk;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.component.AutoTextEditField;
import net.rim.device.api.ui.component.Dialog;

public class MessageTextField extends AutoTextEditField {
	public static BTalk btalk;
	public MessageTextField(){ 
		super();
	}
	
	protected boolean keyChar(char key, int status, int time) {
		if (key == Keypad.KEY_ENTER) {
			if (this.getText().length() > 0) {
				if (btalk.state == BTalk.STATE_ONLINE) {
					btalk.sendMessage(this.getText());
					this.setText("");
				} else {
					Dialog.alert("You are currently not online!");
				}
			}
			return true;
		} else {
			return super.keyChar(key, status, time);
		}
	}
	
}
