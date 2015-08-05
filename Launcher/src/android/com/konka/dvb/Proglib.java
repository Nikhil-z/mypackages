package android.com.konka.dvb;

import java.util.ArrayList;

import android.os.Parcel;
import android.util.Log;
import java.io.UnsupportedEncodingException;

public class Proglib {

	private DVB mDVB;
	private int mNativeContext; // accessed by native methods
	private final static String TAG = "PROGLIB";

	public Proglib(DVB dvb)
	{
		mDVB = dvb;
		mNativeContext = dvb.mNativeContext;
		Log.d(TAG, "Proglib  construct"+ mNativeContext);
	}

	protected void finalize()
	{
	}

	/*���ڽ�Ŀ���ݿ�Ĳ���**/
	/**writenow ���� 0 ��ʾ����д��E2P*/
	/**���������java�㲻��Ҫ�ٴη�װ�����ˣ����Ѿ���dvb��init��ͳһ����*/
	public final native int InitDbase( int writenow);

	/*���úͻ�ȡֵtype SW_DBaseParam_e**/
	public final native  int SetDbaseParam(int type,int value,int writenow);
	public final native  int GetDbaseParam(int type,int[] value);
	/*��������E2P����Ŀ�����ݿ�*/
	public final native  int ResetDbase(int writenow);
	
/**type   SW_ProgType_e*/
	/**��Ŀ�༭*/
	public final native  int DeleteProgByIndex(int type,int index);
	public final native  int SwapProgByIndex(int type,int index1,int index2);

	/*��ȡ��ǰ������ص�һЩ��Ϣ*/
	public final native  int GetCurrProgType();
	public final native  int GetCurrProgIndex();
	public final native  int GetLastProgIndex();
	public final native  int SetLastProgIndex(int type,int index);
	public final native  int SetCurrProgType(int type);
	public final native  int SetCurrProgIndex(int index);
	/*user:SW_UserType_e ��������Ŀ�б���ѭ��ѡ����һ����Ŀ������һ����Ŀ*/
	public final native  int StepProg(int user,int step);

	
			/*tsid ����Ϊ����ֵ*/
	public final native  int GetIndexByServID(int[] index,int type,int servid,int tsid);
	public final native  int GetIndexTypeByServID(int[] index,int[] type,int servid,int tsid);

	/**��ȡ��Ŀ����*/
	public final native  int GetProgNumOfCurType(int[] TotalNum);
	public final native  int GetProgNumOfType(int[] TotalNum,int type);

/*��ȡ��Ŀ��Ϣ*/
	/*����servid����ȡ��Ŀ��Ϣ*/
	public final native  int GetProgInfoByServID(int tsid,int servid,ProgInfo info);
	/*��ȡ��ǰ���Ž�Ŀ����Ϣ*/
	public final native  int GetProgInfoOfCurProg(ProgInfo info);
	/*�����������ݿ��INDEX����ȡ��Ŀ��Ϣ*/
	public final native  int GetProgInfoByIndex(int index,ProgInfo info);
	/*���ݽ�Ŀ���ͼ�INDEX����ȡ��Ŀ��Ϣ**/
	public final native  int GetProgInfoByTypeIndex(int index,int type,ProgInfo info);
	public final native  int GetProgInfoByIndexOfCurType( int index,ProgInfo info);


	/*��Ŀ�༭��Ϣ**/
	public final native  int GetMangInfoOfCurProg(ProgList MangInfo);
	public final native  int GetProgMangInfoOfCurrType(int index,ProgList MangInfo);
	public final native  int GetProgMangInfoOfType(int  type,int index,ProgList MangInfo);

	public final native  int SetupDbaseRamMap(int type,int[] markid);
	public final native  int CopyDbaseToRamMap(int type,int oldindex,int newindex,int markid);
	public final native  int CopyRamMapToDbase(int type,int maxnum,int markid);
	/*param:SW_MangParam_e*/
	public final native  int SetCurrProgMangInfoParam(int  param,int  value);
	public final native  int SetProgMangInfoParam(int type,int  index,int  param,int value);

	/*��Ŀ�ź������ŵ��л�*/	
	public final native  int ProgIndexToShowNo(int type,int index );
	public final native  int ShowNoToProgIndex(int  type,int  showno);
	public final native  int ShowNoToProgIndexOfCurType(int  showno);
	public final native  int ProgIndexToShowNoOfCurType(int index);

	/**TS����Ϣ*/
	public final native  int GetFreqOfServiceID( int serviceid, int tsid,  int []TsId, int []freq);
	public final native  int GetTsInfoOfFreq( int freq,  int[] tsid, int[] rate, int[] qam);
	public final native  int GetFreqInfoOfTSID(int TSID, int[] freq, int[] qam, int[] rate);
	public final native  int GetFreqInfoOfCurrProg(int[] freq,int[] qam,int[] rate);
	public final native  int GetFreqInfoOfStockServiceType(int[] serviceid,int[] TsId,int[] freq,int service_type);
	/**BAT�����Ϣ*/
	/*param��������û��*/
	public final native  int GetBouquetIDNum(int param);
	public final native  int GetBouquetNameByID(int batid,int param,BatType Bat);
	public final native  int GetBouquetIDInfoByIndex(int index,int param,BatType Bat);

	/*ϲ���б�paramû����**/
	public final native  int GetFavProgHeader(int  type,int  fav,int[] header,int param);
	public final native  int GetFavProgByIndex(int type,int fav,int curr,
		 int[] next,int param,FavProg Fav);
	public final native  int FactorySet();
	public final native  int GetAdFileNameByProNo(int index,byte[] AdFile);
	
/*
	public final native int GetProgNumOfType(int type);
	public final native int GetProgInfoOfType(int index,int type, ProgInfo info);
	public final native int ProgIndexToShowNo(int type,int index);
	public final native int GetCurrProgType();
	public final native int GetCurrProgIndex();
	public final native int SetCurrProgType(int type);
	public final native int SetCurrProgIndex(int index);
*/
	public class ProgInfo
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

		public ProgInfo()
		{
			ucAudStrType = new char[2];
			sAudPid = new short[2];
		}
	}

	public class ProgList{
		public short used;
		public short lock;
		public short volue;
		public short fav;
		public short user;
		public short track; 
		public short index;
		public short type;
	}

	public class BatType
	{
		public char status; 
		public short batid;
		String name; 
	}


	public class FavProg
	{
		public char status; 
		public short progindex;
		String	name;
	}
}


