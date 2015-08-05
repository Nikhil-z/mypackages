package android.test.ui.epg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.Epg.NewEpgInfo;
import android.com.konka.dvb.SWTimer.SysTime;
import android.com.konka.dvb.av.AVPlayerManager;
import android.com.konka.dvb.epg.EpgManager;
import android.com.konka.dvb.prog.ProgManager;
import android.com.konka.dvb.prog.ProgManager.ProgListShowInfo;
import android.com.konka.dvb.prog.ProgManager.ProgShowInfo;
import android.com.konka.dvb.timer.TimerManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.ui.CaViewSmallWindow;
import android.test.ui.R;
import android.test.ui.SettingActivity_List;
import android.test.ui.Topmost;
import android.test.ui.R.id;
import android.test.ui.R.layout;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EpgWindow extends Activity implements OnItemSelectedListener, OnItemClickListener, OnFocusChangeListener{
	private String TAG = "EpgWindow";
	private Context mContext;
	
	private AVPlayerManager aVPlayerManager;
	private ProgManager mProgManager;
	private EpgManager mEpgManager;
	private TimerManager mTimerManager;
	
	private ListView mProgListView;
	private ProgListAdapter mProgListAdapter;
	private ProgListShowInfo progListShowInfo;
	private ArrayList<ProgShowInfo> pProgList;
	
	private ListView mEpgList;
	private EpgListAdapter mEpgListAdapter;
	private Button[] mDates;
	private int[] datesId = {R.id.date0, R.id.date1, R.id.date2, R.id.date3, R.id.date4, R.id.date5, R.id.date6};
	private int[] datesTextId = {R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday, R.string.sunday};
	private ArrayList<NewEpgInfo> epgInfoList;

	private Drawable leftIcon;
	private int currPro = -1;//�?开始的
	
	private Handler handler;
	private boolean isLongpress = false;
	private long preClick = 0;
	private long currentClick = 0;
	
	/**add by liuhuasheng for small win msg*/
	private CaViewSmallWindow caViewSmallWindow;
	
//	private ArrayList<String> epgDate0;
//	private ArrayList<String> epgDate1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.epgwindow);
		
		mContext = EpgWindow.this;
		
		initView();
	}

	private void initView(){
		mProgListView = (ListView) findViewById(R.id.proglist);
		mProgListAdapter = new ProgListAdapter(mContext);
		mProgListView.setAdapter(mProgListAdapter);
		mProgListView.setOnItemSelectedListener(this);
		mProgListView.setOnItemClickListener(this);
		
		mEpgList = (ListView) findViewById(R.id.epglist);
		mEpgListAdapter = new EpgListAdapter(mContext);
		mEpgList.setAdapter(mEpgListAdapter);
		mEpgList.setOnItemSelectedListener(this);
		mEpgList.setNextFocusLeftId(R.id.proglist);
		mEpgList.setNextFocusUpId(R.id.epglist);

		mDates = new Button[datesId.length];
		for (int i = 0; i < datesId.length; i++) {
			mDates[i] = (Button) findViewById(datesId[i]);
			mDates[i].setOnFocusChangeListener(this);
		}
		
		leftIcon = getResources().getDrawable(R.drawable.dot);
		leftIcon.setBounds(8, 0, 21+8, 21);
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				int what = msg.what;
				Log.v(TAG,"============handler what="+what+" arg="+msg.arg1+" currPro="+currPro);
				switch (what) {
					case 0:
						if(mProgListAdapter == null){
							return;
						}
						
						if(currPro != msg.arg1){//避免重复执行切换
							currPro = msg.arg1;
							mProgListAdapter.setSelect(currPro);
							ProgShowInfo progShowInfo = mProgListAdapter.getItem(msg.arg1);
							if(progShowInfo != null){
								String str = progShowInfo.prog_num;
								
								seletctProg(str);
								
//								setEpgInfo(R.id.date0);
								handler.removeMessages(1);
								Message msg0 = handler.obtainMessage(1);
								msg0.arg1 = R.id.date0;
								handler.sendMessageDelayed(msg0, 200);
							}
						}
						break;
						
					case 1:
						setEpgInfo(msg.arg1);
						break;
						
					default:
						break;
				}
				super.handleMessage(msg);
			}
		};
	}
	
	private void initDate(){
		if(aVPlayerManager == null){
			aVPlayerManager = AVPlayerManager.getInstance();
		}
		startPlay();
		
		if(mTimerManager == null){
			mTimerManager = TimerManager.getInstance();
		}
		SysTime sysTime = mTimerManager.GetSysTime();
		if(sysTime != null && sysTime.Weekday > 0){
			for (int i = 0; i < datesId.length; i++) {
				mDates[i].setText(datesTextId[(i+sysTime.Weekday-1)%datesTextId.length]);
			}
		}
		
		if(mProgManager == null){
			mProgManager = ProgManager.getInstance();
		}
		if(mProgManager != null){
			progListShowInfo = mProgManager.getCurrverntProgListInfo();
			if(progListShowInfo != null){
				pProgList = progListShowInfo.pProgList;
				mProgListAdapter.updateProList(pProgList);
				try {
					int i = Integer.parseInt(progListShowInfo.currPlay_ProgNum);
					currPro = i-1;
				} catch (NumberFormatException e) {
					// TODO: handle exception
					Log.e(TAG,"NumberFormatException e="+e.toString());
				}
			}
		}
		
		if(mEpgManager == null){
			mEpgManager = EpgManager.getInstance();
		}
		
//		epgDate0 = new ArrayList<String>();
//		epgDate1 = new ArrayList<String>();
//		for (int i = 0; i < 10; i++) {
//			epgDate0.add("这个杀手不太冷"+i);
//			if(i < 5){
//				epgDate1.add("国家宝藏"+i);
//			}
//		}
		
		if(currPro != 0){//因为android默认就是选中0，不需要重复选，防止部分焦点被覆�?
			setEpgListLeft(currPro);
		}else{
//			setEpgInfo(R.id.date0);
			handler.removeMessages(1);
			Message msg = handler.obtainMessage(1);
			msg.arg1 = R.id.date0;
			handler.sendMessage(msg);
		}
		
		/**add by liuhuasheng for small win msg*/
		caViewSmallWindow = new CaViewSmallWindow(this);

	}
	
	private void startPlay() {
		aVPlayerManager.startPlayer();
		aVPlayerManager.setPlayerWindow(530, 100, 533, 330);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		DVB.GetInstance().SetDVBStatus(1);
		super.onResume();
		initDate();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(aVPlayerManager != null){
			aVPlayerManager.stopPlayer();
		}
		/**add by liuhuasheng for small win msg*/
		caViewSmallWindow.cancelAllSmallWinShowMsg();
		DVB.GetInstance().ReleaseResource();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		int keyCode = event.getKeyCode();
		int action = event.getAction();
		
		View v = getCurrentFocus();
//		Log.v(TAG, "=============dispatchKeyEvent-->viewid="+v.getId()+" keyCode="+keyCode+" action="+action);
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && action == KeyEvent.ACTION_DOWN){
			//从日期到下面时，仍然显示蓝色小圆�?
			if(v != null && isDateView(v.getId())){
				((Button) v).setCompoundDrawables(leftIcon,null,null,null);
			}
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT && action == KeyEvent.ACTION_DOWN){
			//从当前日期按左到节目列表时，选中之前的台
			if(v != null && (v.getId() == R.id.epglist || v.getId() == R.id.date0)){
				setEpgListLeft(currPro);
				return true;
			}
		}
		
		//节目列表中长按设置isLongpress标志
		if(v != null && v.getId() == R.id.proglist){
			
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				
				currentClick = event.getEventTime();
				long time_difference = currentClick - preClick;
				if (time_difference < 150) {
					isLongpress = true;
				}
				preClick = currentClick;
				
			} else if (event.getAction() == KeyEvent.ACTION_UP) {
				isLongpress = false;
			}
		}
		
		
