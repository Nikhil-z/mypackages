package android.test.ui;

import java.net.URISyntaxException;

import android.app.Activity;
import android.app.AlertDialog;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.av.AVPlayerManager;
import android.com.konka.dvb.prog.ProgManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.test.ui.epg.EpgWindow;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SettingActivity extends Activity implements OnClickListener {
	
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;
	private AVPlayerManager aVPlayerManager = null;
	private ProgManager progManager = null;
	private static int volume = 16;
	
	/**add by liuhuasheng for small win msg*/
	private CaViewSmallWindow caViewSmallWindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_setting);
		init();
		Log.i("setting","setting onCreate");
		aVPlayerManager = AVPlayerManager.getInstance();
        
		aVPlayerManager.setPlayerVolume(volume);
	}
	
	private void init() {
		button1 = (Button)findViewById(R.id.setting_button1);
		button2 = (Button)findViewById(R.id.setting_button2);
		button3 = (Button)findViewById(R.id.setting_button3);
		button4 = (Button)findViewById(R.id.setting_button4);
		button5 = (Button)findViewById(R.id.setting_button5);
		button6 = (Button)findViewById(R.id.setting_button6);
		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.setting_button1:
			intent.setClass(SettingActivity.this, SettingActivity_Search.class);
			startActivity(intent);		
			break;
		case R.id.setting_button2:
			intent.setClass(SettingActivity.this, SettingActivity_List.class);
			startActivity(intent);	
			break;
		case R.id.setting_button3:
			intent.setClass(SettingActivity.this, EpgWindow.class);
			startActivity(intent);	
			break;
		case R.id.setting_button4:
			intent.setClass(SettingActivity.this, CaActivity.class);
			startActivity(intent);	
			break;
		case R.id.setting_button5:
			
			break;
		case R.id.setting_button6:
			AlertDialog isReset = new AlertDialog.Builder(this).create();  
            // 设置对话框标题  
			isReset.setTitle("恢复出厂设置:");  
            // 设置对话框消息  
			isReset.setMessage("确定要恢复出厂设置吗？");  
            // 添加选择按钮并注册监听  
			isReset.setButton("确定", listener);  
			isReset.setButton2("取消", listener);  
            // 显示对话框  
			isReset.show(); 
			break;	
		default:
		   break;
		}
	}
	
	 DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	    {  
	        public void onClick(DialogInterface dialog, int which)  
	        {  
	            switch (which)  
	            {  
	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
	            	progManager = ProgManager.getInstance();
	            	progManager.factorySet();
	            	updatecaViewSmallWindow();
	                break;  
	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框 
	            	dialog.dismiss();
	                break;  
	            default:  
	                break;  
	            }  
	        }  
	    }; 
	
	@Override
	protected void onResume() {
		Log.i("setting","setting onResume");
		DVB.GetInstance().SetDVBStatus(1);
		aVPlayerManager.setPlayerWindow(545, 138, 450, 320);
		aVPlayerManager.startPlayer();
		
		
		updatecaViewSmallWindow();
		super.onResume();
	}
	
	private void updatecaViewSmallWindow() {
		// TODO Auto-generated method stub
		/**add by liuhuasheng for small win msg*/
		caViewSmallWindow = new CaViewSmallWindow(this);
	}

	@Override
	protected void onPause() {
		Log.i("setting","setting onPause");
		super.onPause();
		if(aVPlayerManager != null){
			aVPlayerManager.stopPlayer();
		}
		/**add by liuhuasheng for small win msg*/
		caViewSmallWindow.cancelAllSmallWinShowMsg();
		DVB.GetInstance().ReleaseResource();
	}
	
	@Override
	protected void onRestart(){
		Log.i("setting","setting onRestart");
		super.onStop();
		
	}

	@Override
	protected void onStop() {
		Log.i("setting","setting onStop");
		//aVPlayerManager.stopPlayer();
		super.onStop();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

}
