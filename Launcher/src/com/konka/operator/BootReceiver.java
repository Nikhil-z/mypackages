package com.konka.operator;

import com.konka.launcher.app.AppOperator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver
{
	private String TAG = "BootReceiver";
	
	private Context mContext;
	private AppOperator mAppOperator;
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		mContext = context;
		String action = intent.getAction();
		if(action.equals("android.intent.action.BOOT_COMPLETED")){
			Log.v(TAG,"!android.intent.action.BOOT_COMPLETED");
			bootStart();
		}
	}
	
	// 运行开机启动应用
	private void bootStart()
	{
		if (mAppOperator == null)
		{
			mAppOperator = new AppOperator(mContext);
		}
		String pkg = mAppOperator.getGeneralBootPkg();
		if (pkg != null && !pkg.equals("none"))
		{
			mAppOperator.doStartApplicationWithPackageName(pkg);
		}
	}
}
