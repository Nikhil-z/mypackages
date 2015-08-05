package android.test.ui;

import android.app.Activity;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.av.AVPlayerManager;
import android.content.Intent;
import android.os.Bundle;
import android.test.comm.KeyChange;
//import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
//import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CaActivity extends Activity {
	private String TAG = "CaActivity";
	protected Button button_ca_card_version;
	protected Button button_ca_card_info;
	protected Button button_card_management;
	private Intent intent = new Intent();
	private AVPlayerManager aVPlayerManager = null;
	/**add by liuhuasheng for small win msg*/
	private CaViewSmallWindow caViewSmallWindow;
	
	/*
	protected Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == LittleDownTimer.TIME_OUT_MSG)
			{
				CaActivity.this.finish();
				Intent intent = new Intent(CaActivity.this,
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
		setContentView(R.layout.ca_main_menu);
		//LittleDownTimer.setHandler(handler);
		aVPlayerManager = AVPlayerManager.getInstance();
		fingViews();
		setOnClickLisenters();
	}
    @Override
    protected void onResume()
    {
    	Log.d("KonkaCaSystem", "CaActivity onResume");
        //LittleDownTimer.resumeMenu();
    	DVB.GetInstance().SetDVBStatus(1);
		if( null != aVPlayerManager)
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
        Log.d("KonkaCaSystem", "ca activity onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","ca activity onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }
    
	@Override
	public void onUserInteraction()
	{
		Log.d("KonkaCaSystem", "CaActivity onUserInteraction");
		//LittleDownTimer.resetMenu();
		super.onUserInteraction();
	}

	private void fingViews() {
		button_ca_card_version = (Button) findViewById(R.id.button_ca_card_version);
		button_ca_card_info = (Button) findViewById(R.id.button_ca_card_info);
		button_card_management = (Button) findViewById(R.id.button_ca_card_management);
	}

	private void setOnClickLisenters() {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.i(TAG, "view.getId()=" + view.getId() + "\n");
				Log.i(TAG, "R.id.button_ca_card_info="+ R.id.button_ca_card_info + "\n");
				Log.i(TAG, "R.id.button_card_management="+ R.id.button_ca_card_management + "\n");
				switch (view.getId()) {
				case R.id.button_ca_card_version:
					aVPlayerManager.stopPlayer();
					intent.setClass(CaActivity.this,CaVersionInfoActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.button_ca_card_info:
			        aVPlayerManager.stopPlayer();
					intent.setClass(CaActivity.this, CaOperatorList.class);
					startActivity(intent);
					finish();
					break;
				case R.id.button_ca_card_management:
					aVPlayerManager.stopPlayer();
					intent.setClass(CaActivity.this, CaManagementActivity.class);
					startActivity(intent);
					finish();
					break;
				default:
					break;
				}
			}
		};
		button_ca_card_version.setOnClickListener(listener);
		button_ca_card_info.setOnClickListener(listener);
		button_card_management.setOnClickListener(listener);
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		keyCode = KeyChange.getInstance().translateIRKey(keyCode);
		Intent intent = new Intent();

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			intent.setClass(CaActivity.this, SettingActivity.class);
			//intent.putExtra("currentPage", 4);
			startActivity(intent);
			finish();
			break;
		
		case KeyEvent.KEYCODE_PROG_RED:
			/**
			ProgManager pProgManager = ProgManager.getInstance();
			Log.i(TAG, "pProgManager.getProgNumByServiceid(401) =" + pProgManager.getProgNumByServiceid(401) + "\n");

			//getProgNumByServiceid();
			intent.setClass(CaActivity.this, CaEmailListActivity.class);
			intent.putExtra("currentPage", 4);
			startActivity(intent);
			//finish();
			//ProgManager p = ProgManager.getInstance();
			//p.factorySet();
			 * *
			 */
			//ProgManager p = ProgManager.getInstance();
			//p.factorySet();
			break;
			
			
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}

