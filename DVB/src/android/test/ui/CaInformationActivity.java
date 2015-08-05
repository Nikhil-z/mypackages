package android.test.ui;

import android.test.comm.KeyChange;
import android.app.Activity;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.av.AVPlayerManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class CaInformationActivity extends Activity {
	private String OperatorId;
	private String CURRNT_OPERATOR_ID = "OperatorId";
	private String TAG = "CaInformationActivity";
	protected Button button_ca_entitle_info;
	protected Button button_ca_detitle_info;
	protected Button button_ca_purse_info;
	protected Button button_ca_aclist_info;
	protected Button button_ca_ippv_info;
	private Intent intent = new Intent();
	private AVPlayerManager aVPlayerManager = null;
	/**add by liuhuasheng for small win msg*/
	private CaViewSmallWindow caViewSmallWindow;
	
	/*
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == LittleDownTimer.TIME_OUT_MSG) {
				CaInformationActivity.this.finish();
				Intent intent = new Intent(CaInformationActivity.this,
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
		setContentView(R.layout.ca_card_information);
		if ((getIntent() != null) && (getIntent().getExtras() != null)) {
			OperatorId = getIntent().getStringExtra(CURRNT_OPERATOR_ID);
			Log.d(TAG, "get String extra,OperatorId=" + OperatorId);
		}
		//LittleDownTimer.setHandler(handler);
		findViews();
		setOnClickLisenters();
	}

	@Override
	protected void onResume() {
		Log.d("KonkaCaSystem", "CaInformationActivity onResume");
		//LittleDownTimer.resumeMenu();
		DVB.GetInstance().SetDVBStatus(1);
		aVPlayerManager = AVPlayerManager.getInstance();

		if(null != aVPlayerManager)
		{
			aVPlayerManager.startPlayer();
			aVPlayerManager.setPlayerWindow(545, 110, 450, 380);
		}
		
		/**add by liuhuasheng for small win msg*/
		caViewSmallWindow = new CaViewSmallWindow(this);
		
		super.onResume();
	}

    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "CaInformationActivity onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","ippvprograminfo onPause");
        DVB.GetInstance().ReleaseResource();
		/**add by liuhuasheng for small win msg*/
		caViewSmallWindow.cancelAllSmallWinShowMsg();
        super.onPause();

    }
	
	@Override
	public void onUserInteraction() {
		Log.d("KonkaCaSystem", "CaInformationActivity onUserInteraction");
		//LittleDownTimer.resetMenu();
		super.onUserInteraction();
	}

	private void findViews() {
		button_ca_entitle_info = (Button) findViewById(R.id.button_ca_entitle_info);
		button_ca_detitle_info = (Button) findViewById(R.id.button_ca_detitle_info);
		button_ca_purse_info = (Button) findViewById(R.id.button_ca_purse_info);
		button_ca_aclist_info = (Button) findViewById(R.id.button_ca_aclist_info);
		button_ca_ippv_info = (Button) findViewById(R.id.button_ca_ippv_info);
	}

	private void setOnClickLisenters() {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.button_ca_entitle_info:
					if(null != aVPlayerManager)
					{
						aVPlayerManager.stopPlayer();
					}
					intent.setClass(CaInformationActivity.this,CaEntitleInfoActivity.class);
					intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
					startActivity(intent);
					finish();
					break;
				case R.id.button_ca_detitle_info:
					if(null != aVPlayerManager)
					{
						aVPlayerManager.stopPlayer();
					}
					intent.setClass(CaInformationActivity.this,CaDetitleInfoActivity.class);
					intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
					startActivity(intent);
					finish();
					break;
				case R.id.button_ca_purse_info:
					if(null != aVPlayerManager)
					{
						aVPlayerManager.stopPlayer();
					}
					intent.setClass(CaInformationActivity.this,CaPurseInfoActivity.class);
					intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
					startActivity(intent);
					finish();
					break;
				case R.id.button_ca_ippv_info:
					if(null != aVPlayerManager)
					{
						aVPlayerManager.stopPlayer();
					}
					intent.setClass(CaInformationActivity.this,CaIppvProgramInfo.class);
					intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
					startActivity(intent);
					finish();
					break;

				case R.id.button_ca_aclist_info:
					if(null != aVPlayerManager)
					{
						aVPlayerManager.stopPlayer();
					}
					intent.setClass(CaInformationActivity.this,CaACListInfoActivity.class);
					intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
					startActivity(intent);
					finish();
					break;
				default:
					break;
				}
			}
		};
		button_ca_entitle_info.setOnClickListener(listener);
		button_ca_detitle_info.setOnClickListener(listener);
		button_ca_purse_info.setOnClickListener(listener);
		button_ca_ippv_info.setOnClickListener(listener);
		button_ca_aclist_info.setOnClickListener(listener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		keyCode = KeyChange.getInstance().translateIRKey(keyCode);
		Intent intent = new Intent();

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			if(null != aVPlayerManager)
			{
				aVPlayerManager.stopPlayer();
			}
			intent.setClass(CaInformationActivity.this, CaOperatorList.class);
			if ((getIntent() != null) && (getIntent().getExtras() != null)) {
				OperatorId = getIntent().getStringExtra(CURRNT_OPERATOR_ID);
				Log.d(TAG, "get String extra,OperatorId=" + OperatorId);
			}
			intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}


