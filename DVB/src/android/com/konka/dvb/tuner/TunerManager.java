package android.com.konka.dvb.tuner;


import android.com.konka.dvb.DVB;
import android.com.konka.dvb.IDVBListener;
import android.com.konka.dvb.Tuner;
import android.util.Log;

public class TunerManager {
	
	
	public static class TunerInfo {
		
		public int Quality;

		public int Strength;

		public int BerRatio;

		public int CurrFreq; /*Freq  频率  KHZ(435000)*/

		public int CurrLockFlag;/*1 is lock,0 is not lock*/

	}

	private final static String TAG = "DVB";
	private static Tuner mTuner = null;
	private static TunerManager tunerManager = null;
	private static TunerInfo tunerInfo = null;
	private static TunerEventListener onTunerEventListener = null;
	

	private TunerManager()
    {
		if(!DVB.IsInstanced())
		{
			DVB.GetInstance().create("unfdemo");
		}

		mTuner = DVB.GetInstance().GetTunerInstance();
       	
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
			
			if(null != onTunerEventListener){
				onTunerEventListener.onTunerLockStatus( mTuner.GetCurrentFreq(), mTuner.TunerIsLocked());
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
	
	public int LockFreq( int freq, int rate, int qam ) {
		
     	if(mTuner == null){
     		Log.e(TAG,"LockFreq failed mTumer null");
    		return 1;
     	}
      	return  mTuner.LockFreq(freq,rate,qam);
	}
	
	/*
	public int GetCurrentFreq() {
		
     	if(mTuner == null){
     		Log.e(TAG,"GetCurrentFreq failed mTumer null");
    		return 0;
     	}
      	return  mTuner.GetCurrentFreq();
	}
	
	public int TunerIsLocked() {
		
     	if(mTuner == null){
     		Log.e(TAG,"TunerIsLocked failed mTumer null");
    		return 0;
     	}
      	return  mTuner.TunerIsLocked();
      	
	}
	*/
	
	public TunerInfo GetInfo(){
		
		int[] strength=new int[1];
		int[] signalquality=new int[1];
		int[] bitErrorRate=new int[1];
		
		int[] RatioStrength = new int [1];
		int[] Ratiosignalquality = new int [1];
		int[] BERRate=new int[1];
		
		int CurrFreq = 0;
		int CurrLockFlag = 0;
		
		strength[0] = 0;
		signalquality[0] = 0;
		bitErrorRate[0] = 0;
		
		RatioStrength[0] = 0;
		Ratiosignalquality[0] = 0;
		BERRate[0] = 0;
		
		
     	if(mTuner == null){
     		Log.e(TAG,"GetInfo failed mTumer null");
    		return null;
     	}
		
		if(tunerInfo == null){
			tunerInfo = new TunerInfo();
		}	
		
		/*get tuner type info*/
		if(0!=mTuner.GetSignalStatus(strength, signalquality, bitErrorRate)){
			
			Log.e(TAG,"GetSignalStatus failed ");
			return null;
		}
		
		/*get ratio strength by type info*/
		if(0 != mTuner.GetTunerSignalInfo(0, strength[0], RatioStrength)){
			
			Log.e(TAG,"GetTunerSignalInfo failed ");
			return null;
		}
		
		/*get Ratiosignalquality by type info*/
		if(0 != mTuner.GetTunerSignalInfo(1, signalquality[0], Ratiosignalquality)){
			
			Log.e(TAG,"GetTunerSignalInfo failed ");
			return null;
		}	
		
		/*get BERRate by type info*/
		if(0 != mTuner.GetTunerSignalInfo(2, bitErrorRate[0], BERRate)){
			
			Log.e(TAG,"GetTunerSignalInfo failed ");
			return null;
		}
			
		CurrFreq = mTuner.GetCurrentFreq();
		CurrLockFlag = mTuner.TunerIsLocked();
		

		tunerInfo.Quality = Ratiosignalquality[0];
		tunerInfo.Strength = RatioStrength[0];
		tunerInfo.BerRatio = BERRate[0];
		tunerInfo.CurrFreq = CurrFreq;
		tunerInfo.CurrLockFlag = CurrLockFlag;
		
		Log.i(TAG, "tunerInfo.Quality=" + tunerInfo.Quality + "\n");
		Log.i(TAG, "tunerInfo.Strength=" + tunerInfo.Strength + "\n");
		Log.i(TAG, "tunerInfo.BerRatio=" + tunerInfo.BerRatio + "\n");
		Log.i(TAG, "tunerInfo.CurrFreq=" + tunerInfo.CurrFreq + "\n");
		Log.i(TAG, "tunerInfo.CurrLockFlag=" + tunerInfo.CurrLockFlag + "\n");
			
		return tunerInfo;
	}
	
    
    public void setOnTunerEventListener(TunerEventListener listener)
    {
    	onTunerEventListener = listener;
    }
    
}
