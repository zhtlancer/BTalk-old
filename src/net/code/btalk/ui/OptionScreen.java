package net.code.btalk.ui;

import net.code.btalk.BTalkResource;
import net.code.btalk.SavedData;
import net.rim.device.api.i18n.Locale;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

public class OptionScreen extends MainScreen {
	CheckboxField ringCurCheckbox;
	CheckboxField vibCurCheckbox;
	CheckboxField ringOtherCheckbox;
	CheckboxField vibOtherCheckbox;
	CheckboxField ledOtherCheckbox;
	
	ObjectChoiceField popupChoiceField;
	
	CheckboxField autoRetryCheckbox;
	EditField delayEditField;
	EditField retryLimEditField;
	
	EditField fontSizeEditField;
	
	CheckboxField sendMenuCheckbox;
	
	ObjectChoiceField langChoiceField;
	static final int LANG_en_US = 0;
	static final int LANG_zh_CN = 1;
	
	public OptionScreen() {
		this.setTitle("Options");
		
		
		//ringCurCheckbox = new CheckboxField(BTalkLocale.locale[BTalkLocale.ITEM_RING_CUR][SavedData.lang], SavedData.ringForCurrent);
		ringCurCheckbox = new CheckboxField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_RING_CUR), SavedData.ringForCurrent);
		//vibCurCheckbox = new CheckboxField(BTalkLocale.locale[BTalkLocale.ITEM_VIB_CUR][SavedData.lang], SavedData.vibForCurrent);
		vibCurCheckbox = new CheckboxField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_VIB_CUR), SavedData.vibForCurrent);
		
		//ringOtherCheckbox = new CheckboxField(BTalkLocale.locale[BTalkLocale.ITEM_RING_OTH][SavedData.lang], SavedData.ringForOther);
		ringOtherCheckbox = new CheckboxField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_RING_OTH), SavedData.ringForOther);
		//vibOtherCheckbox = new CheckboxField(BTalkLocale.locale[BTalkLocale.ITEM_VIB_OTH][SavedData.lang], SavedData.vibForOther);
		vibOtherCheckbox = new CheckboxField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_VIB_OTH), SavedData.vibForOther);
		//ledOtherCheckbox = new CheckboxField(BTalkLocale.locale[BTalkLocale.ITEM_LED][SavedData.lang], SavedData.ledForOther);
		ledOtherCheckbox = new CheckboxField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_LED), SavedData.ledForOther);
		//popupChoiceField = new ObjectChoiceField(BTalkLocale.locale[BTalkLocale.ITEM_POPUP][SavedData.lang],
		//		new String[] {BTalkLocale.locale[BTalkLocale.ITEM_POPUP_NONE][SavedData.lang],
		//				BTalkLocale.locale[BTalkLocale.ITEM_POPUP_DIALOG_ALWAYS][SavedData.lang], 
		//				BTalkLocale.locale[BTalkLocale.ITEM_POPUP_DIALOG][SavedData.lang], 
		//				BTalkLocale.locale[BTalkLocale.ITEM_POPUP_WIN][SavedData.lang]},
		//		SavedData.popupMethod);
		popupChoiceField = new ObjectChoiceField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_POPUP),
				new String[] {BTalkLocale.getString(BTalkResource.OPTION_SCREEN_OP_POPUP_NONE),
						BTalkLocale.getString(BTalkResource.OPTION_SCREEN_OP_POPUP_DIALOG_ALW), 
						BTalkLocale.getString(BTalkResource.OPTION_SCREEN_OP_POPUP_DIALOG_BACK), 
						BTalkLocale.getString(BTalkResource.OPTION_SCREEN_OP_POPUP_WIN)},
				SavedData.popupMethod);
		
		//autoRetryCheckbox = new CheckboxField(BTalkLocale.locale[BTalkLocale.ITEM_AUTO_RETRY][SavedData.lang], SavedData.autoRetry);
		autoRetryCheckbox = new CheckboxField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_AUTO_RETRY), SavedData.autoRetry);
		//delayEditField = new EditField(BTalkLocale.locale[BTalkLocale.ITEM_RETRY_DELAY][SavedData.lang], String.valueOf(SavedData.retryDelay), 10, EditField.FILTER_NUMERIC);
		delayEditField = new EditField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_RETRY_DELAY), String.valueOf(SavedData.retryDelay), 10, EditField.FILTER_NUMERIC);
		//retryLimEditField = new EditField(BTalkLocale.locale[BTalkLocale.ITEM_RETRY_LIMIT][SavedData.lang], String.valueOf(SavedData.retryLimit), 10, EditField.FILTER_NUMERIC);
		retryLimEditField = new EditField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_RETRY_LIMIT), String.valueOf(SavedData.retryLimit), 10, EditField.FILTER_NUMERIC);
		
		//fontSizeEditField = new EditField(BTalkLocale.locale[BTalkLocale.ITEM_FONT_SIZE][SavedData.lang], String.valueOf(SavedData.fontSize), 10, EditField.FILTER_NUMERIC);
		fontSizeEditField = new EditField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_FONT_SIZE), String.valueOf(SavedData.fontSize), 10, EditField.FILTER_NUMERIC);
		
		//sendMenuCheckbox = new CheckboxField(BTalkLocale.locale[BTalkLocale.ITEM_SEND_MENUKEY][SavedData.lang], SavedData.sendWithMenu);
		sendMenuCheckbox = new CheckboxField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_SEND_WITH_MENUKEY), SavedData.sendWithMenu);
		//langChoiceField = new ObjectChoiceField(BTalkLocale.locale[BTalkLocale.ITEM_LANG][SavedData.lang],
		//		new String[] { "English", "中文" }, SavedData.lang);
		langChoiceField = new ObjectChoiceField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_LANG),
				new String[] { "English", "中文" }, SavedData.lang);
		
		//this.add(new LabelField(BTalkLocale.locale[BTalkLocale.ITEM_NOTI_LAB][SavedData.lang]));
		this.add(new LabelField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_C_NOTIFICATION)));
		this.add(ringCurCheckbox);
		this.add(vibCurCheckbox);
		
		this.add(ringOtherCheckbox);
		this.add(vibOtherCheckbox);
		this.add(ledOtherCheckbox);
		this.add(popupChoiceField);
		
		this.add(new SeparatorField());
		//this.add(new LabelField(BTalkLocale.locale[BTalkLocale.ITEM_AUTO_LAB][SavedData.lang]));
		this.add(new LabelField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_C_CONNECTION)));
		this.add(autoRetryCheckbox);
		this.add(delayEditField);
		this.add(retryLimEditField);
		
		this.add(new SeparatorField());
		//this.add(new LabelField(BTalkLocale.locale[BTalkLocale.ITEM_APPEAR_LAB][SavedData.lang]));
		this.add(new LabelField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_C_APPEARANCE)));
		this.add(fontSizeEditField);
		
		this.add(new SeparatorField());
		//this.add(new LabelField(BTalkLocale.locale[BTalkLocale.ITEM_MISC_LAB][SavedData.lang]));
		this.add(new LabelField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_LA_C_MISC)));
		this.add(sendMenuCheckbox);
		this.add(langChoiceField);
		
		this.add(new ButtonField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_BT_SAVE)) {
			protected boolean keyChar(char key, int status, int time) {
				if (key == Keypad.KEY_ENTER) {
					setOptions();					
					SavedData.saveOptions();
					this.getScreen().getUiEngine().popScreen(this.getScreen());
					return true;
				} else {
					return false;
				}
			}
			
			protected boolean navigationClick(int status, int time) {
				if ((status & KeypadListener.STATUS_FOUR_WAY) != 0) {
					setOptions();					
					SavedData.saveOptions();
					this.getScreen().getUiEngine().popScreen(this.getScreen());
					return true;
				} else {
					return false;
				}
			}
		});
		
		this.add(new ButtonField(BTalkLocale.getString(BTalkResource.OPTION_SCREEN_BT_CANCEL)) {
			protected boolean keyChar(char key, int status, int time) {
				if (key == Keypad.KEY_ENTER) {
					this.getScreen().getUiEngine().popScreen(this.getScreen());
					return true;
				} else {
					return false;
				}
			}
			
			protected boolean navigationClick(int status, int time) {
				if ((status & KeypadListener.STATUS_FOUR_WAY) != 0) {
					this.getScreen().getUiEngine().popScreen(this.getScreen());
					return true;
				} else {
					return false;
				}
			}
		});
	}
	
	protected boolean onSave() {
		setOptions();
		
		SavedData.saveOptions();
		return true;
	}
	
	private void setOptions() {
		SavedData.ringForCurrent = ringCurCheckbox.getChecked();
		SavedData.vibForCurrent = vibCurCheckbox.getChecked();
		
		SavedData.ringForOther = ringOtherCheckbox.getChecked();
		SavedData.vibForOther = vibOtherCheckbox.getChecked();
		SavedData.ledForOther = ledOtherCheckbox.getChecked();
		SavedData.popupMethod = popupChoiceField.getSelectedIndex();
		
		SavedData.autoRetry = autoRetryCheckbox.getChecked();
		SavedData.retryDelay = Integer.parseInt(delayEditField.getText());
		SavedData.retryLimit = Integer.parseInt(retryLimEditField.getText());
		
		SavedData.fontSize = Integer.parseInt(fontSizeEditField.getText());
		
		SavedData.sendWithMenu = sendMenuCheckbox.getChecked();
//		switch (langChoiceField.getSelectedIndex()) {
//		case LANG_en_US:
//			SavedData.lang = Locale.LOCALE_en;
//			break;
//		case LANG_zh_CN:
//			SavedData.lang = Locale.LOCALE_zh_CN;
//			break;
//		}
		SavedData.lang = langChoiceField.getSelectedIndex();
	}
}
