package com.konka.launcher.app;

import com.konka.launcher.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ApplicationOperation extends DialogFragment implements OnClickListener{
	private static String TAG = "ApplicationOperation";
	
	private Activity activity;
	
	private RelativeLayout common;
	private RelativeLayout boot_start;
	private RelativeLayout uninstall;
	
	private String pkg;
	
	private AppMetroInfo mAppMetroInfo;
	private AppOperator mAppOperator;
	private BroadcastReceiver mBroadcastReceiver;
	
	public static ApplicationOperation newInstance(OnClickListener listener,String pkg) {
		// TODO Auto-generated method stub
		ApplicationOperation dia = new ApplicationOperation();
		
		Bundle args = new Bundle();
        args.putString("pkg", pkg);
        dia.setArguments(args);//传递num参数给ArrayListFragment
		return dia;
	}
	
	
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		registerReceiver(activity);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		unregisterReceiver(activity);
		super.onDetach();
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);//全屏
		
		if(getArguments() != null){//获取setArguments传递过来的参数
			pkg = getArguments().getString("pkg");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.application_operation, container, false);// 自定义Dialog布局
		
		activity = getActivity();
		initView(v);
		
		return v;
	}
	
	private void initView(View v){
		mAppMetroInfo = new AppMetroInfo(activity, pkg);
		mAppOperator = new AppOperator(activity);
		
		common = (RelativeLayout) v.findViewById(R.id.common);
		common.setOnClickListener(this);
		boot_start = (RelativeLayout) v.findViewById(R.id.boot_start);
		boot_start.setOnClickListener(this);
		uninstall = (RelativeLayout) v.findViewById(R.id.uninstall);
		uninstall.setOnClickListener(this);
		
		//初始化标题栏
		TextView title_summary = (TextView) v.findViewById(R.id.title_summary);
		title_summary.setText(getString(R.string.app_operation_summary, mAppMetroInfo.getLabel()));
		Log.v(TAG, "================pkg="+pkg+" label="+mAppMetroInfo.getLabel());
		
		//初始化常用
		if(mAppMetroInfo.isCommon()){
			((TextView)common.getChildAt(0)).setText(R.string.app_operation_common_cancel);
			((TextView)common.getChildAt(1)).setText(R.string.app_operation_common_cancel_summary);
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
		boolean dis = false;
		if(pkg == null){
			return;
		}
		
		int id = v.getId();
		Log.v(TAG, "onClick id="+id+" getNext()="+mAppOperator.getGeneralNext()+" isCommon()="+mAppMetroInfo.isCommon());
		switch (id) {
		case R.id.common:
			if(mAppMetroInfo.isCommon()){
				dis = mAppOperator.setCommon(mAppMetroInfo.getPackageName(), false, true);
			}else{
				dis = mAppOperator.setCommon(mAppMetroInfo.getPackageName(), true, true);
			}
			break;
		case R.id.boot_start:
			if(mAppMetroInfo.isBoot()){
				mAppOperator.setBoot(mAppMetroInfo.getPackageName(), false, true);
			}else{
				mAppOperator.setBoot(mAppMetroInfo.getPackageName(), true, true);
			}
			dis = true;
			break;
		case R.id.uninstall:
			mAppOperator.unInstall(mAppMetroInfo.getPackageName());
			dis = false;
			break;

		default:
			break;
		}
		
		if(dis){
			this.dismiss();
		}
	}
	
	
	
	private void registerReceiver(Context context) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addDataScheme("package");
		intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		
		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				handleEvent(context, intent);
			}
		};
		getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
		
	}

	private void unregisterReceiver(Context context) {
		if(mBroadcastReceiver != null){
			getActivity().unregisterReceiver(mBroadcastReceiver);
		}
	}
	
	private void handleEvent(Context context, Intent intent) {
		String action = intent.getAction();
//		this.dismiss();
		
		common.setClickable(false);
		boot_start.setClickable(false);
		uninstall.setClickable(false);
		
		((TextView)common.getChildAt(0)).setTextColor(Color.GRAY);
		((TextView)common.getChildAt(1)).setTextColor(Color.GRAY);
		((TextView)boot_start.getChildAt(0)).setTextColor(Color.GRAY);
		((TextView)boot_start.getChildAt(1)).setTextColor(Color.GRAY);
		((TextView)uninstall.getChildAt(0)).setTextColor(Color.GRAY);
		
		
	}
}