//		if(v != null && v.getId() == R.id.proglist){
//			
//			if (event.getAction() == KeyEvent.ACTION_DOWN) {
//				if (isLockpress == false) {
//					preClick = event.getEventTime();
//				} else {
//					currentClick = event.getEventTime();
//					long time_difference = currentClick - preClick;
//					if (time_difference < 1500) {
//						Log.v(TAG, "================return key!");
//						return true;//摒弃�?
//					} else {
//						preClick = currentClick;
//					}
//				}
//				isLockpress = true;
//				
//			} else if (event.getAction() == KeyEvent.ACTION_UP) {
//				isLockpress = false;
//			}
//		}
		
		return super.dispatchKeyEvent(event);
	}
	
	private boolean isDateView(int id){
		for (int i = 0; i < datesId.length; i++) {
			if(id == datesId[i]){
				return true;
			}
		}
		return false;
	}

	
	private void seletctProg(String num){
		aVPlayerManager.stopPlayer();
		aVPlayerManager.startPlayerByProgNum(num);
	}
	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(parent != null && parent.getId() == R.id.proglist){
			if(isLongpress){
				handler.removeMessages(0);
				Message msg = handler.obtainMessage(0);
				msg.arg1 = position;
				handler.sendMessageDelayed(msg, 180);
				
			}else{
				handler.removeMessages(0);
				Message msg = handler.obtainMessage(0);
				msg.arg1 = position;
				handler.sendMessage(msg);
			}
			
		}else if(parent != null && parent.getId() == R.id.epglist){
			if(mEpgListAdapter == null){
				return;
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(parent != null && parent.getId() == R.id.proglist){
			if(mProgListAdapter == null){
				return;
			}
			
			currPro = position;
			ProgShowInfo progShowInfo = mProgListAdapter.getItem(position);
			if(progShowInfo != null){
				String str = progShowInfo.prog_num;
				Intent intent = new Intent();
				intent.setClass(EpgWindow.this, Topmost.class);
				intent.putExtra("num", str);
				startActivity(intent);
			}
		}
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
//		Log.v(TAG, "============onFocusChange hasFocus="+hasFocus);
		if(hasFocus){
			setEpgInfo(v.getId());
		}
	}
	
	private void setEpgInfo(int id){
		int index = setEpgIcon(id);
		if(mEpgManager != null){
			epgInfoList = mEpgManager.getCurrverntProgSchInfo(index);
			if(epgInfoList != null){
				mEpgListAdapter.updateEpgList(epgInfoList);
			}
		}
		
		mEpgList.setNextFocusUpId(id);
	}
	
	private int setEpgIcon(int id){
		int index = 0;
		for (int i = 0; i < mDates.length; i++) {
			mDates[i].setCompoundDrawables(null, null, null, null);
		}
		
		switch (id) {
			case R.id.date0:
//				mEpgListAdapter.updateEpgDebugList(epgDate0);
				index = 0;
				break;
			case R.id.date1:
//				mEpgListAdapter.updateEpgDebugList(epgDate1);
				index = 1;
				break;
			case R.id.date2:
//				mEpgListAdapter.updateEpgDebugList(epgDate0);
				index = 2;
				break;
			case R.id.date3:
//				mEpgListAdapter.updateEpgDebugList(epgDate1);
				index = 3;
				break;
			case R.id.date4:
//				mEpgListAdapter.updateEpgDebugList(epgDate0);
				index = 4;
				break;
			case R.id.date5:
//				mEpgListAdapter.updateEpgDebugList(epgDate1);
				index = 5;
				break;
			case R.id.date6:
//				mEpgListAdapter.updateEpgDebugList(epgDate0);
				index = 6;
				break;
			default:
				break;
		}
		mDates[index].setCompoundDrawables(leftIcon,null,null,null);
		return index;
	}
	
	
	private void setEpgListLeft(int curp){
		mProgListView.setSelectionFromTop(curp,10);
		mProgListView.requestFocus();
		
//		setEpgInfo(R.id.date0);
		handler.removeMessages(1);
		Message msg = handler.obtainMessage(1);
		msg.arg1 = R.id.date0;
		handler.sendMessage(msg);
	}
}