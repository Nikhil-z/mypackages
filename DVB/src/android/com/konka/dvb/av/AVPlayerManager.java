package android.com.konka.dvb.av;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.com.konka.dvb.AVPlayer;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.IDVBListener;
import android.com.konka.dvb.Proglib;
import android.com.konka.dvb.AVPlayer.PlayInfo;
import android.com.konka.dvb.Proglib.ProgInfo;
import android.com.konka.dvb.Proglib.ProgList;
import android.util.Log;

public class AVPlayerManager {
	
	private enum sw_mangparam_e
	{
		PROG_LOCK,
		PROG_FAVOR,
		PROG_TRACK,
		PROG_VOLUE,
		PROG_USER,
		PROG_TYPE,	
		PROG_PARAM_NUM
	}

	
	public static class CurrPlayerInfo {
		
		public int servid;
		public int pmtpid;
		public char	track;
		public char	tunerLockFlag;
		public char	soundInfo;
		public short vidPID;
		public ArrayList<Short> pAudPID;
		public short pcrPID;
		public short rate;
		public short qam;
		public int	freq;

	}
	private final static String TAG = "AVPlayer";
	private static AVPlayer mAVPlayer = null;
	private static AVPlayerManager avPlayerManager = null;
	private static AVPlayEventListener onAVPlayEventListener = null;
	private Proglib mProglib = null;

	
	private AVPlayerManager()
    {
		if(!DVB.IsInstanced())
		{
			DVB.GetInstance().create("unfdemo");
		}
	
		mAVPlayer = DVB.GetInstance().GetAVPlayerInstance();
		if(mAVPlayer == null){
			Log.e(TAG,"AVPlayerManager init mAVPlayer error");
			return ;
		}
		
		mProglib = DVB.GetInstance().GetProglibInstance();
		if(mProglib == null){
			Log.e(TAG,"AVPlayerManager init mProglib error");
			return ;
		}
		
		/**
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
			
			if(null != onAVPlayEventListener){
				
			}
				
		}
	}, 0);
	*/
	   	
	}
	
	public static AVPlayerManager getInstance()
	{
	    if (avPlayerManager == null)
	    	avPlayerManager = new AVPlayerManager();
	    return avPlayerManager;
	}
	
	public CurrPlayerInfo getCurrPlayParam() {
		
		int []servid = new int[1];
		int []pmtpid = new int[1];
		CurrPlayerInfo pCurrPlayerInfo = new CurrPlayerInfo();
		PlayInfo pPlayInfo = mAVPlayer.new PlayInfo();
		
		if (mAVPlayer == null){
			Log.e(TAG,"GetCurrPlayParam failed mAVPlayer not init");
			return null;
		}
		
		mAVPlayer.GetCurrPlayParam(servid,pmtpid);	
		mAVPlayer.GetPlayStatusInfo(pPlayInfo, 0);
		
		pCurrPlayerInfo.servid = servid[0];
		pCurrPlayerInfo.pmtpid = pmtpid[0];
		pCurrPlayerInfo.track = pPlayInfo.Track;
		pCurrPlayerInfo.tunerLockFlag = pPlayInfo.TunerLock;
		pCurrPlayerInfo.soundInfo = pPlayInfo.SoundInfo;
		pCurrPlayerInfo.vidPID = pPlayInfo.VidPID;
		for(int i = 0; i < pPlayInfo.AudPID.length; i++)
		{
			pCurrPlayerInfo.pAudPID.add(pPlayInfo.AudPID[i]);
			Log.e(TAG,"audio pid is:" + pPlayInfo.AudPID[i]);
		}
		pCurrPlayerInfo.pcrPID = pPlayInfo.PcrPID;
		pCurrPlayerInfo.rate = pPlayInfo.Rate;
		pCurrPlayerInfo.qam = pPlayInfo.Qam;
		pCurrPlayerInfo.freq = pPlayInfo.Freq;
		
		return pCurrPlayerInfo;
	}
	
