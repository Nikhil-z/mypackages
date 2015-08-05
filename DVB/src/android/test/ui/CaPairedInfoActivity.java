package android.test.ui;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaCardSNInfo;
import android.com.konka.dvb.casys.data.CaOperatorChildStatus;
import android.com.konka.dvb.casys.data.CaPairedInfo;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.test.ui.CaErrorType.RETURN_CODE;
import android.test.comm.KeyChange;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class CaPairedInfoActivity extends Activity {
	private TextView textview_ca_card_paired_status_txt = null;
	private TextView textview_ca_card_paired_status_stbidlist_info = null;
	private TextView textview_ca_card_paired_stbid[] = new TextView[5];
	private CaDeskManager caDesk = null;
	
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
		setContentView(R.layout.ca_card_paired_info);
		//LittleDownTimer.setHandler(handler);
	    
	    caDesk = CaDeskManager.getCaMgrInstance();
	    
	    findViews();
	    getInitData();
	}
    @Override
    protected void onResume()
    {
    	Log.d("KonkaCaSystem", "CaPairedInfoActivity onResume");
        //LittleDownTimer.resumeMenu();
    	DVB.GetInstance().SetDVBStatus(1);
        super.onResume();
    }
    
    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "CaPairedInfoActivity onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","CaPairedInfoActivity onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }

	@Override
	public void onUserInteraction()
	{
		Log.d("KonkaCaSystem", "CaPairedInfoActivity onUserInteraction");
		//LittleDownTimer.resetMenu();
		super.onUserInteraction();
	}
	
	private void findViews()
	{
		textview_ca_card_paired_status_txt = (TextView) findViewById(R.id.textview_ca_card_paired_status_txt);
		textview_ca_card_paired_status_stbidlist_info = (TextView)findViewById(R.id.textview_ca_card_paired_status_stbidlist_info);
		textview_ca_card_paired_stbid[0] = (TextView) findViewById(R.id.textview_ca_card_paired_stbid_1);
		textview_ca_card_paired_stbid[1] = (TextView) findViewById(R.id.textview_ca_card_paired_stbid_2);
		textview_ca_card_paired_stbid[2] = (TextView) findViewById(R.id.textview_ca_card_paired_stbid_3);
		textview_ca_card_paired_stbid[3] = (TextView) findViewById(R.id.textview_ca_card_paired_stbid_4);
		textview_ca_card_paired_stbid[4] = (TextView) findViewById(R.id.textview_ca_card_paired_stbid_5);
	}
	
	private void getInitData()
	{
		short showstbid = 0;
		short index = 0;
		
		CaPairedInfo caPairedInfo = null;
		
		try {
			caPairedInfo = caDesk.CaIsPaired();
		} catch (CaCommonException e) {
			e.printStackTrace();
		}
		
		if(caPairedInfo==null)
		{
			return;
		}
		
		if(caPairedInfo.sPairedState==RETURN_CODE.ST_CA_RC_OK.getRetCode())
		{
			showstbid = 1;
			textview_ca_card_paired_status_txt.setText(R.string.cdca_rc_ok);
		}else if(caPairedInfo.sPairedState==RETURN_CODE.ST_CA_RC_CARD_INVALID.getRetCode())
		{
			textview_ca_card_paired_status_txt.setText(R.string.cdca_rc_invalid);
		}else if(caPairedInfo.sPairedState==RETURN_CODE.ST_CA_RC_CARD_NOPAIR.getRetCode())
		{
			textview_ca_card_paired_status_txt.setText(R.string.cdca_rc_card_nopair);
			
		}else if(caPairedInfo.sPairedState==RETURN_CODE.ST_CA_RC_CARD_PAIROTHER.getRetCode())
		{
			showstbid = 1;	
			textview_ca_card_paired_status_txt.setText(R.string.cdca_rc_card_pairother);
		}
		
		if(showstbid==1)
		{
			if(0 < caPairedInfo.pSTBIdList.length)
			{
				textview_ca_card_paired_status_stbidlist_info.setText(R.string.cdca_paried_list_info_txt);
			}
			
			for(index = 0; index < caPairedInfo.pSTBIdList.length; index ++)
			{
				textview_ca_card_paired_stbid[index].setText(caPairedInfo.pSTBIdList[index]);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		keyCode = KeyChange.getInstance().translateIRKey(keyCode);
		Intent intent = new Intent();

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			intent.setClass(CaPairedInfoActivity.this, CaManagementActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}

