package android.test.ui;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaStartIPPVBuyDlgInfo;
//import com.mstar.tvsettingservice.TvDeskProvider;
import android.com.konka.dvb.casys.func.CaDeskEventListener.CA_EVENT;

public class CaStartIppvBuyActivity extends Activity {
	private CaStartIPPVBuyDlgInfo StIppvInfo;
	private int pricestate;  
	private MyCaHandler myHandler; 
	//protected TvDeskProvider tvManagerProvider;
	//TVRootApp rootapp = null;
	
	class MyCaHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			int what = msg.what;
			if (what == CA_EVENT.EV_CA_HIDE_IPPV_DLG.ordinal())
			{
				CaStartIppvBuyActivity.this.finish();
			}
		

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ca_ippv_buy_dialog);
		
		//rootapp = (TVRootApp) getApplication();
		myHandler = new MyCaHandler();
		//tvManagerProvider = rootapp.getTvDeskProvider();
		//tvManagerProvider.getCommonManagerInstance().setHandler(myHandler, 4);
		StIppvInfo = (CaStartIPPVBuyDlgInfo) getIntent().getSerializableExtra(
				"StIppvInfo");
		TextView ippv_buy_dialog_segment = (TextView) findViewById(R.id.ippv_buy_dialog_segment);
		TextView ippv_buy_dialog_tvsid = (TextView) findViewById(R.id.ippv_buy_dialog_tvsid);
		TextView ippv_buy_dialog_productid = (TextView) findViewById(R.id.ippv_buy_dialog_productid);
		TextView ippv_buy_dialog_slotid = (TextView) findViewById(R.id.ippv_buy_dialog_slotid);
		TextView ippv_buy_dialog_expireddate = (TextView) findViewById(R.id.ippv_buy_dialog_expireddate);
		final TextView ippv_buy_dialog_price = (TextView) findViewById(R.id.ippv_buy_dialog_price);
		Button confirm_button = (Button) findViewById(R.id.ippv_buy_dialog_confirm);
		Button cancel_button = (Button) findViewById(R.id.ippv_buy_dialog_cancel);
		confirm_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(CaStartIppvBuyActivity.this, CaIppvPinActivity.class);
				Bundle bundle=new Bundle();
				if(pricestate==1){
					bundle.putShort("price", StIppvInfo.m_Price[1].m_wPrice);
					bundle.putShort("pricecode", StIppvInfo.m_Price[1].m_byPriceCode);
				}else if(pricestate==0){
					bundle.putShort("price", StIppvInfo.m_Price[0].m_wPrice);
					bundle.putShort("pricecode", StIppvInfo.m_Price[0].m_byPriceCode);
				}else{
					return;
				}
				bundle.putShort("ecmpid", StIppvInfo.getwEcmPid());
				bundle.putShort("messagetype", StIppvInfo.getWyMessageType());
				intent.putExtras(bundle);
				CaStartIppvBuyActivity.this.startActivity(intent);
				finish();

			}
		});
		cancel_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		switch (StIppvInfo.wyMessageType) {
		case 0:
			ippv_buy_dialog_segment.setText(getResources().getString(
					R.string.ippv_freeviewed_segment));
			ippv_buy_dialog_expireddate.setText(getResources().getString(
					R.string.ippv_payviewed_expireddate)
					+ dateConvert(StIppvInfo.wExpiredDate));
			break;
		case 1:
			ippv_buy_dialog_segment.setText(getResources().getString(
					R.string.ippv_payviewed_segment));
			ippv_buy_dialog_expireddate.setText(getResources().getString(
					R.string.ippv_payviewed_expireddate)
					+ dateConvert(StIppvInfo.wExpiredDate));
			break;
		case 2:
			ippv_buy_dialog_segment.setText(getResources().getString(
					R.string.ippt_payviewed_segment));
			ippv_buy_dialog_expireddate.setText(getResources().getString(
					R.string.ippt_interval_min)
					+ StIppvInfo.wExpiredDate+getResources().getString(
							R.string.str_work_time_minute_text));
			break;

		}

		ippv_buy_dialog_tvsid.setText(getResources().getString(
				R.string.ippv_tvsid)
				+ StIppvInfo.wTvsID);
		ippv_buy_dialog_productid.setText(getResources().getString(
				R.string.ippv_payviewed_productid)
				+ StIppvInfo.dwProductID);
		ippv_buy_dialog_slotid.setText(getResources().getString(
				R.string.ippv_payviewed_slotid)
				+ StIppvInfo.wySlotID);

		int pricecode = StIppvInfo.m_Price[1].m_byPriceCode;
		if (pricecode == 1) {
			pricestate = 1;
			ippv_buy_dialog_price.setText(getResources().getString(
					R.string.ippv_payviewed_cantape)
					+ StIppvInfo.m_Price[1].m_wPrice);
		} else if (pricecode == 0) {
			pricestate = 0;
			ippv_buy_dialog_price.setText(getResources().getString(
					R.string.ippv_payviewed_cannottape)
					+ StIppvInfo.m_Price[0].m_wPrice);
		}

    
		ippv_buy_dialog_price
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							ippv_buy_dialog_price
									.setBackgroundResource(R.drawable.programme_epg_img_focus);
						} else {
							ippv_buy_dialog_price
									.setBackgroundResource(Color.TRANSPARENT);
						}
					}
				});
		
		ippv_buy_dialog_price.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
						&& event.getAction() == KeyEvent.ACTION_UP) {
					if (pricestate == 1) {
						ippv_buy_dialog_price.setText(getResources().getString(
								R.string.ippv_payviewed_cannottape)
								+ StIppvInfo.m_Price[0].getM_wPrice());
						pricestate = 0;
					} else if (pricestate == 0) {
						ippv_buy_dialog_price.setText(getResources().getString(
								R.string.ippv_payviewed_cantape)
								+ StIppvInfo.m_Price[1].getM_wPrice());
						pricestate = 1;
					}
				}
				return false;
			}
		});
		ippv_buy_dialog_price.requestFocus();

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent arg1)
	{
        if(keyCode!=KeyEvent.KEYCODE_BACK){
        	return true;
        }
	    return super.onKeyDown(keyCode, arg1);
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
        //LittleDownTimer.pauseMenu();
        Log.i("TT","start ippv onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }
    
}
