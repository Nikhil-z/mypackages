package android.test.ui;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaFeedDataInfo;
import android.com.konka.dvb.casys.data.CaOperatorChildStatus;
import android.com.konka.dvb.casys.data.CaOperatorIds;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.test.ui.CaErrorType.RETURN_CODE;
import android.test.comm.KeyChange;
import android.test.util.Tools;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CaCardFeedActivity extends Activity {
	private CaDeskManager caDeskImpl = null;
	private ListView ca_card_feed_listview;
	private List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
    private static int  CDCAS_OK=0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ca_card_feed);

		ca_card_feed_listview = (ListView) findViewById(R.id.ca_card_feed_listview);
		caDeskImpl = CaDeskManager.getCaMgrInstance();

		getInitData();
		
		SimpleAdapter adapter = new SimpleAdapter(this,(List<Map<String, Object>>) contents,
				R.layout.ca_card_feed_listview_item, 
				new String[] { "operator","isparentcard", "delaytime", "lastfeedtime" },
				new int[] { R.id.ca_card_feed_listviewitem_operator_id,
						R.id.ca_card_feed_listviewitem_child_or_parent,
						R.id.ca_card_feed_listviewitem_delaytime,
						R.id.ca_card_feed_listviewitem_lastfeedtime });
		
		ca_card_feed_listview.setAdapter(adapter);
		ca_card_feed_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						final short wTVSID=(Short) contents.get(position).get("operator");
						if (!getResources().getString(
								R.string.str_ca_mg_parent_card).equals(
								contents.get(position).get("isparentcard"))) {
							new AlertDialog.Builder(CaCardFeedActivity.this)
							.setTitle(R.string.please_insert_parent_card)
							.setNegativeButton(android.R.string.cancel, null)
							.setPositiveButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
										try {
											CaFeedDataInfo caFeedDataInfo= caDeskImpl.CaReadFeedDataFromParent(wTVSID);
											if(caFeedDataInfo.sFeedDataState==CDCAS_OK){
												showInsertChildDialog(wTVSID, caFeedDataInfo);
											}else{
												showpRomptDialog(R.string.read_feeddata_from_parent_failed, null);
											}
										} catch (CaCommonException e) {
											e.printStackTrace();
										}
							
								}
							})
							.create().show();

						}
					}
				});

	}
	
	
	
	private void showInsertChildDialog(final short wTVSID,final CaFeedDataInfo caFeedDataInfo){
		showpRomptDialog(R.string.please_insert_child_card, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				short result;
				try {
					result = caDeskImpl.CaWriteFeedDataToChild(wTVSID, caFeedDataInfo);
					if(result==CDCAS_OK){
						showpRomptDialog(R.string.feeddata_successed,null);
						
					}else{
						showpRomptDialog(R.string.feeddata_failed,null);
					}
				} catch (CaCommonException e) {
					e.printStackTrace();
				}
		
			}
		});
	}

	private void getInitData() {
		try {
			CaOperatorIds OperatorIds = caDeskImpl.CaGetOperatorIds();
			if (OperatorIds == null) {
				return;
			}
			contents.clear();
			Log.d("KonkaCaSystem", "OperatorIds.sOperatorIdState is " + OperatorIds.sOperatorIdState + "length is " + OperatorIds.OperatorId.length);
			if (OperatorIds.sOperatorIdState == RETURN_CODE.ST_CA_RC_OK.getRetCode()) {
				if (OperatorIds.OperatorId.length > 0) {
					for (int i = 0; i < OperatorIds.OperatorId.length; i++) {
						if (OperatorIds.OperatorId[i] == 0)// CA Spec:if
															// OperatorId=0,need
															// continue
						{
							continue;
						}
						
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("operator", OperatorIds.OperatorId[i]);
						
						CaOperatorChildStatus caOperatorChildStatus = caDeskImpl.CaGetOperatorChildStatus(OperatorIds.OperatorId[i]);				
						if (caOperatorChildStatus.sIsChild == 0) {
							map.put("isparentcard",
									getResources().getString(
											R.string.str_ca_mg_parent_card));
							map.put("delaytime", "");
							map.put("lastfeedtime", "");
						} else if (caOperatorChildStatus.sIsChild == 1) {
							map.put("isparentcard",
									getResources().getString(
											R.string.str_ca_mg_child_card));
							map.put("delaytime",
									caOperatorChildStatus.sDelayTime);
							map.put("lastfeedtime",
									dateConvert(caOperatorChildStatus.wLastFeedTime));
						}
						contents.add(map);
					}
				}
			} else if (OperatorIds.sOperatorIdState == RETURN_CODE.ST_CA_RC_CARD_INVALID
					.getRetCode()) {
				Tools.toastShow(R.string.st_ca_rc_card_invalid, this);
			} else if (OperatorIds.sOperatorIdState == RETURN_CODE.ST_CA_RC_POINTER_INVALID
					.getRetCode()) {
				Tools.toastShow(R.string.st_ca_rc_pointer_invalid, this);
			}
		} catch (CaCommonException e) {
			e.printStackTrace();
		}
	}

	private void showpRomptDialog(int resId,DialogInterface.OnClickListener listener){
		new AlertDialog.Builder(CaCardFeedActivity.this)
		.setTitle(resId)
		.setPositiveButton(android.R.string.ok,
				listener)
		.create().show();
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		keyCode = KeyChange.getInstance().translateIRKey(keyCode);

		Intent intent = new Intent();
		Log.d("KonkaCaSystem", "CaInfoModifyPin keyCode" + keyCode);
		Log.d("KonkaCaSystem", "CaInfoModifyPin KeyEvent.KEYCODE_FORWARD_DEL"
				+ KeyEvent.KEYCODE_FORWARD_DEL);
		Log.d("KonkaCaSystem", "CaInfoModifyPin KeyEvent.KEYCODE_DEL"
				+ KeyEvent.KEYCODE_DEL);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			intent.setClass(CaCardFeedActivity.this, CaManagementActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private String dateConvert(int dayNum) {
		String endDate = "";
		Calendar ca = Calendar.getInstance();
		ca.set(2000, Calendar.JANUARY, 1);
		ca.add(Calendar.DATE, (short) (dayNum >> 16));
		Format s = new SimpleDateFormat("yyyy-MM-dd");
		endDate = s.format(ca.getTime());

		return endDate;
	}
	
	@Override
	protected void onResume() {
		Log.d("KonkaCaSystem", "CaDetitleInfoActivity onResume");
		//LittleDownTimer.resumeMenu();
		DVB.GetInstance().SetDVBStatus(1);
		super.onResume();
	}

	

    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "card feed onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","card feed onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }

}
