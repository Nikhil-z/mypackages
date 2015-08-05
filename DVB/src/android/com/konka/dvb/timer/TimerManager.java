package android.com.konka.dvb.timer;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.IDVBListener;
import android.com.konka.dvb.SWTimer;
import android.com.konka.dvb.SWTimer.CallBackSWTimer;
import android.com.konka.dvb.SWTimer.SysTime;
import android.com.konka.dvb.timer.TimerEventListener;
import android.util.Log;

public class TimerManager {



	private final static String TAG = "DVB";
	private static SWTimer mTimer = null;
	private static TimerManager timerManager = null;
	private static TimerEventListener onTimerEventListener = null;

private TimerManager()
{
	if(!DVB.IsInstanced())
	{
		DVB.GetInstance().create("unfdemo");
	}

	mTimer = DVB.GetInstance().GetSWTimerInstance();
	
	/*for tuner event listener.maybe no need.*/
   	DVB.GetInstance().SubScribeEvent(DVB.Event.KK_TIMER, new IDVBListener() {
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
		
		if(mTimer == null){
     		Log.e(TAG,"mTumer not init");
    		return ;
     	}
		
		if(null != onTimerEventListener){
			
		}

	}
}, 0);
   	
}

public static TimerManager getInstance()
{
	
	
    if (timerManager == null)
    	timerManager = new TimerManager();
    
    return timerManager;
}

public int InitTmer()
{
	if(mTimer != null)
	{
		return mTimer.InitTimer();
		
	}
	return 0;
}


public int CentiSecSWTimer()
{
	if(mTimer != null)
	{
	    return mTimer.CentiSecSWTimer();
		
	}
	return 0;
}

public int GetCurrentMs()
{
	if(mTimer != null)
	{
	    return mTimer.GetCurrentMs();
		
	}
	return 0;
}

public SysTime GetSysTime()
{
	
	if(mTimer != null)
	{
		SysTime sTime = mTimer.new SysTime();
		 mTimer.GetSysTime(sTime);
		 return sTime;
		
	}
	else return null;
	
}


public int GetSysUTCTime(int[] data, int[] time)
{
	
	if(mTimer != null)
	{		
		return mTimer.GetSysUTCTime(data,time);		
		
	}
	
	 return 0;
	
}

public int GetSysLocalTime(int[] data, int[] time)
{
	
	if(mTimer != null)
	{		
		return mTimer.GetSysLocalTime(data,time);		
		
	}
	
	 return 0;
	
}

public int LocalTimeAdjust(int utcdata, int utctime, int[] data, int[] time)
{
	
	if(mTimer != null)
	{		
		return mTimer.LocalTimeAdjust(utcdata,utctime,data,time);			
	}	
	 return 0;
	
}

public SysTime TransUtctimeToSystime(int utcdata, int utctime)
{
	
	if(mTimer != null)
	{	
		 SysTime sTime = mTimer.new SysTime();
		 Log.e(TAG,"TimerManager  TransUtctimeToSystime mTimer start!");
		 mTimer.TransUtctimeToSystime(utcdata,utctime,sTime);			 
		 return sTime;
	}
	else 
	    Log.e(TAG,"TimerManager  TransUtctimeToSystime mTimer is null!");
	
	 return null;
	
}

public SysTime TransLocalTimeToSystime(int ldata, int ltime)
{
	
	if(mTimer != null)
	{		
		 SysTime systime = mTimer.new SysTime();
		 mTimer.TransLocalTimeToSystime(ldata,ltime,systime);		
		 return systime;
	}
	
	 return null;
	
}

public int UtcTimeDec(int utcdata0, int utctime0, int utcdata1, int utctime1)
{
	
	if(mTimer != null)
	{		
		return mTimer.UtcTimeDec(utcdata0,utctime0,utcdata1,utctime1);		
		
	}
	
	 return 0;
	
}

public int EnableTDTReq(int enable)
{
	
	if(mTimer != null)
	{		
		return mTimer.EnableTDTReq(enable);		
		
	}
	
	 return 0;
	
}

public int AllocSWTimer(CallBackSWTimer timer)
{
	
	if(mTimer != null)
	{		
		return mTimer.AllocSWTimer(timer);		
		
	}
	
	 return 0;
	
}

public int DeleteSWTimer(int handle)
{
	
	if(mTimer != null)
	{		
		return mTimer.DeleteSWTimer(handle);		
		
	}
	
	 return 0;
	
}

public int EnableEventClock(int enable)
{
	
	if(mTimer != null)
	{		
		return mTimer.EnableEventClock(enable);		
		
	}
	
	 return 0;
	
}

public  int SetEventClockLoop( int ms)
{
	
	if(mTimer != null)
	{		
		return mTimer.SetEventClockLoop(ms);		
		
	}
	
	 return 0;	
}


public void setOnTimerEventListener(TimerEventListener listener)
{
	onTimerEventListener = listener;
}
}