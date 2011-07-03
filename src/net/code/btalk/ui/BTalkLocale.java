package net.code.btalk.ui;

import net.code.btalk.BTalkResource;
import net.code.btalk.SavedData;
import net.rim.device.api.i18n.Locale;
import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.i18n.ResourceBundleFamily;

public class BTalkLocale {
	
	public final static int LANG_EN_US = 0;
	public final static int LANG_ZH_CN = 1;
	private final static int [] LOCALE_MAP = {Locale.LOCALE_en, Locale.LOCALE_zh_CN};
	
	static ResourceBundleFamily _resBundleFamily = 
		ResourceBundle.getBundle(BTalkResource.BUNDLE_ID, BTalkResource.BUNDLE_NAME);
	static ResourceBundle _resource = _resBundleFamily.getBundle(Locale.get(LOCALE_MAP[SavedData.lang]));
	
	public static void setLocale(int localeCode) {
		_resource = _resBundleFamily.getBundle(Locale.get(LOCALE_MAP[localeCode]));
	}
	
	public static String getString(int key) {
		return _resource.getString(key);
	}
	
	
/*	public final static int LANG_EN_US = 0;
	public final static int LANG_ZH_CN = 1;
	
	public final static int ITEM_LOGIN = 0;
	public final static int ITEM_USERNAME = 1;
	public final static int ITEM_PASSWORD = 2;
	public final static int ITEM_SAVE_PASSWORD = 3;
	public final static int ITEM_LOGIN_STATUS = 4;
	
	public final static int ITEM_OPTIONS = 5;
	public final static int ITEM_ABOUT = 6;
	public final static int ITEM_EXIT = 7;
	
	public final static int ITEM_INVALID_UP = 8;
	public final static int ITEM_LOGGING = 9;
	public final static int ITEM_AUTH_FAIL = 10;
	public final static int ITEM_RETRY_EXC = 11;
	public final static int ITEM_CON_FAIL = 12;
	
	public final static int ITEM_CHAT = 13;
	public final static int ITEM_RETRY = 14;
	public final static int ITEM_ADD_BUDDY = 15;
	public final static int ITEM_RM_BUDDY = 16;
	public final static int ITEM_RENAME_BUDDY = 17;
	
	public final static int ITEM_STATUS_AVA = 18;
	public final static int ITEM_STATUS_WAIT1 = 19;
	public final static int ITEM_STATUS_WAIT2 = 20;
	public final static int ITEM_STATUS_RETRYING = 21;
	public final static int ITEM_STATUS_FAILED = 22;
	
	public final static int ITEM_ADD_LAB = 23;
	public final static int ITEM_RM_LAB = 24;
	public final static int ITEM_RENAME_LAB = 25;
	
	public final static int ITEM_RING_CUR = 26;
	public final static int ITEM_VIB_CUR = 27;
	public final static int ITEM_RING_OTH = 28;
	public final static int ITEM_VIB_OTH = 29;
	public final static int ITEM_LED = 30;
	public final static int ITEM_POPUP = 31;
	public final static int ITEM_AUTO_RETRY = 32;
	public final static int ITEM_RETRY_DELAY = 33;
	public final static int ITEM_RETRY_LIMIT = 34;
	public final static int ITEM_FONT_SIZE = 35;
	public final static int ITEM_SEND_MENUKEY = 36;
	public final static int ITEM_LANG = 37;
	
	public final static int ITEM_SAVE = 38;
	public final static int ITEM_CANCEL = 39;
	
	public final static int ITEM_NOTI_LAB = 40;
	public final static int ITEM_AUTO_LAB = 41;
	public final static int ITEM_APPEAR_LAB = 42;
	public final static int ITEM_MISC_LAB = 43;
	
	public final static int ITEM_ABOUT_TEXT = 44;
	
	public final static int ITEM_REQUEST_BUDDY = 46;
	public final static int ITEM_ACCEPT = 47;
	public final static int ITEM_DENY = 48;
	public final static int ITEM_LATER = 49;
	
	public final static int ITEM_REMOVED = 50;
	
	public final static int ITEM_POPUP_NONE = 51;
	public final static int ITEM_POPUP_DIALOG = 52;
	public final static int ITEM_POPUP_WIN = 53;
	
	public final static int ITEM_BUDDY_INFO  = 54;
	
	public static final int ITEM_BUDDY_OFFLINE = 55;
	public static final int ITEM_BUDDY_AWAY 	= 56;
	public static final int ITEM_BUDDY_BUSY 	= 57;
	public static final int ITEM_BUDDY_ONLINE 	= 58;
	
	public static final int ITEM_SEND_MSG = 59;
	public static final int ITEM_CLEAR_SCR = 60;
	
	public static final int ITEM_POPUP_DIALOG_ALWAYS = 61;
	
	public static final int ITEM_SERVER_TYPE = 62;
	public static final int ITEM_CUSTOM_SERVER = 63;
	
	public static final int ITEM_SERVER = 64;
	public static final int ITEM_USE_SSL = 65;
	
	public static final int ITEM_USE_WIFI = 66;
	
	public static final int ITEM_CHECKUPDATE = 67;
	
	public static final int ITEM_UPDATED1 = 68;
	public static final int ITEM_UPDATED2 = 69;
	
	public static final int ITEM_NO_UPDATED = 70;
	
	public static final int ITEM_UPDATE_FAILED = 71;

	public final static String [][] locale = {
		{ "Login", "登录" },			//0
		{ "Username:", "用户名:" },
		{ "Password:", "密码:" },
		{ "Remember password", "记住密码" },
		{ "Status: ", "登录状态: " },
		{ "Options", "设置" },			//5
		{ "About", "关于" },
		{ "Exit", "退出" },
		{ "Invalid username/password!", "无效的用户名/密码!" },
		{ "Logging in...", "登录中..." },
		{ "Login failed. Please check your username and password", "用户名/密码错误" },		//10
		{ "Retry count exceeded", "重试次数超出上限" },
		{ "Connection failed, please retry", "网络连接错误，请检查后重试" },
		{ "Chat", "聊天" },
		{ "Retry", "重试" },
		{ "New buddy", "添加好友" },				//15
		{ "Delete buddy", "删除好友" },
		{ "", "" },
		{ "Available", "在线" },
		{ "Will retry in ", "" },
		{ "s...", "秒后重连..." },				//20
		{ "Reconnecting...", "重连中..." },
		{ "Offline", "已掉线" },
		{ "Buddy ID (example@gmail.com)", "对方GTalk账号" },
		{ "Delete buddy", "删除好友" },
		{ "", "" },							//25
		{ "Sound(Current)", "当前好友消息声音提示" },
		{ "Vibration(Current)", "当前好友消息振动提示" },
		{ "Sound(Other)", "其它好友消息声音提示" },
		{ "Vibration(Other)", "其它好友消息振动提示" },
		{ "Led blink on unread", "未读消息LED闪烁" },			//30
		{ "Popup notification", "弹出提醒方式" },
		{ "Auto reconnect", "启用自动重连" },
		{ "Retry delay(sec): ", "重试延时(秒): " },
		{ "Retry limit: ", "重试次数限制: " },
		{ "Font size(need re-login): ", "字体大小(需要重登陆): " },			//35
		{ "Send with menu key", "菜单键发送" },
		{ "Language(need re-login)", "语言(需要重登陆)" },
		{ "Save", "保存" },
		{ "Cancel", "取消" },
		{ "Notification", "消息提醒" },				//40
		{ "Connection", "连接" },
		{ "Appearance", "外观" },
		{ "Misc", "杂项" },
		{ "You can follow @zhtlancer to keep track of this project, or visit the \"btalk\" project site at google code.\nThis is a free software under GPL v2 License",
			"在twitter上Follow @zhtlancer或直接访问BTalk在google code上的项目主页可以查看更新动态。\n这是一个自由软件\n本软件受GPLv2许可证保护" },
		{ "", "" },									//45
		{ "request to add you as buddy.", "请求加你为好友." },
		{ "Accept", "接受" },
		{ "Deny", "拒绝" },
		{ "Later", "以后再说" },
		{ " has removed you frome his/her buddy list", "已将你从好友列表中删除" },									//50
		{ "None", "无弹出提醒" },
		{ "Popup dialog(Background)", "弹出对话框(后台)" },
		{ "Popup window(Background)", "弹出聊天窗口(后台)" },
		{ "Buddy info", "好友信息" },
		{ "Offline", "离线" },						//55
		{ "Away", "离开" },
		{ "Busy", "忙碌" },
		{ "Online", "在线" },
		{ "Send", "发送" },
		{ "Clear screen", "清屏" },						//60
		{ "Always popup dialog", "总是弹出对话框" },
		{ "Server type", "服务器类型" },
		{ "Custom server", "自定义服务器" },
		{ "Server: ", "服务器地址: " },
		{ "Use SSL", "使用SSL加密连接" },				//65
		{ "Use WiFi", "使用WiFi连接" },
		{ "Check update", "检查更新" },
		{ "New version ", "有新的版本" },
		{ " available\nVisit wap.feelberry.com from your mobile browser for more info.",
			" 可用.\n请访问wap.feelberry.com查看更新信息." },		//70
		{ "No update available.\nPlease check later.", "暂无可用更新." },
		{ "Failed checking update.\nplease retry later.", "检查更新出错\n请稍候重试" },
	};
	*/
}
