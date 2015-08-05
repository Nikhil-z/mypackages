package android.test.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaDetitleChkNums;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.test.ui.CaErrorType.RETURN_CODE;
import android.test.comm.KeyChange;
import android.test.util.Tools;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
//import com.mstar.tvsettingservice.TvDeskProvider;

public class CaDetitleInfoActivity extends Activity {
	private String TAG = "CaDetitleInfoActivity";
	private String OperatorId;
	private ListView listdetitle;
	//private TextView textview_ca_detitle_item_state_Head;
	private String CURRNT_OPERATOR_ID = "OperatorId";
	private CaDeskManager caDeskImpl = null;
	private List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
	private CaDetitleChkNums DetitleChkNums;
	private SimpleAdapter adapter;

	/*
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == LittleDownTimer.TIME_OUT_MSG) {
				CaDetitleInfoActivity.this.finish();
				Intent intent = new Intent(CaDetitleInfoActivity.this,
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
		setContentView(R.layout.ca_detitle_info);
		//LittleDownTimer.setHandler(handler);
		if ((getIntent() != null) && (getIntent().getExtras() != null)) {
			OperatorId = getIntent().getStringExtra(CURRNT_OPERATOR_ID);
			Log.d(TAG, "get String extra,OperatorId=" + OperatorId);
		}
		findViews();

		System.out
				.println("listdetitle.hasFocus()===" + listdetitle.hasFocus());
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
        Log.d("KonkaCaSystem", "detitleinfo onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","detitleinfo onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }

	@Override
	public void onUserInteraction() {
		Log.d("KonkaCaSystem", "CaDetitleInfoActivity onUserInteraction");
		//LittleDownTimer.resetMenu();
		super.onUserInteraction();
	}

	private void findViews() {
		listdetitle = (ListView) findViewById(R.id.listdetitle);
		//textview_ca_detitle_item_state_Head = (TextView) findViewById(R.id.textview_ca_detitle_item_state_Head);

		contents.clear();
		adapter = new SimpleAdapter(this, (List<Map<String, Object>>) contents,
				R.layout.ca_detitle_item, new String[] { "detitle_id","detitle_status" },
				new int[] { R.id.textview_ca_detitle_item_list,R.id.textview_ca_detitle_item_status });

		getInitData();
		listdetitle.setAdapter(adapter);
	}

	private void getInitData() {

		caDeskImpl = CaDeskManager.getCaMgrInstance();
		contents.clear();
		try {
				DetitleChkNums = caDeskImpl.CaGetDetitleChkNums(Short.parseShort(OperatorId));
				if (DetitleChkNums == null) {
					return;
			}

			short aa = DetitleChkNums.sDetitleChkNumsState;
			int bb = RETURN_CODE.ST_CA_RC_DATA_NOT_FIND.getRetCode();
			if (DetitleChkNums.sDetitleChkNumsState == RETURN_CODE.ST_CA_RC_OK.getRetCode()) {
				for (int i = 0; i < DetitleChkNums.pdwEntitleIds.length; i++) {
					if (DetitleChkNums.pdwEntitleIds[i] != 0) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("detitle_id", DetitleChkNums.pdwEntitleIds[i]);
						
						if (DetitleChkNums.bReadFlag) {
							//textview_ca_detitle_item_state_Head.setText(R.string.str_ca_detitle_state_yes);
							map.put("detitle_status", getString(R.string.str_ca_detitle_state_yes));
						} else {
							//textview_ca_detitle_item_state_Head.setText(R.string.str_ca_detitle_state_no);
							map.put("detitle_status", getString(R.string.str_ca_detitle_state_no));
						}
						
						contents.add(map);
					}
				}


			} else if (DetitleChkNums.sDetitleChkNumsState == RETURN_CODE.ST_CA_RC_CARD_INVALID.getRetCode()) {
				toastShow(R.string.st_ca_rc_card_invalid);
			} else if (DetitleChkNums.sDetitleChkNumsState == RETURN_CODE.ST_CA_RC_POINTER_INVALID.getRetCode()) {
				toastShow(R.string.st_ca_rc_pointer_invalid);
			} else if (DetitleChkNums.sDetitleChkNumsState == RETURN_CODE.ST_CA_RC_DATA_NOT_FIND.getRetCode()) {
				toastShow(R.string.st_ca_rc_data_not_find);
			}
		} catch (CaCommonException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_PROG_RED) {
			System.out.println(DetitleChkNums.pdwEntitleIds[0]
					+ "--------------" + listdetitle.hasFocus());
			if (DetitleChkNums.pdwEntitleIds[0] != 0 && listdetitle.hasFocus()) {

				new AlertDialog.Builder(this)
						.setTitle(R.string.really_remove_this_info)
						.setNegativeButton(android.R.string.cancel, null)
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										try {
											boolean state=caDeskImpl.CaDelDetitleChkNum(
													Short.parseShort(OperatorId),
													Integer.parseInt( contents
															.get(listdetitle
																	.getSelectedItemPosition())
															.get("detitle_id")+""));
											if(state){
												Tools.toastShow(R.string.delete_successed, CaDetitleInfoActivity.this);
											}else{
												Tools.toastShow(R.string.delete_failed, CaDetitleInfoActivity.this);
											}
										} catch (CaCommonException e) {
											e.printStackTrace();
										}
										getInitData();
										adapter.notifyDataSetChanged();
									}
								}).create().show();
			}
		}else if(keyCode == KeyEvent.KEYCODE_PROG_GREEN){
			if (DetitleChkNums.pdwEntitleIds[0] != 0 ) {

				new AlertDialog.Builder(this)
						.setTitle(R.string.really_remove_all_info)
						.setNegativeButton(android.R.string.cancel, null)
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										try {
											boolean state=caDeskImpl.CaDelDetitleChkNum(
													Short.parseShort(OperatorId),
													0);
											if(state){
												Tools.toastShow(R.string.delete_successed, CaDetitleInfoActivity.this);
											}else{
												Tools.toastShow(R.string.delete_failed, CaDetitleInfoActivity.this);
											}
										} catch (CaCommonException e) {
											e.printStackTrace();
										}
										getInitData();
										adapter.notifyDataSetChanged();
									}
								}).create().show();
			}
		}
		keyCode = KeyChange.getInstance().translateIRKey(keyCode);
		Intent intent = new Intent();

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			intent.setClass(CaDetitleInfoActivity.this,
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
	
	private void toastShow(int resId) {
		Toast toast = new Toast(this);
		TextView MsgShow = new TextView(this);
		toast.setDuration(Toast.LENGTH_LONG);
		MsgShow.setTextColor(Color.RED);
		MsgShow.setTextSize(25);
		MsgShow.setText(resId);
		toast.setView(MsgShow);
		toast.show();
	}

}
