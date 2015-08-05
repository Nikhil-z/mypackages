package com.konka.launcher.app;

import java.util.Iterator;
import java.util.Map;

import com.konka.launcher.MainActivity;
import com.konka.launcher.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LauncherMenu extends Activity implements OnClickListener
{
	private static String TAG = "LauncherMenu";
	private PackageManager mPackageManager;
	private Context mContext;
	
	private LinearLayout tips;
	private ImageView arrow;
	public static FrameLayout[] apps;
	public static int[] app_id = {R.id.app_1, R.id.app_2, R.id.app_3, R.id.app_4, R.id.app_5, R.id.app_6, R.id.app_7, R.id.app_8};
	public static int appNum = 8;
	
	private FrameLayout app_add;
	
	private SharedPreferences sp_menuapp;
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		mContext = LauncherMenu.this;
		mPackageManager = getPackageManager();
		sp_menuapp = mContext.getSharedPreferences("menuapp", Activity.MODE_PRIVATE);
		
		initView();
		addButtonLis();
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				arrow.setImageResource(R.drawable.arrow_down);
				super.handleMessage(msg);
			}
		};
		handler.sendEmptyMessageDelayed(0, 400);
	}

	public void initView(){
		arrow = (ImageView) findViewById(R.id.arrow_up);
		tips = (LinearLayout) findViewById(R.id.tips);
		tips.setVisibility(View.VISIBLE);
		apps = new FrameLayout[appNum];
		for (int i = 0; i < appNum; i++) {
			apps[i] = (FrameLayout) findViewById(app_id[i]);
		}
		
		app_add = (FrameLayout) findViewById(R.id.app_add);
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initDate();
		
		//updateExpose();//常用栏数据曝光
	}
	
//	private void updateExpose(){
//		Map appMap = MainActivity.sp_menuapp.getAll();
//		Iterator iterator = appMap.keySet().iterator();
//		Bundle data = new Bundle();
//		LctsEvent event = new LctsEvent();
//		event.setType(LctsEvent.TYPE_ITEM_EXPOSE);
//		String tag = null;
//		String label = null;
//		while (iterator.hasNext())
//		{
//			tag = (String) iterator.next();
//			label = MainActivity.sp_menuapp.getString(tag, null);
//			if (!tag.equals("boot"))
//			{
//				data.putString(tag, label);
//			}
//		}
//		event.setData(data);
//		
//	}
	
	private void initDate(){
		String pkg;
		ApplicationInfo info;
		int num = 0;
		for (int i = 0; i < appNum; i++) {
			pkg = sp_menuapp.getString(""+AppOperator.APPTAG[i], "none");//backPackage.contains(""+app_id[i])
			Log.v(TAG, "i="+i+" pkg="+pkg);
			
			if(pkg.equals("none")){
				apps[i].setVisibility(View.GONE);
				continue;
			}
			num++;
			apps[i].setVisibility(View.VISIBLE);
			try {
				info = mPackageManager.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
				((ImageView) apps[i].getChildAt(0)).setImageDrawable(mPackageManager.getApplicationIcon(info));
				((TextView) apps[i].getChildAt(1)).setText(mPackageManager.getApplicationLabel(info).toString());
				
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保证常用栏为空时，添加按钮和首页左对齐
		int w = getResources().getDimensionPixelSize(R.dimen.small_button_width);
		int h = getResources().getDimensionPixelSize(R.dimen.small_button_height);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
		params.addRule(RelativeLayout.RIGHT_OF, R.id.app_8);
		params.addRule(RelativeLayout.ALIGN_TOP, R.id.app_8);
		if(num == appNum){
			app_add.setVisibility(View.GONE);
			params.leftMargin = 0;
			app_add.setLayoutParams(params);
		}else if(num == 0){
			params.leftMargin = getResources().getDimensionPixelSize(R.dimen.button_to_left);
			app_add.setLayoutParams(params);
			app_add.setVisibility(View.VISIBLE);
		}else{
			params.leftMargin = 0;
			app_add.setLayoutParams(params);
			app_add.setVisibility(View.VISIBLE);
		}
	}
	

	public void addButtonLis(){
		for (int i = 0; i < appNum; i++) {
			apps[i].setOnClickListener(this);
		}
		app_add.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		String tag = (String) v.getTag();
		Log.v(TAG, "onClick-->id="+id+" tag="+tag);
		if(!(contain(app_id, id) || id == R.id.app_add)){
			return;
		}
		
//		if(id == R.id.app_1){
//			backPackage.edit().clear().commit();//8040 仅仅用于测试
//		}
		
		if(id == R.id.app_add){
			toOtherApp(mContext, tag);
		}else{
			String pkg = sp_menuapp.getString(""+tag, "none");
			if(sp_menuapp.contains(""+tag)){
				
				mPackageManager = getPackageManager();
				Intent intent = mPackageManager.getLaunchIntentForPackage(pkg);
				if(intent == null)
				{
					return;
				}
				
				String cls = intent.getComponent().getClassName();
				ComponentName componet = new ComponentName(pkg, cls);
				Intent i = new Intent();
				i.setComponent(componet);
				startActivity(i);
				
				//埋点
				//ButtonOperator.updateClickInfo(tag, pkg);
				
				finish();
			}
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		
		View view = getCurrentFocus();
		if(view == null){
			return super.dispatchKeyEvent(event);
		}
		
		int id = view.getId();
		String tag = (String) view.getTag();
		if(!contain(app_id, id) && (id != R.id.app_add)){
			return super.dispatchKeyEvent(event);
		}
		
		Log.v(TAG, "dispatchKeyEvent-->id="+Integer.toHexString(id));
		if ((event.getKeyCode() == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) && (id != R.id.app_add)){
			Intent intent = new Intent(mContext, ApplicationMenu.class);
			intent.putExtra("which", tag);
			mContext.startActivity(intent);
		}else if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_UP){
			finish();
		}
		
		return super.dispatchKeyEvent(event);
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onDestroy");
		MainActivity.main_bottom.setVisibility(View.VISIBLE);
		super.onDestroy();
	}

	private boolean contain(int[] all, int target){
		for (int i = 0; i < all.length; i++) {
			if(target == all[i]){
				return true;
			}
		}
		return false;
	}
	
	public static void toOtherApp(Context context, String which){
		Intent intent = new Intent(context, OtherApps.class);
		intent.putExtra("which", which);
		context.startActivity(intent);
	}
	
	
	
}