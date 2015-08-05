package android.com.konka.dvb.stock;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.IDVBListener;
import android.com.konka.dvb.Proglib;
import android.com.konka.dvb.Tuner;

import android.util.Log;

public class TunerManager {
	
		
	private final static String TAG = "STOCK_TUNER";
	private static Tuner mTuner = null;
	private static TunerManager tunerManager = null;


	private static Proglib mProglib = null;

	private TunerManager()
    {
		if(!DVB.IsInstanced())
		{
			DVB.GetInstance().create("unfdemo");
		}

		mTuner = DVB.GetInstance().GetTunerInstance();
       	
		mProglib = DVB.GetInstance().GetProglibInstance();
		
		if(mProglib == null){
			Log.e(TAG,"ProgManager init error");
			return ;
		}
		
		/*for tuner event listener.maybe no need.*/
       	DVB.GetInstance().SubScribeEvent(DVB.Event.KK_TUNER, new IDVBListener() {
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
			
			if(mTuner == null){
	     		Log.e(TAG,"mTumer not init");
	    		return ;
	     	}
			

		}
	}, 0);
       	
    }

	 public static TunerManager getInstance()
	 {
	        if (tunerManager == null)
	        	tunerManager = new TunerManager();
	        
	        return tunerManager;
	 }
	 
	 
	public CableLockParams GetCableLockParams(int service_type)
	{   
		int result = 0;
		
		if(tunerManager == null)
		{
			getInstance();
		}
		
		if(mProglib == null){
			Log.e(TAG,"ProgManager init error");
			return null;
		}
		
		CableLockParams freqinfo = new CableLockParams();
		
		int[] freq = new int[1];
		int[] servid = new int[1];
		int[] tsid = new int[1];
		
		result = mProglib.GetFreqInfoOfStockServiceType(servid,tsid,freq,service_type);
		if(result != 0)
		{
		    freqinfo.frequency = freq[0];
		    freqinfo.qamMode = 3;
		    freqinfo.symrates = 6875;		
		    
		    Log.e(TAG,"GetFreqInfoOfStockServiceType "+ freqinfo.frequency);
		    return freqinfo;
		}
		else
		{
			 Log.e(TAG,"GetFreqInfoOfStockServiceType null");
			return null;
		}
	}
	
	public boolean Tunerlock(CableLockParams params)
	{
		boolean result = false;
		
		if(tunerManager == null)
		{
			getInstance();
		}
		
		if(mTuner == null){
			
     		    Log.e(TAG,"LockFreq failed mTumer null");
     		
    		     return false;			
     	}
		
		if(0==mTuner.LockFreq(params.frequency,params.symrates,params.qamMode))
		{
			  Log.e(TAG,"LockFreq OK ");
			result = true;
		}
		else   Log.e(TAG,"LockFreq failed ");
		
		return  result;
	}

	public static class CableLockParams { 
		/**锁定频率,单位HZ */ 
		public int frequency; 
		/**调制方式*/ 
		public int qamMode; 
		/**符号率, 单位HZ */ 
		public int symrates; 
		}
    
}
