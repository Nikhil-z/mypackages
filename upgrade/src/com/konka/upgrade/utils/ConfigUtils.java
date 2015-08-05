package com.konka.upgrade.utils;

import com.konka.upgrade.R;

import android.os.SystemProperties;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class ConfigUtils {	
    
    private static String LoopPeriodKey = "period";
    private static String LoopDelayKey = "delay";
    private static String VersionNeverRemind = "version_never_remind";
    private static String OneUpgradeJob = "one_upgrade_job";
    
    private final static long DEFAULT_PERIOD = 2400000L;//40minute
    private final static int DEFAULT_DELEY = 120000;
	
    public static boolean getBooleanConfig(Context context, int id) {
    	return context.getResources().getBoolean(id); 
    }

    public static String getStringConfig(Context context, int id) {
    	return context.getResources().getString(id);
    }
    
    public static String getDefaultXmlUrl(Context context) {
    	String xmlUrl = SystemProperties.get("ro.config.server_xml_url");
    	if( (xmlUrl == null) ||
    			(xmlUrl.equals("")) ) {
    		xmlUrl = context.getResources().getString(R.string.config_net_server_xml_url);
    	}
        return xmlUrl;
    }
    
    public static int getDefaultLongDely(Context context){

    	String longDelyStr = SystemProperties.get("ro.config.def_check_period");
    	int longDely = 0;
    	if ((longDelyStr == null)||(longDelyStr.equals(""))){
		longDely = 20;
	}
	else{
		longDely = Integer.parseInt(longDelyStr);		
	}

	if (longDely <= 0){
		longDely = 20;
	}
    	return longDely;
    }
    
    public static String getDefaultXmlVerifyUrl(Context context) {
    	String xmlVerifyUrl = SystemProperties.get("ro.config.server_xml_verify_url");
    	if( (xmlVerifyUrl == null) ||
    			(xmlVerifyUrl.equals("")) ) {
    		xmlVerifyUrl = context.getResources().getString(R.string.config_net_server_xml_verify_url);
    	}
        return xmlVerifyUrl;
    }
    
    public static String getDefaultUsbUpgradeName(Context context) {
        String usbUpgradeName = SystemProperties.get("ro.config.force_upgrade_name");
        if( (usbUpgradeName == null) ||
                (usbUpgradeName.equals("")) ) {
            usbUpgradeName = context.getResources().getString(R.string.config_recovery_force_upgrade_name);
        }
        return usbUpgradeName;
    
    }
    
    public static long getPeriod(Context context) {

	long period = 0;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

	period = preferences.getLong(LoopPeriodKey, 0);
	if (period  <= 0){
		
		String PeriodStr = SystemProperties.get("ro.config.def_check_period");
	    	if ((PeriodStr == null)||(PeriodStr.equals(""))){
			period = DEFAULT_PERIOD;
		}
		else{
			period = Integer.parseInt(PeriodStr)*60000L;		
		}

		if (period <= 0){
			period = DEFAULT_PERIOD;
		}
	}
		
        return period;
    }
    
    public static void setPeriod(Context context,long period) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong(LoopPeriodKey, period);
        editor.apply();
    }
    
    public static long getDelay(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(LoopDelayKey, DEFAULT_DELEY);
    }
    
    public static void setDelay(Context context,long delay) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong(LoopDelayKey, delay);
        editor.apply();
    }
    
    public static void setNeverRemindVersion(Context context,long version) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong(VersionNeverRemind, version);
        editor.apply();
    }
    
    public static long getNeverRemindVersion(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(VersionNeverRemind, 0);
    }
    
    public static boolean getMultithreadDownload(Context context) {
        return context.getResources().getBoolean(R.bool.config_multithread_download);
    }
    
    public static void setBootFlag(Context context,boolean boot) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean(OneUpgradeJob, boot);
        editor.apply();
    }
    
    public static boolean getBootFlag(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(OneUpgradeJob, false);
    }
}
