package android.com.konka.dvb.search;

import java.util.ArrayList;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.IDVBListener;
import android.com.konka.dvb.ProgSearch;
import android.com.konka.dvb.ProgSearch.ProgType;
import android.com.konka.dvb.ProgSearch.Prog_Info;
import android.com.konka.dvb.tuner.TunerManager;
import android.com.konka.dvb.tuner.TunerManager.TunerInfo;
import android.util.Log;

public class SearchManager {
	
    protected enum eProgSearchStatus
    {
        PROG_ONETSOK,/*0*/
        PROG_ONETSTIMEOUT,/*1*/
        PROG_ONETSFAIL,/*2*/
        PROG_NITSENDFAIL,/*3*/
        PROG_NITDATAOK,/*4*/
        PROG_STARTSEARCH,/*5*/
        PROG_SEARCHFINISH,/*6*/
        PROG_NITLOCKFAIL,/*7*/
        
        PROG_NITLOCKING,/*8*/
        PROG_BATLOCKFAIL,/*9*/
        PROG_ONETSLOCKFAIL,/*10*/
        PROG_DBREFLESH,/*11*/
        PROG_SETSEARCH_FREQMUN,/*12*/
    }
	
	public static class Prog_SearchShowInfo {
		
		public String sProgName;

	}

	public static class SearchResultInfo {
		
		public int freq;

		public int qam;

		public int rate;

		public int all_prog_num; 
		
		public int tvprog_num;
		
		public int gbprog_num;
		
		public int refnvod_num;
		
		public int shiftnvod_num;
		
		public int dbc_num;
		
		public int mosaic_num;
		
		public int other_num;
			
        public ArrayList<Prog_SearchShowInfo> pProgInfo;
        
        public SearchResultInfo()
        {
        	pProgInfo = new ArrayList<Prog_SearchShowInfo>();
        }
        public boolean add(String name)
        {
        	Prog_SearchShowInfo info = new Prog_SearchShowInfo();
            Log.v(TAG, "name "+name);
            info.sProgName = name;
            this.pProgInfo.add(info);
            return true;
        }
        public ArrayList<Prog_SearchShowInfo> get()
        {
            return this.pProgInfo;
        }
	
	}
	
	private final static String QAM256 = "QAM256";
	private final static String QAM128 = "QAM128";
	private final static String QAM64 = "QAM64";
	private final static String QAM32 = "QAM32";
	private final static String QAM16 = "QAM16";
	private final static String QAM8 = "QAM8";
	private final static String TAG = "ProgSearch";
	private static ProgSearch mProgSearch = null;
	private static SearchManager searchManager = null;
	private static int SEARCH_MAX_PROCESS = 0;
	private final static int PROG_SEARCHONETS = 0;
	private final static int PROG_SEARCHBYNIT = 1;
	private final static int PROG_SEARCHBYNET = 2;
	private static int currVernSearchType = PROG_SEARCHBYNIT;
	private static SearchEventListener onSearchEventListener = null;
	//private static SearchResultInfo searchResultInfo = null;
	public  static TunerManager mtunerManager = null;

