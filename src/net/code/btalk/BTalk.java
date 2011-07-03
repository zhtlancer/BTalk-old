package net.code.btalk;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import net.code.btalk.ui.BTalkLocale;
import net.code.btalk.ui.BuddyListField;
import net.code.btalk.ui.BuddyScreen;
import net.code.btalk.ui.DebugScreen;
import net.code.btalk.ui.IconLabelField;
import net.code.btalk.ui.LoginScreen;
import net.code.btalk.ui.MessageTextField;
import net.code.btalk.ui.NotifyDialog;
import net.rim.device.api.system.WLANInfo;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.sourceforge.jxa.Jxa;
import net.sourceforge.jxa.XmppListener;

public class BTalk extends UiApplication implements XmppListener {
	
	public static boolean DEBUG = true;
	public static DebugScreen debugConsole;
	
	public static final String VERSION = "0.5.170";
	public static final int REVISION = 170;
	
//	public static final String DEVICE = "4.5";

	public static final int SERVER_GTALK = 0;
	public static final int SERVER_CUSTOM = 1;
	
	public static final String GTALK_SERVER = "talk.google.com";
	public static final String GTALK_PORT = "5223";
	
	private Jxa jxa;
	private LoginScreen loginscreen;
	private String _name;
	private String _jid;
	private String _password;
	public int serverType = SERVER_GTALK;
	public String _server = GTALK_SERVER;
	public String _port = GTALK_PORT;
	public boolean use_ssl = true;
	public boolean use_wifi = false;
	public int retryCount;
	
	public BuddyListField buddyList;
	public BuddyScreen buddyscreen;
	
//	private MessageScreen msgScreen;
//	private MessageTextField msgText;
	
	public Buddy currentBuddy;
	
	private int unreadCount;
	private byte[] unreadLock = new byte[0];
	
	// status values
	public Object stateLock;
	public int state;
	public final static int STATE_STARTUP = 0x0;
	public final static int STATE_LOGINING = 0x1;
	public final static int STATE_ONLINE = 0x2;
	public final static int STATE_FAILED = 0x3;
	public final static int STATE_WAITING = 0x4;
	public final static int STATE_RETRYING = 0x5;
	
	public final static int STATE_EXITING = 0x100;
	//boolean login;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BTalk app = new BTalk();
		
		if (BTalk.DEBUG) {
			BTalk.debugConsole = new DebugScreen();
		}
		
		// initialize static variables
//		BuddyScreen.btalk = app;
//		Buddy.btalk = app;
//		MessageTextField.btalk = app;
//		LoginScreen.btalk = app;
//		Notification.btalk = app;
		
