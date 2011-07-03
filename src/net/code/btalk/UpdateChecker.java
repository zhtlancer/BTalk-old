package net.code.btalk;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.code.btalk.ui.BTalkLocale;
import net.rim.device.api.ui.component.Dialog;

public class UpdateChecker extends Thread {
	
	final static String LATEST_URL = "http://btalk.googlecode.com/files/LATEST";
	
	String httpMask;
	
	public UpdateChecker(boolean use_wifi) {
		if (use_wifi) {
			this.httpMask = ";deviceside=true;interface=wifi";
		} else {
			this.httpMask = ";deviceside=true";
		}
	}
	
	public void run() {
		try {
			HttpConnection conn = (HttpConnection) Connector.open(LATEST_URL+this.httpMask);
			conn.setRequestMethod(HttpConnection.GET);
			InputStream in = conn.openInputStream();
			int len = (int)conn.getLength();
			byte[] result = new byte[len];
			in.read(result, 0, len);
			final String retver = new String(result);
			int newver = Integer.parseInt(retver);
			
			if (newver > BTalk.REVISION) {
				BTalk.getApplication().invokeLater(new Runnable() {
					public void run() {
						//Dialog dia = new Dialog(Dialog.D_OK, 
						//		BTalkLocale.locale[BTalkLocale.ITEM_UPDATED1][SavedData.lang]+retver+BTalkLocale.locale[BTalkLocale.ITEM_UPDATED2][SavedData.lang],
						//		0, null, 0);
						Dialog dia = new Dialog(Dialog.D_OK, 
								BTalkLocale.getString(BTalkResource.WHOLE_POP_NEW_VERSION_1) + retver +
								BTalkLocale.getString(BTalkResource.WHOLE_POP_NEW_VERSION_2),
								0, null, 0);
						dia.show();
					}
				});
			} else {
				BTalk.getApplication().invokeLater(new Runnable() {
					public void run() {
						//Dialog.inform(BTalkLocale.locale[BTalkLocale.ITEM_NO_UPDATED][SavedData.lang]);
						Dialog.inform(BTalkLocale.getString(BTalkResource.WHOLE_POP_NO_UPDATE));
					}
				});
			}
			
		} catch (Exception e) {
			BTalk.getApplication().invokeLater(new Runnable() {
				public void run() {
					//Dialog.alert(BTalkLocale.locale[BTalkLocale.ITEM_UPDATE_FAILED][SavedData.lang]);
					Dialog.alert(BTalkLocale.getString(BTalkResource.WHOLE_POP_CHECK_UPDATE_FAIL));
				}
			});
		}
		
		
	}

}