	public int stopPlayer() {
		
		if (mAVPlayer == null){
			Log.e(TAG,"stopPlay failed mAVPlayer not init");
			return 1;
		}
		
		Log.i(TAG,"stopPlayer!");
		return mAVPlayer.StopNewChannel(0);
	}
	
	/*start curr prog*/
	public int startPlayer() {
		if (mAVPlayer == null){
			Log.e(TAG,"startPlayer failed mAVPlayer not init");
			return 1;
		}
		
		if (mProglib == null){
			Log.e(TAG,"startPlayer failed mProglib not init");
			return 1;
		}
		
		if(1 == mAVPlayer.IsAVPlaying()){
			Log.e(TAG,"AV is Playing.no need replay!");
			return 0;
		}
		Log.i(TAG,"startPlayer!");
		return mAVPlayer.PlayCurrProg();
	}
	
	/**start prog by serviceid*/
	public int startPlayer(int serviceid) {

		if (mAVPlayer == null){
			Log.e(TAG,"stopPlay failed mAVPlayer not init");
			return 1;
		}
		if (mProglib == null){
			Log.e(TAG,"setAVPlayerUnMute failed mProglib not init");
			return 1;
		}
		int result = 0;
		int[] progindex = new int[1];
		int type = 0;
	
		Log.i(TAG,"startPlayer serviceid " + serviceid);
					
		type = mProglib.GetCurrProgType();
		result = mProglib.GetIndexByServID(progindex,type, serviceid, 0xFFFFFFFF);
		if(0 != result){
			Log.e(TAG,"startPlayer GetIndexByServID failed result is:" + result);
			return 1;
		}
	
		if(1 == mAVPlayer.IsAVPlaying()){
			
			Log.i(TAG,"need stop!");
			mAVPlayer.StopNewChannel(0);
		}
		
		/**set play prog index of database*/
		result = mProglib.SetCurrProgIndex(progindex[0]);
		if(0 > result){
			Log.e(TAG,"startPlayer SetCurrProgIndex failed result is:" + result);
			return 1;
		}
		
		return mAVPlayer.PlayCurrProg();
	}
	
	/**start prog by serviceid and tsid*/
	public int startPlayer(int serviceid, int tsid) {

		if (mAVPlayer == null){
			Log.e(TAG,"stopPlay failed mAVPlayer not init");
			return 1;
		}
		if (mProglib == null){
			Log.e(TAG,"setAVPlayerUnMute failed mProglib not init");
			return 1;
		}
		int result = 0;
		int[] progindex = new int[1];
		int type = 0;
		
		
		type = mProglib.GetCurrProgType();
		result = mProglib.GetIndexByServID(progindex,type, serviceid, tsid);
		if(0 != result){
			Log.e(TAG,"startPlayer GetIndexByServID failed result is:" + result);
			return 1;
		}
		
		Log.i(TAG,"startPlayer serviceid " + serviceid + "tsid " + tsid);
		if(1 == mAVPlayer.IsAVPlaying()){
			
			Log.i(TAG,"startPlayer need stop!");
			mAVPlayer.StopNewChannel(0);
		}
		
		/**set play prog index of database*/
		result = mProglib.SetCurrProgIndex(progindex[0]);
		if(0 > result){
			Log.e(TAG,"startPlayer SetCurrProgIndex failed result is:" + result);
			return 1;
		}
		
		return mAVPlayer.PlayCurrProg();
	}
	
