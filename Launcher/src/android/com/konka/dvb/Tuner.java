package android.com.konka.dvb;

import android.util.Log;

public class Tuner {

	private DVB mDVB;
	private int mNativeContext; // accessed by native methods
	private final static String TAG = "DVB";
	

	
	public Tuner(DVB dvb)
	{
		mDVB = dvb;
		mNativeContext = dvb.mNativeContext;
		Log.d(TAG, "Tuner  construct"+ mNativeContext);
	}


	public final native int LockFreq( int freq, int rate, int qam );
	public final native int GetCurrentFreq();
	public final native int TunerIsLocked();
	public final native int GetTunerSignalInfo( int info,  int inparam, int[] value);
	public final native int GetSignalStatus( int[] Strength ,  int[] SignalQuality, int[] BitErrorRate );
	/**
	\brief tuner锁定操作
	\attention \n
	无
	\param[in] Input:  TunerID :Tuner编号，从0开始
	\param[in] Input:  Freq  频率  KHZ(279000)
	\param[in] Input:  Symbol 符号率 Ksym/s(6875000)
	\param[in] Input:  QamType 调制方式

	\retval ::0: sucess, other, failed
	\see \n
	::HI_ADP_TUNER_DEV_TYPE_E
	
	public final native int Lock(int TunerID, int Freq, int SymbolRate, int QamType);


	/**
	\brief 获取tuner信息
	\attention \n
	无
	\param[in]  TunerID       tuner id.
	\param[out]TunerInfo      用于输出频点信息.
	\retval ::0: sucess, other failed
	\see \n
	
	
	public final native int GetInfo(int TunerID, TunerInfo TunerInfo);

	/**
	\brief 获取tuner锁定状态
	\attention \n
	无
	\param[in]  TunerID :Tuner编号，从0开始
	\param[out] LockFlag   锁定状态 ,
	\param[out] LockFreq  锁定状态对应的频点
	\retval ::0: sucess, other ,failed
	\see \n
	::HI_TUNER_TPINFO_S
	
	public final native int GetLockStatus(int TunerID,int[]LockFlag, long[]Freq);

	/*tune to the specified program number
	public  final native int TuneProgram(int u32ProgramNum);
	public  final native int StartAVPlay();
	public  final native int StopAVPlay();
	public static class TunerInfo {

		public int Quality;

		public int Strength;

		public int[] Ber = new int[3]; // 3个元素INT 数组

		public int CurrFrq;

		public int CurrSym;

		public int CurrQam;

		public int CurrLockFlag;
	}
	*/
}
