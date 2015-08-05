package android.com.konka.dvb;

import java.util.ArrayList;

import android.os.Parcel;
import android.util.Log;
import java.io.UnsupportedEncodingException;

public class AVPlayer {

	private DVB mDVB;
	private int mNativeContext; // accessed by native methods
	private final static String TAG = "AVPLAYER";
	
	public AVPlayer(DVB dvb)
	{
		mDVB = dvb;
		mNativeContext = dvb.mNativeContext;
		Log.d(TAG, "AVPlayer  construct"+ mNativeContext);
	}

	protected void finalize()
	{
	}
	public final native int GetCurrPlayParam( int []servid, int []pmtpid);
	public final native int StopNewChannel( int needcloseiframe);
	public final native int PlayCurrProg();
	public final native int FPanelShowLed();
	public final native int CheckSingalStatus(int []tuner,int []avsignal);
	public final native int GetPlayStatusInfo(PlayInfo info, int param);
	public final native int IsAVPlaying();
	public final native int AVStop( int mode);
	public final native int AVPlay( int vidpid,  int audpid, int pcrpid, char V_type, char A_type,  int param);
	public final native int AVGetCurPlayInfo( int []vidpid,  int []audpid, int []pcrpid);
	public final native int AVCurProgIsPlaying();
	public final native int AVMuteSound();
	public final native int AVUmMuteSound();
	public final native int AVSetSoundVolumeAdj( int volume, int adj);
	public final native int AVSetSoundMode( int mode);
	public final native int AVSetPlayWindow( int x, int y, int w, int h, int aduRatio);
	public final native int AVShowVideoWindow( int show);
	public final native int AVShowIFramePicture( int show, int PicID);
	public final native int AVStartCurrentAV();
	public final native int AVSetEncodingMode( int mode);
	public final native int AVGetEncodingMode();
	public final native int AVSetAspectRatio( int mode);
	public final native int AVSetScreenRatio( int ratio);
	public final native int SignalSetValueForFactSet();

	public class PlayInfo
	{
		public char	used;
		public char	Track;
		public char	TunerLock;
		public char	SoundInfo;
		public short	VidPID;
		public short[]	AudPID;
		public short	PcrPID;
		public short	ServID;
		public short	Rate;
		public short	Qam;
		public int		Freq;
	}
/*
	public final native  int  StartPlay( int ID, int VidPID,  int AudPID, int PCRPID, int V_Type, int A_Type);
	public final native int StopPlay( int ID, int Mode);

	public final native int EnableVideoWindow( int  ID, int  Enable);
	public final native int SetWindow( int ID, int x, int y, int w, int h );

	public final native int SetVolume( int ID, int Volume);
	public final native  int SetAudioOutputMode( int ID, int Mode);
	public final native  int AudioMute( int ID);
	public final native  int AudioUnMute( int ID);
	public final native int SetPictureParams( int Param, int Value );
	public final native int SetScreenRatio( int Ratio);
	public final native int SetAspectRatio( int Mode);
	public final native int SetResolution( int Resolution); 
*/

}


