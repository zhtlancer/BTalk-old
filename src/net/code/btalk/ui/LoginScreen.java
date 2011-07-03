package net.code.btalk.ui;



import net.code.btalk.BTalk;
import net.code.btalk.BTalkResource;
import net.code.btalk.ServerDef;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class LoginScreen extends MainScreen {
	
	public EditField usernameField;
	public PasswordEditField passwordField;
	public RichTextField logginState;
	private ObjectChoiceField serverChoiceField;
	private VerticalFieldManager vm1;
	private EditField serverField;
	private CheckboxField usesslCheck;
	private CheckboxField useWifiCheck;
	public CheckboxField saveField;
	private ButtonField loginButton;
	public static BTalk btalk;
	
	public LoginScreen(boolean saved, String username, String password, int serverType, String server, boolean usessl) {
		//this.add(new LabelField(BTalkLocale.locale[BTalkLocale.ITEM_USERNAME][SavedData.lang]));
		this.add(new LabelField(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_LA_USERNAME)));
		usernameField = new EditField(EditField.NO_COMPLEX_INPUT | EditField.NO_NEWLINE);
		this.add(usernameField);
		//this.add(new LabelField(BTalkLocale.locale[BTalkLocale.ITEM_PASSWORD][SavedData.lang]));
		this.add(new LabelField(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_LA_PASSWORD)));
		passwordField = new PasswordEditField();
		this.add(passwordField);
		
		//serverChoiceField = new ObjectChoiceField(BTalkLocale.locale[BTalkLocale.ITEM_SERVER_TYPE][SavedData.lang],
		//		new String[] {"Google Talk", BTalkLocale.locale[BTalkLocale.ITEM_CUSTOM_SERVER][SavedData.lang]}, serverType);
		serverChoiceField = new ObjectChoiceField(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_LA_SERVER_TYPE),
				new String[] {BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_OP_SERVER_GOOGLE), BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_OP_SERVER_CUSTOM)}, serverType);
		serverChoiceField.setChangeListener(null);
		serverChoiceField.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				if (field == serverChoiceField) {
					ObjectChoiceField obj = (ObjectChoiceField) field;
					if (obj.getSelectedIndex() == 0) {
//						serverField.setText(BTalk.GTALK_SERVER);
//						serverField.setEditable(false);
//						usesslCheck.setChecked(true);
//						usesslCheck.setEditable(false);
						vm1.delete(serverField);
						vm1.delete(usesslCheck);
					} else {
						vm1.add(serverField);
						vm1.add(usesslCheck);
						serverField.setText("");
						serverField.setEditable(true);
						usesslCheck.setChecked(true);
						usesslCheck.setEditable(true);
					}
				}
			}
			
		});
		this.add(serverChoiceField);
		
		vm1= new VerticalFieldManager();
		this.add(vm1);
		
		//serverField = new EditField(BTalkLocale.locale[BTalkLocale.ITEM_SERVER][SavedData.lang], server, 50,EditField.NO_COMPLEX_INPUT | EditField.NO_NEWLINE);
		serverField = new EditField(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_LA_SERVER_ADDR), server, 50,EditField.NO_COMPLEX_INPUT | EditField.NO_NEWLINE);
//		this.add(serverField);
		
		//usesslCheck = new CheckboxField(BTalkLocale.locale[BTalkLocale.ITEM_USE_SSL][SavedData.lang], usessl);
		usesslCheck = new CheckboxField(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_LA_USE_SSL), usessl);
