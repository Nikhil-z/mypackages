package com.konka.launcher;

import com.konka.operator.OnFocusChangeLis;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public  class MainTabApplication extends Fragment implements OnClickListener
{
	private Context mContext;
	
	public static Button button4_1;
	public static Button button4_2;
	public static Button button4_3;
	public static Button button4_4;
	public static Button button4_5;
	public static Button button4_6;
	public static Button button4_7;
	public static Button button4_8;
	public static Button button4_9;
	public static Button button4_10;
	public static Button button4_11;
	public static Button button4_12;
	public static ImageView focus_img;
	
	
	public MainTabApplication()
	{
	}
	
	public MainTabApplication(Context context)
	{
		super();
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View childLayout = inflater.inflate(R.layout.main_tab_application, container, false);
		
		initView(childLayout);
		
		return childLayout;
	}
	
	/**
	 * @author wangyuhang 初始化视觉控件
	 * */
	public void initView(View view)
	{
		button4_1 = (Button) view.findViewById(R.id.button4_1);
		button4_2 = (Button) view.findViewById(R.id.button4_2);
		button4_3 = (Button) view.findViewById(R.id.button4_3);
		button4_4 = (Button) view.findViewById(R.id.button4_4);
		button4_5 = (Button) view.findViewById(R.id.button4_5);
		button4_6 = (Button) view.findViewById(R.id.button4_6);
		button4_7 = (Button) view.findViewById(R.id.button4_7);
		button4_8 = (Button) view.findViewById(R.id.button4_8);
		button4_9 = (Button) view.findViewById(R.id.button4_9);
		button4_10 = (Button) view.findViewById(R.id.button4_10);
		button4_11 = (Button) view.findViewById(R.id.button4_11);
		button4_12 = (Button) view.findViewById(R.id.button4_12);
		
		focus_img = (ImageView) view.findViewById(R.id.child_focus_img);
		
		button4_1.setNextFocusUpId(R.id.tab_btn_child);
		button4_2.setNextFocusUpId(R.id.tab_btn_child);
		button4_3.setNextFocusUpId(R.id.tab_btn_child);
		button4_4.setNextFocusUpId(R.id.tab_btn_child);
		
		OnFocusChangeLis focusChangeListener = new OnFocusChangeLis();
		button4_1.setOnFocusChangeListener(focusChangeListener);
		button4_2.setOnFocusChangeListener(focusChangeListener);
		button4_3.setOnFocusChangeListener(focusChangeListener);
		button4_4.setOnFocusChangeListener(focusChangeListener);
		button4_5.setOnFocusChangeListener(focusChangeListener);
		button4_6.setOnFocusChangeListener(focusChangeListener);
		button4_7.setOnFocusChangeListener(focusChangeListener);
		button4_8.setOnFocusChangeListener(focusChangeListener);
		button4_9.setOnFocusChangeListener(focusChangeListener);
		button4_10.setOnFocusChangeListener(focusChangeListener);
		button4_11.setOnFocusChangeListener(focusChangeListener);
		button4_12.setOnFocusChangeListener(focusChangeListener);
		
		button4_1.setOnClickListener(this);
		button4_2.setOnClickListener(this);
		button4_3.setOnClickListener(this);
		button4_4.setOnClickListener(this);
		button4_5.setOnClickListener(this);
		button4_6.setOnClickListener(this);
		button4_7.setOnClickListener(this);
		button4_8.setOnClickListener(this);
		button4_9.setOnClickListener(this);
		button4_10.setOnClickListener(this);
		button4_11.setOnClickListener(this);
		button4_12.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.button4_1:
			gotoOutPakage("com.HTRD.stock","com.HTRD.stock.SplashActivity");
			break;
		case R.id.button4_2:
			gotoOutPakage("android.test.ui", "android.test.ui.CaEmailListActivity");
			break;
		case R.id.button4_3:
			ComponentName comp1 = new ComponentName("com.konka.settingsmbox","com.konka.settingsmbox.SettingsMboxActivity");
			intent.putExtra("which", 1);
			intent.setComponent(comp1);			
			startActivity(intent);
			break;
		case R.id.button4_4:
			gotoOutPakage("android.test.ui", "android.test.ui.SettingActivity");
			break;
		case R.id.button4_5:
			gotoOutPakage("com.jrm.localmm", "com.jrm.localmm.ui.main.FileBrowserActivity");
			break;
		case R.id.button4_6:
			ComponentName comp2 = new ComponentName("com.konka.settingsmbox","com.konka.settingsmbox.SettingsMboxActivity");
			intent.putExtra("which", 2);
			intent.setComponent(comp2);			
			startActivity(intent);
			break;
		case R.id.button4_12:
			gotoOutPakage("com.konka.launcher", "com.konka.launcher.OtherApplication");
			break;
		default:
			break;
		}
		
	}
	private void gotoOutPakage(String pkg, String cls) {
		ComponentName comp = new ComponentName(pkg, cls);
		Intent intent = new Intent();	
		intent.setComponent(comp);			
		startActivity(intent);
		}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);
		
		if(isVisibleToUser)
		{
			Intent intent = new Intent();
			intent.putExtra("page", 4);
			intent.setAction("com.konka.launcher.PAGE_VISIBILITY");
			getActivity().sendBroadcast(intent);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
}
