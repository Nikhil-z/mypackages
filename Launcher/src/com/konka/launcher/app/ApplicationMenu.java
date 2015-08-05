package com.konka.launcher.app;

import com.konka.launcher.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ApplicationMenu extends Activity implements OnClickListener{
	private static String TAG = "ApplicationMenu";
	
	private Context mContext = ApplicationMenu.this;
	
	private RelativeLayout common;
	private RelativeLayout replace;
	private RelativeLayout boot_start;
	
	private SharedPreferences sp_menuapp;
	private String mWhich;
	private String pkg;
	
	private AppMetroInfo mAppMetroInfo;
	private AppOperator mAppOperator;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application_menu);
		
		sp_menuapp = mContext.getSharedPreferences("menuapp", Activity.MODE_PRIVATE);
		
		initView();
	}
	
	private void initView(){
		common = (RelativeLayout) findViewById(R.id.common);
		replace = (RelativeLayout) findViewById(R.id.replace);
		boot_start = (RelativeLayout) findViewById(R.id.boot_start);
		
		common.setOnClickListener(this);
		replace.setOnClickListener(this);
		boot_start.setOnClickListener(this);
		
		Intent intent = getIntent();
		mWhich = intent.getStringExtra("which");
		pkg = sp_menuapp.getString(""+mWhich, "none");
		mAppMetroInfo = new AppMetroInfo(mContext, pkg);
		mAppOperator = new AppOperator(mContext);
		
		//初始化标题栏
		if(sp_menuapp.contains(""+mWhich) && !pkg.equals("none")){
			TextView title_summary = (TextView) findViewById(R.id.title_summary);
			title_summary.setText(getString(R.string.app_operation_summary, mAppMetroInfo.getLabel()));
		}
		
		//初始化开机启动
		if(mAppMetroInfo.isBoot()){
			((TextView) boot_start.getChildAt(0)).setText(R.string.app_operation_boot_start_cancel);
			((TextView) boot_start.getChildAt(1)).setText(R.string.app_operation_boot_start_cancel_summary);
		}	
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onClick v="+v+" mWhich="+mWhich);
		
		int id = v.getId();
		switch (id) {
		case R.id.common:
			if(mAppMetroInfo.isCommon()){
				mAppOperator.setCommon(mAppMetroInfo.getPackageName(), false, true);
			}else{
				mAppOperator.setCommon(mAppMetroInfo.getPackageName(), true, true);
			}
			finish();
			break;
		case R.id.replace:
//			replace();
			LauncherMenu.toOtherApp(mContext, mWhich);
			finish();
			break;
		case R.id.boot_start:
			if(mAppMetroInfo.isBoot()){
				mAppOperator.setBoot(mAppMetroInfo.getPackageName(), false, true);
			}else{
				mAppOperator.setBoot(mAppMetroInfo.getPackageName(), true, true);
			}
			finish();
			break;
		default:
			break;
		}
	}
	
//	//替换
//	private void replace(){
//		Intent intent = new Intent(mContext, OtherApps.class);
//		intent.putExtra("which", mWhich);
//		mContext.startActivity(intent);
//	}
}