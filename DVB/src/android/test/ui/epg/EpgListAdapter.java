package android.test.ui.epg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.com.konka.dvb.Epg.NewEpgInfo;
import android.com.konka.dvb.SWTimer.SysTime;
import android.com.konka.dvb.prog.ProgManager.ProgShowInfo;
import android.com.konka.dvb.timer.TimerManager;
import android.content.Context;
import android.test.ui.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EpgListAdapter extends BaseAdapter {
	private String TAG = "EpgListAdapter";
	private Context mContext;
	private LayoutInflater mInflater;
	
	private List<NewEpgInfo> mEpgList;
	private TimerManager mTimerManager;
	private SysTime curSysTime;
	
//	private boolean isDebug = false;
//	private ArrayList<String> mEpgDebugList;
	
	public EpgListAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mEpgList = new ArrayList<NewEpgInfo>();
		mTimerManager = TimerManager.getInstance();
		curSysTime = mTimerManager.GetSysTime();
		
//		if(isDebug){
//			mEpgDebugList = new ArrayList<String>();
//		}
	}

	public void updateEpgList(ArrayList<NewEpgInfo> epgList){
//		if(isDebug){
//			return;
//		}
		
		mEpgList = epgList;
		notifyDataSetChanged();
	}
	
//	public void updateEpgDebugList(ArrayList<String> epgDebugList){
//		if(isDebug){
//			mEpgDebugList = epgDebugList;
//			notifyDataSetChanged();
//		}
//	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		if(isDebug){
//			return mEpgDebugList.size();
//		}
		
		if(mEpgList != null){
			return mEpgList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
//		if(isDebug){
//			return mEpgDebugList.get(position);
//		}
		
		if(mEpgList != null){
			mEpgList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
//		if(isDebug){
//			return position;
//		}
		
		if(mEpgList != null && position < mEpgList.size()){
			return position;
		}
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mInflater.inflate(R.layout.epglist_item, null);
		
		EpgListItemHolder holder = new EpgListItemHolder();
		holder.epg_time = (TextView) convertView.findViewById(R.id.epg_time);
		holder.epg_title = (TextView) convertView.findViewById(R.id.epg_title);
		holder.epg_status = (TextView) convertView.findViewById(R.id.epg_status);
		
//		if(isDebug){
//			holder.epg_title.setText(mEpgDebugList.get(position));
//			convertView.setTag(holder);
//			return convertView;
//		}
		
		NewEpgInfo newEpgInfo = mEpgList.get(position);
		holder.epg_title.setText(newEpgInfo.memEventName);
		if(mTimerManager != null){
			SysTime startTime = mTimerManager.TransUtctimeToSystime(newEpgInfo.utcStartData, newEpgInfo.utcStartTime);
			SysTime endTime = mTimerManager.TransUtctimeToSystime(newEpgInfo.utcStartData, newEpgInfo.utcStartTime+newEpgInfo.DurSeconds);
			if(startTime != null && endTime != null){
//				Log.v(TAG,startTime.Year+" "+startTime.Month+" "+startTime.Day+" "+startTime.Hour+" "+startTime.Minute+" "+startTime.Minute+" "+startTime.Second);
//				Log.v(TAG,endTime.Year+" "+endTime.Month+" "+endTime.Day+" "+endTime.Hour+" "+endTime.Minute+" "+endTime.Minute+" "+endTime.Second);
//				Log.v(TAG,curSysTime.Year+" "+curSysTime.Month+" "+curSysTime.Day+" "+curSysTime.Hour+" "+curSysTime.Minute+" "+curSysTime.Minute+" "+curSysTime.Second);
				holder.epg_time.setText(getFormateTime(startTime)+"-"+getFormateTime(endTime));
				
				if(curSysTime != null){
					if(isBefore(getFormateTime0(startTime), getFormateTime0(curSysTime)) && isBefore(getFormateTime0(curSysTime), getFormateTime0(endTime))){
						holder.epg_status.setText(R.string.playing);
					}
				}
			}
		}
		
		convertView.setTag(holder);
		
		return convertView;
	}
	
	private boolean isBefore(String date0, String date1){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date d0;
		Date d1;
		try {
			d0 = dateFormat.parse(date0);
			d1 = dateFormat.parse(date1);
			if(d0.before(d1)){
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * 转换成标准格式
	 * @param sysTime
	 * @return
	 */
	private String getFormateTime0(SysTime sysTime){
		if(sysTime == null){
			return null;
		}
		
		StringBuilder time = new StringBuilder();
		time.append(sysTime.Year);
		
		if(sysTime.Month < 10){
			time.append("/0"+sysTime.Month);
		}else{
			time.append("/"+sysTime.Month);
		}
		
		if(sysTime.Day < 10){
			time.append("/0"+sysTime.Day);
		}else{
			time.append("/"+sysTime.Day);
		}
		
		if(sysTime.Hour < 10){
			time.append(" 0"+sysTime.Hour);
		}else{
			time.append(" "+sysTime.Hour);
		}
		
		if(sysTime.Minute < 10){
			time.append(":0"+sysTime.Minute);
		}else{
			time.append(":"+sysTime.Minute);
		}
		
		if(sysTime.Second < 10){
			time.append(":0"+sysTime.Second);
		}else{
			time.append(":"+sysTime.Second);
		}
		
		return time.toString();
	}
	
	/**
	 * 转换成标准格式
	 * @param sysTime
	 * @return
	 */
	private String getFormateTime(SysTime sysTime){
		if(sysTime == null){
			return null;
		}
		
		StringBuilder time = new StringBuilder();
		if(sysTime.Hour < 10){
			time.append("0"+sysTime.Hour);
		}else{
			time.append(sysTime.Hour);
		}
		
		if(sysTime.Minute < 10){
			time.append(":0"+sysTime.Minute);
		}else{
			time.append(":"+sysTime.Minute);
		}
		
		return time.toString();
	}
	
	private class EpgListItemHolder{
		private TextView epg_time;
		private TextView epg_title;
		private TextView epg_status;
	}
	
}