package com.myanmarunicorn.ooredootracker;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class balancetracker extends Activity implements B4AActivity{
	public static balancetracker mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.myanmarunicorn.ooredootracker", "com.myanmarunicorn.ooredootracker.balancetracker");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (balancetracker).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
		BA.handler.postDelayed(new WaitForLayout(), 5);

	}
	private static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.myanmarunicorn.ooredootracker", "com.myanmarunicorn.ooredootracker.balancetracker");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.myanmarunicorn.ooredootracker.balancetracker", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (balancetracker) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (balancetracker) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return balancetracker.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (balancetracker) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (balancetracker) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtphonenumber = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btntrack = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllog = null;
public anywheresoftware.b4a.samples.httputils2.httputils2service _v7 = null;
public com.myanmarunicorn.ooredootracker.main _v0 = null;
public com.myanmarunicorn.ooredootracker.imeitracker _v6 = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 17;BA.debugLine="Activity.LoadLayout(\"BalanceTracker\")";
mostCurrent._activity.LoadLayout("BalanceTracker",mostCurrent.activityBA);
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 20;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _btntrack_click() throws Exception{
anywheresoftware.b4a.samples.httputils2.httpjob _job = null;
 //BA.debugLineNum = 28;BA.debugLine="Sub btnTrack_Click";
 //BA.debugLineNum = 29;BA.debugLine="If edtPhoneNumber.Text.Length = 0 Then";
if (mostCurrent._edtphonenumber.getText().length()==0) { 
 //BA.debugLineNum = 30;BA.debugLine="ToastMessageShow(\"Phone Number Should Not Be Emp";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Phone Number Should Not Be Empty.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 31;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 33;BA.debugLine="ProgressDialogShow2(\"Tracking...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Tracking...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 34;BA.debugLine="lblLog.Text = \"\"";
mostCurrent._lbllog.setText((Object)(""));
 //BA.debugLineNum = 35;BA.debugLine="Dim job As HttpJob";
_job = new anywheresoftware.b4a.samples.httputils2.httpjob();
 //BA.debugLineNum = 36;BA.debugLine="job.Initialize(\"Job\", Me)";
_job._initialize(processBA,"Job",balancetracker.getObject());
 //BA.debugLineNum = 37;BA.debugLine="job.Download2(\"http://ecareapp.ooredoo.com.mm/pag";
_job._download2("http://ecareapp.ooredoo.com.mm/pageapi.aspx",new String[]{"action","apiquerybalance","ws","ecare","userid","95"+mostCurrent._edtphonenumber.getText().substring((int) (1))});
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 11;BA.debugLine="Private edtPhoneNumber As EditText";
mostCurrent._edtphonenumber = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Private btnTrack As Button";
mostCurrent._btntrack = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Private lblLog As Label";
mostCurrent._lbllog = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(anywheresoftware.b4a.samples.httputils2.httpjob _job) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.Map _xml = null;
String _status_code = "";
String _status_desc = "";
String _mobileno = "";
anywheresoftware.b4a.objects.collections.Map _balance = null;
String _amount = "";
String _expiretime = "";
String _packtype = "";
String _desc = "";
anywheresoftware.b4a.objects.collections.Map _data = null;
 //BA.debugLineNum = 40;BA.debugLine="Sub JobDone (Job As HttpJob)";
 //BA.debugLineNum = 41;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 42;BA.debugLine="If Job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 43;BA.debugLine="Select Job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"Job")) {
case 0:
 //BA.debugLineNum = 45;BA.debugLine="Try";
try { //BA.debugLineNum = 46;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 47;BA.debugLine="parser.Initialize(Job.GetString)";
_parser.Initialize(_job._getstring());
 //BA.debugLineNum = 48;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 49;BA.debugLine="Dim xml As Map = root.Get(\"xml\")";
_xml = new anywheresoftware.b4a.objects.collections.Map();
_xml.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("xml"))));
 //BA.debugLineNum = 50;BA.debugLine="Dim status_code As String = xml.Get(\"status_c";
