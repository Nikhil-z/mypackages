package com.konka.launcher;

import com.konka.operator.OnFocusChangeBig;
import com.konka.operator.OnFocusChangeLis;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.av.AVPlayerManager;
import android.com.konka.dvb.prog.ProgManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.konka.custom.view.CaViewSmallWindow;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
//import android.os.SystemProperties;

public class MainTabLive extends Fragment implements OnClickListener
{
	private static final String TAG = "MainTabRecommend";
	
	private Context mContext;
	
	public static SurfaceView button1_1;
	public static Button button1_51;
	public static Button button1_52;
	public static Button button1_2;
	public static Button button1_3;
	public static Button button1_4;
	public static Button button1_5;
	public static Button button1_6;
	public static Button button1_7;
	public static Button button1_8;
	public static Button button1_9;
	public static Button button1_10;
	public static ImageView focus_img;
	public static Button message_spot;
	public static ImageView video_play_icon;
	public static Boolean isPlaying = false;
	public MediaPlayer mediaPlayer;
	private static MainTabLive mainTabLive = null;
	public AVPlayerManager aVPlayerManager = null;
	public ProgManager proManager = null;
	private static String curr_prog_num;// 当前台号
	public static int volume = 16;  //声音初始值
	//当前fragment处于用户可见状态时的时间
	public static long visible_time = 0;
	//当前fragment处于用户不可见状态时的时间
	public static long invisible_time = 0;
	
	/**add by liuhuasheng for small win msg*/
	private CaViewSmallWindow caViewSmallWindow = null;
	private View layoutView = null;
	
	
	
	private HorizontalScrollView horizontalScrollView;
	
	public MainTabLive()
	{
	}
	
	public MainTabLive(Context context)
	{
		super();
		mContext = context;
	}
	
	/*public static MainTabLive getInstance(){
		return mainTabLive;
	}
	*/
	
	/**add by liuhuasheng for small win msg*/
	private void updatecaViewSmallWindow(View view) {
		// TODO Auto-generated method stub
		
		caViewSmallWindow = new CaViewSmallWindow(view,mContext);
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View recommendLayout = inflater.inflate(R.layout.main_tab_live,
				container, false);
		layoutView = recommendLayout;
		
		initView(recommendLayout);
		return recommendLayout;
	}
	
	@Override
	public void onResume()
	{
		DVB.GetInstance().SetDVBStatus(1);
		aVPlayerManager.startPlayer();
		aVPlayerManager.setPlayerVolume(volume);
		aVPlayerManager.setPlayerWindow(50, 110, 470, 350);
		
		updatecaViewSmallWindow(layoutView);
		
		super.onResume();	
	}
	