    private SearchManager()
    {
		if(!DVB.IsInstanced())
		{
			DVB.GetInstance().create("unfdemo");
		}

		mProgSearch = DVB.GetInstance().GetProgSearchInstance();
		mtunerManager = TunerManager.getInstance();
		if((mProgSearch == null)||(mtunerManager == null)){
			Log.e(TAG,"SearchManager init error");
			return ;
		}
       	
       	DVB.GetInstance().SubScribeEvent(DVB.Event.KK_SEARCH, new IDVBListener() {
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
	
			if(mProgSearch == null){
	     		Log.e(TAG,"mProgSearch not init");
	    		return ;
	     	}
		
			eProgSearchStatus ev[] = eProgSearchStatus.values();

			//底层触发用来更新显示界面 arg6：rate，qam arg7：freq
			//Log.v(TAG, "SEARCH_BOX event: ("+arg0+","+arg1+","+arg2+","+arg3+","+arg4+","+arg5+","+arg6+","+arg7);
			switch(ev[arg3])
			{
				case PROG_ONETSOK:/*0*/
					//Log.v(TAG,"search prog one ts ok. step is " + arg5 + " SEARCH_MAX_PROCESS is " + SEARCH_MAX_PROCESS);
					if(null != onSearchEventListener)
					{
						int freq = arg7;
						/*get prog info*/
						SearchResultInfo pSearchResultInfo = getSearchResultInfo(freq);
						if(null != pSearchResultInfo)
						{
							int step = arg5;
							onSearchEventListener.onUpdateSearchProgress(step,SEARCH_MAX_PROCESS);
							onSearchEventListener.onUpdateProgInfoOfCurrvernFreq(freq,pSearchResultInfo);
						}
					}
					break;
					
				case PROG_ONETSTIMEOUT:/*1*/
					//Log.v(TAG,"search one ts time out. step is " + arg5 + " SEARCH_MAX_PROCESS is " + SEARCH_MAX_PROCESS);
					if(null != onSearchEventListener)
					{
						int freq = arg7;
						int step = arg5;
						
						/*get prog info*/
						SearchResultInfo pSearchResultInfo = getSearchResultInfo(freq);
						if(null != pSearchResultInfo)
						{
						
							onSearchEventListener.onUpdateProgInfoOfCurrvernFreq(freq,pSearchResultInfo);
						}
						
						onSearchEventListener.onUpdateSearchProgress(step,SEARCH_MAX_PROCESS);
					}
					break;
					
				case PROG_ONETSFAIL:/*2*/
					//Log.v(TAG,"search prog one ts failed. step is " + arg5 + " SEARCH_MAX_PROCESS is " + SEARCH_MAX_PROCESS);
					if(null != onSearchEventListener)
					{
						int freq = arg7;
						int step = arg5;
						/*get prog info*/
						SearchResultInfo pSearchResultInfo = getSearchResultInfo(freq);
						if(null != pSearchResultInfo)
						{
							onSearchEventListener.onUpdateProgInfoOfCurrvernFreq(freq,pSearchResultInfo);
						}
						onSearchEventListener.onUpdateSearchProgress(step,SEARCH_MAX_PROCESS);
					}
					break;
					
				case PROG_NITSENDFAIL:/*3*/
					//Log.v(TAG,"search nit failed");
					if(null != onSearchEventListener){
						searchManager.stopSearch();
						onSearchEventListener.onSearchNitFailed();
					}
					break;
					
				case PROG_NITDATAOK:/*4*/
					//Log.v(TAG,"search nit data success SEARCH_MAX_PROCESS is " + arg4);
					SEARCH_MAX_PROCESS = arg4;
					if(null != onSearchEventListener){
						onSearchEventListener.onUpdateSearchProgress(0,SEARCH_MAX_PROCESS);
						onSearchEventListener.onReceiveNitTable();				
					}
					break;
					
				case PROG_STARTSEARCH:/*5*/
					//Log.v(TAG,"search start");
					if(null != onSearchEventListener){
						TunerInfo tunerInfo = null;
						
						/**send process*/
						if(currVernSearchType == PROG_SEARCHONETS){
							onSearchEventListener.onUpdateSearchProgress(0,SEARCH_MAX_PROCESS);
						}
						
						tunerInfo = mtunerManager.GetInfo();
						onSearchEventListener.onUpdateSingalInfo(tunerInfo);
					}
					break;
					
				case PROG_SEARCHFINISH:/*6*/
					Log.v(TAG,"search finish");
					if(null != mProgSearch)
					{
						/*exit search and save prog*/
						mProgSearch.ExitSearch(currVernSearchType);
					}
					if(null != onSearchEventListener){
						searchManager.stopSearch();
						onSearchEventListener.onSearchAllDataFinish();
					}
					break;
					
				case PROG_NITLOCKFAIL:/*7*/
					Log.v(TAG,"lock nit freq failed");
					break;		   
					
				case PROG_NITLOCKING:/*8*/
					Log.v(TAG,"lock nit freq");
					if(null != onSearchEventListener){
						
						/**add for nit search*/
						TunerInfo tunerInfo = null;
						
						//Log.v(TAG,"lock nit ---->>>");
						onSearchEventListener.onUpdateSearchProgress(0,100);
						
						tunerInfo = mtunerManager.GetInfo();
						
						//Log.v(TAG,"lock nit BerRatio ---->>>  " + tunerInfo.BerRatio);
						//Log.v(TAG,"lock nit Quality ---->>>  " + tunerInfo.Quality);
						//Log.v(TAG,"lock nit Strength ---->>>  " + tunerInfo.Strength);
						onSearchEventListener.onUpdateSingalInfo(tunerInfo);
					}
					break;
					
				case PROG_BATLOCKFAIL:/*9*/
					Log.v(TAG,"search bat table failed");
					break;
					
				case PROG_ONETSLOCKFAIL:/*10*/
					Log.v(TAG,"search currvernt ts failed");
					break;
					
				case PROG_DBREFLESH:/*11*/
					Log.v(TAG,"database reflesh");
					break;
					
				case PROG_SETSEARCH_FREQMUN:/*12*/
					Log.v(TAG,"set freq num SEARCH_MAX_PROCESS:" + arg4);
					SEARCH_MAX_PROCESS = arg4;
					if(null != onSearchEventListener){
									
						onSearchEventListener.onUpdateSearchProgress(0,SEARCH_MAX_PROCESS);
					}
					break;
					
				default:
					Log.v(TAG,"MESSAGE NOT VALID");
					break;
			}  
		}
	}, 0);
       	
    }
    
    public static SearchManager getInstance()
    {
        if (searchManager == null)
        	searchManager = new SearchManager();
        return searchManager;
    }
	
	/**
	\brief 通过NIT主频点搜�?
	\attention \n
	�?
	\retval :0 ,success,  other failed
	\see \n
	**/
	public int startSearchByNit() {
		
		if(mProgSearch == null){
			Log.e(TAG,"startSearchByNit failed mProgSearch not init");
			return 1;
		}
		
		SEARCH_MAX_PROCESS = 0;
		currVernSearchType = PROG_SEARCHBYNIT;
		return mProgSearch.StartSearch(PROG_SEARCHBYNIT, 0, 0, 0,0);
	}

	/**
	\brief 全频搜索
	\attention \n
	�?
	\retval :0 ,success,  other failed
	\see \n
	**/
	public int startSearchByNetwork() {
		
		if(mProgSearch == null){
			Log.e(TAG,"startSearchByNetwork failed mProgSearch not init");
			return 1;
		}
		
		SEARCH_MAX_PROCESS = 0;
		currVernSearchType = PROG_SEARCHBYNET;
		return mProgSearch.StartSearch(PROG_SEARCHBYNET, 0, 0, 0,0);
	}
	
	/**
	\brief 指定频点搜索
	\attention \n
	�?
	\param[in]  frep       			   搜索频点.
	\param[in]  rate       			   符号�?
	\param[in]  qam       			   调制方式.
	\retval :0 ,success,  other failed
	\see \n
	**/
	public int startSearchByManu(int freq,int rate,int qam) {
		
		if(mProgSearch == null){
			Log.e(TAG,"startSearchByManu failed mProgSearch not init");
			return 1;
		}
		
		SEARCH_MAX_PROCESS = 1;
		currVernSearchType = PROG_SEARCHONETS;
		return mProgSearch.StartSearch(PROG_SEARCHONETS, freq, rate, qam,0);

	}
	
	/**
	\brief 指定频点搜索
	\attention \n
	�?
	\param[in]  frep       			   搜索频点.
	\param[in]  rate       			   符号�?
	\param[]  qam       			   调制方式.
	\retval :0 ,success,  other failed
	\see \n
	**/
	public int startSearchByManu(int freq,int rate,String sQam) {
		
		int iQam = 0;
		
		if(mProgSearch == null){
			Log.e(TAG,"startSearchByManu failed mProgSearch not init");
			return 1;
		}
		
		if((sQam == null)||(sQam.equals("")))
		{
			Log.e(TAG,"startSearchByManu failed Qam error");
			return 1;
		}
		
		if(sQam.equals(QAM8)){
			iQam = 0;
		}else if(sQam.equals(QAM16)){
			iQam = 1;
		}else if(sQam.equals(QAM32)){
			iQam = 2;
		}else if(sQam.equals(QAM64)){
			iQam = 3;
		}else if(sQam.equals(QAM128)){
			iQam = 4;
		}else if(sQam.equals(QAM256)){
			iQam = 5;
		}else{
			iQam = 3;
		}
		
		Log.e(TAG,"startSearchByManu freq:"+freq +"rate:"+rate + "Qam:"+ iQam);
		SEARCH_MAX_PROCESS = 1;
		currVernSearchType = PROG_SEARCHONETS;
		return mProgSearch.StartSearch(PROG_SEARCHONETS, freq, rate, iQam,0);

	}
	
	/**
	\brief 退出节目搜�?
	\attention \n
	�?
	\see \n
	**/
	public int stopSearch() {
		
		if(mProgSearch == null){
			Log.e(TAG,"stopSearch failed mProgSearch not init");
			return 1;
		}
		
		return mProgSearch.ExitSearch(currVernSearchType);
	}
	
	/**
	\brief 获取节目搜索信息
	\attention \n
	�?
	\param[in]  frep       			   当前搜索频点.
	\param[out]SearchResultInfo      用于输出节目信息.
	\retval ::SearchResultInfo: null, other failed
	\see \n
	**/
	private SearchResultInfo getSearchResultInfo(int freq){
		
		int[] num = new int[1];
		int[] arg0 = new int[1];
		int[] mrate = new int[1];
		int[] mqam = new int[1];
		int ret = 0;
		int index = 0;
		Prog_SearchShowInfo pProgInfo = null;
		int[] tvprog_num = new int[1];
		int[] gbprog_num = new int[1];
		int[] refnvod_num = new int[1];
		int[] shiftnvod_num = new int[1];
		int[] dbc_num = new int[1];
		int[] mosaic_num = new int[1];
		int[] other_num = new int[1];		
		
		SearchResultInfo searchResultInfo = null;
			
		if(mProgSearch == null){
			Log.e(TAG,"getSearchResultInfo failed mProgSearch not init");
			return null;
		}
		
		searchResultInfo = new SearchResultInfo();
		Prog_Info info = mProgSearch.new Prog_Info();
		
		/*get prog num by freq*/
		ret = mProgSearch.GetTsSearchRes(freq, num, arg0, info);
		Log.v(TAG,"GetTsSearchRes  ret:" + ret + "mum is " + num[0]);
		if( 0!= ret){
			Log.w(TAG,"GetTsSearchRes failed");
			return null;
		}
		
		/*get rate\qam info by freq.1 is return success*/
		ret = mProgSearch.GetProgSearchInfo(freq, mrate, mqam, arg0);
		if( 1!= ret){
			Log.w(TAG,"GetProgSearchInfo failed");
			return null;
		}
		
		/*get prog info by freq*/
		for(index = 0; index < num[0]; index++)
		{
			ret = mProgSearch.GetProgSearchRes( freq,  index, arg0, info);
			if((0!= ret)||(info == null)){
				Log.w(TAG,"GetProgSearchRes failed");
				return null;
			}
			
			if(pProgInfo == null){
				pProgInfo = new Prog_SearchShowInfo();
			}
			//Log.i(TAG,"pProgInfo.sProgName is " + pProgInfo.sProgName + "num is" + num[0]);
			
			searchResultInfo.add(info.cServiceName);
			
		}
	

		ret = mProgSearch.GetProgNumOfThisSearch(freq, 0, tvprog_num);
		if((0!= ret)){
			Log.w(TAG,"GetProgNumOfThisSearch failed");
			return null;
		}
		ret = mProgSearch.GetProgNumOfThisSearch(freq, 1, gbprog_num);
		if((0!= ret)){
			Log.w(TAG,"GetProgNumOfThisSearch failed");
			return null;
		}
		ret = mProgSearch.GetProgNumOfThisSearch(freq, 2, refnvod_num);
		if((0!= ret)){
			Log.w(TAG,"GetProgNumOfThisSearch failed");
			return null;
		}
		
		ret = mProgSearch.GetProgNumOfThisSearch(freq, 3, shiftnvod_num);
		if((0!= ret)){
			Log.w(TAG,"GetProgNumOfThisSearch failed");
			return null;
		}
		
		ret = mProgSearch.GetProgNumOfThisSearch(freq, 4, dbc_num);
		if((0!= ret)){
			Log.w(TAG,"GetProgNumOfThisSearch dbc_num failed");
			return null;
		}
		
		ret = mProgSearch.GetProgNumOfThisSearch(freq, 5, mosaic_num);
		if((0!= ret)){
			Log.w(TAG,"GetProgNumOfThisSearch mosaic_num failed");
			return null;
		}
		
		ret = mProgSearch.GetProgNumOfThisSearch(freq, 6, other_num);
		if((0!= ret)){
			Log.w(TAG,"GetProgNumOfThisSearch other_num failed");
			return null;
		}
		
		Log.w(TAG,"search freq :" + freq + "prog num is :" + searchResultInfo.pProgInfo.size());
		for (int i = 0; i < searchResultInfo.pProgInfo.size(); i++ ){

			  Prog_SearchShowInfo progInfo = (Prog_SearchShowInfo) searchResultInfo.pProgInfo.get(i);
			  //System.out.println(book.getName());
			  Log.w(TAG,"prog name is:" + progInfo.sProgName + "size is " + searchResultInfo.pProgInfo.size());
		}

		searchResultInfo.freq = freq;
		searchResultInfo.all_prog_num = num[0];
		searchResultInfo.qam = mqam[0];
		searchResultInfo.rate = mrate[0];
		
		searchResultInfo.tvprog_num = tvprog_num[0];
		searchResultInfo.gbprog_num =  gbprog_num[0];	
		searchResultInfo.refnvod_num = refnvod_num[0];	
		searchResultInfo.shiftnvod_num = shiftnvod_num[0];	
		searchResultInfo.dbc_num = dbc_num[0];
		searchResultInfo.mosaic_num = mosaic_num[0];	
		searchResultInfo.other_num = other_num[0];
		
		/*
		Log.i(TAG,"tvprog_num is : " + tvprog_num[0]);
		Log.i(TAG,"gbprog_num is : " + gbprog_num[0]);
		Log.i(TAG,"refnvod_num is : " + refnvod_num[0]);
		Log.i(TAG,"shiftnvod_num is : " + shiftnvod_num[0]);
		Log.i(TAG,"dbc_num is : " + dbc_num[0]);
		Log.i(TAG,"other_num is : " + other_num[0]);
		*/
		
				
		return searchResultInfo;
	}


    public void setOnSearchEventListener(SearchEventListener listener)
    {
    	onSearchEventListener = listener;
    }
}
