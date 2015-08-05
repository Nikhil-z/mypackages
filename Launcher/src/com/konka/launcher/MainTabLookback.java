package com.konka.launcher;


import android.content.Context;
import android.content.Intent;
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

public class MainTabLookback extends Fragment
{
	private Context mContext;
	
	public static Button button2_1;
	public static Button button2_16;
	public static Button button2_17;
	public static Button button2_18;
	public static Button button2_2;
	public static Button button2_3;
	public static Button button2_11;
	public static Button button2_12;
	public static Button button2_13;
	public static Button button2_14;
	public static Button button2_15;
	public static Button button2_21;
	public static Button button2_22;
	public static Button button2_23;
	public static ImageView focus_img;
	
	
	public MainTabLookback()
	{
	}
	
	public MainTabLookback(Context context)
	{
		super();
		mContext = context;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View movieLayout = inflater.inflate(R.layout.main_tab_lookback,
				container, false);
		
		initView(movieLayout);
		
		return movieLayout;
	}
	
	/**
	 * @author wangyuhang
	 * 初始化视觉控件
	 * */
	public void initView(View view)
	{
		button2_1 = (Button) view.findViewById(R.id.button2_1);
		
		focus_img = (ImageView) view.findViewById(R.id.movie_focus_img);
		
//		button2_1.setButtonNextFocusUpId(R.id.tab_btn_movie);
//		button2_2.setButtonNextFocusUpId(R.id.tab_btn_movie);
//		button2_3.setButtonNextFocusUpId(R.id.tab_btn_movie);
//		button2_13.setButtonNextFocusUpId(R.id.tab_btn_movie);
//		button2_21.setButtonNextFocusUpId(R.id.tab_btn_movie);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);
		
		if(isVisibleToUser)
		{
			Intent intent = new Intent();
			intent.putExtra("page", 2);
			intent.setAction("com.konka.launcher.PAGE_VISIBILITY");
			getActivity().sendBroadcast(intent);
		}
	}
}
