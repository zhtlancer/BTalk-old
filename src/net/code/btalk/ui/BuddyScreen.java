package net.code.btalk.ui;

import net.code.btalk.BTalk;
import net.code.btalk.BTalkResource;
import net.code.btalk.Buddy;
import net.code.btalk.SavedData;
import net.code.btalk.UpdateChecker;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.DialogClosedListener;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.PopupScreen;

public class BuddyScreen extends MainScreen {
	public static BTalk btalk;
	public IconLabelField statusBanner;
	public BuddyListField buddyList;
	
	public BuddyScreen(BuddyListField l) {
		//statusBanner = new IconLabelField(BuddyListField.onlineIcon, BTalkLocale.locale[BTalkLocale.ITEM_STATUS_AVA][SavedData.lang]);
		statusBanner = new IconLabelField(BuddyListField.onlineIcon, BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_STATUS_AVAILABLE));
		this.setTitle(statusBanner);
		
		this.buddyList = l;
		this.add(l);
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_CHAT][SavedData.lang], 0, 0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_MI_CHAT), 0, 0) {
			public void run() {
				int idx = buddyList.getSelectedIndex();
				if (idx >= 0) {
					btalk.currentBuddy = (Buddy) buddyList.buddyVector.elementAt(idx);
					btalk.openBuddy(btalk.currentBuddy);
				}
			}
		});
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_RETRY][SavedData.lang], 1, 0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_MI_RETRY), 1, 0) {
			public void run() {
				if (btalk.state == BTalk.STATE_FAILED) {
					btalk.state = BTalk.STATE_RETRYING;
					btalk.setMyStatus(BTalk.STATE_RETRYING, false, null);
					btalk.retryCount = 0;
					btalk.retryBtalk();
				}
			}
		});
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_BUDDY_INFO][SavedData.lang], 0x00020010, 0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_MI_BUDDY_INFO), 0x00020010, 0) {
			// NOTICE: this array use status as index
			//final String[] STATUS_STR = new String[] {BTalkLocale.locale[BTalkLocale.ITEM_BUDDY_OFFLINE][SavedData.lang], 
			//			BTalkLocale.locale[BTalkLocale.ITEM_BUDDY_AWAY][SavedData.lang],
			//			BTalkLocale.locale[BTalkLocale.ITEM_BUDDY_BUSY][SavedData.lang],
			//			BTalkLocale.locale[BTalkLocale.ITEM_BUDDY_ONLINE][SavedData.lang]};
			final String[] STATUS_STR = new String[] {BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_POP_INFO_OFFLINE), 
					BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_POP_INFO_AWAY),
					BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_POP_INFO_BUSY),
					BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_POP_INFO_AVA)};
			public void run() {
				if (buddyList.buddyVector.size() <= 0)
					return;
				Buddy b = buddyList.getBuddyAt(buddyList.getSelectedIndex());
				
				if (b == null)
					return;
				if (b.name.equalsIgnoreCase(b.jid)) {
					Dialog buddyInfoDialog = new Dialog(Dialog.D_OK, "ID: "+ b.jid+"\n"+STATUS_STR[b.status]+"\n"+b.custom_str,
							0, null, Screen.LEFTMOST);
					buddyInfoDialog.setEscapeEnabled(true);
					buddyInfoDialog.show();
				} else {
					Dialog buddyInfoDialog = new Dialog(Dialog.D_OK, "Name: "+b.name+"\nID: "+b.jid+"\n"+STATUS_STR[b.status]+"\n"+b.custom_str,
							0, null, Screen.LEFTMOST);
					buddyInfoDialog.setEscapeEnabled(true);
					buddyInfoDialog.show();
				}
			}
		});
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_ADD_BUDDY][SavedData.lang], 0x00020011, 0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_MI_NEW_BUDDY), 0x00020011, 0) {
			public void run() {
				// TODO finish add new buddy
				final EditField jidField;
				//Dialog addBuddyDialog = new Dialog(Dialog.D_OK_CANCEL, BTalkLocale.locale[BTalkLocale.ITEM_ADD_LAB][SavedData.lang], 0, null, Manager.USE_ALL_WIDTH);
				Dialog addBuddyDialog = new Dialog(Dialog.D_OK_CANCEL, BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_POP_ADD_BUDDY_ID),
						0, null, Manager.USE_ALL_WIDTH);
				jidField = new EditField(EditField.NO_COMPLEX_INPUT | EditField.NO_NEWLINE);
				addBuddyDialog.add(jidField);
				addBuddyDialog.setDialogClosedListener(new DialogClosedListener() {
					public void dialogClosed(Dialog dialog, int choice) {
						switch (choice) {
						case 0:
							final String jid = jidField.getText();
							if (jid.indexOf('@') == -1) {
								Dialog.alert("Not a legal Email address!");
							} else {
								(new Thread() {
									public void run() {
										btalk.subscribe(jid);
									}
								}).start();
							}
							return;
						case -1:
							return;
						default:
							return;
						}
					}
				});
				addBuddyDialog.show();
			}
			
		});
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_RM_BUDDY][SavedData.lang], 0x00020012, 0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_MI_DELETE_BUDDY), 0x00020012, 0) {
			public void run() {
				//TODO finish delete buddy
				if (buddyList.buddyVector.size() <= 0)
					return;
				final Buddy b = buddyList.getBuddyAt(buddyList.getSelectedIndex());
				String str;
				if (!b.name.equals(b.jid))
					str = b.name+"("+b.jid+")";
				else
					str = b.jid;
				//int rst = Dialog.ask(BTalkLocale.locale[BTalkLocale.ITEM_RM_LAB][SavedData.lang]+" \""+str+"\"?", new String[] {"Yes", "No"}, new int[] {1, 2}, 2);
				int rst = Dialog.ask(BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_POP_DELETE_BUDDY)
						+" \""+str+"\"?", new String[] {"Yes", "No"}, new int[] {1, 2}, 2);
				
				switch (rst) {
				case 1:
					(new Thread() {
						public void run() {
							btalk.unsubscribe(b.jid);
						}
					}).start();
					buddyList.deleteBuddy(b.jid);
					return;
				case 2:
					return;
				}
			}
		});
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_OPTIONS][SavedData.lang], 0x00030005, 0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.WHOLE_MI_OPTION), 0x00030005, 0) {
			public void run() {
				btalk.pushScreen(new OptionScreen());
			}
		});
		
		// add exit menuitem
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_EXIT][SavedData.lang], 0x00030010, 0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_MI_EXIT), 0x00030010, 0) {
			public void run() {
				btalk.exitBtalk();
			}
		});
		if (BTalk.DEBUG) {
			this.addMenuItem(new MenuItem("Debug console", 0x00030006, 0) {
				public void run() {
					btalk.pushScreen(BTalk.debugConsole);
				}
			});
		}
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_CHECKUPDATE][SavedData.lang], 0x00030007, 0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.WHOLE_MI_CHECK_UPDATE), 0x00030007, 0) {
			public void run() {
				(new UpdateChecker(btalk.use_wifi)).start();
			}
		});
		
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_ABOUT][SavedData.lang], 0x00030009, 0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.WHOLE_MI_ABOUT), 0x00030009, 0) {
			public void run() {
				//Dialog.inform("BTalk "+BTalk.VERSION+
				//				"\nBy zhtlancer\nzhtlancer@gmail.com\n" +
				//				BTalkLocale.locale[BTalkLocale.ITEM_ABOUT_TEXT][SavedData.lang]);
				Dialog.inform("BTalk "+BTalk.VERSION+
						"\nBy zhtlancer\nzhtlancer@gmail.com\n" +
						BTalkLocale.getString(BTalkResource.ABOUT_MAIN_TEXT));
			}
		});
	}
	
	public boolean onClose() {
		if (btalk.state == BTalk.STATE_ONLINE ||
				btalk.state == BTalk.STATE_RETRYING ||
				btalk.state == BTalk.STATE_WAITING) {
			btalk.requestBackground();
		} else {
			btalk.exitBtalk();
		}
		return true;
	}
	
	public boolean pageDown(int amount, int status, int time) {
		return this.trackwheelRoll(amount, status, time);
	}
	
	public boolean pageUp(int amount, int status, int time) {
		return this.trackwheelRoll(amount, status, time);
	}
}
