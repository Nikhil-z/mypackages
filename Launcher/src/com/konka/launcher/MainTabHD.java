package com.konka.launcher;

import java.io.File;
import java.io.ObjectInputStream.GetField;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainTabHD extends Fragment
{
	private Context mContext;
	
	public static Button button3_1;
	public static Button button3_11;
	public static Button button3_12;
	public static Button button3_2;
	public static Button button3_3;
	public static Button button3_13;
	public static Button button3_14;
	public static Button button3_15;
	public static Button button3_16;
	public static Button button3_17;
	
	public static ImageView focus_img;
	
	public MainTabHD()
	{
	}
	
	public MainTabHD(Context context)
	{
		super();
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View gameLayout = inflater.inflate(R.layout.main_tab_hd, container,
				false);
		
		initView(gameLayout);
		
		return gameLayout;
	}

	/**
	 * @author wangyuhang 初始化视觉控件
	 * */
	public void initView(View view)
	{
		button3_1 = (Button) view.findViewById(R.id.button3_1);
	
		focus_img = (ImageView) view.findViewById(R.id.game_focus_img);
		
//		button3_1.setButtonNextFocusUpId(R.id.tab_btn_game);
//		button3_2.setButtonNextFocusUpId(R.id.tab_btn_game);
//		button3_3.setButtonNextFocusUpId(R.id.tab_btn_game);
//		button3_15.setButtonNextFocusUpId(R.id.tab_btn_game);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);
		
		if(isVisibleToUser)
		{
			Intent intent = new Intent();
			intent.putExtra("page", 3);
			intent.setAction("com.konka.launcher.PAGE_VISIBILITY");
			getActivity().sendBroadcast(intent);
		}
	}
}
