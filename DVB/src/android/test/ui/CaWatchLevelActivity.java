package android.test.ui;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaRatingInfo;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.test.ui.CaErrorType.RETURN_CODE;
import android.test.comm.KeyChange;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CaWatchLevelActivity extends Activity {
	protected int level_index = 0;
	protected Intent intent = new Intent();
	protected LinearLayout linearlayout_watch_level;
	protected TextView textview_watch_level = null;
	private EditText editText_ca_watch_level_pin;
	protected Button btnSubmit;
	protected CaDeskManager caDesk=null;

	protected String[] levels = { "4", "5", "6", "7", "8", "9", "10", "11",
			"12", "13", "14", "15", "16", "17", "18", };
	
	/*
	protected Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			super.handleMessage(msg);
			if(editText_ca_watch_level_pin.hasFocus())
			{
				LittleDownTimer.resetMenu();
				return;
			}
			if (msg.what == LittleDownTimer.TIME_OUT_MSG)
			{
				CaWatchLevelActivity.this.finish();
				Intent intent = new Intent(CaWatchLevelActivity.this,
				        RootActivity.class);
				startActivity(intent);
				finish();
			}
		};
	};
	*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ca_info_watch_level);
		//LittleDownTimer.setHandler(handler);

		/*
		TVRootApp app = (TVRootApp) getApplication();
	    TvDeskProvider serviceProvider = (TvDeskProvider) app
	            .getTvDeskProvider();
	    */
	    caDesk = CaDeskManager.getCaMgrInstance();
		findViews();
		getInitData();
	}
	
    @Override
    protected void onResume()
    {
    	Log.d("KonkaCaSystem", "CaWatchLevelActivity onResume");
        //LittleDownTimer.resumeMenu();
    	DVB.GetInstance().SetDVBStatus(1);
        super.onResume();
    }
    
    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "CaWatchLevelActivity onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","cawatchlevel onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }
    
    
	@Override
	public void onUserInteraction()
	{
		Log.d("KonkaCaSystem", "CaWatchLevelActivity onUserInteraction");
		//LittleDownTimer.resetMenu();
		super.onUserInteraction();
	}

	private void findViews() {
		linearlayout_watch_level = (LinearLayout) findViewById(R.id.linearlayout_watch_level);
		editText_ca_watch_level_pin = (EditText) findViewById(R.id.editText_ca_watch_level_pin);
		textview_watch_level = (TextView) findViewById(R.id.textview_watch_level);
		textview_watch_level.setText(levels[level_index]);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		setOnClickLisenters();
		setOnFocusChangeListeners();
	}
	
	private void getInitData()
	{
		try
		{
			CaRatingInfo ratinginfo=caDesk.CaGetRating();
			if(ratinginfo==null)
			{
				return;
			}
			if(ratinginfo.sRatingState==RETURN_CODE.ST_CA_RC_OK.getRetCode())
			{
				textview_watch_level.setText(Short.toString(ratinginfo.pbyRating));
				for(int i=0;i<levels.length;i++)
				{
					if(ratinginfo.pbyRating==Short.parseShort(levels[i]))
					{
						level_index=i;
						break;
					}
				}
			}
			else if(ratinginfo.sRatingState==RETURN_CODE.ST_CA_RC_CARD_INVALID.getRetCode())
			{
				toastShow(R.string.st_ca_rc_card_invalid);
			}
			else if(ratinginfo.sRatingState==RETURN_CODE.ST_CA_RC_POINTER_INVALID.getRetCode())
			{
	            toastShow(R.string.st_ca_rc_pointer_invalid);
			}
			else if(ratinginfo.sRatingState==RETURN_CODE.ST_CA_RC_UNKNOWN.getRetCode())
			{
	            toastShow(R.string.st_ca_rc_unknown);
			}
		}
        catch (CaCommonException e)
        {
            e.printStackTrace();
        }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		int currentid = CaWatchLevelActivity.this.getCurrentFocus().getId();
		keyCode = KeyChange.getInstance().translateIRKey(keyCode);

		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			switch (currentid) {
			case R.id.linearlayout_watch_level:
				if (level_index == levels.length - 1) {
					level_index = 0;
				} else {
					level_index++;
				}
				textview_watch_level.setText(levels[level_index]);
				break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			switch (currentid) {
			case R.id.linearlayout_watch_level:
				if (level_index == 0) {
					level_index = levels.length - 1;
				} else {
					level_index--;
				}
				textview_watch_level.setText(levels[level_index]);
				break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			intent.setClass(CaWatchLevelActivity.this,
					CaManagementActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setOnClickLisenters() {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.linearlayout_watch_level:
					linearlayout_watch_level.getChildAt(0).setVisibility(
							View.VISIBLE);
					linearlayout_watch_level.getChildAt(3).setVisibility(
							View.VISIBLE);
					break;
				case R.id.btnSubmit:
					modifyWatchLevel();
					break;
				default:
					break;
				}
			}
		};
		linearlayout_watch_level.setOnClickListener(listener);
		btnSubmit.setOnClickListener(listener);
	}
	
	private void modifyWatchLevel()
	{
		try
		{
			String pin=editText_ca_watch_level_pin.getText().toString();
			if(pin.length()<6)
			{
				toastShow(R.string.st_ca_rc_pin_len);
				return;
			}
			short rating=Short.parseShort(textview_watch_level.getText().toString());
			short ret=caDesk.CaSetRating(pin,rating);
			if(ret==RETURN_CODE.ST_CA_RC_OK.getRetCode())
			{
	            toastShow(R.string.st_ca_rc_ok);
			}
			else if(ret==RETURN_CODE.ST_CA_RC_CARD_INVALID.getRetCode())
			{
	            toastShow(R.string.st_ca_rc_card_invalid);
			}
			else if(ret==RETURN_CODE.ST_CA_RC_POINTER_INVALID.getRetCode())
			{
				toastShow(R.string.st_ca_rc_pointer_invalid);
			}
			else if(ret==RETURN_CODE.ST_CA_RC_PIN_INVALID.getRetCode())
			{
	            toastShow(R.string.st_ca_rc_pin_invalid);
			}
			else if(ret==RETURN_CODE.ST_CA_RC_UNKNOWN.getRetCode())
			{
				toastShow(R.string.st_ca_rc_unknown);
			}
			else if(ret==RETURN_CODE.ST_CA_RC_WATCHRATING_INVALID.getRetCode())
			{
				toastShow(R.string.st_ca_rc_watchrating_invalid);
			}
		}
		catch (CaCommonException e)
        {
            e.printStackTrace();
        }
	}

	private void setOnFocusChangeListeners() {
		OnFocusChangeListener FocuschangesListener = new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				LinearLayout container = (LinearLayout) v;
				container.getChildAt(0).setVisibility(View.GONE);
				container.getChildAt(3).setVisibility(View.GONE);
			}
		};
		linearlayout_watch_level.setOnFocusChangeListener(FocuschangesListener);
	}
	
	private void toastShow(int resId)
	{
		Toast toast = new Toast(this);
    	TextView MsgShow=new TextView(this);
    	toast.setDuration(0);
    	MsgShow.setTextColor(Color.RED);
    	MsgShow.setTextSize(25);
    	MsgShow.setText(resId);
    	toast.setView(MsgShow);
    	toast.show();
	}
}


