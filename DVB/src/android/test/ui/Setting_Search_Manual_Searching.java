package android.test.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.search.SearchEventListener;
import android.com.konka.dvb.search.SearchManager;
import android.com.konka.dvb.search.SearchManager.SearchResultInfo;
import android.com.konka.dvb.tuner.TunerManager.TunerInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Setting_Search_Manual_Searching extends Activity {
	
	

	private ListView listView;
	private TextView textView1_1;
	private TextView textView1_2;
	private TextView textView1_3;
	private TextView textView1_4;
	private TextView textView3_1;
	private TextView textView3_2;
	private TextView textView3_3;
	private ProgressBar progress1;
	private ProgressBar progress2;
	private ProgressBar progress3;
	public SearchManager searchManager = null;
	private int quality =0;   //信号质量
	private int strenth =0;   //信号强度
	private int currFreq =0;  //搜索频点
	private  int tvprog_num =0;  //电视个数
	private  int gbprog_num =0;  //广播个数
	private  int zxprog_num =0;  //资讯个数
	private  int dbprog_num =0;  //点播个数
	private int progress_now =0;  //进度条当前值
	private int progress_max =0;  //进度最大值
	private MyAdapter adapter;
	
	
	private List<String>  list;
	int x= 0;
	int y= 0;
	int z= 0;
	
	
	private class SearchListener implements SearchEventListener{

		@Override
		public void onUpdateSingalInfo(TunerInfo tunerInfo) {
			// TODO Auto-generated method stub
			Log.i("TT","CurrFreq"+tunerInfo.CurrFreq+"Quality"+tunerInfo.Quality+"Strength"+tunerInfo.Strength+"BerRatio"+tunerInfo.BerRatio);
			quality = tunerInfo.Quality;
			strenth = tunerInfo.Strength;
			currFreq = tunerInfo.CurrFreq;
		
			
			textView3_1.setText(strenth+"%");
			textView3_2.setText(quality+"%");
			
			progress1.setProgress(strenth);
			progress2.setProgress(quality);
			
		}

		@Override
		public void onUpdateProgInfoOfCurrvernFreq(int freq,
				SearchResultInfo searchResultInfo) {
			// TODO Auto-generated method stub
			Log.i("TT","freq"+searchResultInfo.freq +"tvprog_num--"+searchResultInfo.tvprog_num+"gbprog_num---"+
					searchResultInfo.gbprog_num+"prog num"+searchResultInfo.pProgInfo.size()
					+"name1"+searchResultInfo.pProgInfo.get(0).sProgName);
			currFreq = searchResultInfo.freq;
			tvprog_num = searchResultInfo.tvprog_num;
			gbprog_num = searchResultInfo.gbprog_num;
			zxprog_num = searchResultInfo.refnvod_num;
			dbprog_num = searchResultInfo.shiftnvod_num;
			
			textView1_1.setText("电视："+tvprog_num);
			textView1_2.setText("广播："+gbprog_num);
			textView1_3.setText("资讯："+zxprog_num);
			textView1_4.setText("点播："+dbprog_num);
			
			y=tvprog_num-y;
			z=gbprog_num-z;
			x = x+1;
			String pd = currFreq+"HZ"+"   "+"QAM64";
			list.add(x+"               "+pd+"          "+y+" "+"电视"+"  "+z+" "+"广播");
			
			adapter.notifyDataSetChanged();
			listView.setSelection(listView.getCount()-1);
			y = tvprog_num;
			z = gbprog_num;
		}

		@Override
		public void onUpdateSearchProgress(int step, int max_step) {
			// TODO Auto-generated method stub
			Log.i("TT","progress step " + step + "max_step "+ max_step);
			progress_now = step;
			progress_max = max_step;
			int bf = progress_now*100/progress_max;
			textView3_3.setText(bf+"%");
			progress3.setMax(progress_max);
			progress3.setProgress(progress_now);
		}

		@Override
		public void onReceiveNitTable() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSearchNitFailed() {
			// TODO Auto-generated method stub
			Log.i("TT","onSearchNitFailed" );
			Intent intent =new Intent();
			intent.setClass(Setting_Search_Manual_Searching.this, Topmost.class);
			startActivity(intent); 
			finish();
			
		}

		@Override
		public void onSearchAllDataFinish() {
			// TODO Auto-generated method stub
			Log.i("TT","search finish");
			Toast.makeText(Setting_Search_Manual_Searching.this,"搜索完毕！" , Toast.LENGTH_LONG).show();
			Intent intent =new Intent();
			intent.setClass(Setting_Search_Manual_Searching.this, Topmost.class);
			startActivity(intent); 
			finish();
			
		}
		
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting_search_manual_searching);
		init();
		

	}

	private void init() {
		listView = (ListView)findViewById(R.id.searching_listview);
		textView1_1 = (TextView)findViewById(R.id.searching_textview1_1);
		textView1_2 = (TextView)findViewById(R.id.searching_textview1_2);
		textView1_3 = (TextView)findViewById(R.id.searching_textview1_3);
		textView1_4 = (TextView)findViewById(R.id.searching_textview1_4);
		textView3_1 = (TextView)findViewById(R.id.searching_textview3_1);
		textView3_2 = (TextView)findViewById(R.id.searching_textview3_2);
		textView3_3 = (TextView)findViewById(R.id.searching_textview3_3);
		progress1 =(ProgressBar)findViewById(R.id.searching_progress1);
		progress2 =(ProgressBar)findViewById(R.id.searching_progress2);
		progress3 =(ProgressBar)findViewById(R.id.searching_progress3);
		
		list = new ArrayList<String>();

		adapter = new MyAdapter(this,list);
		listView.setAdapter(adapter);
		
		searchManager = SearchManager.getInstance();
		
		
		if ((getIntent() != null) && (getIntent().getExtras() != null)) {
			int i = getIntent().getIntExtra("way", 0);
			searchManager.setOnSearchEventListener(new SearchListener());
			if(i==0){
				int pl = getIntent().getIntExtra("PL", 259000);
				int ml =getIntent().getIntExtra("ML", 6875);
				String style = getIntent().getStringExtra("Style");	
				//手动搜索
				searchManager.startSearchByManu(pl, ml, style);
				Log.i("TT","手动search");
			}
			if(i==1){
				//全频搜索
				searchManager.startSearchByNetwork();
				Log.i("TT","全频search");
			}
			if(i==2){
				//自动搜索
				searchManager.startSearchByNit();
				Log.i("TT","自动search");
			}
		}
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
		Log.i("TT","settint 11 search onPause");
		DVB.GetInstance().ReleaseResource();	
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
            // 创建退出对话框  
            AlertDialog isExit = new AlertDialog.Builder(this).create();  
            // 设置对话框标题  
            isExit.setTitle("系统提示");  
            // 设置对话框消息  
            isExit.setMessage("确定要退出搜索吗？");  
            // 添加选择按钮并注册监听  
            isExit.setButton("确定", listener);  
            isExit.setButton2("取消", listener);  
            // 显示对话框  
            isExit.show();  
  
        }  
          
        return false;
	}
	/**监听对话框里面的button点击事件*/  
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
    {  
        public void onClick(DialogInterface dialog, int which)  
        {  
            switch (which)  
            {  
            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
            	searchManager.stopSearch();
            	if(0 == tvprog_num)
            	{
					Intent intent =new Intent();
					intent.setClass(Setting_Search_Manual_Searching.this, SettingActivity_Search.class);
					startActivity(intent);
					finish();
					
            	}else{
					Intent intent =new Intent();
					intent.setClass(Setting_Search_Manual_Searching.this, Topmost.class);
					startActivity(intent);
					finish();
            	}
                break;  
            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框 
            	
                break;  
            default:  
                break;  
            }  
        }  
    };    


}
