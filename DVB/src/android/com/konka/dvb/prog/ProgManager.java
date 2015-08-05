package android.com.konka.dvb.prog;

import java.util.ArrayList;
import java.util.Arrays;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.IDVBListener;
import android.com.konka.dvb.Proglib;
import android.com.konka.dvb.Proglib.BatType;
import android.com.konka.dvb.Proglib.FavProg;
import android.com.konka.dvb.Proglib.ProgInfo;
import android.util.Log;

public class ProgManager {
	
	public static class ProgShowInfo {
	
		public String prog_num;
		public String prog_name;

	}
	
	public static class ProgListShowInfo {
		
		public String currPlay_ProgNum;/*record currvern play prog num*/
		public int currPlay_type;
		
        public ArrayList<ProgShowInfo> pProgList;
        
        public ProgListShowInfo()
        {
        	pProgList = new ArrayList<ProgShowInfo>();
        }
        
        public boolean add(String prog_num, String prog_name)
        {
            ProgShowInfo progshowinfo = new ProgShowInfo();
            
            //Log.v(TAG, "prog_num "+prog_num);
            //Log.v(TAG, "prog_name "+prog_name);
            
            progshowinfo.prog_num = prog_num;
            progshowinfo.prog_name = prog_name;
            
            this.pProgList.add(progshowinfo);
            
            return true;
        }
        
        public ArrayList<ProgShowInfo> get()
        {
            return this.pProgList;
        }
	}
	
	private final static String TAG = "Proglib";
	private static Proglib mProglib = null;
	private static ProgManager progManager = null;
	private static ProgEventListener onProgEventListener = null;

	
	private ProgManager()
    {
		if(!DVB.IsInstanced())
		{
			DVB.GetInstance().create("unfdemo");
		}
	
		mProglib = DVB.GetInstance().GetProglibInstance();
		if(mProglib == null){
			Log.e(TAG,"ProgManager init error");
			return ;
		}
	
		DVB.GetInstance().SubScribeEvent(DVB.Event.KK_SUBFORPROG, new IDVBListener() {
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
			
			if(null != onProgEventListener){
				
			}
				
		}
	}, 0);
	   	
	}
	
	public static ProgManager getInstance()
	{
	    if (progManager == null)
	    	progManager = new ProgManager();
	    return progManager;
	}
	
	/*get prog list info*/
	public ProgListShowInfo getCurrverntProgListInfo(){
		int type = 0;
		int play_index = 0;
		int[] totalnum = new int[1];
		int result = 0;
		Proglib.ProgInfo info = mProglib.new ProgInfo();
		int tempNo = 0;
		int Play_tempNo = 0;
				
		ProgListShowInfo pProgListShowInfo = null;
	
		if(mProglib == null){
			Log.e(TAG,"getSearchResultInfo mProglib init error");
			return null;
		}	
	
		type = mProglib.GetCurrProgType();
		Log.w(TAG,"type is " + type);
		play_index = mProglib.GetCurrProgIndex();
		
		result = mProglib.GetProgNumOfType(totalnum,type);
		if(0 != result){
			Log.e(TAG,"GetProgNumOfType error" + result + totalnum[0]);
			return null;
		}

		
		pProgListShowInfo = new ProgListShowInfo();	
		
		Log.i(TAG,"currPlay_type is " + type);
		Log.i(TAG,"currPlay_ProgNum is " + play_index);
		Log.i(TAG,"all num is " + totalnum[0]);
	
		for(int i = 0; i < totalnum[0]; i++)
		{
			result = mProglib.GetProgInfoByTypeIndex(i, type, info);
			if((0!= result)||(info == null)){
				Log.w(TAG,"GetProgInfoByTypeIndex failed");
				return null;
			}	

			tempNo =mProglib.ProgIndexToShowNo(type,i);
			pProgListShowInfo.add(String.format("%03d",tempNo),info.cServiceName);
			//Log.i(TAG,"tempNo is " + tempNo + " prog name " + info.cServiceName);
			
		}
		

		Play_tempNo =mProglib.ProgIndexToShowNo(type,play_index);
		
		pProgListShowInfo.currPlay_type = type;
		pProgListShowInfo.currPlay_ProgNum = String.format("%03d",Play_tempNo);
	
		
		return pProgListShowInfo;
	}
	
	public String getProgNumByServiceid(int serviceid)
	{
		
		String sProg_Num = new String();
		int result = 0;
		int[] progindex = new int[1];
		int type = 0;
		int tempNo = 0;
		
		if(mProglib == null){
			Log.e(TAG,"factorySet mProglib init error");
			return null;
		}	
		
		type = mProglib.GetCurrProgType();
		result = mProglib.GetIndexByServID(progindex,type, serviceid, 0xFFFFFFFF);
		if(0 != result){
			Log.e(TAG,"startPlayer GetIndexByServID failed result is:" + result);
			return null;
		}
		
		tempNo = mProglib.ProgIndexToShowNoOfCurType(progindex[0]);
		if(-1 == tempNo)
		{
			tempNo = 1;
		}
		
		
		sProg_Num = String.format("%d",tempNo);
		
		return sProg_Num;
	}
	
	public int factorySet()
	{
		if(mProglib == null){
			Log.e(TAG,"factorySet mProglib init error");
			return 0;
		}	
		
		return mProglib.FactorySet();
	}
	
	public String GetAdFileNameByProNo(int index)
	{
		byte [] AdFile = new byte[100];
		
		int res = 0;
		
		for(int i = 0; i < 100;i++)
			AdFile[i]=0;
		
		if(mProglib == null){
			Log.e(TAG,"GetAdFileNameByProNo mProglib ini error");
			return null;
		}	
		
		res =  mProglib.GetAdFileNameByProNo(index,AdFile);
		
		int temp_length = 0;
		for(int i=0; i<AdFile.length; i++)
		{
			if(AdFile[i] == 0)
			{
				break;
			}
			temp_length ++;
		}
		
		byte[] result = Arrays.copyOf(AdFile, temp_length);
		
		String str= new String(result);

		
		Log.e(TAG,"GetAdFileNameByProNo res:"+res+":"+str);
		
		if(res==0)
			return str;
					
					
		
		return null;
	}
	
	
	public void setOnProgEventListener(ProgEventListener listener)
	{
		onProgEventListener = listener;
	}
}