	@Override
	public void onPause() {
		
		if(aVPlayerManager != null){
			aVPlayerManager.stopPlayer();
		}
		/**add by liuhuasheng for small win msg*/
		caViewSmallWindow.cancelAllSmallWinShowMsg();
		DVB.GetInstance().ReleaseResource();
		super.onPause();
		
	}
	@Override
	public void onStop() {	
    		
		super.onStop();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * @author wangyuhang 初始化视觉控件
	 * */
	public void initView(View view)
	{
		//horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.main_recommend);
		
		button1_1 = (SurfaceView) view.findViewById(R.id.button1_1);
		button1_2 = (Button) view.findViewById(R.id.button1_2);
		button1_3 = (Button) view.findViewById(R.id.button1_3);
		button1_51 = (Button) view.findViewById(R.id.button1_51);
		button1_52 = (Button) view.findViewById(R.id.button1_52);
		button1_4 = (Button) view.findViewById(R.id.button1_4);
		button1_5 = (Button) view.findViewById(R.id.button1_5);
		button1_6 = (Button) view.findViewById(R.id.button1_6);
		button1_7 = (Button) view.findViewById(R.id.button1_7);
		button1_8 = (Button) view.findViewById(R.id.button1_8);
		button1_9 = (Button) view.findViewById(R.id.button1_9);
		button1_10 = (Button) view.findViewById(R.id.button1_10);
		focus_img = (ImageView) view.findViewById(R.id.recommend_focus_img);
		video_play_icon = (ImageView) view.findViewById(R.id.video_play_icon);
		//video_pause_shadow = (Button) view.findViewById(R.id.video_pause_shadow);
		
		button1_2.setNextFocusUpId(R.id.tab_btn_recommend);
		button1_3.setNextFocusUpId(R.id.tab_btn_recommend);
		button1_4.setNextFocusUpId(R.id.tab_btn_recommend);
		button1_1.setNextFocusUpId(R.id.tab_btn_recommend);
		
		OnFocusChangeLis focusChangeListener = new OnFocusChangeLis();
		OnFocusChangeBig focusChangeBig = new OnFocusChangeBig();
		button1_1.setOnFocusChangeListener(focusChangeBig);
		button1_2.setOnFocusChangeListener(focusChangeListener);
		button1_3.setOnFocusChangeListener(focusChangeListener);
		button1_51.setOnFocusChangeListener(focusChangeListener);
		button1_52.setOnFocusChangeListener(focusChangeListener);
		button1_4.setOnFocusChangeListener(focusChangeListener);
		button1_5.setOnFocusChangeListener(focusChangeListener);
		button1_6.setOnFocusChangeListener(focusChangeListener);
		button1_7.setOnFocusChangeListener(focusChangeListener);
		button1_8.setOnFocusChangeListener(focusChangeListener);
		button1_9.setOnFocusChangeListener(focusChangeListener);
		button1_10.setOnFocusChangeListener(focusChangeListener);
		
		button1_1.setOnClickListener(this);
		button1_2.setOnClickListener(this);
		button1_3.setOnClickListener(this);
		button1_51.setOnClickListener(this);
		button1_52.setOnClickListener(this);
		button1_4.setOnClickListener(this);
		button1_5.setOnClickListener(this);
		button1_6.setOnClickListener(this);
		button1_7.setOnClickListener(this);
		button1_8.setOnClickListener(this);
		button1_9.setOnClickListener(this);
		button1_10.setOnClickListener(this);
		
		aVPlayerManager = AVPlayerManager.getInstance();
		proManager = ProgManager.getInstance();
		if(null !=  aVPlayerManager.getCurrProgShowInfoByMap())
		{
			curr_prog_num = aVPlayerManager.getCurrProgShowInfoByMap().get("PROG_NUM");
		}else{
			curr_prog_num = null;
		}
	}
	
	@Override
	public void onClick(View v) {
		String numId = null;
		if(null !=  aVPlayerManager.getCurrProgShowInfoByMap())
		{
			curr_prog_num = aVPlayerManager.getCurrProgShowInfoByMap().get("PROG_NUM");
		}else{
			curr_prog_num = null;
		}
		if(curr_prog_num!=null){
		switch (v.getId()) {
		case R.id.button1_1:
			gotoOutPakage(curr_prog_num);
			break;
		case R.id.button1_2:   //CCTV-1
			 numId = proManager.getProgNumByServiceid(4001);
			if(numId!=null){
			gotoOutPakage(numId);
			}
			break;
		case R.id.button1_3:  //CCTV-3
			// numId = proManager.getProgNumByServiceid(78);
			/*if(numId!=null){
			gotoOutPakage(numId);
			}*/
			break;
		case R.id.button1_4:	//CCTV-5
			numId = proManager.getProgNumByServiceid(4101);
			if(numId!=null){
			gotoOutPakage(numId);
			}
			break;
		case R.id.button1_5:	//CCTV-6
			//numId = proManager.getProgNumByServiceid(4001);
			/*if(numId!=null){
			gotoOutPakage(numId);
			}*/
			break;
		case R.id.button1_6:	//CCTV-9
			numId = proManager.getProgNumByServiceid(4102);
			if(numId!=null){
			gotoOutPakage(numId);
			}
			break;
		case R.id.button1_7:	//北京高清
			numId = proManager.getProgNumByServiceid(4201);
			if(numId!=null){
			gotoOutPakage(numId);
			}
			break;
		case R.id.button1_8:	//浙江高清
			numId = proManager.getProgNumByServiceid(4601);
			if(numId!=null){
			gotoOutPakage(numId);
			}
			break;
		case R.id.button1_9:	//江苏高清
			numId = proManager.getProgNumByServiceid(4501);
			if(numId!=null){
			gotoOutPakage(numId);
			}
			break;
		case R.id.button1_10:	//湖南高清
			numId = proManager.getProgNumByServiceid(4401);
			if(numId!=null){
			gotoOutPakage(numId);
			}
			break;
		default:
			break;
		}
		}
	}
	private void gotoOutPakage(String proNum) {
		ComponentName comp = new ComponentName("android.test.ui", "android.test.ui.Topmost");
		Intent intent = new Intent();
		intent.putExtra("num",proNum);
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
			intent.putExtra("page", 1);
			intent.setAction("com.konka.launcher.PAGE_VISIBILITY");
			getActivity().sendBroadcast(intent);
		}
	}
}