		Notification.midStream = app.getClass().getResourceAsStream("/mid/new_msg.mid");
		Notification.midStream.mark(150);
		app.enterEventDispatcher();
	}
	
	public BTalk() {
		
		BuddyScreen.btalk = this;
		Buddy.btalk = this;
		MessageTextField.btalk = this;
		LoginScreen.btalk = this;
		Notification.btalk = this;
		SavedData.btalk = this;
		
		if (SavedData.needReset()) {
			SavedData.resetData();
		} else {
			SavedData.readOptions();
		}
		
		this.stateLock = new Object();
		this.state = BTalk.STATE_STARTUP;
		Vector up = SavedData.getUserInfo();
		if (up != null) {
			ServerDef serverDef = (ServerDef) up.elementAt(2);
			loginscreen = new LoginScreen(true, (String)up.elementAt(0), (String)up.elementAt(1),
					serverDef.serverType, serverDef.server, serverDef.usessl);
		} else {
			loginscreen = new LoginScreen(false, null, null, BTalk.SERVER_GTALK, null, true);
		}
		
		this.pushScreen(loginscreen);
		unreadCount = 0;
		this.retryCount = 0;
	}
	
	/*
	 * Only called by MessageTextField and currentBuddy mustn't be null
	 */
	public void sendMessage(final String msg) {
		(new Thread() {
			public void run() {
				jxa.sendMessage(currentBuddy.jid, msg);
			}
		}).start();
//		jxa.sendMessage(currentBuddy.jid, msg);
//		currentBuddy.getMsgList().sendMsg(msg);
		currentBuddy.sendMessage(msg);
	}
	
	public void openBuddy(Buddy b) {
		if (currentBuddy != null && currentBuddy.getMsgScreen().isDisplayed()) {
			this.popScreen(currentBuddy.getMsgScreen());
		}
		
		if (b.unread) {
			synchronized (unreadLock) {
				--unreadCount;
				if (unreadCount == 0) {
					Notification.clearNotification();
				}
			}
			b.unread = false;
		}
		this.currentBuddy = b;
		buddyList.buddyReposition(b);
		this.pushScreen(b.getMsgScreen());
	}
	
	public void switchBuddy(Buddy b) {
		if (currentBuddy == b) {
			return;
		} else if (currentBuddy != null && currentBuddy.getMsgScreen().isDisplayed()) {
			this.popScreen(currentBuddy.getMsgScreen());
			currentBuddy = null;
		}
		
		this.openBuddy(b);
	}
	
	// Only called by LoginScreen
	public void loginJxa(final String username, final String password, ServerDef serverDef) {
		if (this.state != BTalk.STATE_LOGINING) {
			this.state = BTalk.STATE_LOGINING;
			//loginscreen.logginState.setText(BTalkLocale.locale[BTalkLocale.ITEM_LOGIN_STATUS][SavedData.lang]+
			//								BTalkLocale.locale[BTalkLocale.ITEM_LOGGING][SavedData.lang]);
			loginscreen.logginState.setText(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_LA_LOGIN_STATUS)+
					BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_STATUS_LOGGING_IN));
			this._name = new String(username);
			
			this._password = new String(password);
			this.serverType = serverDef.serverType;
			this._server = new String(serverDef.server);
			this._port = new String (serverDef.port);
			this.use_ssl = serverDef.usessl;
			this.use_wifi = serverDef.useWifi;
			
			if (serverDef.serverType == BTalk.SERVER_GTALK) {	
				if (username.indexOf("@") == -1)
					this._jid = username + "@gmail.com";
				else
					this._jid = new String(username);
			} else {
				if (username.indexOf('@') == -1) {
					this._jid = username + "@" + this._server;
				} else {
					this._jid = username;
				}
			}
						
			
			jxa = new Jxa(this._jid, password, "BTalk", 10, this._server, this._port, this.use_ssl, this.use_wifi);
			jxa.addListener(this);
			jxa.start();
		}
	}
	
	public void setMyStatus(int s, boolean customText, String text) {
		switch (s) {
		case BTalk.STATE_ONLINE:
			//buddyscreen.statusBanner = new IconLabelField(BuddyListField.onlineIcon, BTalkLocale.locale[BTalkLocale.ITEM_STATUS_AVA][SavedData.lang]);
			buddyscreen.statusBanner = new IconLabelField(BuddyListField.onlineIcon, BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_STATUS_AVAILABLE));
			buddyscreen.setTitle(buddyscreen.statusBanner);
//			buddyscreen.statusBanner.setIcon(BuddyListField.onlineIcon);
//			buddyscreen.statusBanner.setText("Available");
//			buddyscreen.statusBanner.setDirty(true);
			break;
		
		case BTalk.STATE_WAITING:
			//buddyscreen.statusBanner = new IconLabelField(BuddyListField.offlineIcon, BTalkLocale.locale[BTalkLocale.ITEM_STATUS_WAIT1][SavedData.lang]+String.valueOf(SavedData.retryDelay)+BTalkLocale.locale[BTalkLocale.ITEM_STATUS_WAIT2][SavedData.lang]);
			buddyscreen.statusBanner = new IconLabelField(BuddyListField.offlineIcon, BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_STATUS_WAIT_RETRY_1) + 
					String.valueOf(SavedData.retryDelay) + 
					BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_STATUS_WAIT_RETRY_2));
			buddyscreen.setTitle(buddyscreen.statusBanner);
//			buddyscreen.statusBanner.setIcon(BuddyListField.offlineIcon);
//			buddyscreen.statusBanner.setText("Waiting to reconnect...");
//			buddyscreen.statusBanner.setDirty(true);
			break;
			
		case BTalk.STATE_RETRYING:
			//buddyscreen.statusBanner = new IconLabelField(BuddyListField.offlineIcon, BTalkLocale.locale[BTalkLocale.ITEM_STATUS_RETRYING][SavedData.lang]);
			buddyscreen.statusBanner = new IconLabelField(BuddyListField.offlineIcon, BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_STATUS_RETRYING));
			buddyscreen.setTitle(buddyscreen.statusBanner);
//			buddyscreen.statusBanner.setIcon(BuddyListField.offlineIcon);
//			buddyscreen.statusBanner.setText("Reconnecting...");
//			buddyscreen.statusBanner.setDirty(true);
			break;
			
		case BTalk.STATE_FAILED:
			//buddyscreen.statusBanner = new IconLabelField(BuddyListField.offlineIcon, BTalkLocale.locale[BTalkLocale.ITEM_STATUS_FAILED][SavedData.lang]);
			buddyscreen.statusBanner = new IconLabelField(BuddyListField.offlineIcon, BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_STATUS_OFFLINE));
			buddyscreen.setTitle(buddyscreen.statusBanner);
