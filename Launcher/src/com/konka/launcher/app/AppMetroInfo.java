package com.konka.launcher.app;

import com.konka.launcher.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class AppMetroInfo {
	private String TAG = "AppMetroInfo";
	private Context mContext;
	private PackageManager mPackageManager;
	private SharedPreferences sp_menuapp;
	
	private String packageName;
	private boolean isCommon = false;
	private boolean isBoot = false;
	private int which = -1;
	
	private int next = -1;
	private String bootPkg;
	
	public AppMetroInfo(Context context, String pkg){
		mContext = context;
		mPackageManager = mContext.getPackageManager();
		sp_menuapp = mContext.getSharedPreferences("menuapp", Activity.MODE_PRIVATE);
		
		packageName = pkg;
		initInfo();
	}
	
	public String getPackageName() {
		return packageName;
	}


	/**
	 * 初始化数据
	 * isCommon	常用栏中应用
	 * which	在常用栏中绑定控件的ID
	 * index	在常用应用栏中控件的index
	 * next		常用应用栏中下一个空余的控件ID
	 */
	public void initInfo(){
		for (int i = 0; i < LauncherMenu.appNum; i++) {
			if(sp_menuapp.contains(""+AppOperator.APPTAG[i])){
				if(packageName.equals(sp_menuapp.getString(""+AppOperator.APPTAG[i], "none"))){
					isCommon = true;
					which = AppOperator.APPTAG[i];
				}
			}else{
				next = AppOperator.APPTAG[i];
				break;
			}
		}
		
		if(sp_menuapp.contains("boot")){
			bootPkg = sp_menuapp.getString("boot", "none");
			if(bootPkg.equals(packageName) && !packageName.equals("none")){
				isBoot = true;
			}
		}
	}
	
	public String getLabel(){
		try {
			ApplicationInfo info = mPackageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
			return mPackageManager.getApplicationLabel(info).toString();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean isCommon(){
		return isCommon;
	}
	
	public boolean isBoot(){
		return isBoot;
	}
	
	public int getWhich(){
		return which;
	}
	
	
	
	
	
//	/**
//	 * 获取常用栏下一个空余的控件ID
//	 * @return
//	 */
//	public int getGeneralNext(){
//		return next;
//	}
//	
//	/**
//	 * 获取当前开机启动包名
//	 * @return
//	 */
//	public String getGeneralBootPkg(){
//		if(bootPkg != null && !bootPkg.equals("none")){
//			return bootPkg;
//		}
//		return null;
//	}
//	
//	/**
//	 * 设置/取消常用。取消时会依次使用后面的控件内容填充
//	 * @param show
//	 * @return
//	 */
//	public boolean setCommon(boolean show) {
//		Log.v(TAG, "============ setCommon isCommon=" + isCommon);
//		if (isCommon) {// 设置成非常用
//			int index = -1;
//			Editor editor = backPackage.edit();
//
//			for (int i = 0; i < LauncherMenu.appNum; i++) {
//				if (index != -1) {
//					if (backPackage.contains("" + LauncherMenu.app_id[i])) {
////						Log.v(TAG,"============"+ Integer.toHexString(LauncherMenuA.app_id[i - 1])+"("+(i-1)+")<--"+ Integer.toHexString(LauncherMenuA.app_id[i])+"("+i+")");
//						editor.putString("" + LauncherMenu.app_id[i - 1], backPackage.getString("" + LauncherMenu.app_id[i], "none"));
//						
//						if(i == LauncherMenu.appNum-1){
////							Log.v(TAG,"============2remove "+ Integer.toHexString(LauncherMenuA.app_id[i])+"("+i+")");
//							editor.remove("" + LauncherMenu.app_id[i]);
//							editor.commit();
//						}
//						
//					} else {
////						Log.v(TAG,"============remove "+ Integer.toHexString(LauncherMenuA.app_id[i - 1])+"("+(i-1)+")");
//						editor.remove("" + LauncherMenu.app_id[i - 1]);
//						editor.commit();
//						break;
//					}
//				}
//
//				if (which == LauncherMenu.app_id[i]) {
////					Log.v(TAG, "============which=" + which+" i="+i);
//					index = i;
//
//					if (i == LauncherMenu.appNum - 1) {// 当要删除是最后一个时，直接删除
//						editor.remove("" + LauncherMenu.app_id[i]);
//						editor.commit();
//					}
//				}
//			}
//
//			if(show){
//				Toast.makeText(mContext, R.string.app_operation_common_cancel_done,Toast.LENGTH_SHORT).show();
//			}
//
//		} else {// 设置成常用
//			if (next == -1) {
//				if(show){
//					Toast.makeText(mContext, R.string.app_operation_common_full,Toast.LENGTH_SHORT).show();
//				}
//				return false;
//			} else {
//				backPackage.edit().putString("" + next, packageName).commit();
//				if(show){
//					Toast.makeText(mContext, R.string.app_operation_common_done,Toast.LENGTH_SHORT).show();
//				}
//			}
//		}
//		return true;
//	}
//	
//	public void setBoot(boolean show){
//		if(isBoot){
//			backPackage.edit().remove("boot").commit();
//			if(show){
//				Toast.makeText(mContext, R.string.app_operation_boot_start_cancel_done, Toast.LENGTH_SHORT).show();
//			}
//		}else if(getGeneralBootPkg() != null){
//			backPackage.edit().putString("boot", packageName).commit();
//			if(show){
//				Toast.makeText(mContext, mContext.getString(R.string.app_operation_boot_start_done1, getGeneralLabel(bootPkg)), Toast.LENGTH_SHORT).show();
//			}
//		}else{
//			backPackage.edit().putString("boot", packageName).commit();
//			if(show){
//				Toast.makeText(mContext, R.string.app_operation_boot_start_done, Toast.LENGTH_SHORT).show();
//			}
//		}
//	}
//	
//	public void unInstall(){
//		if(isCommon){
//			setCommon(false);
//		}
//		
//		if(isBoot){
//			setBoot(false);
//		}
//		
//		String uri = "package:"+packageName;
//		Intent intent = new Intent();
//	    intent.setAction(Intent.ACTION_DELETE);
//	    intent.setData(Uri.parse(uri));
//		mContext.startActivity(intent);
//	}
//	
//	
//	/**
//	 * 通过包名获取标签名
//	 * @param pkg
//	 * @return
//	 */
//	public String getGeneralLabel(String pkg){
//		try {
//			ApplicationInfo info = mPackageManager.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
//			return mPackageManager.getApplicationLabel(info).toString();
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	
	
	
	
}