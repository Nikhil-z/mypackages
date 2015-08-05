package android.com.konka.dvb;


import android.os.Parcel;
import android.util.Log;

public class SWTimer {

	private DVB mDVB;
	private int mNativeContext; // accessed by native methods
	private final static String TAG = "TIMER";
	

	public SWTimer(DVB dvb)
	{
		mDVB = dvb;
		mNativeContext = dvb.mNativeContext;
		Log.d(TAG, "SWTimer  construct"+ mNativeContext);
	}
	
	public final native int InitTimer();
	public final native int CentiSecSWTimer();
	public final native int GetCurrentMs();
	public final native int GetSysTime(SysTime input_p);
	public final native int GetSysUTCTime( int[] data, int[] time);
	public final native int GetSysLocalTime( int[] data, int[] time);
	public final native int LocalTimeAdjust( int utcdata, int utctime, int[] ldata, int[] ltime);
	public final native int TransUtctimeToSystime( int  utcdata, int  utctime,SysTime systime);
	public final native int TransLocalTimeToSystime( int  ldata, int  ltime,SysTime systime);
	public final native int UtcTimeDec( int utcdata0, int utctime0, int utcdata1, int utctime1);
	public final native int EnableTDTReq( int enable);
	public final native  int AllocSWTimer(CallBackSWTimer timer);
	public final native int DeleteSWTimer( int handle);
	public final native int EnableEventClock( int  enable);
	public final native int SetEventClockLoop( int ms);

	public class SysTime
	{
		public int	Year;
		public int 	Month;
		public int 	Day;
		public int 	Hour;
		public int 	Minute;
		public int 	Second;
		public int 	Weekday;
	}

	public class CallBackSWTimer
	{
		public char used;
		public char type;
		public short handle;
		public int 	time1;
		public int 	time2;
		public int cbfun;
		//SWTIMERCBFun 	cbfun;
		public int 	param;
	}
}
