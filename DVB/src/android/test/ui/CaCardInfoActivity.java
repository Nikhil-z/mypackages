package android.test.ui;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaOperatorChildStatus;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.test.comm.KeyChange;
import android.test.util.Constant;
import android.test.util.Tools;

public class CaCardInfoActivity extends Activity {
	private String CURRNT_OPERATOR_ID = "OperatorId";
	private String OperatorId;
	private CaDeskManager caDeskImpl = null;
	private CaOperatorChildStatus caOperatorChildStatus;
	private TextView ca_card_feed_child_or_parent_textview;
	private TextView ca_card_num_textview2;
	private TextView ca_card_delaytime_textview;
	private TextView ca_card_delaytime_lastfeedtime_textview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ca_card_info);
		findView();
		if ((getIntent() != null) && (getIntent().getExtras() != null)) {
			OperatorId = getIntent().getStringExtra(CURRNT_OPERATOR_ID);
		}
		/*
		TVRootApp app = (TVRootApp) getApplication();
		TvDeskProvider serviceProvider = (TvDeskProvider) app
				.getTvDeskProvider();
		*/
		caDeskImpl = CaDeskManager.getCaMgrInstance();
		try {
			caOperatorChildStatus = caDeskImpl.CaGetOperatorChildStatus(Short.parseShort(OperatorId));
			if(caOperatorChildStatus.sOperatorChildState==Constant.CDCA_RC_POINTER_INVALID){
				Tools.toastShow(R.string.cdca_rc_pointer_invalid, this);
			}else if(caOperatorChildStatus.sOperatorChildState==Constant.CDCA_RC_CARD_INVALID){
				Tools.toastShow(R.string.cdca_rc_invalid, this);
			}else if(caOperatorChildStatus.sOperatorChildState==Constant.CDCA_RC_DATA_NOT_FIND){
				Tools.toastShow(R.string.ippv_cdca_rc_data_not_find, this);
			}
			
		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException");
			e.printStackTrace();
		} catch (CaCommonException e) {
			System.out.println("CaCommonException");
			e.printStackTrace();
		}
		if (caOperatorChildStatus.sIsChild == 0) {
			ca_card_feed_child_or_parent_textview.setText(getResources()
					.getString(R.string.str_ca_mg_parent_card));
		} else if (caOperatorChildStatus.sIsChild == 1) {
			ca_card_feed_child_or_parent_textview.setText(getResources()
					.getString(R.string.str_ca_mg_child_card));
			ca_card_num_textview2.setText(caOperatorChildStatus.pParentCardSN);
			ca_card_delaytime_textview
					.setText(caOperatorChildStatus.sDelayTime+"");
			ca_card_delaytime_lastfeedtime_textview
					.setText(dateConvert(caOperatorChildStatus.wLastFeedTime));
		}
		

	}

	private void findView() {
		ca_card_feed_child_or_parent_textview = (TextView) findViewById(R.id.ca_card_feed_child_or_parent_textview);
		ca_card_num_textview2 = (TextView) findViewById(R.id.ca_card_num_textview2);
		ca_card_delaytime_textview = (TextView) findViewById(R.id.ca_card_delaytime_textview);
		ca_card_delaytime_lastfeedtime_textview = (TextView) findViewById(R.id.ca_card_delaytime_lastfeedtime_textview);
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		keyCode = KeyChange.getInstance().translateIRKey(keyCode);
		Intent intent = new Intent();

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			intent.setClass(CaCardInfoActivity.this,
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
        Log.d("KonkaCaSystem", "cardinfo onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","cardinfo onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }
}
