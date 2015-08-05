package android.com.konka.dvb;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.PowerManager;
import android.view.SurfaceHolder;
import android.util.Log;

public class DVB {

	public static final class Event{
		private int Value;
		private Event(int v){Value = v;}
		public String toString(){return ""+Value;}
		public int GetValue(){return Value;}
		public static final Event
		    KK_TUNER = new Event(0),
		    KK_EPG = new Event(1),
		    KK_SEARCH = new Event(2),
		    KK_TIMER = new Event(3),
		    KK_SUBFORPROG = new Event(4),
		    KK_DB = new Event(5),
		    KK_CA = new Event(6),
		    KK_FILTER = new Event(7),
		    KK_EXIT = new Event(100);
	};
	
	private final static String TAG = "DVB";

	private Tuner mTuner;
	private Epg mEpg;
	private Proglib mProglib;
	private ProgSearch mProgSearch;
	private AVPlayer mAvplayer;
	private SWTimer mtimer;
	private CaManager mCaManager;
	private Demux mDemux;
	private EventHandler mEventHandler;
	public int mNativeContext; // accessed by native methods
	static DVB m_instance = null;
	private HashMap<Integer, EventMapType> mListnerMap;

	static {
		try{
		System.loadLibrary("DVBjni");
		}catch(UnsatisfiedLinkError ule) {
			System.err.println("WARNING: Could not load library!");
		}

		native_init();
	}

	public static DVB GetInstance() {
		if (m_instance == null) {
			m_instance = new DVB();
		}
		return m_instance;
	}

	public static boolean IsInstanced() {
		if (m_instance == null)
			return false;
		else
			return true;
	}

	private void InitSubObjects() {
		mTuner = new Tuner(this);
		mEpg = new Epg(this);
		mProglib = new Proglib(this);
		mProgSearch = new ProgSearch(this);
		mAvplayer =  new AVPlayer(this);
		mtimer = new SWTimer(this);
		mCaManager = new CaManager(this);
		mDemux= new Demux(this);
	}

	public void create(String uri) {

		mListnerMap = new HashMap<Integer, EventMapType>();
		Looper looper;
		if ((looper = Looper.myLooper()) != null) {
			mEventHandler = new EventHandler(this, looper);
		} else if ((looper = Looper.getMainLooper()) != null) {
			mEventHandler = new EventHandler(this, looper);
		} else {
			mEventHandler = null;
		}
		native_setup(new WeakReference<DVB>(this));
		native_create(uri);
		InitSubObjects();
		
		native_AppInit();
	}

    public void SetDVBStatus(int statue) {
        native_SetDvbRunStatus(statue);
    }

    public void ReleaseResource() {
        native_releaseresource();
    }

	private static void postEventFromNative(Object DVB_ref,int what,int arg0,int arg1,
	int arg00,int arg01,int arg02,int arg03,int arg04,Object obj) {
		DVB dvb = (DVB) ((WeakReference) DVB_ref).get();
		if (dvb == null) {
			return;
		}

		if (dvb.mEventHandler != null) {
			Message m = dvb.mEventHandler.obtainMessage(what,arg0,arg1);
			Bundle b = new Bundle();
			b.putInt("param0", arg00);
			b.putInt("param1", arg01);
			b.putInt("param2", arg02);
			b.putInt("param3", arg03);
			b.putInt("param4", arg04);
			m.setData(b);
			dvb.mEventHandler.sendMessage(m);
		}
	}

	private class EventHandler extends Handler {
		private DVB mDVBr;

		public EventHandler(DVB mp, Looper looper) {
			super(looper);
			mDVBr = mp;
		}

		public void handleMessage(Message msg) {
			try {
				EventMapType Dvblistener = mListnerMap.get(msg.what);
				Bundle b = msg.getData();
				int param0 = b.getInt("param0");
				int param1 = b.getInt("param1");
				int param2 = b.getInt("param2");
				int param3 = b.getInt("param3");
				int param4 = b.getInt("param4");
				Log.d(TAG, "JAVA DVB " + msg.what);
				if (Dvblistener != null) {
					Dvblistener.mEventListener.NotifyEvent(msg.what,msg.arg1,msg.arg2,param0,param1,param2,param3,param4);
				} else {
					Log.d(TAG, "Listener is null form msg : " + msg.what);
				}
			} catch (Exception e) {

			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////

	private class EventMapType {
		int mEventType;
		int mPrivateParam;
		IDVBListener mEventListener;

		EventMapType(int EventType, IDVBListener EventListener, int PrivateParam) {
			mEventType = EventType;
			mEventListener = EventListener;
			mPrivateParam = PrivateParam;
		}
	}
    
	public void SubScribeEvent(DVB.Event mEvent, IDVBListener EventListener,
			int PrivateParam) {
		int EventID = mEvent.GetValue();
		Log.d(TAG, "JAVA: SubScribeEvent" + EventID);
		EventMapType Dvblistener = mListnerMap.get(EventID);
		if (null == Dvblistener) {
			mListnerMap.put(EventID, new EventMapType(EventID, EventListener,
					PrivateParam));
		} else {
			Log.d(TAG, "The DvbListenr is exist for :" + EventID);
		}
		native_SubScribeEvt(EventID);
	}

	public void UnSubScribeEvent(DVB.Event mEvent) {
		int EventID = mEvent.GetValue();
		EventMapType Dvblistener = mListnerMap.get(EventID);
		if (null != Dvblistener) {
			native_UnSubScribeEvt(EventID);
			mListnerMap.remove(EventID);
		}
	}
	
	public Tuner GetTunerInstance() {
		return mTuner;
	}
	
	public Epg GetEpgInstance(){
		return mEpg;
	}

	public Proglib GetProglibInstance(){
		return mProglib;
	}
	
	public ProgSearch GetProgSearchInstance() {			
		return mProgSearch;
	}

	public AVPlayer GetAVPlayerInstance() {
		return mAvplayer;
	}

	public SWTimer GetSWTimerInstance() {
		return mtimer;
	}
	
	public CaManager GetCaManagerInstance() {
		return mCaManager;
	}	

	public Demux GetDemuxInstance() {
		return mDemux;
	}
	
	private native final void native_finalize();

	protected void finalize() {
		Log.d(TAG, "DVB finalize");
		native_finalize();
	}

	public void release() {
		mListnerMap.clear();
		native_release();
	}
	

	private native int native_create(String uri);

	private native int native_SubScribeEvt(int EventID);

	private native int native_UnSubScribeEvt(int EventID);

	private static native final void native_init();

	private native final void native_setup(Object dvb_this);

	private native void native_release();

	private native int native_AppInit();

    private native int native_SetDvbRunStatus(int statue);
    
    private native int native_releaseresource();
}
