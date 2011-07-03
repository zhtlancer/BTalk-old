package net.code.btalk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.CustomItem;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import net.code.btalk.ui.BTalkLocale;
import net.rim.device.api.i18n.Locale;

public class SavedData {
//	private static final long USER_INFO_ID = 0x730a7fda314c41dcL;
//	private static final long OPTIONS_ID = 0x6c69ee88659ab5acL;
	
	public static BTalk btalk;
	
	public static boolean save_account;
	
	public static boolean ringForCurrent = true;
	public static boolean vibForCurrent = true;
	
	public static boolean ringForOther = true;
	public static boolean vibForOther = true;
	public static boolean ledForOther = true;
	
	public final static int POPUP_NONE = 0;
	public final static int POPUP_DIALOG_ALWAYS = 1;
	public final static int POPUP_DIALOG = 2;
	public final static int POPUP_WINDOW = 3;
	public static int popupMethod = POPUP_DIALOG_ALWAYS;
	
	public static boolean autoRetry = true;
	public static int retryDelay = 10;
	public static int retryLimit = 10;
	
	public static int fontSize = 18;
	
	public static boolean sendWithMenu = false;
	public static int lang = getLocale();
	
	private static int getLocale() {
		if (Locale.getDefault().equals(Locale.get(Locale.LOCALE_zh_CN))) {
			return BTalkLocale.LANG_ZH_CN;
		} else {
			return BTalkLocale.LANG_EN_US;
		}
		//return Locale.getDefault().getCode();
	}
	
	public static void resetData() {
		ringForCurrent = true;
		vibForCurrent = true;
		
		ringForOther = true;
		vibForOther = true;
		ledForOther = true;
		
		popupMethod = POPUP_DIALOG_ALWAYS;
		
		autoRetry = true;
		retryDelay = 10;
		retryLimit = 10;
		
		fontSize = 18;
		
		sendWithMenu = false;
		lang = getLocale();
		BTalkLocale.setLocale(lang);
		
		// clean login info
		destroyUserInfo();
		
		//set default options
		saveOptions();
		
	}
	
