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

public class imeitracker extends Activity implements B4AActivity{
	public static imeitracker mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.myanmarunicorn.ooredootracker", "com.myanmarunicorn.ooredootracker.imeitracker");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (imeitracker).");
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
		activityBA = new BA(this, layout, processBA, "com.myanmarunicorn.ooredootracker", "com.myanmarunicorn.ooredootracker.imeitracker");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.myanmarunicorn.ooredootracker.imeitracker", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (imeitracker) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (imeitracker) Resume **");
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
		return imeitracker.class;
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
        BA.LogInfo("** Activity (imeitracker) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (imeitracker) Resume **");
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
public anywheresoftware.b4a.objects.EditTextWrapper _edtimei = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btntrack = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllog = null;
public anywheresoftware.b4a.samples.httputils2.httputils2service _v7 = null;
public com.myanmarunicorn.ooredootracker.main _v0 = null;
public com.myanmarunicorn.ooredootracker.balancetracker _v5 = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 17;BA.debugLine="Activity.LoadLayout(\"IMEITracker\")";
mostCurrent._activity.LoadLayout("IMEITracker",mostCurrent.activityBA);
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
 //BA.debugLineNum = 29;BA.debugLine="If edtIMEI.Text.Length = 0 Then";
if (mostCurrent._edtimei.getText().length()==0) { 
 //BA.debugLineNum = 30;BA.debugLine="ToastMessageShow(\"IMEI Should Not Be Empty.\", Tr";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("IMEI Should Not Be Empty.",anywheresoftware.b4a.keywords.Common.True);
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
_job._initialize(processBA,"Job",imeitracker.getObject());
 //BA.debugLineNum = 37;BA.debugLine="job.Download2(\"https://ecareapp.ooredoo.com.mm/de";
_job._download2("https://ecareapp.ooredoo.com.mm/deviceapi/Default.aspx",new String[]{"Action","getsubscriberinfo","IMEI",mostCurrent._edtimei.getText()});
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 11;BA.debugLine="Private edtIMEI As EditText";
mostCurrent._edtimei = new anywheresoftware.b4a.objects.EditTextWrapper();
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
anywheresoftware.b4a.objects.collections.Map _subscriber = null;
String _id = "";
anywheresoftware.b4a.objects.collections.List _info = null;
anywheresoftware.b4a.objects.collections.Map _colinfo = null;
String _name = "";
String _value = "";
anywheresoftware.b4a.objects.collections.Map _result = null;
String _content = "";
 //BA.debugLineNum = 40;BA.debugLine="Sub JobDone (Job As HttpJob)";
 //BA.debugLineNum = 41;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 42;BA.debugLine="If Job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 43;BA.debugLine="Select Job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"Job")) {
case 0:
 //BA.debugLineNum = 45;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 46;BA.debugLine="parser.Initialize(XMLToJSON(Job.GetString))";
_parser.Initialize(_vv1(_job._getstring()));
 //BA.debugLineNum = 47;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 48;BA.debugLine="If root.ContainsKey(\"Subscriber\") Then";
if (_root.ContainsKey((Object)("Subscriber"))) { 
 //BA.debugLineNum = 49;BA.debugLine="Dim Subscriber As Map = root.Get(\"Subscriber\"";
_subscriber = new anywheresoftware.b4a.objects.collections.Map();
_subscriber.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("Subscriber"))));
 //BA.debugLineNum = 50;BA.debugLine="Dim id As String = Subscriber.Get(\"id\")";
_id = BA.ObjectToString(_subscriber.Get((Object)("id")));
 //BA.debugLineNum = 51;BA.debugLine="Dim info As List = Subscriber.Get(\"info\")";
_info = new anywheresoftware.b4a.objects.collections.List();
_info.setObject((java.util.List)(_subscriber.Get((Object)("info"))));
 //BA.debugLineNum = 52;BA.debugLine="For Each colinfo As Map In info";
