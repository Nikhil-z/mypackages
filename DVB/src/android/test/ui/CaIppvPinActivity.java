package android.test.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.com.konka.dvb.casys.data.CaStopIPPVBuyDlgInfo;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;
//import com.mstar.tvsettingservice.TvDeskProvider;
import android.test.util.Tools;

public class CaIppvPinActivity extends Activity {
	//private TvDeskProvider tvManagerProvider;
	private CaDeskManager caDesk = null;
	//private TVRootApp rootapp = null;
	private EditText ippv_pin_edittext;
	private Button ippv_pin_confirm;
	private Button ippv_pin_cancel;
	private TextView ippv_pin_textview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		setContentView(R.layout.ippv_pin);
		Intent intent=getIntent();
		Bundle mBundle=intent.getExtras();
		final short price=mBundle.getShort("price");
		final short pricecode=mBundle.getShort("pricecode");
		final short ecmpid=mBundle.getShort("ecmpid");
		final short messagetype=mBundle.getShort("messagetype");
		
		
		ippv_pin_textview=(TextView) findViewById(R.id.ippv_pin_textview);
		
		switch (messagetype) {
		case 0:
			ippv_pin_textview.setText(getResources().getString(
					R.string.ippv_freeviewed_segment));
			break;
		case 1:
			ippv_pin_textview.setText(getResources().getString(
					R.string.ippv_payviewed_segment));
			break;
		case 2:
			ippv_pin_textview.setText(getResources().getString(
					R.string.ippt_payviewed_segment));
			break;

		}
		rootapp = (TVRootApp) getApplication();
		tvManagerProvider = rootapp.getTvDeskProvider();
		caDesk = tvManagerProvider.getCaManagerInstance();
		ippv_pin_edittext=(EditText) findViewById(R.id.ippv_pin_edittext);
		ippv_pin_confirm=(Button)findViewById(R.id.ippv_pin_confirm);
		ippv_pin_cancel=(Button)findViewById(R.id.ippv_pin_cancel);
		
		ippv_pin_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CaStopIPPVBuyDlgInfo caStopIPPVBuyDlgInfo=new CaStopIPPVBuyDlgInfo();
				caStopIPPVBuyDlgInfo.setPbyPinCode(ippv_pin_edittext.getText().toString());
				caStopIPPVBuyDlgInfo.setsPrice(price);
				caStopIPPVBuyDlgInfo.setsPriceCode(pricecode);
				caStopIPPVBuyDlgInfo.setsIPPVBuyDlgState((short)1);
				caStopIPPVBuyDlgInfo.setsEcmPid(ecmpid);
				try {
					short state=caDesk.CaStopIPPVBuyDlg(caStopIPPVBuyDlgInfo);
					if(state==0x00){
						Tools.toastShow(R.string.ippv_pin_ok, IppvPinActivity.this);
					}else if(state==0x02){
						Tools.toastShow(R.string.ippv_pointer_invalid, IppvPinActivity.this);
					}else if(state==0x03){
						Tools.toastShow(R.string.ippv_card_invalid, IppvPinActivity.this);
					}else if(state==0x0A){
						Tools.toastShow(R.string.ippv_card_no_room, IppvPinActivity.this);
					}else if(state==0x09){
						Tools.toastShow(R.string.ippv_prog_status_invalid, IppvPinActivity.this);
					}else if(state==0x04){
						Tools.toastShow(R.string.ippv_pin_invalid, IppvPinActivity.this);
					}else if(state==0x08){
						Tools.toastShow(R.string.ippv_data_not_find, IppvPinActivity.this);
					}else{
						Tools.toastShow(R.string.st_ca_rc_unknown, IppvPinActivity.this);
					}
					finish();
				} catch (CaCommonException e) {
					e.printStackTrace();
				}

			}
		});
		*/
		ippv_pin_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		
	}
	


}