	/**start prog by prog num*/
	public int startPlayerByProgNum(int prog_num) {

		if (mAVPlayer == null){
			Log.e(TAG,"stopPlay failed mAVPlayer not init");
			return 1;
		}
		if (mProglib == null){
			Log.e(TAG,"setAVPlayerUnMute failed mProglib not init");
			return 1;
		}
		Log.i(TAG,"startPlayerByProgNum prog_num " + prog_num);
		int tempNo = 0;
		int newPlayIndex = 0;
		int allNum[] = new int[1];

		
		mProglib.GetProgNumOfCurType(allNum);
		//Log.i(TAG,"startPlayerByProgNum allNum is " + allNum[0]);
	
		newPlayIndex = mProglib.ShowNoToProgIndexOfCurType(prog_num);
		if((-1 == newPlayIndex)||(allNum[0] <= newPlayIndex))
		{
			Log.i(TAG,"startPlayerByProgNum prog num not exist!");
			return 1;
		}
		//Log.i(TAG,"newPlayIndex " + newPlayIndex + " iProg_num " + iProg_num);
		
		if(1 == mAVPlayer.IsAVPlaying()){
			
			tempNo =  mProglib.GetCurrProgIndex();
			tempNo =  mProglib.ProgIndexToShowNoOfCurType(tempNo);
			if(tempNo == prog_num){
				Log.i(TAG,"startPlayerByProgNum the same prog.no need replay!");
				return 0;
			}
			else{		
				Log.i(TAG,"startPlayerByProgNum need stop!");
				mAVPlayer.StopNewChannel(0);
			}
		}
			
		/**set play prog index of database*/
		tempNo =mProglib.SetCurrProgIndex(newPlayIndex);
		if(-1 == tempNo) {
			return 1;
		}
		
		return mAVPlayer.PlayCurrProg();
	}
	

	/**start prog by prog num*/
	public int startPlayerByProgNum(String sProg_num) {

		if (mAVPlayer == null){
			Log.e(TAG,"stopPlay failed mAVPlayer not init");
			return 1;
		}
		if (mProglib == null){
			Log.e(TAG,"setAVPlayerUnMute failed mProglib not init");
			return 1;
		}
		
		if((sProg_num == null)||(sProg_num.equals("")))
		{
			Log.e(TAG,"startPlayerByProgNum failed sProg_num error");
			return 1;
		}

		int tempNo = 0;
		int newPlayIndex = 0;
		int allNum[] = new int[1];

		
		int iProg_num = Integer.valueOf(sProg_num).intValue(); 
		//Log.i(TAG,"startPlayerByProgNum iProg_num is " + iProg_num);
		
		mProglib.GetProgNumOfCurType(allNum);
		//Log.i(TAG,"startPlayerByProgNum allNum is " + allNum[0]);
	
		newPlayIndex = mProglib.ShowNoToProgIndexOfCurType(iProg_num);
		if((-1 == newPlayIndex)||(allNum[0] <= newPlayIndex))
		{
			Log.i(TAG,"startPlayerByProgNum prog num not exist!");
			return 1;
		}
		//Log.i(TAG,"newPlayIndex " + newPlayIndex + " iProg_num " + iProg_num);
		
		if(1 == mAVPlayer.IsAVPlaying()){
			
			tempNo =  mProglib.GetCurrProgIndex();
			tempNo =  mProglib.ProgIndexToShowNoOfCurType(tempNo);
			if(tempNo == iProg_num){
				Log.i(TAG,"startPlayerByProgNum the same prog.no need replay!");
				return 0;
			}
			else{		
				Log.i(TAG,"startPlayerByProgNum need stop!");
				mAVPlayer.StopNewChannel(0);
			}
		}
		
		/**set play prog index of database*/
		tempNo =mProglib.SetCurrProgIndex(newPlayIndex);
		if(-1 == tempNo) {
			return 1;
		}
		
		return mAVPlayer.PlayCurrProg();
	}
	
	
	
	/**play next prog*/
	public int startPlayerNextProg() {

		if (mAVPlayer == null){
			Log.e(TAG,"stopPlay failed mAVPlayer not init");
			return 1;
		}
		if (mProglib == null){
			Log.e(TAG,"setAVPlayerUnMute failed mProglib not init");
			return 1;
		}

		mAVPlayer.StopNewChannel(0);
		mProglib.StepProg(0,-1);

		return mAVPlayer.PlayCurrProg();
	}
	
