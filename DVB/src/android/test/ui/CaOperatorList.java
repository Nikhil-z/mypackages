package android.test.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaOperatorIds;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class CaOperatorList extends Activity implements OnItemSelectedListener,
		OnItemClickListener {
	private String TAG = "CaOperatorList";
	private String OperatorId;
	private String CURRNT_OPERATOR_ID = "OperatorId";
	private ListView listOperator;
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
				CaOperatorList.this.finish();
				Intent intent = new Intent(CaOperatorList.this,
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
		setContentView(R.layout.ca_operator_list);
		
		caDesk = CaDeskManager.getCaMgrInstance();
	    //LittleDownTimer.setHandler(handler);
		findViews();
	}
	
    @Override
    protected void onResume()
    {
    	Log.d("KonkaCaSystem", "CaOperatorList onResume");
        //LittleDownTimer.resumeMenu();
    	DVB.GetInstance().SetDVBStatus(1);
        super.onResume();
    }
    
    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "CaOperatorList onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","CaOperatorList onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }


	@Override
	public void onUserInteraction()
	{
		Log.d("KonkaCaSystem", "CaOperatorList onUserInteraction");
		//LittleDownTimer.resetMenu();
		super.onUserInteraction();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HashMap<String, Object> data = (HashMap<String, Object>) parent.getItemAtPosition(position);
		OperatorId =data.get("operator").toString();
		Intent intent = new Intent();
		intent.setClass(CaOperatorList.this, CaInformationActivity.class);
		intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
		startActivity(intent);
		finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		Log.d("nothingselected", "nothing selected");
	}

	private void findViews() {
		if ((getIntent() != null) && (getIntent().getExtras() != null)) {
			OperatorId = getIntent().getStringExtra(CURRNT_OPERATOR_ID);
			Log.d(TAG, "get String extra,OperatorId=" + OperatorId);
		}
		listOperator = (ListView) findViewById(R.id.listOperator);

		contents.clear();
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents,
				R.layout.ca_operator_item, new String[] { "id", "operator" },
				new int[] { R.id.textview_ca_operator_item_id,
						R.id.textview_ca_operator_item_operator_id });

		getInitData();
		listOperator.setAdapter(adapter);
		listOperator.setOnItemClickListener(this);
		listOperator.setOnItemSelectedListener(this);
		if (OperatorId == null || OperatorId == "") {
			return;
		}
		for (int OperatorIdx = 0; OperatorIdx < contents.size(); OperatorIdx++) {
			HashMap<String, Object> data = (HashMap<String, Object>) contents
					.get(OperatorIdx);
			if (OperatorId.equals(data.get("operator").toString())) {
				listOperator.setSelection(OperatorIdx);
				return;
			}
		}
	}
	
	private void getInitData()
	{
	    try
	    {
		CaOperatorIds OperatorIds=caDesk.CaGetOperatorIds();
		if(OperatorIds==null)
		{
			return;
		}
		if(OperatorIds.sOperatorIdState==RETURN_CODE.ST_CA_RC_OK.getRetCode())
		{
			if(OperatorIds.OperatorId.length>0)
			{
				for(int i=0;i<OperatorIds.OperatorId.length;i++)
				{
					if(OperatorIds.OperatorId[i]==0)//CA Spec:if OperatorId=0,need continue
					{
						continue;
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", i+1);
					map.put("operator", OperatorIds.OperatorId[i]);
					contents.add(map);
				}
			}
		}
		else if(OperatorIds.sOperatorIdState==RETURN_CODE.ST_CA_RC_CARD_INVALID.getRetCode())
		{
			toastShow(R.string.st_ca_rc_card_invalid);
		}
		else if(OperatorIds.sOperatorIdState==RETURN_CODE.ST_CA_RC_POINTER_INVALID.getRetCode())
		{
			toastShow(R.string.st_ca_rc_pointer_invalid);
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
			intent.setClass(CaOperatorList.this, CaActivity.class);
			intent.putExtra("currentPage", 2);
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