	public static boolean needReset() {
			try {
				RecordStore store = RecordStore.openRecordStore("revision", true);
				int numRecord = store.getNumRecords();
				
				if (numRecord > 0) {
					byte[] data = store.getRecord(1);
					DataInputStream is = new DataInputStream(new ByteArrayInputStream(data));
					int preRev = is.readInt();
					
					if (preRev == BTalk.REVISION) {
						store.closeRecordStore();
						return false;
					} else {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						DataOutputStream os = new DataOutputStream(baos);
						
						os.writeInt(BTalk.REVISION);
						data = baos.toByteArray();
						store.setRecord(1, data, 0, data.length);
						store.closeRecordStore();
						return true;
					}
					
				} else {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					DataOutputStream os = new DataOutputStream(baos);
					
					os.writeInt(BTalk.REVISION);
					byte[] data = baos.toByteArray();
					store.addRecord(data, 0, data.length);
					store.closeRecordStore();
					
					return true;
				}
			} catch (RecordStoreFullException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RecordStoreNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RecordStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
	}
	
	public static void saveOptions() {
		try {
			RecordStore store = RecordStore.openRecordStore("options", true);
			int numRecord = store.getNumRecords();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(baos);
			os.writeBoolean(ringForCurrent);
			os.writeBoolean(vibForCurrent);
			os.writeBoolean(ringForOther);
			os.writeBoolean(vibForOther);
			os.writeBoolean(ledForOther);
			os.writeInt(popupMethod);
			os.writeBoolean(autoRetry);
			os.writeInt(retryDelay);
			os.writeInt(retryLimit);
			os.writeInt(fontSize);
			os.writeBoolean(sendWithMenu);
			os.writeInt(lang);
			
			byte[] data = baos.toByteArray();
			
			if (numRecord == 0) {
				store.addRecord(data, 0, data.length);
			} else {
				store.setRecord(1, data, 0, data.length);
			}
			
			store.closeRecordStore();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readOptions() {
		try {
			RecordStore store = RecordStore.openRecordStore("options", true);
			int numRecord = store.getNumRecords();
			
			if (numRecord > 0) {
				byte[] data = store.getRecord(1);
				
				DataInputStream is = new DataInputStream(new ByteArrayInputStream(data));
				ringForCurrent = is.readBoolean();//os.writeBoolean(ringForCurrent);
				vibForCurrent = is.readBoolean();//os.writeBoolean(vibForCurrent);
				ringForOther = is.readBoolean();//os.writeBoolean(ringForOther);//
				vibForOther = is.readBoolean();//os.writeBoolean(vibForOther);
				ledForOther = is.readBoolean();//os.writeBoolean(ledForOther);
				popupMethod = is.readInt();//os.writeBoolean(popupOnMsg);
				autoRetry = is.readBoolean();//os.writeBoolean(autoRetry);
				retryDelay = is.readInt();//os.writeInt(retryDelay);
				retryLimit = is.readInt();//os.writeInt(retryLimit);
				fontSize = is.readInt();//os.writeInt(fontSize);
				sendWithMenu = is.readBoolean();//os.writeBoolean(sendWithMenu);
				lang = is.readInt();//os.writeInt(lang);
			} 
			
			store.closeRecordStore();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BTalkLocale.setLocale(lang);
	}
	
	public static void setUserInfo(String username, String password, int serverType, String server, boolean usessl) {
//		PersistentObject store = PersistentStore.getPersistentObject(USER_INFO_ID);
//		Vector up = new Vector();
//		up.addElement(username);
//		up.addElement(password);
//		
//		synchronized (store) {
//			store.setContents(up);
//			store.commit();
//		}
		RecordStore store = null;
		int numRecord = -1;
		try {
			store = RecordStore.openRecordStore("userinfo", true);
			numRecord = store.getNumRecords();
			// convert user info into byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(baos);
			
			os.writeBoolean(true);
			os.writeUTF(username);
			os.writeUTF(password);
			
			os.writeInt(serverType);
			if (serverType == BTalk.SERVER_CUSTOM) {
				os.writeUTF(server);
				os.writeBoolean(usessl);
			}
			
			os.close();
			
			// check if the store is empty
			if (numRecord == 0) {
				byte[] data = baos.toByteArray();
				store.addRecord(data, 0, data.length);
			} else {
				byte[] data = baos.toByteArray();
				store.setRecord(1, data, 0, data.length);
			}
			store.closeRecordStore();
			
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	
	public static Vector getUserInfo() {
//		PersistentObject store = PersistentStore.getPersistentObject(USER_INFO_ID);
//		Vector up;
//		
//		synchronized (store) {
//			up = (Vector) store.getContents();
//		}
//		
//		return up;
		RecordStore store = null;
		int numRecord = -1;
		try {
			store = RecordStore.openRecordStore("userinfo", true);
			numRecord = store.getNumRecords();
			
			// empty recordstore
			if (numRecord == 0)
				return null;
			
			byte[] data = store.getRecord(1);
			DataInputStream is = new DataInputStream(new ByteArrayInputStream(data));
			boolean saved = is.readBoolean();
			
			if (saved) {
				String username = is.readUTF();
				String password = is.readUTF();
				
				ServerDef serverDef = new ServerDef();
				
				serverDef.serverType = is.readInt();
				
				Vector v = new Vector();
				v.addElement(username);
				v.addElement(password);
				
				if (serverDef.serverType == BTalk.SERVER_CUSTOM) {
					serverDef.server = is.readUTF();
					serverDef.usessl = is.readBoolean();
				} else {
					serverDef.server = BTalk.GTALK_SERVER;
					serverDef.port = BTalk.GTALK_PORT;
					serverDef.usessl = true;
				}
				v.addElement(serverDef);

				store.closeRecordStore();
				return v;
			} else {
				store.closeRecordStore();
				return null;
			}
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void destroyUserInfo() {
//		PersistentStore.destroyPersistentObject(USER_INFO_ID);
		RecordStore store = null;
		int numRecord = -1;
		try {
			store = RecordStore.openRecordStore("userinfo", true);
			numRecord = store.getNumRecords();
			
			if (numRecord != 0) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream os = new DataOutputStream(baos);
				
				os.writeBoolean(false);
				os.close();
				byte[] data = baos.toByteArray();
				store.setRecord(1, data, 0, data.length);
				store.closeRecordStore();
			}
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
