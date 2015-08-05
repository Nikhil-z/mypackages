package android.test.ui;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaIPPVProgramInfo;
import android.com.konka.dvb.casys.data.CaIPPVProgramInfos;
import android.com.konka.dvb.casys.data.CaServiceEntitles;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.test.comm.KeyChange;
import android.test.ui.CaErrorType.RETURN_CODE;
import android.test.util.Constant;
import android.test.util.Tools;

public class CaIppvProgramInfo extends Activity {
	private String CURRNT_OPERATOR_ID = "OperatorId";
	private String OperatorId;
	private CaDeskManager caDesk = null;
	private CaIPPVProgramInfos caIPPVProgramInfos;
	private ArrayList<CaIPPVProgramInfo> list = new ArrayList<CaIPPVProgramInfo>();
	private ListView list_ippv_prog_info;
	private List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
	private int currentposition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ca_ippv_program_info);

		if ((getIntent() != null) && (getIntent().getExtras() != null)) {
			OperatorId = getIntent().getStringExtra(CURRNT_OPERATOR_ID);
		}
		findViews();
	}
	
	private void findViews() {
		list_ippv_prog_info = (ListView) findViewById(R.id.list_ippv_prog_info_id);

		contents.clear();
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents, R.layout.ca_ippv_program_item,
				new String[] {"operatorid", "purseid","prog","status", "video","price", "date" }, new int[] {
						R.id.textview_ca_ippv_item_id_operator,
						R.id.textview_ca_ippv_purse_id,
						R.id.textview_ca_ippv_item_prog_id,
						R.id.textview_ca_ippv_item_status,
						R.id.textview_ca_entitle_item_video_status,
						R.id.textview_ca_entitle_item_price,
						R.id.textview_ca_entitle_item_outdate,});
		
		getInitData();
		list_ippv_prog_info.setAdapter(adapter);
	}
	
	private void getInitData()
	{
		caDesk = CaDeskManager.getCaMgrInstance();
		try {
			caIPPVProgramInfos = caDesk.CaGetIPPVProgram(Short.parseShort(OperatorId));
			if(caIPPVProgramInfos.sIPPVInfoState==Constant.CDCA_RC_OK){
				//Tools.toastShow(R.string.ippv_cdca_rc_ok, CaIppvProgramInfo.this);
			}else if(caIPPVProgramInfos.sIPPVInfoState==Constant.CDCA_RC_CARD_INVALID){
				Tools.toastShow(R.string.cdca_rc_invalid, CaIppvProgramInfo.this);
			}else if(caIPPVProgramInfos.sIPPVInfoState==Constant.CDCA_RC_DATA_NOT_FIND){
				Tools.toastShow(R.string.ippv_cdca_rc_data_not_find, CaIppvProgramInfo.this);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (CaCommonException e) {
			e.printStackTrace();
		}
		Log.i("IPPV","num is " + caIPPVProgramInfos.sNumber);
		if (caIPPVProgramInfos.sNumber == 0) 
		{
			//ippv_program_info_linearlayout.setVisibility(View.INVISIBLE);
			Toast.makeText(CaIppvProgramInfo.this,
					getResources().getString(R.string.ippv_data_not_find), 0);
		} else 
		{
			
			for (int i = 0; i < caIPPVProgramInfos.sNumber; i++) 
			{				
				//list.add(caIPPVProgramInfos.IPPVProgramInfo[i]);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("operatorid", OperatorId);
				map.put("purseid", caIPPVProgramInfos.IPPVProgramInfo[i].sSlotID);
				map.put("prog", caIPPVProgramInfos.IPPVProgramInfo[i].wdwProductID);
				
				if(caIPPVProgramInfos.IPPVProgramInfo[i].sBookEdFlag==0x01){
					map.put("status", getResources().getString(R.string.ippv_product_state_booking));
				}else if(caIPPVProgramInfos.IPPVProgramInfo[i].sBookEdFlag==0x03){
					map.put("status", getResources().getString(R.string.ippv_product_state_viewed));
				}
				if(caIPPVProgramInfos.IPPVProgramInfo[i].sCanTape==0){
					map.put("video",getResources().getString(R.string.ippv_can_not_tape));
				}else if(caIPPVProgramInfos.IPPVProgramInfo[i].sCanTape==1){
					map.put("video",getResources().getString(R.string.ippv_can_tape));
					
				}

				map.put("price", caIPPVProgramInfos.IPPVProgramInfo[i].sPrice);
				map.put("date", dateConvert(caIPPVProgramInfos.IPPVProgramInfo[i].sExpiredDate));
				
				contents.add(map);
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
			intent.setClass(CaIppvProgramInfo.this, CaInformationActivity.class);
			intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private String dateConvert(short dayNum) {
		String endDate = "";
		Calendar ca = Calendar.getInstance();
		ca.set(2000, Calendar.JANUARY, 1);
		ca.add(Calendar.DATE, dayNum);
		Format s = new SimpleDateFormat("yyyy-MM-dd");
		endDate = s.format(ca.getTime());

		return endDate;
	}
	
    @Override
	protected void onResume() {
    	Log.d("KonkaCaSystem", "CaManagementActivity onResume");
        //LittleDownTimer.resumeMenu();
    	DVB.GetInstance().SetDVBStatus(1);
        super.onResume();
    }
    

    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "CaManagementActivity onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","ippvprograminfo onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }
}


