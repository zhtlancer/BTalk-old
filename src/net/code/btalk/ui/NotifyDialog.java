package net.code.btalk.ui;

import net.rim.device.api.ui.component.Dialog;

public class NotifyDialog implements Runnable {
	private final String info;
	public NotifyDialog(String msg) {
		info = msg;
	}
	public void run() {
		Dialog.alert(info);
	}

}
