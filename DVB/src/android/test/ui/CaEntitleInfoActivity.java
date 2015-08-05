package android.test.ui;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaServiceEntitles;
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


public class CaEntitleInfoActivity extends Activity {
	private String TAG = "CaEntitleInfoActivity";
	private String OperatorId;
	private ListView listentitle;
	private String CURRNT_OPERATOR_ID = "OperatorId";
	private CaDeskManager caDesk=null;
	private List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
	
	/*
	protected Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == LittleDownTimer.TIME_OUT_MSG)
			{
				CaEntitleInfoActivity.this.finish();
				Intent intent = new Intent(CaEntitleInfoActivity.this,
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
		setContentView(R.layout.ca_entitle_info);
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
    	Log.d("KonkaCaSystem", "CaEntitleInfoActivity onResume");
        //LittleDownTimer.resumeMenu();
    	DVB.GetInstance().SetDVBStatus(1);
        super.onResume();
    }
    
    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "CaEntitleInfoActivity onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","CaEntitleInfoActivity onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }
    
	@Override
	public void onUserInteraction()
	{
		Log.d("KonkaCaSystem", "CaEntitleInfoActivity onUserInteraction");
		//LittleDownTimer.resetMenu();
		super.onUserInteraction();
	}

	private void findViews() {
		listentitle = (ListView) findViewById(R.id.listentitle);

		contents.clear();
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents, R.layout.ca_entitle_item,
				new String[] {"operatorid", "id", "video", "date" }, new int[] {
						R.id.textview_ca_entitle_item_operatorid,
						R.id.textview_ca_entitle_item_id,
						R.id.textview_ca_entitle_item_video,
						R.id.textview_ca_entitle_item_date });
		
		getInitData();
		listentitle.setAdapter(adapter);
	}
	
	private void getInitData()
	{
	    caDesk = CaDeskManager.getCaMgrInstance();
	    try
	    {
	    CaServiceEntitles Entitles=caDesk.CaGetServiceEntitles(Short.parseShort(OperatorId));
	    if(Entitles==null)
	    {
	    	Log.d("KonkaCaSystem", "CaEntitleInfoActivity CaGetServiceEntitles null data");
	    	return;
	    }
	    if(Entitles.sEntitlesState==RETURN_CODE.ST_CA_RC_OK.getRetCode())
	    {
	    	for(int i=0;i<Entitles.sProductCount;i++)
	    	{
	    		Map<String, Object> map = new HashMap<String, Object>();
	    		
	    		map.put("operatorid", OperatorId);
				map.put("id", Entitles.cEntitles[i].wwProductID);
				String video_flag_yes=this.getResources().getString(R.string.str_ca_entitle_video_flag_yes);
				String video_flag_no=this.getResources().getString(R.string.str_ca_entitle_video_flag_no);
				if(Entitles.cEntitles[i].m_bCanTape==0)
				{
					map.put("video",video_flag_no);
				}else
				{
					map.put("video", video_flag_yes);
				}
		
				map.put("date", dateConvert(Entitles.cEntitles[i].sExpireDate));
				contents.add(map);
	    	}
	    }
		else if(Entitles.sEntitlesState==RETURN_CODE.ST_CA_RC_CARD_INVALID.getRetCode())
		{
			toastShow(R.string.st_ca_rc_card_invalid);
		}
		else if(Entitles.sEntitlesState==RETURN_CODE.ST_CA_RC_POINTER_INVALID.getRetCode())
		{
			toastShow(R.string.st_ca_rc_pointer_invalid);
		}
	    else if(Entitles.sEntitlesState==RETURN_CODE.ST_CA_RC_DATA_NOT_FIND.getRetCode())
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
			intent.setClass(CaEntitleInfoActivity.this,
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
	
	private String dateConvert(short dayNum)
	{
		 String endDate="";
	     Calendar ca = Calendar.getInstance();
	     ca.set(2000, Calendar.JANUARY, 1);
		 ca.add(Calendar.DATE,dayNum);
		 Format s = new SimpleDateFormat("yyyy-MM-dd");  
		 endDate=s.format(ca.getTime());
		
		return endDate;
	}
}
