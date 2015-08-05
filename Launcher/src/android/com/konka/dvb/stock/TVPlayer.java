
package android.com.konka.dvb.stock;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.com.konka.dvb.AVPlayer;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.Proglib;
import android.com.konka.dvb.av.AVPlayerManager;
import android.util.Log;
import android.view.View;

@SuppressLint("NewApi")
public class TVPlayer { 
	
	private static AVPlayerManager mAVPlayerManager = null;
	private static TVPlayer tvplayerManager = null;
	private final static String TAG = "STOCK_AV";
	private static AVPlayer mAVPlayer = null;
	private static Proglib mProglib = null;
	
	private TVPlayer()
    {
		if(!DVB.IsInstanced())
		{
			DVB.GetInstance().create("unfdemo");
		}
		
		
		mAVPlayerManager = AVPlayerManager.getInstance();
		
		if(mAVPlayerManager == null){
			Log.e(TAG,"AVPlayerManager init error");
			return ;
		}
		
		mAVPlayer = DVB.GetInstance().GetAVPlayerInstance();
		if(mAVPlayer == null){
			Log.e(TAG,"AVPlayerManager init mAVPlayer error");
			return ;
		}
		
		mProglib = DVB.GetInstance().GetProglibInstance();
		if(mProglib == null){
			Log.e(TAG,"TVPlayer init mProglib error");
			return ;
		}
		
    }
		
	public static TVPlayer getInstance()
	 {
	        if (tvplayerManager == null)
	        	tvplayerManager = new TVPlayer();
	        
	        return tvplayerManager;
	 }
	
/**������Ƶ���λ�úʹ�С*/ 
public boolean SetBounds(int x,int y,int width,int high)
{
	if(tvplayerManager == null)
	{
		getInstance();
	}
	

	if (mAVPlayer == null){
		Log.e(TAG,"setPlayerWindow failed mAVPlayer not init");
		return false;
	}	
		
	
	if(0== mAVPlayer.AVSetPlayWindow(x,y,width,high,0))
	{
	
	     return true;
	}
	else 
	{
		return false;
	}
}

/**��view�в�����Ƶ*/ 
public boolean Play(Channel channel,View view)
{
	if(tvplayerManager == null)
	{
		getInstance();
	}
	
	
	if (mAVPlayerManager == null){
		Log.e(TAG,"Play failed mAVPlayerManager not init");
		return false;
	}	
		
	SetBounds((int)view.getX(),(int)view.getY(),view.getWidth(),view.getHeight());
	
	if(0== mAVPlayerManager.startPlayer(channel.ServerID,channel.TsID))
	{
	
	      return true;
	}else 
		return false;
}

public boolean Play(Channel channel)
{
	if(tvplayerManager == null)
	{
		getInstance();
	}
	

	if (mAVPlayerManager == null){
		Log.e(TAG,"Play failed mAVPlayerManager not init");
		return false;
	}	
	
	if(0== mAVPlayerManager.startPlayer(channel.ServerID,channel.TsID))
	{
	
	      return true;
	}else 
		return false;
	
}

/**�ر���Ƶ*/ 
public boolean Close()
{
	if(tvplayerManager == null)
	{
		getInstance();
	}
	
	if (mAVPlayer == null){
		Log.e(TAG,"stopPlay failed mAVPlayer not init");
		return false;
	}
	
	if(0== mAVPlayer.StopNewChannel(0))
	
	    return true;
	else
		return false;
}

/**��ȡ���е���Ƶ��Ƶ��Ŀ*/ 

public ArrayList<Channel> GetAllChannels(int frequency)
{
	ArrayList<Channel> chanList = new ArrayList<Channel>();
	int result = 0;

	
	if(tvplayerManager == null)
	{
		getInstance();
	}
	
	if (mProglib == null){
		Log.e(TAG,"GetAllChannels failed mProglib not init");
		return null;
	}
	
	Proglib.ProgInfo pinfo = mProglib.new ProgInfo();
	
	for(int i =0; i< 700;i++)
	{
		
		result = mProglib.GetProgInfoByIndex(i, pinfo);
		if((0== result)&&(pinfo != null)){
			Log.e(TAG,"GetAllChannels pinfo"+pinfo.freq+":"+frequency+pinfo.cServiceName);
			if(pinfo.freq == frequency)
			{
				Channel cInfo = new Channel();
				
				if(cInfo != null)
				{
					cInfo.ServerID = pinfo.serviceid;
					cInfo.TsID=pinfo.tsid;
					cInfo.name = pinfo.cServiceName;
					cInfo.OnID = pinfo.networkid;
					if(pinfo.cServiceType==1)
					{
						cInfo.isVideo= true;
						cInfo.isAudio=false;
						cInfo.isData=false;
					}
					else if(pinfo.cServiceType==2)
					{
						cInfo.isVideo= false;
						cInfo.isAudio=true;
						cInfo.isData=false;
					}
					else 
					{
						cInfo.isVideo= false;
						cInfo.isAudio=false;
						cInfo.isData=true;
					}
						
					
				}
				chanList.add(cInfo);
			}
		}
		else
		{
		//	break;
		}
	}
	
	return chanList;
} 

/**��ȡ����״̬*/
public boolean IsMute()
{
	return true;	
}

/**���þ���*/ 
public void SetMute(boolean needMute)
{
	if(tvplayerManager == null)
	{
		getInstance();
	}
	
	if (mAVPlayer == null){
		Log.e(TAG,"setAVPlayerMute failed mAVPlayer not init");
		return ;
	}
	
	if(needMute)
	    mAVPlayer.AVMuteSound();
	else 	
	    mAVPlayer.AVUmMuteSound();
}


/**��ȡ����*/ 
public int GetVolume()
{
	
	return 0;
}//�õ�Ϊ0-31  

/**��������*/ 
public boolean SetVolume(int level)
{
	if(tvplayerManager == null)
	{
		getInstance();
	}
	

	if (mAVPlayer == null){
		Log.e(TAG,"setPlayerWindow failed mAVPlayer not init");
		return false;
	}	
		
  if(0==  mAVPlayer.AVSetSoundVolumeAdj(level*2,100))
  {
    
	return true;
  }
  else
  {
	  return false;
  }
}//����Ϊ0-31 


/**����ϵͳ������UI ��С����*/ 
public  void  VolumeDown()
{
	
}//����Ϊ0-31 

/**����ϵͳ������UI ��������*/ 
public  void  VolumeUp()
{
	
}//����Ϊ0-31 


/**��Ŀ��Ϣ*/ 
public class Channel { 
/**��Ŀ����*/ 
public String name; 
/**�Ƿ�Ϊ��Ƶ����*/ 
public boolean isAudio; 
/**�Ƿ�Ϊ��Ƶ����*/ 
public boolean isVideo; 
/**�Ƿ�Ϊ��������*/ 
public boolean isData; //������������,app��ʹ��,�ṩ���м����������Ƶʹ��  
/**����id*/
public int OnID; 
/**TS id*/ 
public int TsID; 
/**Server id*/ 
public int ServerID; 
}
}
