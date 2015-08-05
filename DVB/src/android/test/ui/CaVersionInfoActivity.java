package android.test.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaCardSNInfo;
import android.com.konka.dvb.casys.data.CaSTBIDInfo;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.test.comm.KeyChange;

public class CaVersionInfoActivity extends Activity {
	private TextView textview_ca_version_id = null;
	private TextView textview_ca_version_stb_num = null;
	private TextView textview_ca_card_id = null;
	private TextView textview_str_ca_platform_id = null;
	private CaDeskManager caDesk = null;
	private String cardSN="";
	
	/*
	protected Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == LittleDownTimer.TIME_OUT_MSG)
			{
				CaVersionInfoActivity.this.finish();
				Intent intent = new Intent(CaVersionInfoActivity.this,
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
		setContentView(R.layout.ca_version_info);
		//LittleDownTimer.setHandler(handler);
			    
	    caDesk = CaDeskManager.getCaMgrInstance();
	    
	    findViews();
		getInitData();
	}
    @Override
    protected void onResume()
    {
    	Log.d("KonkaCaSystem", "CaVersionInfoActivity onResume");
        //LittleDownTimer.resumeMenu();
    	DVB.GetInstance().SetDVBStatus(1);
        super.onResume();
    }
    
    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "CaVersionInfoActivity onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","caversion onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }
    
    
	@Override
	public void onUserInteraction()
	{
		Log.d("KonkaCaSystem", "CaVersionInfoActivity onUserInteraction");
		//LittleDownTimer.resetMenu();
		super.onUserInteraction();
	}
	
	private void findViews()
	{
		textview_str_ca_platform_id = (TextView)findViewById(R.id.textview_str_ca_platform_id);
		textview_ca_version_id = (TextView) findViewById(R.id.textview_ca_version_id);
		textview_ca_version_stb_num = (TextView) findViewById(R.id.textview_ca_version_stb_num);
		textview_ca_card_id = (TextView) findViewById(R.id.textview_ca_card_id);
	}
	
	private void getInitData()
	{
		try
		{
			CaCardSNInfo snInfo=caDesk.CaGetCardSN();
			if(snInfo!=null&&snInfo.sCardState==0)
			{
				cardSN=snInfo.pcardsn;
			}
			
			int caVer=caDesk.CaGetVer();
			
			CaSTBIDInfo caSTBIDInfo = caDesk.CaGetSTBID();
			if(caSTBIDInfo!=null&&caSTBIDInfo.sState==0)
			{
				Log.d("CAVerSion", "STBID wPlatformID is " + caSTBIDInfo.wPlatformID + "dwUniqueID" + caSTBIDInfo.dwUniqueID);
				textview_str_ca_platform_id.setText("0x"+String.format("%04x",caSTBIDInfo.wPlatformID).toUpperCase());
				textview_ca_version_stb_num.setText("0x"+String.format("%04x",caSTBIDInfo.wPlatformID).toUpperCase()+String.format("%08x",caSTBIDInfo.dwUniqueID));
			}
	   
			textview_ca_version_id.setText("0x"+Integer.toHexString(caVer).toUpperCase());
			textview_ca_card_id.setText(cardSN);
		}
		catch(CaCommonException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		keyCode = KeyChange.getInstance().translateIRKey(keyCode);
		Intent intent = new Intent();

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			intent.setClass(CaVersionInfoActivity.this, CaActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}