_status_code = BA.ObjectToString(_xml.Get((Object)("status_code")));
 //BA.debugLineNum = 51;BA.debugLine="Dim status_desc As String = xml.Get(\"status_d";
_status_desc = BA.ObjectToString(_xml.Get((Object)("status_desc")));
 //BA.debugLineNum = 52;BA.debugLine="If status_code = \"0\" Then";
if ((_status_code).equals("0")) { 
 //BA.debugLineNum = 53;BA.debugLine="Dim mobileno As String = xml.Get(\"mobileno\")";
_mobileno = BA.ObjectToString(_xml.Get((Object)("mobileno")));
 //BA.debugLineNum = 54;BA.debugLine="Dim balance As Map = xml.Get(\"balance\")";
_balance = new anywheresoftware.b4a.objects.collections.Map();
_balance.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_xml.Get((Object)("balance"))));
 //BA.debugLineNum = 55;BA.debugLine="Dim amount As String = balance.Get(\"amount\")";
_amount = BA.ObjectToString(_balance.Get((Object)("amount")));
 //BA.debugLineNum = 56;BA.debugLine="Dim expiretime As String = balance.Get(\"expi";
_expiretime = BA.ObjectToString(_balance.Get((Object)("expiretime")));
 //BA.debugLineNum = 57;BA.debugLine="Dim packtype As String = balance.Get(\"packty";
_packtype = BA.ObjectToString(_balance.Get((Object)("packtype")));
 //BA.debugLineNum = 58;BA.debugLine="Dim desc As String = balance.Get(\"desc\")";
_desc = BA.ObjectToString(_balance.Get((Object)("desc")));
 //BA.debugLineNum = 59;BA.debugLine="lblLog.Text = desc & CRLF & \"    Amount: \" &";
mostCurrent._lbllog.setText((Object)(_desc+anywheresoftware.b4a.keywords.Common.CRLF+"    Amount: "+_amount+anywheresoftware.b4a.keywords.Common.CRLF+"    Expiry Date: "+_expiretime));
 //BA.debugLineNum = 60;BA.debugLine="Dim data As Map = xml.Get(\"data\")";
_data = new anywheresoftware.b4a.objects.collections.Map();
_data.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_xml.Get((Object)("data"))));
 //BA.debugLineNum = 61;BA.debugLine="Dim amount As String = data.Get(\"amount\")";
_amount = BA.ObjectToString(_data.Get((Object)("amount")));
 //BA.debugLineNum = 62;BA.debugLine="Dim expiretime As String = data.Get(\"expiret";
_expiretime = BA.ObjectToString(_data.Get((Object)("expiretime")));
 //BA.debugLineNum = 63;BA.debugLine="Dim packtype As String = data.Get(\"packtype\"";
_packtype = BA.ObjectToString(_data.Get((Object)("packtype")));
 //BA.debugLineNum = 64;BA.debugLine="Dim desc As String = data.Get(\"desc\")";
_desc = BA.ObjectToString(_data.Get((Object)("desc")));
 //BA.debugLineNum = 65;BA.debugLine="lblLog.Text = lblLog.Text & CRLF & CRLF & de";
mostCurrent._lbllog.setText((Object)(mostCurrent._lbllog.getText()+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+_desc+anywheresoftware.b4a.keywords.Common.CRLF+"    Amount: "+_amount+anywheresoftware.b4a.keywords.Common.CRLF+"    Expiry Date: "+_expiretime));
 }else {
 //BA.debugLineNum = 67;BA.debugLine="ToastMessageShow(status_desc, True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(_status_desc,anywheresoftware.b4a.keywords.Common.True);
 };
 } 
       catch (Exception e55) {
			processBA.setLastException(e55); //BA.debugLineNum = 70;BA.debugLine="Log(\"Error: \" & LastException)";
anywheresoftware.b4a.keywords.Common.Log("Error: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)));
 };
 break;
}
;
 }else {
 //BA.debugLineNum = 74;BA.debugLine="ToastMessageShow(\"Error: \" & Job.ErrorMessage, T";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error: "+_job._errormessage,anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 76;BA.debugLine="Job.Release";
_job._release();
 //BA.debugLineNum = 77;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
}
