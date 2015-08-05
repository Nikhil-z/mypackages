package com.konka.launcher.app;

import java.util.List;

import com.konka.launcher.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class AppOperator {
	private String TAG = "AppOperator";
	
	private Context mContext;
	private PackageManager mPackageManager;
	private SharedPreferences sp_menuapp;
	public static int[] APPTAG = {1401, 1402, 1403, 1404, 1405, 1406, 1407, 1408};
	
	public AppOperator(Context context){
		mContext = context;
		mPackageManager = mContext.getPackageManager();
		sp_menuapp = mContext.getSharedPreferences("menuapp", Activity.MODE_PRIVATE);
	}
	
	/**
	 * 获取常用栏下一个空余的控件ID
	 * @return
	 */
	public int getGeneralNext(){
		int next = -1;
		for (int i = 0; i < LauncherMenu.appNum; i++) {
			if(!sp_menuapp.contains(""+APPTAG[i])){
				next = APPTAG[i];
				break;
			}
		}
		return next;
	}
	
	/**
	 * 获取当前开机启动包名
	 * @return
	 */
	public String getGeneralBootPkg(){
		if(sp_menuapp.contains("boot")){
			return sp_menuapp.getString("boot", "none");
		}
		return null;
	}
	
	
	/**
	 * 判断当前包是否常用应用，若是则返回在控件中的位置，否则返回-1
	 * @param pkg
	 * @return
	 */
	public int isCommon(String pkg){
		int which = -1;
		for (int i = 0; i < LauncherMenu.appNum; i++) {
			if(sp_menuapp.contains(""+APPTAG[i])){
				if(pkg.equals(sp_menuapp.getString(""+APPTAG[i], "none"))){
					which = APPTAG[i];//8040
				}
			}
		}
		return which;
	}
	
	/**
	 * 判断当前包应用是否开机启动
	 * @param pkg
	 * @return
	 */
	public boolean isBoot(String pkg){
		if(sp_menuapp.contains("boot")){
			String bootPkg = sp_menuapp.getString("boot", "none");
			if(bootPkg.equals(pkg) && !pkg.equals("none")){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 设置/取消常用。取消时会依次使用后面的控件内容填充
	 * @param show
	 * @return
	 */
	public boolean setCommon(String packageName, boolean enable, boolean show) {
		Log.v(TAG, "============ setCommon enable=" + enable);
		if (enable) {// 设置成常用
			if (getGeneralNext() == -1) {
				if(show){
					Toast.makeText(mContext, R.string.app_operation_common_full,Toast.LENGTH_SHORT).show();
				}
				return false;
			} else {
				sp_menuapp.edit().putString("" + getGeneralNext(), packageName).commit();
				if(show){
					Toast.makeText(mContext, R.string.app_operation_common_done,Toast.LENGTH_SHORT).show();
				}
			}

		} else {// 设置成非常用
			
			int index = -1;
			Editor editor = sp_menuapp.edit();

			for (int i = 0; i < LauncherMenu.appNum; i++) {
				if (index != -1) {
					if (sp_menuapp.contains("" + APPTAG[i])) {
						Log.v(TAG,"============"+ APPTAG[i - 1]+"("+(i-1)+")<--"+ APPTAG[i]+"("+i+")");
						editor.putString("" + APPTAG[i - 1], sp_menuapp.getString("" + APPTAG[i], "none"));
						
						if(i == LauncherMenu.appNum-1){
							Log.v(TAG,"============2remove "+ APPTAG[i]+"("+i+")");
							editor.remove("" + APPTAG[i]);
							editor.commit();
						}
						
					} else {
						Log.v(TAG,"============remove "+ APPTAG[i - 1]+"("+(i-1)+")");
						editor.remove("" +APPTAG[i - 1]);
						editor.commit();
						break;
					}
				}
				Log.v(TAG, "============1which=" + isCommon(packageName) +" i="+i);
				if (isCommon(packageName) == APPTAG[i]) {
					Log.v(TAG, "============which=" + isCommon(packageName) +" i="+i);
					index = i;

					if (i == LauncherMenu.appNum - 1) {// 当要删除是最后一个时，直接删除
						editor.remove("" +APPTAG[i]);
						editor.commit();
					}
				}
			}

			if(show){
				Toast.makeText(mContext, R.string.app_operation_common_cancel_done,Toast.LENGTH_SHORT).show();
			}
		}
		return true;
	}
	
	public void setCommon(String tag, String pkg){
		Log.v(TAG, "============!tag="+tag+" pkg="+pkg);
		sp_menuapp.edit().putString(tag, pkg).commit();
	}
	
	/**
	 * 设置/取消开机启动
	 * @param pkg
	 * @param enable
	 * @param show
	 */
	public void setBoot(String pkg, boolean enable, boolean show){
		if(!enable){
			sp_menuapp.edit().remove("boot").commit();
			if(show){
				Toast.makeText(mContext, R.string.app_operation_boot_start_cancel_done, Toast.LENGTH_SHORT).show();
			}
		}else if(getGeneralBootPkg() != null){
			if(show){
				Toast.makeText(mContext, mContext.getString(R.string.app_operation_boot_start_done1, getGeneralLabel(getGeneralBootPkg())), Toast.LENGTH_SHORT).show();
			}
			sp_menuapp.edit().putString("boot", pkg).commit();
		}else{
			sp_menuapp.edit().putString("boot", pkg).commit();
			if(show){
				Toast.makeText(mContext, R.string.app_operation_boot_start_done, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * 卸载应用。若应用为常用、开机启动项则相应取消
	 * @param pkg
	 */
	public void unInstall(String pkg){
		String uri = "package:"+pkg;
		Intent intent = new Intent();
	    intent.setAction(Intent.ACTION_DELETE);
	    intent.setData(Uri.parse(uri));
		mContext.startActivity(intent);
	}
	
	
	/**
	 * 通过包名获取标签名
	 * @param pkg
	 * @return
	 */
	public String getGeneralLabel(String pkg){
		try {
			ApplicationInfo info = mPackageManager.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
			return mPackageManager.getApplicationLabel(info).toString();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 通过包名启动程序
	 * @param packagename
	 */
	public void doStartApplicationWithPackageName(String packagename) {
		Log.v(TAG, "doStartApplicationWithPackageName packagename="+packagename);
		try {
			// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
			PackageInfo packageinfo;
			packageinfo = mPackageManager.getPackageInfo(packagename, 0);

			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(packageinfo.packageName);
	
			List<ResolveInfo> resolveinfoList = mPackageManager.queryIntentActivities(resolveIntent, 0);
	
			ResolveInfo resolveinfo = resolveinfoList.iterator().next();
			if (resolveinfo != null) {
				String packageName = resolveinfo.activityInfo.packageName;
				String className = resolveinfo.activityInfo.name;
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
	
				ComponentName cn = new ComponentName(packageName, className);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setComponent(cn);
				
				mContext.startActivity(intent);
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
