package com.konka.upgrade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.konka.upgrade.service.UpgradeService;

public class AndroidBootReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        PackageManager packageManager = context.getPackageManager();
        try {
            System.out.println("App version: "+packageManager.getPackageInfo("com.konka.upgrade", 0).versionName);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        OneShotSignal.setUpgradeReady();
	Log.i("DownloadManager", "###BootReceiver###");
        context.startService(new Intent(context,UpgradeService.class));
    }

}
