package android.test.ui;

import android.com.konka.dvb.DVB;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CaInfoModifyPin extends Activity {
	private EditText editText_ca_mg_old_pin;
	private EditText editText_ca_mg_new_pin;
	private EditText editText_ca_mg_new_pin2;
	protected LinearLayout linearlayout_ca_mg_old_pin;
	protected Button btnSubmit;
	private CaDeskManager caDesk=null;
	
	/*
	protected Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			super.handleMessage(msg);
			if(editText_ca_mg_old_pin.hasFocus()||editText_ca_mg_new_pin.hasFocus()||editText_ca_mg_new_pin2.hasFocus())
			{
				LittleDownTimer.resetMenu();
				return;
			}

			if (msg.what == LittleDownTimer.TIME_OUT_MSG)
			{
				CaInfoModifyPin.this.finish();
				Intent intent = new Intent(CaInfoModifyPin.this,
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
		setContentView(R.layout.ca_info_modify_pin);
		//LittleDownTimer.setHandler(handler);
		finsViews();
		
	}
	
    @Override
    protected void onResume()
    {
    	Log.d("KonkaCaSystem", "CaInfoModifyPin onResume");
        //LittleDownTimer.resumeMenu();
    	DVB.GetInstance().SetDVBStatus(1);
	    caDesk = CaDeskManager.getCaMgrInstance();
        super.onResume();
    }
    
    @Override
    protected void onPause()
    {
        Log.d("KonkaCaSystem", "CaInfoModifyPin onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","CaInfoModifyPin onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }
    
	@Override
	public void onUserInteraction()
	{
		Log.d("KonkaCaSystem", "CaInfoModifyPin onUserInteraction");
		//LittleDownTimer.resetMenu();
		super.onUserInteraction();
	}


	private void finsViews() {
		editText_ca_mg_old_pin = (EditText) findViewById(R.id.editText_ca_mg_old_pin);
		editText_ca_mg_new_pin = (EditText) findViewById(R.id.editText_ca_mg_new_pin);
		editText_ca_mg_new_pin2 = (EditText) findViewById(R.id.editText_ca_mg_new_pin2);
		linearlayout_ca_mg_old_pin = (LinearLayout) findViewById(R.id.linearlayout_ca_mg_old_pin);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		editText_ca_mg_old_pin.requestFocus();
		setOnClickLisenters();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		keyCode = KeyChange.getInstance().translateIRKey(keyCode);

		Intent intent = new Intent();
		Log.d("KonkaCaSystem", "CaInfoModifyPin keyCode"+keyCode);
		Log.d("KonkaCaSystem", "CaInfoModifyPin KeyEvent.KEYCODE_FORWARD_DEL"+KeyEvent.KEYCODE_FORWARD_DEL);
		Log.d("KonkaCaSystem", "CaInfoModifyPin KeyEvent.KEYCODE_DEL"+KeyEvent.KEYCODE_DEL);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			intent.setClass(CaInfoModifyPin.this, CaManagementActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setOnClickLisenters() {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.btnSubmit:
					setPin();
					break;
				default:
					break;
				}
			}
		};
		btnSubmit.setOnClickListener(listener);
	}
	
	private void setPin()
	{
		String oldPin=editText_ca_mg_old_pin.getText().toString();
		String newPin=editText_ca_mg_new_pin.getText().toString();
		String newPin2=editText_ca_mg_new_pin2.getText().toString();
		if(oldPin.length()<6||newPin.length()<6||newPin2.length()<6)
		{
			toastShow(R.string.st_ca_rc_pin_len);
			return;
		}
		if (!newPin.equals(newPin2)) 
		{
			toastShow(R.string.st_ca_rc_diff_pin);
			return;
		}
		try
		{
			short ret=caDesk.CaChangePin(oldPin, newPin);
			if(ret==RETURN_CODE.ST_CA_RC_OK.getRetCode())
			{
				toastShow(R.string.st_ca_rc_ok);
			}
			else if(ret==RETURN_CODE.ST_CA_RC_CARD_INVALID.getRetCode())
			{
				toastShow(R.string.st_ca_rc_card_invalid);
			}
			else if(ret==RETURN_CODE.ST_CA_RC_POINTER_INVALID.getRetCode())
			{
				toastShow(R.string.st_ca_rc_pointer_invalid);
			}
			else if(ret==RETURN_CODE.ST_CA_RC_PIN_INVALID.getRetCode())
			{
				toastShow(R.string.st_ca_rc_pin_invalid);
			}
			else if(ret==RETURN_CODE.ST_CA_RC_UNKNOWN.getRetCode())
			{
				toastShow(R.string.st_ca_rc_unknown);
			}
		}
		catch (CaCommonException e)
        {
            e.printStackTrace();
        }
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

