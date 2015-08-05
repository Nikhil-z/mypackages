package android.com.konka.dvb;


import java.util.ArrayList;

import android.com.konka.dvb.timer.TimerManager;

import android.util.Log;


public class Epg {

	private DVB mDVB;
	private int mNativeContext; // accessed by native methods
	private final static String TAG = "EPG";
	private static TimerManager timerManager = null;
	private ArrayList<NewEpgInfo> EpgSchInfo = null;
	private ArrayList<NewEpgInfo> EpgPfInfo = null;
	
	public Epg(DVB dvb)
	{
		mDVB = dvb;
		mNativeContext = dvb.mNativeContext;
		Log.d(TAG, "Epg  construct"+ mNativeContext);
		EpgNativeCreate();
		EpgSchInfo = new ArrayList<NewEpgInfo>();
		EpgPfInfo = new ArrayList<NewEpgInfo>();
		
		  
			
	}
    
	public void SetTimerManager( TimerManager Handle)
	{
		timerManager = Handle;
		
	}
	
	protected void finalize()
	{
		EpgNativeDestroy();
	}
	
	public int setPFListener(Epg.OnEpgListener Pf)
	{
		return EpgNativeRegisterPFCallback(Pf);
	}
	
	public int setSCHListener(Epg.OnEpgListener Sch)
	{
		return EpgNativeRegisterSCHCallback(Sch);
	}
	
	public int SearchEpgData(int tsid,int serviceid)
	{
		EpgSchInfo.clear();
		EpgPfInfo.clear();
		return EpgNativeSentNewEpgDataReq(tsid,serviceid);
	}
	
	public ArrayList<NewEpgInfo> getSchInfo(int tsid,int serviceid,int dayIndex)
	{
		int index,next;
		int aftertime = 0;
		int datatime = 0;
	    int nowsysmins = 0;
	    int prog_start=0,prog_end=0,prog_ldata=0;
		Log.v("EPG", "GetSchInfo!!!!");
		NewEpgInfo info = new NewEpgInfo();
		EpgSchInfo.clear();
		//EpgSchInfo.add(info);
		
		int[] datatime1 = new int[1];
		int[] sectime1 = new int[1];
		int[]temp = new int[1];
		int[]temp1= new int[1];
		
		if(timerManager== null) return null;
		
		timerManager.GetSysLocalTime(datatime1,sectime1);
		 
		if(dayIndex != 0)
		{
			datatime = datatime1[0] +dayIndex;
		    aftertime = 0;
		}
		else
		{
			datatime = datatime1[0];
		    aftertime = sectime1[0];
		}
		
		nowsysmins = datatime*1440 +  aftertime/60;	
		
		next = index = EpgNativeGetEitHeaderOfServID_core(tsid,serviceid);
		while((-1!=next)&&(-2 != next))
		{
			info = new NewEpgInfo();
			next = EpgNativeGetNextEitOfService_core(index,info);
			/**add by liuhuasheng*/			
			if(-2 == next){
				Log.e("KK","EpgNativeGetNextEitOfService_core kkkkk error");
				break;
			}
			Log.e("EPG","EpgNativeGetNextEitOfService_core next " + next);
			
			timerManager.LocalTimeAdjust(info.utcStartData,info.utcStartTime,temp,temp1);
			
			prog_ldata = temp[0];
			prog_start = temp[0] *1440 + (temp1[0]/60);
			prog_end = prog_start + (info.DurSeconds/60);

			if( (datatime == prog_ldata) &&  (prog_end > nowsysmins))
			{
				EpgSchInfo.add(info);
			}
			
			/*
			Log.v("EPG","cEit_used:"+info.cEit_used);
			Log.v("EPG","eventState:"+info.eventState);
			Log.v("EPG","ucUserNibble1_2:"+info.ucUserNibble1_2);
			Log.v("EPG","ucContentNibble1_2:"+info.ucContentNibble1_2);
			Log.v("EPG","serviceid:"+info.serviceid);
			Log.v("EPG","uiEventId:"+info.uiEventId);
			Log.v("EPG","sNextveit_index:"+info.sNextveit_index);
			Log.v("EPG","utcStartData:"+info.utcStartData);
			Log.v("EPG","utcStartTime:"+info.utcStartTime);
			Log.v("EPG","DurSeconds:"+info.DurSeconds);
			Log.v("EPG","memEventName:"+info.memEventName);
			Log.v("EPG","memEventDesc:"+info.memEventDesc);
			*/
			
			index = next;
		}
		return EpgSchInfo;
	}

	public NewEpgInfo GetPFInfo(int tsid,int serviceid,int param)
	{
		NewEpgInfo info= new NewEpgInfo();
		if(0==EpgNativeGetPfEpgInfo(tsid,serviceid,param,info))
			return info;

		return null;
	}
	/*
	public ArrayList<NewEpgInfo> getPFInfo(int tsid,int serviceid)
	{
		NewEpgInfo info= new NewEpgInfo();
		EpgSchInfo.clear();
		
		if(0==EpgNativeGetPfEpgInfo(tsid,serviceid,0,info))
		{
		//	Log.v("EPG","memEventName:"+info.memEventName);
			EpgPfInfo.add(info);
		}

		if(0==EpgNativeGetPfEpgInfo(tsid,serviceid,1,info))
		{
		//	Log.v("EPG","memEventName:"+info.memEventName);
			EpgPfInfo.add(info);
		}
		return EpgPfInfo;
	}
	*/
	public int exitModule()
	{
		return EpgNativeExitOutNewEpgModule();
	}
	
	public int reEnterModule()
	{
		return EpgNativeReEnterNewEpgModule();
	}
	
	
	private final native int EpgNativeCreate();
	private final native int EpgNativeDestroy();

	private final native int EpgNativeRegisterPFCallback(Epg.OnEpgListener CbFunction);
	private final native int EpgNativeRegisterSCHCallback(Epg.OnEpgListener CbFunction);

	private final native int EpgNativeEpgGetEitNumOfServID_core(int tsid,int serviceid);
	private final native int EpgNativeSentNewEpgDataReq(int tsid,int serviceid);
	
	private final native  int EpgNativeGetEitHeaderOfServID_core(int tsid,int serviceid);
	
	private final native int EpgNativeGetNextEitOfService_core(int index,NewEpgInfo info);
	
	private final native  int EpgNativeGetPfEpgInfo(int tsid,int serviceid,int index,NewEpgInfo info);
	
	private final native int EpgNativeExitOutNewEpgModule();
	
	private final native int EpgNativeReEnterNewEpgModule();


	public class NewEpgInfo
	{
		public	char 	cEit_used;			/*0: no used; 1: used*/	
		public	char  	eventState;			/*CAmode  */
		public	char  	ucUserNibble1_2;
		public	char  	ucContentNibble1_2;
		public	short 	serviceid;
		public	short 	uiEventId;        	/*msb16: serviceid; lsb: eitid*/
		public	short	sNextveit_index;	/*-1; inavailed*/

		public	int	    utcStartData;
		public	int 	utcStartTime;
		public	int 	DurSeconds;

		public	String 	memEventName;
		public	String 	memEventDesc;
	}
	
	public interface OnEpgListener
	{
		public int OnReceive(int serviceid,int tsid,int param);
	}
}