_colinfo = new anywheresoftware.b4a.objects.collections.Map();
final anywheresoftware.b4a.BA.IterableList group37 = _info;
final int groupLen37 = group37.getSize();
for (int index37 = 0;index37 < groupLen37 ;index37++){
_colinfo.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(group37.Get(index37)));
 //BA.debugLineNum = 53;BA.debugLine="Dim name As String = colinfo.Get(\"name\")";
_name = BA.ObjectToString(_colinfo.Get((Object)("name")));
 //BA.debugLineNum = 54;BA.debugLine="Dim value As String = colinfo.Get(\"value\")";
_value = BA.ObjectToString(_colinfo.Get((Object)("value")));
 //BA.debugLineNum = 55;BA.debugLine="Select name";
switch (BA.switchObjectToInt(_name,"imsi","msisdn","modelname")) {
case 0:
 //BA.debugLineNum = 57;BA.debugLine="lblLog.Text = lblLog.Text & CRLF & \"IMSI:";
mostCurrent._lbllog.setText((Object)(mostCurrent._lbllog.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"IMSI: "+_value));
 break;
case 1:
 //BA.debugLineNum = 59;BA.debugLine="lblLog.Text = lblLog.Text & CRLF & \"Phone";
mostCurrent._lbllog.setText((Object)(mostCurrent._lbllog.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"Phone Number: +"+_value));
 break;
case 2:
 //BA.debugLineNum = 61;BA.debugLine="lblLog.Text = lblLog.Text & CRLF & \"Model";
mostCurrent._lbllog.setText((Object)(mostCurrent._lbllog.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"Model Number: "+_value.substring((int) (_value.indexOf("/")+1))));
 break;
}
;
 }
;
 }else {
 //BA.debugLineNum = 65;BA.debugLine="Dim result As Map = root.Get(\"result\")";
_result = new anywheresoftware.b4a.objects.collections.Map();
_result.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((Object)("result"))));
 //BA.debugLineNum = 66;BA.debugLine="Dim content As String = result.Get(\"content\")";
_content = BA.ObjectToString(_result.Get((Object)("content")));
 //BA.debugLineNum = 67;BA.debugLine="ToastMessageShow(content, True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(_content,anywheresoftware.b4a.keywords.Common.True);
 };
 break;
}
;
 }else {
 //BA.debugLineNum = 71;BA.debugLine="ToastMessageShow(\"Error: \" & Job.ErrorMessage, T";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error: "+_job._errormessage,anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 73;BA.debugLine="Job.Release";
_job._release();
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public static String  _vv1(String _xml) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator _generator = null;
String _json = "";
anywheresoftware.b4a.objects.collections.Map _root = null;
 //BA.debugLineNum = 76;BA.debugLine="Sub XMLToJSON(xml As String) As String";
 //BA.debugLineNum = 77;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 78;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 79;BA.debugLine="Dim generator As JSONGenerator";
_generator = new anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator();
 //BA.debugLineNum = 81;BA.debugLine="jo.InitializeNewInstance(\"org.json.XML\", Null)";
_jo.InitializeNewInstance("org.json.XML",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 82;BA.debugLine="Dim json As String = jo.RunMethod(\"toJSONObject\",";
_json = BA.ObjectToString(_jo.RunMethod("toJSONObject",new Object[]{(Object)(_xml)}));
 //BA.debugLineNum = 83;BA.debugLine="parser.Initialize(json)";
_parser.Initialize(_json);
 //BA.debugLineNum = 84;BA.debugLine="Dim root As Map = parser.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _parser.NextObject();
 //BA.debugLineNum = 85;BA.debugLine="generator.Initialize(root)";
_generator.Initialize(_root);
 //BA.debugLineNum = 86;BA.debugLine="Return generator.ToPrettyString(4)";
if (true) return _generator.ToPrettyString((int) (4));
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
}
