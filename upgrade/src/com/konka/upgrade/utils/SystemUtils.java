package com.konka.upgrade.utils;

import java.io.File;
import java.io.IOException;

import com.konka.upgrade.exception.LoaderException;
import com.konka.system.SystemManufactureNative;

import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.os.RecoverySystem;
import android.os.SystemProperties;
import android.util.Log;

public class SystemUtils {
    
	private final static String TAG = "Upgrade.RebootUtils";
    private final static String UPGRADE_PREFIX = "--update_package=";
	private static SystemManufactureNative manufactureNative = new SystemManufactureNative();
	
	public static void rebootInstallPackage(final Context context, final File packageFile) {
        Log.w(TAG, "!!! REBOOT INSTALL PACKAGE !!!");
        
        Log.d(TAG, "file path is " + packageFile.getPath());
        // The reboot call is blocking, so we need to do it on another thread.
        try {
            RecoverySystem.installPackage(context, packageFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	public static void installPackageWithoutReboot(final Context context,final File packFile) {
        Log.w(TAG, "!!! INSTALL PACKAGE !!!");
        
        Log.d(TAG, "file path is " + packFile.getPath());
        // The reboot call is blocking, so we need to do it on another thread.
        try {
            RecoverySystem.installPackageWithoutReboot(context, packFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	public static void rebootNormal(Context context) {
		Log.w(TAG, "!!! REBOOT NORMAL !!!");
	    PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	    pm.reboot("normal_reboot");		
	}		
	
	public static void rebootWipeUserData(final Context context) {
		Log.w(TAG, "!!! REBOOT WIPE USER DATA !!!");
        // The reboot call is blocking, so we need to do it on another thread.
        Thread thr = new Thread("Reboot") {
            @Override
            public void run() {
                try {
                    RecoverySystem.rebootWipeUserData(context);                    
                } catch (IOException e) {
                    Log.e(TAG, "Can't perform rebootInstallPackage", e);
                }
            }
        };
        thr.start();		
	}
}
