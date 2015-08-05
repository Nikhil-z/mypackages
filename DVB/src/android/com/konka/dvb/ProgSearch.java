package android.com.konka.dvb;


import android.os.Parcel;
import android.util.Log;
import java.util.ArrayList;

public class ProgSearch{

    private DVB mDVB;
    private int mNativeContext; // accessed by native methods
    private final static String TAG = "DVB";
    private final static int BATID_MAXNUM = 32;
    private final static int SWSOUNDNUM = 2;
    private final static int SWSERVNAMELEN = 30;

    public ProgSearch(DVB dvb)
    {
        mDVB = dvb;
        mNativeContext = dvb.mNativeContext;
        Log.d(TAG, "ProgSearch construct"+ mNativeContext);     
    }
	
	public final native int StartSearch(int type,int freq,int rate,int qam,int param);
	public final native int ExitSearch(int type);
	public final native int GetTsSearchRes( int freq, int[] num,  int[] next,Prog_Info info);
	public final native int GetProgSearchRes( int freq,  int index,  int[] next,Prog_Info info);
	public final native int GetProgNumOfThisSearch(int freq,int progtype,int[] num);
	public final native int GetProgSearchInfo(int freq,int[] rate,int[] qam,int[] fec);
/*
    public final native int ProgSearchNativeStartSearch(int type,int freq, int rate, int qam,int param);
    public final native int ProgSearchNativeExitSearch(int param);
    public final native int ProgSearchNativeGetCallbackInfo(Progsearch_Res result);
    public final native int ProgSearchNativeGetTsSearchRes(int freq,int[] num,Prog_Info info); 
    public final native int ProgSearchNativeGetProgSearchRes(int freq,int index,Prog_Info info);
    public final native int ProgSearchNativeGetProgSearchInfo(int freq,int[] rate,int[] qam);
    public final native int ProgSearchNativeGetProgNumOfThisSarch(int freq,ProgNum_Type typenum);
    public final native int GetInfoOfCurrProg(Prog_Info info);
*/
    public enum Search_Type
    {
        PROG_SEARCHONETS(0),
        PROG_SEARCHBYNIT(1),
        PROG_SEARCHBYNET(2);
        private int value;
        private Search_Type(int value)
        {
            this.value = value;
        }
        public int value() {
            return this.value;
        }
    }

    public enum SearchCallbackType
    {
        SEARCH_TELLRESULT(0),
        SEARCH_OTHER(1);
        private int value;
        private SearchCallbackType(int value)
        {
            this.value = value;
        }
        public int value() {
            return this.value;
        }
    }

    public class Progsearch_Info
    {
        public int ServiceType;
        public String ServiceName;
    }

    public class Progsearch_Res
    {
        public int status;
        public int freq;
        public int qam;
        public int rate;
        public int CurSearchIndex;
        public int AllSearchNum;
        public int CurTVNum;
        public int CurGBNum;
        public int CurDBCNum;
        public int CurOtherNum;
        public int AllTVNum;
        public int AllGBNum;
        public int AllDBCNum;
        public int AllOtherNum;
        public int ServiceNum;
        public ArrayList<Progsearch_Info> ServiceInfo;
        public Progsearch_Res()
        {
            ServiceInfo = new ArrayList<Progsearch_Info>();
        }
        public boolean add(String name,int type)
        {
            Progsearch_Info info = new Progsearch_Info();
            Log.v("DVB", "name "+name);
            info.ServiceName = name;
            info.ServiceType = type;
            this.ServiceInfo.add(info);
            return true;
        }
        public ArrayList<Progsearch_Info> get()
        {
            return this.ServiceInfo;
        }
    }
    public enum ProgType
    {
    	TVPROG,
    	GBPROG ,
    	REFNVOD ,
    	SHIFTNVOD,
    	DBCPROG,
    	MOSAIC,
    	OTHER,
    	TYPE_NUM
    }

    public class ProgNum_Type
    {
    	public int used;
    	public short[]  prognum;
        public ProgNum_Type()
        {
            prognum = new short[ProgType.TYPE_NUM.ordinal()];
        }
    }

    public class Prog_Info
    {
		public char	status;  
		public char	reserve; 

		public char	ucVidStrType;
		public char	sound_num;
		public char	cServiceType;
		public char	VolumeAdj;
		public char	AudioDirection;
		public char	volumetrack;
		public char	camode;
		public short	bouquetid;
		public short	comptag;

		public short 	tsid;
		public short 	networkid;
		public short 	serviceid;

		public short   	sPmtPid;
		public short   	sPcrPid;
		public short   	sVidPid;
		public short	sNvodindex;
		public int		freq;
		public int		logicindex;

		public char[]	ucAudStrType;
		public short[]	sAudPid;
		public String	cServiceName;
	public Prog_Info()
        {
            ucAudStrType = new char[SWSOUNDNUM];
            sAudPid = new short[SWSOUNDNUM];
        }
    }
}
