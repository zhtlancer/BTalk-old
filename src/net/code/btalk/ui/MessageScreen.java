package net.code.btalk.ui;


import net.code.btalk.BTalkResource;
import net.code.btalk.Buddy;
import net.code.btalk.SavedData;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class MessageScreen extends MainScreen {
	//private final BTalk btalk;
	private final Buddy buddy;
	private static int FONT_TITLE = 0;
	private static int FONT_BODY = 1;
	
	private VerticalFieldManager vm1;
	private VerticalFieldManager vm2;
	
	private static MessageTextField msgTextField = new MessageTextField();
	private static SeparatorField separator = new SeparatorField();
	
	private static final Font[] fonts = new Font[] {Font.getDefault().derive(Font.BOLD|Font.UNDERLINED, SavedData.fontSize),
		Font.getDefault().derive(Font.PLAIN, SavedData.fontSize)};
	
	private static final byte[] textAttr = new byte[] {0, 1};
	
	private static int[] offsets_me = new int[] {0, "me:".length(), 0};
	private int[] offsets_buddy = new int[3];
	
	public MessageScreen(Buddy b) {
		buddy = b;
		this.setTitle("Chat with " + b.name);
		offsets_buddy[0] = 0;
		offsets_buddy[1] = b.name.length()+1;
		vm1 = new VerticalFieldManager();
		vm2 = new VerticalFieldManager();
		this.add(vm1);
		this.add(vm2);
		

		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_SEND_MSG][SavedData.lang], 0, 0x0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.CHAT_SCREEN_MI_SEND), 0, 0x0) {
			public void run() {
				msgTextField.keyChar((char) Keypad.KEY_ENTER, 0, 0);			
			}
		});
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_CLEAR_SCR][SavedData.lang], 0, 0x1000000) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.CHAT_SCREEN_MI_CLS), 0, 0x1000000) {
			public void run() {
				vm1.deleteAll();
				vm1.add(new RichTextField("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));				
			}
		});
		//FIXME need a better way to locate editfield at the bottom
		vm1.add(new RichTextField("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
//		this.add(msgTextField);
	}
	
	// needn't check whether this is the current buddy
	public void sendMessage(String msg, boolean showTitle, String time) {
//		this.delete(msgTextField);
//		this.delete(separator);
//		offsets_me[2] = offsets_me[1] + msg.length();
		if (showTitle) {
			RichTextField t1 = new RichTextField("me ["+time+"]");
			t1.setFont(fonts[FONT_TITLE]);
			this.vm1.add(t1);
		}
		
		RichTextField t2 = new RichTextField(msg);
		t2.setFont(fonts[FONT_BODY]);
		this.vm1.add(t2);
//		this.add(msgTextField);
//		this.add(separator);
		msgTextField.setFocus();
	}
	
	// when pushed into stack
	protected void onDisplay() {
		this.vm2.add(separator);
		this.vm2.add(msgTextField);
		msgTextField.setFocus();
	}
	
	// when popped off the stack
	protected void onUndisplay() {
		this.vm2.delete(msgTextField);
		this.vm2.delete(separator);
		int idx = Buddy.btalk.buddyList.getBuddyIndex(buddy);
		idx = (idx > 0 ) ? idx : 0;
		Buddy.btalk.buddyList.setSelectedIndex(Buddy.btalk.buddyList.getBuddyIndex(buddy));
		Buddy.btalk.currentBuddy = null;
	}
	
	public void receiveMessage(String msg, boolean current, boolean showTitle, String time) {
//		if (current) {
//			this.delete(msgTextField);
//			this.delete(separator);
//		}
		
//		offsets_buddy[2] = offsets_buddy[1] + msg.length();
//		this.vm1.add(new RichTextField(buddy.name+":"+msg, offsets_buddy, textAttr, fonts, 0));
		if (showTitle) {
			RichTextField t1 = new RichTextField(buddy.name+" ["+time+"]");
			t1.setFont(fonts[FONT_TITLE]);
			this.vm1.add(t1);
		}
		
		RichTextField t2 = new RichTextField(msg);
		t2.setFont(fonts[FONT_BODY]);
		this.vm1.add(t2);
		
		if (current) {
//			this.add(separator);
//			this.add(msgTextField);
			msgTextField.setFocus();
		}
		
	}
	
	// handle the trackball/trackwheel click event
	public boolean onMenu(int instance) {
		if (SavedData.sendWithMenu) {
			msgTextField.keyChar((char) Keypad.KEY_ENTER, 0, 0);
			return true;
		} else {
			return super.onMenu(instance);
		}
	}
	
	protected boolean keyChar(char key, int status, int time) {
		if (key == Keypad.KEY_ENTER) {
			if (this.getLeafFieldWithFocus() != msgTextField) {
				msgTextField.setFocus();
				return true;
			}
		}
		return super.keyChar(key, status, time);
	}
	
	public boolean onClose() {
		if (this.getLeafFieldWithFocus() == msgTextField && msgTextField.getText().length() > 0) {
			msgTextField.setText("");
			return false;
		} else {
			msgTextField.setText("");
			this.close();
			return true;
		}
	}
	
//	public void initFields(MessageListField msgListField, TextField text) {
//		this.add(msgListField);
//		this.add(new SeparatorField());
//		this.add(text);
//		text.setFocus();
//	}
	
}
