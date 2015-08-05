package com.konka.launcher.app;

import java.util.Collections;
import java.util.List;

import com.konka.launcher.R;
import com.konka.launcher.R.id;
import com.konka.launcher.R.layout;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class OtherApps extends Activity implements OnItemClickListener {
	private static String TAG = "AllApps";
	private Context mContext;
	private PackageManager mPackageManager;
	
	private List<ResolveInfo> mAllApps;
	private GridView mGridView;
	
	private String mWhich;
	private String current;
	private SharedPreferences sp_menuapp;
	private AppOperator mAppOperator;
	
	private String[] pkgs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mAppOperator = new AppOperator(mContext);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		setContentView(R.layout.other_apps);
		
		initView();
	}

	private void initView(){
		
		//按照app_id顺序筛选出第一个为空的id并保存在current中
		sp_menuapp = mContext.getSharedPreferences("menuapp", Activity.MODE_PRIVATE);
		pkgs = new String[LauncherMenu.appNum];
		for (int i = 0; i < LauncherMenu.appNum; i++) {
			if(sp_menuapp.contains(""+AppOperator.APPTAG[i])){
				pkgs[i] = sp_menuapp.getString(""+AppOperator.APPTAG[i], "none");
				continue;
			}else{
				current = ""+AppOperator.APPTAG[i];
				break;
			}
		}
		
		Intent intent = getIntent();
		mWhich = intent.getStringExtra("which");
		
		//如果是添加按钮，则mWhich赋值为空余控件ID
		if(mWhich.equals("app_add")){
			mWhich = current;
		}
	}
	
	@Override
	protected void onResume() {
		setupViews();

		super.onResume();
	}

	public void setupViews() {
		mContext = OtherApps.this;
		mPackageManager = getPackageManager();
		mGridView = (GridView) findViewById(R.id.allapps);
		bindAllApps();

		mGridView.setAdapter(new GridItemAdapter(mContext, mAllApps));
		mGridView.setNumColumns(7);
		mGridView.setOnItemClickListener(this);
	}

	public void bindAllApps() {
		// 应用的入口
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 符合上面条件的全部查出来,并且排序
		mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
		Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));
		
		
		//已经添加到常用的应用在此屏蔽掉
		for (int i = 0; i < mAllApps.size(); i++) {
			
			ResolveInfo res = mAllApps.get(i);
			String pkg = res.activityInfo.packageName;
			Log.v(TAG,"pkg="+pkg);
			if(pkg.equals(pkgs[0]) || pkg.equals(pkgs[1]) || pkg.equals(pkgs[2])
					|| pkg.equals(pkgs[3]) || pkg.equals(pkgs[4]) || pkg.equals(pkgs[5])
					|| pkg.equals(pkgs[6]) || pkg.equals(pkgs[7])){
				mAllApps.remove(i);
				i--;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		ResolveInfo res = mAllApps.get(position);

		// 该应用的包名和主Activity
		String pkg = res.activityInfo.packageName;
		String cls = res.activityInfo.name;

		Log.v(TAG, "pkg="+pkg+" cls="+cls+" mWhich="+mWhich);
		
		mAppOperator.setCommon(""+mWhich, pkg);
		OtherApps.this.finish();
	}

	private class GridItemAdapter extends BaseAdapter {
		private Context context;
		private List<ResolveInfo> resInfo;

		// 构造函数
		public GridItemAdapter(Context c, List<ResolveInfo> res) {
			context = c;
			resInfo = res;
		}

		@Override
		public int getCount() {
			return resInfo.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(R.layout.application_other_app_item_layout, null);

			ImageView app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
			TextView app_tilte = (TextView) convertView.findViewById(R.id.app_title);

			ResolveInfo res = resInfo.get(position);

			app_icon.setImageDrawable(res.loadIcon(mPackageManager));
			app_tilte.setText(res.loadLabel(mPackageManager).toString());

			return convertView;
		}
	}
}
