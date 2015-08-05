package android.test.ui;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.test.comm.KeyChange;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.graphics.Color;

public class CaEmailDetailActivity extends Activity {
	private TextView emaildetail_theme;
	private TextView emaildetail_time;
	private TextView emaildetail_level;
	private TextView emaildetail_content;
	private CaDeskManager caDesk = null;
	private int emailid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ca_emaildetail);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		String theme=bundle.getString("theme");
		String content=bundle.getString("content");
		int time=bundle.getInt("time");
		int level=bundle.getInt("level");
		
		emailid=bundle.getInt("emailid");
		
		caDesk = CaDeskManager.getCaMgrInstance();
		
		emaildetail_theme=(TextView) findViewById(R.id.emaildetail_theme);
		emaildetail_time=(TextView) findViewById(R.id.emaildetail_time);
		emaildetail_level=(TextView) findViewById(R.id.emaildetail_level);
		emaildetail_content=(TextView) findViewById(R.id.emaildetail_content);
		
		emaildetail_theme.setText(getResources().getString(R.string.email_theme) + ":" + theme);
		emaildetail_time.setText(getResources().getString(R.string.email_time) + ":" + dateConvert(time));
		emaildetail_level.setText(getResources().getString(R.string.email_level) + ":" + formatEmailImportance(level));
		emaildetail_content.setText(content);
		
	}
	
	private String formatEmailImportance(int arg0) {
		return arg0 == 0 ? getResources().getString(R.string.email_ordinary)
				: getResources().getString(R.string.email_important);
	}

	private String dateConvert(int dayNum)

	{

		 String endDate="";

	     Calendar ca = Calendar.getInstance();

		ca.set(1970, Calendar.JANUARY, 1, 0, 0, 0);

		dayNum = dayNum + 8 * 3600; 

		ca.add(Calendar.SECOND, dayNum);

		Format s = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		 endDate=s.format(ca.getTime());
		
		return endDate;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		keyCode = KeyChange.getInstance().translateIRKey(keyCode);

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			goToEmailListActivity();
			break;
			
			
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private void goToEmailListActivity(){
		Intent intent=new Intent();
		intent.setClass(CaEmailDetailActivity.this, CaEmailListActivity.class);
		CaEmailDetailActivity.this.startActivity(intent);
		CaEmailDetailActivity.this.finish();
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
        Log.d("KonkaCaSystem", "emaildetail onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","emaildetail onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }
}
