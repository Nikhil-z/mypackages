package android.test.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.prog.ProgManager;
import android.com.konka.dvb.prog.ProgManager.ProgListShowInfo;
import android.com.konka.dvb.prog.ProgManager.ProgShowInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SettingActivity_List extends Activity {
	private ProListAdapter adapter;
	private List<ProgShowInfo> list;
	private ListView listView;
	public ProgManager pProgManager = null;
	public ProgListShowInfo proglist = null;
	private int currPro = -1;// 从0开始的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_setting_list);
		listView = (ListView) findViewById(R.id.setting_list_listview);
		if(pProgManager==null){
		pProgManager = ProgManager.getInstance();
		}
		list = new ArrayList<ProgShowInfo>();
	
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				String num = proglist.pProgList.get(position).prog_num;
				Intent intent = new Intent();
				intent.setClass(SettingActivity_List.this, Topmost.class);
				intent.putExtra("num", num);
				startActivity(intent);

			}
		});

	}

	@Override
	protected void onResume() {
			DVB.GetInstance().SetDVBStatus(1);
			proglist = pProgManager.getCurrverntProgListInfo();
			if (proglist != null) {
				list = proglist.pProgList;
				adapter = new ProListAdapter(this, list);
				listView.setAdapter(adapter);
				try {
					int i = Integer.parseInt(proglist.currPlay_ProgNum);
					currPro = i-1;
				} catch (NumberFormatException e) {
				}
				listView.setSelectionFromTop(currPro, 10);
			}
		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	
		Log.i("TT","settint list onPause");
		DVB.GetInstance().ReleaseResource();	
	}


	@Override
	protected void onDestroy() {
		finish();
		super.onDestroy();
	}
}
