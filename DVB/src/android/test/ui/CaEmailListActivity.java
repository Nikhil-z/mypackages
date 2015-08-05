package android.test.ui;

import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.casys.data.CaEmailHeadInfo;
import android.com.konka.dvb.casys.data.CaEmailHeadsInfo;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskManager;

public class CaEmailListActivity extends Activity {
	private ListView email_listview;
	private TextView bottomTextView;
	private CaDeskManager caDesk = null;
	private CaEmailHeadsInfo mCaEmailHeadsInfo;
	private CaEmailHeadInfo[] mCaEmailHeadInfos;
	private ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter htSchedule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ca_emaillist);
		
		caDesk = CaDeskManager.getCaMgrInstance();
		getmCaEmailHeadInfos();
		
		htSchedule = new SimpleAdapter(this, mylist, R.layout.ca_emaillist_item,
				new String[] { "theme", "time", "level", "state" }, new int[] {
						R.id.emaillist_item_theme, R.id.emaillist_item_time,
						R.id.emaillist_item_level, R.id.emaillist_item_state });
		email_listview = (ListView) findViewById(R.id.email_listview);
		email_listview.setAdapter(htSchedule);
		email_listview.setOnItemSelectedListener(onEmailItemSelectedListener);
		email_listview.setOnItemClickListener(onEmailItemClickListener);
		bottomTextView = (TextView) findViewById(R.id.emaillist_bottom);
		showEmailNum();
		
	}
	
	private void showEmailNum(){
		short emptynum = 0;
		try {
			emptynum = caDesk.CaGetEmailSpaceInfo().getsEmptyNum();
		} catch (CaCommonException e) {
			e.printStackTrace();
		}

		if(mCaEmailHeadInfos.length==0){
			bottomTextView.setText(getResources().getString(R.string.email_num_txt)
					+ "0/" + mCaEmailHeadInfos.length + " "
					+ getResources().getString(R.string.email_free_space) + " "
					+ emptynum
					+ getResources().getString(R.string.email_num_txt));
		}else{
			bottomTextView.setText(getResources().getString(R.string.email_num_txt) 
					+ "1/" + mCaEmailHeadInfos.length + " "
					+ getResources().getString(R.string.email_free_space) + " "
					+ emptynum);
		}

	}

	private void getmCaEmailHeadInfos(){

		try {
			mCaEmailHeadsInfo = caDesk.CaGetEmailHeads((short) 100, (short) 0);
		} catch (CaCommonException e) {
			System.out.println("CaCommonException");
			e.printStackTrace();
		}
		mCaEmailHeadInfos = mCaEmailHeadsInfo.getEmailHeads();
		mylist.clear();
		for (int i = 0; i < mCaEmailHeadInfos.length; i++) {
			CaEmailHeadInfo caEmailHeadInfo = mCaEmailHeadsInfo.getEmailHeads()[i];
			HashMap<String, String> map = new HashMap<String, String>();
			try {
				map.put("theme", new String(caEmailHeadInfo.getPcEmailHead()
						.getBytes(), "GB2312"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			map.put("time", dateConvert(caEmailHeadInfo.getwCreateTime()));
			map.put("level",
					formatEmailImportance(caEmailHeadInfo.getwImportance()));
			Log.i("liux", "state===" + caEmailHeadInfo.getsEmailHeadState());
			map.put("state",
					formatEmailHeadState(caEmailHeadInfo.getM_bNewEmail()));
			mylist.add(map);
		}
	}
	
	AdapterView.OnItemClickListener onEmailItemClickListener =new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			CaEmailHeadInfo caEmailHeadInfo=mCaEmailHeadInfos[position];
			Intent intent=new Intent();
			Bundle bundle=new Bundle();
			try {
				bundle.putString("theme", new String(caEmailHeadInfo.getPcEmailHead()
						.getBytes(), "GB2312"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			bundle.putInt("time", caEmailHeadInfo.getwCreateTime());
			bundle.putInt("level", caEmailHeadInfo.getwImportance());
			bundle.putInt("emailid", caEmailHeadInfo.getwActionID());
			try {
				try {
					bundle.putString(
							"content",
							 new String(caDesk.CaGetEmailContent(caEmailHeadInfo.getwActionID())
										.getPcEmailContent()
										.getBytes(), "GB2312")
							);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} catch (CaCommonException e) {
				e.printStackTrace();
			}
			intent.putExtras(bundle);
			intent.setClass(CaEmailListActivity.this, CaEmailDetailActivity.class);
			CaEmailListActivity.this.startActivity(intent);
			//CaEmailListActivity.this.finish();
		}
	};

	AdapterView.OnItemSelectedListener onEmailItemSelectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			try {
			bottomTextView.setText((getResources().getString(R.string.email_num_txt) + (position + 1)) + "/"
						+ mCaEmailHeadInfos.length + " "
						+ getResources().getString(R.string.email_free_space)
						+ " " + caDesk.CaGetEmailSpaceInfo().getsEmptyNum());
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (CaCommonException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	private String formatEmailImportance(int arg0) {
		return arg0 == 0 ? getResources().getString(R.string.email_ordinary)
				: getResources().getString(R.string.email_important);
	}

	private String formatEmailHeadState(int arg0) {
		return arg0 == 0 ? getResources().getString(R.string.email_read)
				: getResources().getString(R.string.email_unread);
	}
	
	private String dateConvert(int dayNum)
	
	{

		 String endDate="";

	     Calendar ca = Calendar.getInstance();

		ca.set(1970, Calendar.JANUARY, 1, 0, 0, 0);

		dayNum = dayNum + 8 * 3600; 

		ca.add(Calendar.SECOND, dayNum);

		 Format s = new SimpleDateFormat("yy-MM-dd HH:mm:ss");  

		 endDate=s.format(ca.getTime());
		return endDate;
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_PROG_RED){
			if(mCaEmailHeadInfos.length!=0&&email_listview.hasFocus()){
				
				new AlertDialog.Builder(this)
				.setTitle(R.string.really_remove_this_email)
				.setNegativeButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
						try {
											caDesk.CaDelEmail(mCaEmailHeadInfos[email_listview
													.getSelectedItemPosition()]
													.getwActionID());
						} catch (CaCommonException e) {
							e.printStackTrace();
						}
						getmCaEmailHeadInfos();
						htSchedule.notifyDataSetChanged();
						showEmailNum();
					}
				}).create().show();
			}
			
		}else if(keyCode==KeyEvent.KEYCODE_PROG_GREEN){
			if(mCaEmailHeadInfos.length!=0&&email_listview.hasFocus()){
				new AlertDialog.Builder(this)
				.setTitle(R.string.really_remove_all_email)
				.setNegativeButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
						try {
							caDesk.CaDelEmail(0);
						} catch (CaCommonException e) {
							e.printStackTrace();
						}
						getmCaEmailHeadInfos();
						htSchedule.notifyDataSetChanged();
						showEmailNum();
					}
								}).create().show();
			}
		}		
		
		return super.onKeyDown(keyCode, event);
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
        Log.d("KonkaCaSystem", "emaillist onPause");
        //LittleDownTimer.pauseMenu();
        Log.i("TT","emaillist onPause");
        DVB.GetInstance().ReleaseResource();
        super.onPause();

    }

}
