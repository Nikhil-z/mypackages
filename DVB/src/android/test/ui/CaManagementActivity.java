package android.test.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.av.AVPlayerManager;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.test.comm.KeyChange;
import android.test.util.Tools;

public class CaManagementActivity extends Activity {
	protected Button button_ca_mg_modify_pin;
	protected Button button_ca_mg_modify_time;
	protected Button button_ca_mg_watch_level;
	protected Button button_card_paired;
	protected Button button_child_parent_card_feed;
	protected Intent intent = new Intent();
	private AVPlayerManager aVPlayerManager = null;
	
	/**add by liuhuasheng for small win msg*/
	private CaViewSmallWindow caViewSmallWindow;
	
	/*
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == LittleDownTimer.TIME_OUT_MSG) {
				CaManagementActivity.this.finish();
				Intent intent = new Intent(CaManagementActivity.this,
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
		setContentView(R.layout.ca_card_management);
		//LittleDownTimer.setHandler(handler);
		aVPlayerManager = AVPlayerManager.getInstance();
		findViews();
		setOnClickLisenters();
	}
	
    @Override
	protected void onResume() {
    	Log.d("KonkaCaSystem", "CaManagementActivity onResume");
        //LittleDownTimer.resumeMenu();
    	DVB.GetInstance().SetDVBStatus(1);

		if(null != aVPlayerManager)
		{
			aVPlayerManager.startPlayer();
			aVPlayerManager.setPlayerWindow(545, 138, 450, 320);
		}
        
		/**add by liuhuasheng for small win msg*/
		caViewSmallWindow = new CaViewSmallWindow(this);
		
        super.onResume();
    }
    
    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "CaManagementActivity onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","CaManagementActivity onPause");
		/**add by liuhuasheng for small win msg*/
		caViewSmallWindow.cancelAllSmallWinShowMsg();
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }

    
	@Override
	public void onUserInteraction() {
		Log.d("KonkaCaSystem", "CaManagementActivity onUserInteraction");
		//LittleDownTimer.resetMenu();
		super.onUserInteraction();
	}

	private void findViews() {
		button_ca_mg_modify_pin = (Button) findViewById(R.id.button_ca_mg_modify_pin);
		button_ca_mg_modify_time = (Button) findViewById(R.id.button_ca_mg_modify_time);
		button_ca_mg_watch_level = (Button) findViewById(R.id.button_ca_mg_watch_level);
		button_card_paired = (Button) findViewById(R.id.button_card_paired);
		button_child_parent_card_feed = (Button) findViewById(R.id.button_child_parent_card_feed);
	}

	private void setOnClickLisenters() {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.button_ca_mg_modify_pin:
					if(null != aVPlayerManager)
					{
						aVPlayerManager.stopPlayer();
					}
					intent.setClass(CaManagementActivity.this,CaInfoModifyPin.class);
					startActivity(intent);
					finish();
					break;
				case R.id.button_ca_mg_modify_time:
					if(null != aVPlayerManager)
					{
						aVPlayerManager.stopPlayer();
					}
					intent.setClass(CaManagementActivity.this,CaWorkTimeActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.button_ca_mg_watch_level:
					if(null != aVPlayerManager)
					{
						aVPlayerManager.stopPlayer();
					}
					intent.setClass(CaManagementActivity.this,CaWatchLevelActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.button_card_paired:
					if(null != aVPlayerManager)
					{
						aVPlayerManager.stopPlayer();
					}
					intent.setClass(CaManagementActivity.this,CaPairedInfoActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.button_child_parent_card_feed:
					if(null != aVPlayerManager)
					{
						aVPlayerManager.stopPlayer();
					}
					intent.setClass(CaManagementActivity.this,CaCardFeedActivity.class);
					startActivity(intent);
					finish();
					break;
				default:
					break;
				}
			}
		};
		button_ca_mg_modify_pin.setOnClickListener(listener);
		button_ca_mg_modify_time.setOnClickListener(listener);
		button_ca_mg_watch_level.setOnClickListener(listener);
		button_card_paired.setOnClickListener(listener);
		button_child_parent_card_feed.setOnClickListener(listener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		keyCode = KeyChange.getInstance().translateIRKey(keyCode);
		Intent intent = new Intent();

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
//			if(null != aVPlayerManager)
//			{
//				aVPlayerManager.stopPlayer();
//			}
			intent.setClass(CaManagementActivity.this, CaActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}


