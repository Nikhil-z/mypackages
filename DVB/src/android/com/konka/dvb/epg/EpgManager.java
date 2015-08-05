package android.com.konka.dvb.epg;

import java.util.ArrayList;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.Epg;
import android.com.konka.dvb.Epg.NewEpgInfo;
import android.com.konka.dvb.timer.TimerManager;
import android.com.konka.dvb.IDVBListener;
import android.com.konka.dvb.Proglib;
import android.util.Log;

public class EpgManager {
	
	private final static String TAG = "EPG";
	private static Epg mEpg = null;
	private static EpgManager epgManager = null;
	private static EpgEventListener onEpgEventListener = null;
	private static Proglib mProglib = null;
	private static TimerManager timerManager = null;

public static class CurrPfInfo {
	
	public int	tsid;
	public int servid;	
	public int	freq;
	public String sProgName;
	public int    progno;
	public	int 	utcStartTime;
	public	int 	DurSeconds;
	public	String 	memEventName;
	public	String 	memEventDesc;
  
}

	private EpgManager()
    {
		if(!DVB.IsInstanced())
		{
			DVB.GetInstance().create("unfdemo");
		}
	
		mEpg = DVB.GetInstance().GetEpgInstance();
		
				
		if(mEpg == null ){
			Log.e(TAG,"mEpg init error");
			return ;
		}
	
		timerManager = TimerManager.getInstance();
		if(timerManager == null){
			Log.e(TAG,"timerManager init error");
			return ;
		}
		else mEpg.SetTimerManager(timerManager);
		
		
		mProglib = DVB.GetInstance().GetProglibInstance();
		if(mProglib == null){
			Log.e(TAG,"mProglib init error");
			return ;
		}
			
	
		DVB.GetInstance().SubScribeEvent(DVB.Event.KK_EPG, new IDVBListener() {
		public void NotifyEvent(int arg0, int arg1, int arg2, int arg3, int arg4,
				int arg5, int arg6, int arg7) {
			// TODO Auto-generated method stub
			Log.v(TAG, "arg0:" + arg0 
			+ "arg1:" + arg1 
			+ "arg2:" + arg2
			+ "arg3:" + arg3
			+ "arg4:" + arg4
			+ "arg5:" + arg5
			+ "arg6:" + arg6
			+ "arg7:" + arg7
					);
			
				
			if(null != onEpgEventListener){
				
				Proglib.ProgInfo info = mProglib.new ProgInfo();		
				mProglib.GetProgInfoOfCurProg(info);
				onEpgEventListener.onUpdatePFInfoOfCurrvernProg(info.serviceid,info.tsid);
			}
		}
	}, 0);
	   	
	}
	
	public static EpgManager getInstance()
	{
	    if (epgManager == null)
	    	epgManager = new EpgManager();
	    return epgManager;
	}
	
	
	public CurrPfInfo getCurrverntProgPfInfo(int index){
		
		
		if(mEpg == null){
			Log.e(TAG,"getCurrverntProgPfInfo failed mEpg not init");
			return null;
		}
		
		CurrPfInfo pfInfo = new CurrPfInfo();
		Proglib.ProgInfo  progInfo = mProglib.new ProgInfo();
		NewEpgInfo epgInfo = mEpg.new NewEpgInfo();	
		
		if(progInfo != null)
		    mProglib.GetProgInfoOfCurProg(progInfo);
		
		epgInfo =mEpg.GetPFInfo(progInfo.tsid,progInfo.serviceid,index);
		
		if(epgInfo != null && pfInfo != null)
		{
			pfInfo.freq = progInfo.freq;
			pfInfo.sProgName = progInfo.cServiceName;
			pfInfo.tsid = progInfo.tsid;
			pfInfo.servid = progInfo.serviceid;
		
			pfInfo.utcStartTime = epgInfo.utcStartTime;
			pfInfo.DurSeconds=epgInfo.DurSeconds;
			pfInfo.memEventName=epgInfo.memEventName;
			pfInfo.memEventDesc =epgInfo.memEventDesc;			
		}
		
		return pfInfo;
	}
	
	
	public ArrayList<NewEpgInfo> getCurrverntProgSchInfo(int index){
		
  
		if(mEpg == null){
			Log.e(TAG,"getCurrverntProgPfInfo failed mEpg not init");
			return null;
		}
				 
		Proglib.ProgInfo  progInfo = mProglib.new ProgInfo();
				
		if(progInfo != null)
		    mProglib.GetProgInfoOfCurProg(progInfo);
		
		 return mEpg.getSchInfo(0xFFFFFFFF,progInfo.serviceid,index);			
		
	}
	
	
	public void setOnEpgEventListener(EpgEventListener listener)
	{
		onEpgEventListener = listener;
	}
}