//		this.add(usesslCheck);
		
		if (serverType == 0) {
			serverField.setEditable(false);
			usesslCheck.setEditable(false);
		} else {
			vm1.add(serverField);
			vm1.add(usesslCheck);
		}
		
		//saveField = new CheckboxField(BTalkLocale.locale[BTalkLocale.ITEM_SAVE_PASSWORD][SavedData.lang], saved);
		saveField = new CheckboxField(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_LA_SAVE_UP), saved);
		this.add(saveField);
		
		//useWifiCheck = new CheckboxField(BTalkLocale.locale[BTalkLocale.ITEM_USE_WIFI][SavedData.lang], false);
		useWifiCheck = new CheckboxField(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_LA_USE_WIFI), false);
		this.add(useWifiCheck);
		
		//logginState = new RichTextField(BTalkLocale.locale[BTalkLocale.ITEM_LOGIN_STATUS][SavedData.lang], Field.NON_FOCUSABLE);
		logginState = new RichTextField(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_LA_LOGIN_STATUS), Field.NON_FOCUSABLE);
		this.add(logginState);
		
		//this.add(loginButton = new ButtonField(BTalkLocale.locale[BTalkLocale.ITEM_LOGIN][SavedData.lang]) {
		this.add(loginButton = new ButtonField(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_BT_LOGIN)) {
			
			protected boolean keyChar(char key, int status, int time) {
				if (key == Keypad.KEY_ENTER) {
					return login();
				} else {
					return false;
				}
			}
		});
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_LOGIN][SavedData.lang], 0, 0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_MI_LOGIN), 0, 0) {
			public void run() {
				login();
			}
		});
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_OPTIONS][SavedData.lang], 0, 0){
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.WHOLE_MI_OPTION), 0, 0){
			public void run() {
				btalk.pushScreen(new OptionScreen());
			}
		});
		
		//this.addMenuItem(new MenuItem(BTalkLocale.locale[BTalkLocale.ITEM_ABOUT][SavedData.lang], 0, 0) {
		this.addMenuItem(new MenuItem(BTalkLocale.getString(BTalkResource.WHOLE_MI_ABOUT), 0, 0) {
			public void run() {
				//Dialog.inform("BTalk "+BTalk.VERSION+
				//		"\nBy zhtlancer\nzhtlancer@gmail.com\n" +
				//		BTalkLocale.locale[BTalkLocale.ITEM_ABOUT_TEXT][SavedData.lang]);
				Dialog.inform("BTalk "+BTalk.VERSION+
						"\nBy zhtlancer\nzhtlancer@gmail.com\n" +
						BTalkLocale.getString(BTalkResource.ABOUT_MAIN_TEXT));
			}
		});
		
		if (saved) {
			usernameField.setText(username);
			passwordField.setText(password);
			loginButton.setFocus();
		}
		
	}
	
	private boolean login() {
		if (usernameField.getText().length() == 0 ||
				passwordField.getText().length() == 0) {
			//Dialog.alert(BTalkLocale.locale[BTalkLocale.ITEM_INVALID_UP][SavedData.lang]);
			Dialog.alert(BTalkLocale.getString(BTalkResource.LOGIN_SCREEN_ALERT_UP_INVALID));
			return true;
		}
		
		ServerDef serverDef = new ServerDef();
		
		serverDef.serverType = serverChoiceField.getSelectedIndex();
		serverDef.useWifi = this.useWifiCheck.getChecked();
		
		if (serverDef.serverType == BTalk.SERVER_GTALK) {
			serverDef.server = BTalk.GTALK_SERVER;
			serverDef.port = BTalk.GTALK_PORT;
			serverDef.usessl = true;
			btalk.loginJxa(usernameField.getText(), passwordField.getText(), serverDef);
		} else {
			if (serverField.getText().length() <= 0) {
				Dialog.alert("Invalid server address");
				return true;
			}
			
			serverDef.server = serverField.getText();
			serverDef.usessl = usesslCheck.getChecked();
			if (serverDef.usessl)
				serverDef.port = "5223";
			else
				serverDef.port = "5222";
			
			btalk.loginJxa(usernameField.getText(), passwordField.getText(), serverDef);
		}
		return true;
	}
	
	public boolean onClose() {
		this.close();
		return true;
	}

}
