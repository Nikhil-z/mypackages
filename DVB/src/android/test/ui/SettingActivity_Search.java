package android.test.ui;

import android.app.Activity;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.av.AVPlayerManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SettingActivity_Search extends Activity implements OnClickListener {
	
	public AVPlayerManager aVPlayerManager;
	private static int volume = 16;
	private Button button1;
	private Button button2;
	private Button button3;
	
	/**add by liuhuasheng for small win msg*/
	private CaViewSmallWindow caViewSmallWindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting_search);
		aVPlayerManager = AVPlayerManager.getInstance();
		init();
		
		aVPlayerManager.setPlayerVolume(volume);
		
	}
	
	private void init() {
		button1 = (Button)findViewById(R.id.setting_search_button1);
		button2 = (Button)findViewById(R.id.setting_search_button2);
		button3 = (Button)findViewById(R.id.setting_search_button3);
		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);		
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch(v.getId()){
			case R.id.setting_search_button1:
				intent.setClass(SettingActivity_Search.this,Setting_Search_Manual.class);
				startActivity(intent);
				break;
			case R.id.setting_search_button2:
				intent.setClass(SettingActivity_Search.this, Setting_Search_Manual_Searching.class);
				intent.putExtra("way", 1);
				startActivity(intent);
				break;
			case R.id.setting_search_button3:
				intent.setClass(SettingActivity_Search.this, Setting_Search_Manual_Searching.class);
				intent.putExtra("way", 2);
				startActivity(intent);
				break;
		}
	}
	@Override
	protected void onResume() {
		DVB.GetInstance().SetDVBStatus(1);
		aVPlayerManager.setPlayerWindow(545, 138, 450, 320);
		aVPlayerManager.startPlayer();
	
		/**add by liuhuasheng for small win msg*/
		caViewSmallWindow = new CaViewSmallWindow(this);
		
		super.onResume();
		

	}
	@Override
	protected void onPause() {
		super.onPause();
		if(aVPlayerManager != null){
			aVPlayerManager.stopPlayer();
		}
		/**add by liuhuasheng for small win msg*/
		caViewSmallWindow.cancelAllSmallWinShowMsg();
		DVB.GetInstance().ReleaseResource();	

	}

	@Override
	protected void onStop() {
		//aVPlayerManager.stopPlayer();
		super.onStop();
	}

}
