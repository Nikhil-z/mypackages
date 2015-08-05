package android.test.ui;

import java.util.HashMap;
import java.util.Map;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaACListInfo;
import android.com.konka.dvb.casys.data.CaCardSNInfo;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.test.comm.KeyChange;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class CaACListInfoActivity extends Activity {
	private String CURRNT_OPERATOR_ID = "OperatorId";
	private String OperatorId;
	private CaDeskManager caDeskImpl = null;
	private String TAG = "CaACListInfoActivity";
	private TextView textview_ca_aclist_info_areacode_id = null;
	private TextView textview_ca_aclist_info_bouquetid = null;
	private TextView textview_ca_aclist_info_aclist[] = new TextView[6];

	
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
		setContentView(R.layout.ca_card_aclist_info);
		//LittleDownTimer.setHandler(handler);
		
	
		if ((getIntent() != null) && (getIntent().getExtras() != null)) {
			OperatorId = getIntent().getStringExtra(CURRNT_OPERATOR_ID);
			Log.d(TAG, "get String extra,OperatorId=" + OperatorId);
		}
			    
	    findViews();

	}
    @Override
    protected void onResume()
    {
    	Log.d("KonkaCaSystem", "CaVersionInfoActivity onResume");
        //LittleDownTimer.resumeMenu();
    	DVB.GetInstance().SetDVBStatus(1);
		getInitData();
        super.onResume();
    }
    

    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "ca aclist onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","ca aclist onPause");
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
		textview_ca_aclist_info_areacode_id = (TextView) findViewById(R.id.textview_ca_aclist_info_areacode_id);
		textview_ca_aclist_info_bouquetid = (TextView) findViewById(R.id.textview_ca_aclist_info_bouquetid);
		textview_ca_aclist_info_aclist[0] = (TextView) findViewById(R.id.textview_ca_aclist_info_aclist1);
		textview_ca_aclist_info_aclist[1] = (TextView) findViewById(R.id.textview_ca_aclist_info_aclist2);
		textview_ca_aclist_info_aclist[2] = (TextView) findViewById(R.id.textview_ca_aclist_info_aclist3);
		textview_ca_aclist_info_aclist[3] = (TextView) findViewById(R.id.textview_ca_aclist_info_aclist4);
		textview_ca_aclist_info_aclist[4] = (TextView) findViewById(R.id.textview_ca_aclist_info_aclist5);
		textview_ca_aclist_info_aclist[5] = (TextView) findViewById(R.id.textview_ca_aclist_info_aclist6);
	}
	
	private void getInitData()
	{
		caDeskImpl = CaDeskManager.getCaMgrInstance();
		try
		{
			
			CaACListInfo acListInfo=caDeskImpl.CaGetACList(Short.parseShort(OperatorId));
			
			if(acListInfo!=null&&acListInfo.sACListInfoState==0)
			{
				textview_ca_aclist_info_areacode_id.setText(Integer.toHexString(acListInfo.pACArray[0]));
				textview_ca_aclist_info_bouquetid.setText(Integer.toHexString(acListInfo.pACArray[1]));
	
				if(acListInfo.pACArray.length == 18)
				{
					textview_ca_aclist_info_aclist[0].setText(Integer.toHexString(acListInfo.pACArray[4]));
					textview_ca_aclist_info_aclist[1].setText(Integer.toHexString(acListInfo.pACArray[5]));
					textview_ca_aclist_info_aclist[2].setText(Integer.toHexString(acListInfo.pACArray[6]));
					textview_ca_aclist_info_aclist[3].setText(Integer.toHexString(acListInfo.pACArray[7]));
					textview_ca_aclist_info_aclist[4].setText(Integer.toHexString(acListInfo.pACArray[8]));
					textview_ca_aclist_info_aclist[5].setText(Integer.toHexString(acListInfo.pACArray[9]));
				}
			}
			
		
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
			intent.setClass(CaACListInfoActivity.this, CaInformationActivity.class);
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