//			buddyscreen.statusBanner.setIcon(BuddyListField.offlineIcon);
//			buddyscreen.statusBanner.setText("Offline");
//			buddyscreen.statusBanner.setDirty(true);
			break;
			
		default:
			if (BTalk.DEBUG) {
				//System.out.println("Unhandled state value" + String.valueOf(s));
				BTalk.debugConsole.addDebugMsg("Unhandled state value in setMyStatus");
			}
			break;
		}
		
		if (customText)
			buddyscreen.statusBanner.setText(text);
		
	}
	
	public void subscribe(final String jid) {
		(new Thread() {
			public void run() {
				jxa.subscribe(jid);
			}
		}).start();
	}
	
	public void unsubscribe(final String jid) {
		(new Thread() {
			public void run() {
				jxa.unsubscribe(jid);
			}
		}).start();
	}
	
	public void logoffJxa() {
		(new Thread() {
			public void run() {
				jxa.logoff();
				synchronized (stateLock) {
					state = BTalk.STATE_EXITING;
				}
				Notification.clearNotification();
				System.exit(0);
			}
		}).start();
	}
	
	public void exitBtalk() {
		if (this.state == BTalk.STATE_ONLINE) {
				this.logoffJxa();				
		}
		//Notification.clearNotification();
		//System.exit(0);
	}
	
	private void authBtalkHandler() {
		if (this.state == BTalk.STATE_LOGINING) {
			if (loginscreen.saveField.getChecked()) {
				SavedData.setUserInfo(this._jid, this._password, this.serverType, this._server, this.use_ssl);
			} else {
				SavedData.destroyUserInfo();
			}
			
			buddyList = new BuddyListField(this);
			buddyscreen = new BuddyScreen(buddyList);
			
			this.popScreen(loginscreen);
			this.pushScreen(buddyscreen);
			loginscreen = null;
		} else if (this.state == BTalk.STATE_RETRYING) {
			if (DEBUG) 
				System.out.println("Retry successful");
			this.setMyStatus(BTalk.STATE_ONLINE, false, null);
		}
		this.state = BTalk.STATE_ONLINE;
	}

	/*
	 * Xmpp event handlers
	 * @see net.sourceforge.jxa.XmppListener#onAuth(java.lang.String)
	 */
	public void onAuth(String resource) {
		if (DEBUG)
			System.out.println("Auth: "+resource);
		// TODO Auto-generated method stub
		this.invokeAndWait(new Runnable() {
			public void run() {
				authBtalkHandler();
			}
		});
		
		try {
			jxa.getRoster();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private void authFailedBtalkHandler(final String msg) {
		this.state = BTalk.STATE_FAILED;
		//loginscreen.logginState.setText(BTalkLocale.locale[BTalkLocale.ITEM_AUTH_FAIL][SavedData.lang]);
		loginscreen.logginState.setText(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_ALERT_UP_ERROR));
	}

	public void onAuthFailed(final String message) {
		// TODO Auto-generated method stub
		this.jxa.removeListener(this);
		this.invokeAndWait(new Runnable() {
			public void run() {
				authFailedBtalkHandler(message);
			}
		});
		//this.invokeAndWait(new NotifyDialog(BTalkLocale.locale[BTalkLocale.ITEM_AUTH_FAIL][SavedData.lang]));
		this.invokeAndWait(new NotifyDialog(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_ALERT_UP_ERROR)));
	}
	
	public void retryBtalk() {
		jxa = new Jxa(_jid, _password, "BTalk", 10, this._server, this._port, this.use_ssl, this.use_wifi);
		jxa.addListener(this);
		jxa.start();
	}
	
	private void connFailedBtalkHandler(final String msg) {
		if (BTalk.DEBUG) {
			BTalk.debugConsole.addDebugMsg("[Conn Failed:"+String.valueOf(this.retryCount)+"]"+msg);
		}
		
		if (this.state == BTalk.STATE_ONLINE ||
				this.state == BTalk.STATE_RETRYING) {
			buddyList.invalBuddies();
			
			if (SavedData.autoRetry) {
				this.retryCount++;
				if (SavedData.retryLimit > 0 && this.retryCount > SavedData.retryLimit) {
					this.setMyStatus(BTalk.STATE_FAILED, false, null);
					this.state = BTalk.STATE_FAILED;
					//Dialog.alert(BTalkLocale.locale[BTalkLocale.ITEM_RETRY_EXC][SavedData.lang]);
					Dialog.alert(BTalkLocale.getString(BTalkResource.WHOLE_ALERT_RETRY_EXCEED));
					return;
				}
				
				this.setMyStatus(BTalk.STATE_WAITING, false, null);
				this.state = BTalk.STATE_WAITING;
				TimerTask retryTask = new TimerTask() {
					public void run() {
						state = BTalk.STATE_RETRYING;
						invokeAndWait(new Runnable() {
							public void run() {
								setMyStatus(BTalk.STATE_RETRYING, false, null);
							}
						});
						retryBtalk();
					}
				};
				
				Timer retrytimer = new Timer();
				retrytimer.schedule(retryTask, SavedData.retryDelay * 1000);
				return;
			} else {
				this.state = BTalk.STATE_FAILED;
			}
				
		} else if (this.state == BTalk.STATE_LOGINING) {
			this.state = BTalk.STATE_FAILED;
			//loginscreen.logginState.setText(BTalkLocale.locale[BTalkLocale.ITEM_LOGIN_STATUS][SavedData.lang] +
			//		BTalkLocale.locale[BTalkLocale.ITEM_CON_FAIL][SavedData.lang]);
			loginscreen.logginState.setText(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_LA_LOGIN_STATUS) +
					BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_ALERT_CONN_ERROR));
		}
		//Dialog.alert(BTalkLocale.locale[BTalkLocale.ITEM_CON_FAIL][SavedData.lang] + "\nInfo: "+msg);
		Dialog.alert(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_ALERT_CONN_ERROR) + "\nInfo: "+msg);
	}

	public void onConnFailed(final String msg) {
		if (DEBUG)
			System.out.println("con failed: "+msg);
		// TODO Auto-generated method stub
		this.jxa.removeListener(this);
		this.invokeAndWait(new Runnable() {
			public void run() {
				connFailedBtalkHandler(msg);
			}
		});
//		if (this.state == BTalk.STATE_LOGIN) {
//			this.state = BTalk.STATE_FAILED;
//			this.invokeAndWait(new Runnable() {
//				public void run() {
//					buddyList.clearBuddies();
//				}
//			});
//		}
//		this.invokeAndWait(new NotifyDialog("Connection lost. Please reconnect.\nInfo: "+msg));
	}
	
	private void contactBtalkHandler(final String jid, final String name, final String group, final String subscription) {
		if (subscription.equals("both") && buddyList.findBuddyIndex(jid) == -1) {
			buddyList.addBuddy(new Buddy(jid, name, Buddy.STATUS_OFFLINE));
		}
	}

	public void onContactEvent(final String jid, final String name, final String group,
			final String subscription) {
		if (DEBUG)
			System.out.println("[debug] contact: "+jid+" "+name+" "+group+ " "+subscription);
		// TODO Auto-generated method stub
		this.invokeLater(new Runnable() {
			public void run() {
				contactBtalkHandler(jid, name, group, subscription);
			}
		});
//		if (subscription.equals("both") && buddyList.findBuddyIndex(jid) == -1) {
//			buddyList.addBuddy(new Buddy(jid, name, Buddy.STATUS_OFFLINE));
//			this.invokeAndWait(new Runnable() {
//				public void run() {
//					buddyList.addBuddy(new Buddy(jid, name, Buddy.STATUS_OFFLINE));
//				}
//			});
//		}
	}

	public void onContactOverEvent() {
		System.out.println("over");
		// TODO Auto-generated method stub
		
	}
	
	private void messageBtalkHandler(final String from, final String body) {
		boolean isCurrentBuddy;
		if (currentBuddy != null && currentBuddy.jid.equalsIgnoreCase(from)) {
			isCurrentBuddy = true;
			currentBuddy.receiveMessage(body, true);
			
			Notification.newMessage(currentBuddy, body, isCurrentBuddy);
		// from other buddy
		} else {
			isCurrentBuddy = false;
			final int idx = buddyList.findBuddyIndex(from);
			if (idx != -1) {
				final Buddy b = buddyList.getBuddyAt(idx);
				if (!b.unread) {
					synchronized (unreadLock) {
						++unreadCount;
					}
					b.unread = true;
				}
				b.receiveMessage(body, false);
				buddyList.buddyReposition(idx);
				
				Notification.newMessage(b, body, isCurrentBuddy);
			} else {
				System.out.println("[warning] Message from unkown buddy");
			}
		}
//		Notification.newMessage(from, isCurrentBuddy);		
	}

	public void onMessageEvent(final String from, final String body) {
//		if (DEBUG)
//			System.out.println("[debug] message: "+from+" "+ body);
		if (body.length() == 0)
			return;
		// TODO add notify function
		this.invokeLater(new Runnable() {
			public void run() {
				messageBtalkHandler(from, body);
			}
		});
	}
	
	private void statusBtalkHandler(final String jid, final String show, final String status) {
		int idx = buddyList.findBuddyIndex(jid);
		Buddy b;
		
		if (idx != -1) {
			b = (Buddy)buddyList.buddyVector.elementAt(idx);
			int state = 0;
			if (show.equals(""))
				state = Buddy.STATUS_ONLINE;
			else if (show.equals("chat"))
				state = Buddy.STATUS_ONLINE;
			else if (show.equals("away"))
				state = Buddy.STATUS_AWAY;
			else if (show.equals("xa"))
				state = Buddy.STATUS_AWAY;
			else if (show.equals("dnd"))
				state = Buddy.STATUS_BUSY;
			else if (show.equals("na"))
				state = Buddy.STATUS_OFFLINE;
			else if (BTalk.DEBUG)
				BTalk.debugConsole.addDebugMsg("[BTALK] Unhandled status: "+jid+" " + show+ " "+status);
				//System.out.println("[BTALK] Unhandled status: "+jid+" " + show+ " "+status);
			
			b.custom_str = status;
			if (b.status == state)
				return;
			else {
				b.status = state;
				buddyList.buddyReposition(idx);
			}
		} else {
			if (BTalk.DEBUG)
				BTalk.debugConsole.addDebugMsg("[BTALK] No buddy matches: "+jid+" " + show+ " "+status);
			//System.out.println("[BTALK] No buddy matches: "+jid+" " + show+ " "+status);
		}
	}

	public void onStatusEvent(final String jid, final String show, final String status) {
		if (DEBUG)
			System.out.println("[debug] status:"+jid+" "+show+ " "+ status);
		// TODO Auto-generated method stub
		int idx = jid.indexOf('/');
		final String id;
		// in some instance, jid contains no '/', fix this
		if (idx == -1) {
			id = new String(jid);
		} else {
			id = jid.substring(0, jid.indexOf('/'));
		}
		
		this.invokeLater(new Runnable() {
			public void run() {
				statusBtalkHandler(id, show, status);
			}
		});
	}
	
	private void subscribeBtalkHandler(final String jid) {
		//int rst = Dialog.ask("\""+jid+"\""+BTalkLocale.locale[BTalkLocale.ITEM_REQUEST_BUDDY][SavedData.lang],
		//		new String[] {BTalkLocale.locale[BTalkLocale.ITEM_ACCEPT][SavedData.lang], 
		//						BTalkLocale.locale[BTalkLocale.ITEM_DENY][SavedData.lang], 
		//						BTalkLocale.locale[BTalkLocale.ITEM_LATER][SavedData.lang]},
		//		new int[] {1, 2, 3},
		//		1);
		int rst = Dialog.ask("\""+jid+"\""+BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_POP_REQUEST),
				new String[] {BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_POP_REQUEST_AC), 
								BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_POP_REQUEST_DENY), 
								BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_POP_REQUEST_LATER)},
				new int[] {1, 2, 3}, 1);
		
		switch (rst) {
		case 1:
			(new Thread() {
				public void run() {
					jxa.subscribed(jid);
					jxa.subscribe(jid);
				}
			}).start();
			return;
		case 2:
			(new Thread() {
				public void run() {
					jxa.unsubscribed(jid);
				}
			}).start();
			return;
		case 3:
			return;
		}
	}

	public void onSubscribeEvent(final String jid) {
		// TODO Auto-generated method stub
		if (DEBUG) {
//			System.outut.println("[DEBUG] subscribe from: "+jid);
			BTalk.debugConsole.addDebugMsg("[onSubscribeEvent]:" + jid);
		}
		this.invokeLater(new Runnable() {
			public void run() {
				subscribeBtalkHandler(jid);
			}
		});	
	}

	public void onUnsubscribeEvent(final String jid) {
		// TODO Auto-generated method stub
		if (DEBUG) {
//			System.out.println("[DEBUG] unsubscribe from: " + jid);
			BTalk.debugConsole.addDebugMsg("[onUnsubscribeEvent]:" + jid);
		}
		this.invokeLater(new Runnable() {
			public void run() {
				//Dialog.inform(jid+BTalkLocale.locale[BTalkLocale.ITEM_REMOVED][SavedData.lang]);
				Dialog.inform(jid+BTalkLocale.getString(BTalkResource.BUDDY_SCREEN_POP_REMOVED));
				buddyList.deleteBuddy(jid);
			}
		});
		jxa.unsubscribe(jid);		
	}
}
