package com.myanmarunicorn.ooredootracker.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_main{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 1;BA.debugLine="imvBanner.Height = 37%x"[main/General script]
views.get("imvbanner").vw.setHeight((int)((37d / 100 * width)));
//BA.debugLineNum = 2;BA.debugLine="btnIMEITracker.Bottom = 50%y - 5dip"[main/General script]
views.get("btnimeitracker").vw.setTop((int)((50d / 100 * height)-(5d * scale) - (views.get("btnimeitracker").vw.getHeight())));
//BA.debugLineNum = 3;BA.debugLine="btnBalanceTracker.Top = btnIMEITracker.Bottom + 10dip"[main/General script]
views.get("btnbalancetracker").vw.setTop((int)((views.get("btnimeitracker").vw.getTop() + views.get("btnimeitracker").vw.getHeight())+(10d * scale)));

}
}