	/**play last prog*/
	public int startPlayerLastProg() {

		if (mAVPlayer == null){
			Log.e(TAG,"stopPlay failed mAVPlayer not init");
			return 1;
		}
		if (mProglib == null){
			Log.e(TAG,"setAVPlayerUnMute failed mProglib not init");
			return 1;
		}
		
		mAVPlayer.StopNewChannel(0);
		mProglib.StepProg(0,1);

		return mAVPlayer.PlayCurrProg();
	}
		
	
	 public Map<String,String> getCurrProgShowInfoByMap(){  
	       
			int currProgIndex = 0;
			int result = 0;
			String num_string = null;
			String name_string = null;
			int tempNo = 0;
			
			if (mAVPlayer == null){
				Log.e(TAG,"getShowNumOfCurrProg failed mAVPlayer not init");
				return null;
			}
			
			if (mProglib == null){
				Log.e(TAG,"getShowNumOfCurrProg failed mProglib not init");
				return null;
			}
			
			currProgIndex = mProglib.GetCurrProgIndex();
			if(currProgIndex < 0 ){
				Log.e(TAG,"getShowNumOfCurrProg currProgIndex " + currProgIndex);
				return null;
			}
			
			ProgInfo info = mProglib.new ProgInfo();
			result = mProglib.GetProgInfoOfCurProg(info);
			if((0 != result)||(null == info)){
				Log.e(TAG,"setAVPlayerUnMute failed GetProgInfoOfCurProg error");
				return null;
			}
			
			tempNo = currProgIndex;
			tempNo = mProglib.ProgIndexToShowNoOfCurType(currProgIndex);
			if(-1 == tempNo)
			{
				tempNo = 1;
			}
			
			
			//num_string = String.format("%03d",tempNo);
			num_string = String.format("%d",tempNo);
			name_string = info.cServiceName;	
			Log.i(TAG,"num_string is " + num_string + "name_string is " + name_string);
			 
	        Map<String,String> map = new HashMap<String,String>();
	        
	        map.put("PROG_NUM",num_string);
	        map.put("PROG_NAME", name_string);
	
			
	        return map;
	 }
	
	
	public int setPlayerMute() {
		if (mAVPlayer == null){
			Log.e(TAG,"setAVPlayerMute failed mAVPlayer not init");
			return 1;
		}
		
		return mAVPlayer.AVMuteSound();
	}
	
	public int setPlayerUnMute() {
		if (mAVPlayer == null){
			Log.e(TAG,"setAVPlayerUnMute failed mAVPlayer not init");
			return 1;
		}
		if (mProglib == null){
			Log.e(TAG,"setAVPlayerUnMute failed mProglib not init");
			return 1;
		}
		
		return mAVPlayer.AVUmMuteSound();
	}
	

	public int setPlayerVolume( int volume) {
		int result = 0;
		
		if (mAVPlayer == null){
			Log.e(TAG,"setAVPlayerUnMute failed mAVPlayer not init");
			return 1;
		}
		
		if (mProglib == null){
			Log.e(TAG,"setAVPlayerUnMute failed mProglib not init");
			return 1;
		}
		
		ProgInfo info = mProglib.new ProgInfo();
		result = mProglib.GetProgInfoOfCurProg(info);
		if((0 != result)||(null == info)){
			Log.e(TAG,"setAVPlayerUnMute failed GetProgInfoOfCurProg error");
			return 1;
		}
		
		/**set volume to player*/
		result = mAVPlayer.AVSetSoundVolumeAdj(volume,info.VolumeAdj);
		if(0 != result){
			Log.w(TAG,"setAVPlayerUnMute failed AVSetSoundVolumeAdj error");
		}
		
		/**set volume to data base*/
		result = mProglib.SetCurrProgMangInfoParam(sw_mangparam_e.PROG_VOLUE.ordinal(), volume);
		if(0 != result){
			Log.w(TAG,"setAVPlayerUnMute failed SetCurrProgMangInfoParam error");
		}
		
		return 0;
	}
	
	
	public int setPlayerTrack( int mode) {
		
		int result = 0;
		
		if (mAVPlayer == null){
			Log.e(TAG,"setPlayerTrack failed mAVPlayer not init");
			return 1;
		}
		
		if (mProglib == null){
			Log.e(TAG,"setPlayerTrack failed mProglib not init");
			return 1;
		}
		
		/**set volume for player*/
		result = mAVPlayer.AVSetSoundMode(mode);
		if(0 != result){
			Log.w(TAG,"setPlayerTrack failed AVSetSoundMode error");
		}	
		
		/**set track to data base*/
		result = mProglib.SetCurrProgMangInfoParam(sw_mangparam_e.PROG_TRACK.ordinal(), mode);
		if(0 != result){
			Log.w(TAG,"setPlayerTrack failed SetCurrProgMangInfoParam error");
		}	
		
		return 0;
	}
	
