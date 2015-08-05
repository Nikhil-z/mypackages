package android.com.konka.dvb;

import android.util.Log;

public class Demux {

	private DVB mDVB;
	private int mNativeContext; // accessed by native methods
	private final static String TAG = "DEMUX";
	

	public Demux(DVB dvb)
	{
		mDVB = dvb;
		mNativeContext = dvb.mNativeContext;
		Log.d(TAG, "Demux construct"+ mNativeContext);
	}


	public final native int CancelFilterReq(int pSlotId);
	
	public final native int StartFilterReq( int ReqPid, byte[] data, byte[] Mask, int byLen, int Repeat);
	
	public final native int GetFilterData( int slotId, byte[] DataBuf, int section_buff, int sectionlength);
	
	
}