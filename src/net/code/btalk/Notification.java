package net.code.btalk;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;

import net.rim.device.api.system.Alert;
import net.rim.device.api.system.LED;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.DialogClosedListener;

public class Notification {
	
	public static InputStream midStream;
	public static BTalk btalk;
	public static Dialog popupDialog = null;
	public static Object popupDialogLock = new Object();
	public static Buddy fromBuddy = null;
	
	public static void newMessage(final Buddy from, final String msg, boolean isCurrent) {
		if (btalk.isForeground()) {
			if (SavedData.popupMethod == SavedData.POPUP_DIALOG_ALWAYS && btalk.currentBuddy != from) {
				if (popupDialog != null) {
					popupDialog.cancel();
					popupDialog = null;
				}

				popupDialog = new Dialog("From: "+from.name+"\n"+msg,
						new String[] {"Reply", "Cancel"}, new int[] {1, 2}, 1, null);
				popupDialog.setDialogClosedListener(new DialogClosedListener() {
					public void dialogClosed(Dialog dialog, int choice) {
						switch (choice) {
						case 1:
							btalk.switchBuddy(fromBuddy);
							break;
						case 2:
							break;
						default:
							break;
						}
					}
				});
				popupDialog.setEscapeEnabled(true);
				popupDialog.show();
				fromBuddy = from;
			}
		} else {
			switch (SavedData.popupMethod) {
			case SavedData.POPUP_WINDOW:
				btalk.requestForeground();
				break;
				
			case SavedData.POPUP_DIALOG_ALWAYS:
			case SavedData.POPUP_DIALOG:
				
				//FIXME: need a better way to solve
				if (popupDialog != null) {
					popupDialog.cancel();
					popupDialog = null;
				}
			
				UiEngine ui = Ui.getUiEngine();
				popupDialog = new Dialog("From: "+from.name+"\n"+msg,
						new String[] {"Reply", "Cancel"}, new int[] {1, 2}, 1, null);
				popupDialog.setDialogClosedListener(new DialogClosedListener() {
					public void dialogClosed(Dialog dialog, int choice) {
						switch (choice) {
						case 1:
							btalk.switchBuddy(fromBuddy);
							btalk.requestForeground();
							break;
						case 2:
							break;
						default:
							break;
						}
					}
				});
				popupDialog.setEscapeEnabled(true);
				ui.pushGlobalScreen(popupDialog, 1, UiEngine.GLOBAL_SHOW_LOWER);
				fromBuddy = from;
				break;

			case SavedData.POPUP_NONE:
			default:
				break;
			}	
		}
		
		if (isCurrent) {
			if (SavedData.vibForCurrent) {
				Alert.startVibrate(300);
			}
			
			if (SavedData.ringForCurrent) {
//				InputStream in = from.getClass().getResourceAsStream("/mid/new_msg.mid");
				try {
					midStream.reset();
					Manager.createPlayer(midStream, "audio/mid").start();
				} catch (MediaException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
		// Other buddy
		} else {
			if (SavedData.vibForOther) {
				Alert.startVibrate(500);
			}
			
			if (SavedData.ringForOther) {
//				InputStream in = from.getClass().getResourceAsStream("/mid/new_msg.mid");
				try {
					midStream.reset();
					Manager.createPlayer(midStream, "audio/mid").start();
				} catch (MediaException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
			if (SavedData.ledForOther) {
				LED.setConfiguration(LED.LED_TYPE_STATUS, 500, 500, LED.BRIGHTNESS_50);
				LED.setState(LED.LED_TYPE_STATUS, LED.STATE_BLINKING);
				
			}
			
		}
	}
	
	public static void clearNotification() {
		
		if (SavedData.ledForOther) {
			LED.setState(LED.STATE_OFF);
		}
		
	}

}