	public int getPlayerTrack() {
		
		int result = 0;
		
		if (mAVPlayer == null){
			Log.e(TAG,"getPlayerTrack failed mAVPlayer not init");
			return 0;
		}
		
		if (mProglib == null){
			Log.e(TAG,"getPlayerTrack failed mProglib not init");
			return 0;
		}
		
		ProgList progParam = mProglib.new ProgList();
		
		/**get track to data base*/
		result = mProglib.GetMangInfoOfCurProg(progParam);
		if(0 != result){
			Log.w(TAG,"setPlayerTrack failed SetCurrProgMangInfoParam error");
		}
		
		Log.i(TAG,"progParam.track " + progParam.track);
		
		if(progParam.track >=3 )
		{
			progParam.track = 0;
		}
		
		
		return progParam.track;
	}
	
	
	public int setPlayerWindow( int x, int y, int w, int h) {
		int result = 0;
		
		if (mAVPlayer == null){
			Log.e(TAG,"setPlayerWindow failed mAVPlayer not init");
			return 1;
		}	
		
		/**set window for player*/
		result = mAVPlayer.AVSetPlayWindow(x,y,w,h,0);
		if(0 != result){
			Log.w(TAG,"setPlayerWindow failed AVSetPlayWindow error");
		}	
		
		return 0;
	}
	
	
	public int enablePlayerVideoWindow( int flag) {
		int result = 0;
		
		if (mAVPlayer == null){
			Log.e(TAG,"setPlayerTrack failed mAVPlayer not init");
			return 1;
		}	
		
		/**open or close video window*/
		result = mAVPlayer.AVShowVideoWindow(flag);
		if(0 != result){
			Log.w(TAG,"setPlayerTrack failed AVSetSoundMode error");
		}	
		
		return 0;
	}
	
	public int setResolutionMode( int mode) {
		int result = 0;
		
		if (mAVPlayer == null){
			Log.e(TAG,"setResolutionMode failed mAVPlayer not init");
			return 1;
		}	
		
		/**set resolution*/
		result = mAVPlayer.AVSetEncodingMode(mode);
		if(0 != result){
			Log.w(TAG,"setResolutionMode failed AVSetEncodingMode error");
		}	
		return 0;
	}
	
	public int getResolutionMode() {
			
		if (mAVPlayer == null){
			Log.e(TAG,"getResolutionMode failed mAVPlayer not init");
			return 1;
		}	

		return mAVPlayer.AVGetEncodingMode();
	}
	
	public int setAspectRatioMode( int mode) {
		int result = 0;
		
		if (mAVPlayer == null){
			Log.e(TAG,"SWAV_SetAspectRatio failed mAVPlayer not init");
			return 1;
		}	
		
		/**set resolution*/
		result = mAVPlayer.AVSetAspectRatio(mode);
		if(0 != result){
			Log.w(TAG,"SWAV_SetAspectRatio failed AVSetAspectRatio error");
		}	
		return 0;
	}
	
	public int setScreenRatioMode( int ratio) {
		int result = 0;
		
		if (mAVPlayer == null){
			Log.e(TAG,"setScreenRatioMode failed mAVPlayer not init");
			return 1;
		}	
		
		/**set ScreenRatio*/
		result = mAVPlayer.AVSetScreenRatio(ratio);
		if(0 != result){
			Log.w(TAG,"setScreenRatioMode failed AVSetScreenRatio error");
		}	
		return 0;
	}

	public void setOnAVPlayEventListener(AVPlayEventListener listener)
	{
		onAVPlayEventListener = listener;
	}
}
