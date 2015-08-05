package android.test.ui;

import android.app.Activity;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.search.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * @author wuqi  2015-7-2 上午10:08:03
 * @function: 手动搜索
 */
public class Setting_Search_Manual extends Activity {

	private Button button;
	private Spinner spinner;
	private EditText editText1;
	private EditText editText2;
	private String[] data;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_setting_search_manual);
		
		spinner = (Spinner)findViewById(R.id.manual_spinner);
		button = (Button)findViewById(R.id.manual_button);
		editText1 = (EditText)findViewById(R.id.manual_editview1);
		editText2 = (EditText)findViewById(R.id.manual_editview2);
		data = new String[]{"QAM16","QAM32","QAM64","QAM128","QAM256"};
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(2, true);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String style = spinner.getSelectedItem().toString();        //调制方式
				int pl = Integer.parseInt(editText1.getText().toString());  //频率
				int ml = Integer.parseInt(editText2.getText().toString()); //码率
				
				Log.i("TT","------freq is"+pl);
				Log.i("TT","------ml is"+ml);
				Log.i("TT","------style is"+style);
				
				Intent intent = new Intent();
				intent.setClass(Setting_Search_Manual.this, Setting_Search_Manual_Searching.class);
				intent.putExtra("way", 0);
				intent.putExtra("Style", style);
				intent.putExtra("PL", pl);
				intent.putExtra("ML", ml);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onResume()
	{
		DVB.GetInstance().SetDVBStatus(1);
		super.onResume();
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.i("TT","settint search manu onPause");
		//DVB.GetInstance().ReleaseResource();	
	}
}
