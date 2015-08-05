package android.test.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaSlotIDs;
import android.com.konka.dvb.casys.data.CaSlotInfo;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CaPurseInfoActivity extends Activity {
	private String TAG = "CaPurseInfoActivity";
	private String OperatorId;
	private ListView listpurse;
	private String CURRNT_OPERATOR_ID = "OperatorId";
	CaDeskManager caDesk=null;
	int ST_CA_MAXNUM_SLOT=20;
	private List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
	
	/*
	protected Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == LittleDownTimer.TIME_OUT_MSG)
			{
				CaPurseInfoActivity.this.finish();
				Intent intent = new Intent(CaPurseInfoActivity.this,
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
		setContentView(R.layout.ca_purse_info);
		if ((getIntent() != null) && (getIntent().getExtras() != null)) {
			OperatorId = getIntent().getStringExtra(CURRNT_OPERATOR_ID);
			Log.d(TAG, "get String extra,OperatorId=" + OperatorId);
		}
		//LittleDownTimer.setHandler(handler);
		findViews();
	}
	
    @Override
    protected void onResume()
    {
    	Log.d("KonkaCaSystem", "CaPurseInfoActivity onResume");
        //LittleDownTimer.resumeMenu();
    	DVB.GetInstance().SetDVBStatus(1);
        super.onResume();
    }
    
    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "CaPurseInfoActivity onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","CaPurseInfoActivity onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }
    
	@Override
	public void onUserInteraction()
	{
		Log.d("KonkaCaSystem", "CaPurseInfoActivity onUserInteraction");
		//LittleDownTimer.resetMenu();
		super.onUserInteraction();
	}

	private void findViews() {
		listpurse = (ListView) findViewById(R.id.listpurse);

		contents.clear();
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents, R.layout.ca_purse_item,
				new String[] {"operatorid", "purse_id", "spend_points", "credit" },
				new int[] { R.id.textview_ca_purse_item_operatorid,
						R.id.textview_ca_purse_item_id,
						R.id.textview_ca_purse_item_points,
						R.id.textview_ca_purse_item_credit });

		getInitData();	
		listpurse.setAdapter(adapter);
	}
	
	private void getInitData()
	{
		/*
		TVRootApp app = (TVRootApp) getApplication();
	    TvDeskProvider serviceProvider = (TvDeskProvider) app
	            .getTvDeskProvider();
	    */
	    caDesk = CaDeskManager.getCaMgrInstance();
	    try
	    {
	    CaSlotIDs SlotIDs=caDesk.CaGetSlotIDs(Short.parseShort(OperatorId));
		
		if(SlotIDs==null)
		{
			return;
		}
		if(SlotIDs.sSlotIDsState==RETURN_CODE.ST_CA_RC_OK.getRetCode())
		{
			if(SlotIDs.pbySlotID.length()>0)
			{
			    int slotId[] = new int[ST_CA_MAXNUM_SLOT];	    
			    int len=SlotIDs.pbySlotID.length();
			    for(int i = 0;i<len;i++)
				{
			    	if(SlotIDs.pbySlotID.codePointAt(i)==0)//CA spec:if SlotId=0,need break
			    	{
			    		break;
			    	}
			    	slotId[i] =	SlotIDs.pbySlotID.codePointAt(i);
			    	Log.d(TAG, "sSlotIDsState :"+slotId[i]);
			    	
					CaSlotInfo SlotInfo=caDesk.CaGetSlotInfo(Short.parseShort(OperatorId),(short)slotId[i]);
					if(SlotInfo==null||SlotInfo.sSlotInfoState!=RETURN_CODE.ST_CA_RC_OK.getRetCode())
					{
						continue;
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("operatorid", OperatorId);
					map.put("purse_id", slotId[i]);
					map.put("spend_points", SlotInfo.wBalance);
					map.put("credit", SlotInfo.wCreditLimit);
					contents.add(map);
				}
			}
		}
		else if(SlotIDs.sSlotIDsState==RETURN_CODE.ST_CA_RC_CARD_INVALID.getRetCode())
		{
			toastShow(R.string.st_ca_rc_card_invalid);
		}
		else if(SlotIDs.sSlotIDsState==RETURN_CODE.ST_CA_RC_POINTER_INVALID.getRetCode())
		{
			toastShow(R.string.st_ca_rc_pointer_invalid);
		}
		 else if(SlotIDs.sSlotIDsState==RETURN_CODE.ST_CA_RC_DATA_NOT_FIND.getRetCode())
		{
			 toastShow(R.string.st_ca_rc_data_not_find);
		}
	    }
        catch (CaCommonException e)
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
			intent.setClass(CaPurseInfoActivity.this,
					CaInformationActivity.class);
			intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void toastShow(int resId)
	{
		Toast toast = new Toast(this);
    	TextView MsgShow=new TextView(this);
    	toast.setDuration(Toast.LENGTH_LONG);
    	MsgShow.setTextColor(Color.RED);
    	MsgShow.setTextSize(25);
    	MsgShow.setText(resId);
    	toast.setView(MsgShow);
    	toast.show();
	}
}
