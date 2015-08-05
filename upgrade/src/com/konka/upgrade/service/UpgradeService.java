package com.konka.upgrade.service;

import com.konka.upgrade.OneShotSignal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import static com.konka.upgrade.download.Constants.TAG;

public class UpgradeService extends Service {
    
    private UpgradeManager manager;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
	Log.i(TAG, " UpgradeService onCreate");
        manager = new UpgradeManager(getApplicationContext());
        if(OneShotSignal.isUpgradeReady())
        Log.e("service", "invoked");
        	manager.invoke();
        
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
	Log.i(TAG, " UpgradeService onDestroy");
        Log.e("service", "onstop!!");
        manager.stop();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
    //    Log.i("UpgradeService","service start action "+intent.getAction());
    	Log.e("service", "onstartcommand!");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return manager.getRemoteService();
    }
}
