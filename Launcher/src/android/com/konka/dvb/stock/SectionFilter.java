package android.com.konka.dvb.stock;

import java.util.ArrayList;

import android.com.konka.dvb.DVB;
import android.com.konka.dvb.IDVBListener;
import android.com.konka.dvb.stock.Section;
import android.com.konka.dvb.stock.SectionFilterListener;
import android.com.konka.dvb.stock.SectionFilterManager.SectionFilterListInfo;
import android.com.konka.dvb.Demux;
import android.util.Log;


public  class  SectionFilter { 
	
       
	private final static String TAG = "STOCK_FILTER";
	//private  SectionFilterListener onSectionFilterListener = null;
	private  Demux mDemux = null;
	private static SectionFilter	mFilter = null;
	
	 public static class FilterInfo
	 {
	     public  int  slot ;
	     public int used;
	     public int pid;
	     public byte []match;
	     public byte []mask;
	     public int BufSize;
	     SectionFilterListener Listener;
	 }
	 
	  public ArrayList<FilterInfo> FilterListInfo;
	       
	    public SectionFilter()
        {
        	
        	if(!DVB.IsInstanced())
        	{
        		DVB.GetInstance().create("unfdemo");
        	}

        
        	 Log.v(TAG,".....................SectionFilter123..");
        	 
        	mDemux = DVB.GetInstance().GetDemuxInstance();
   
        	/*for stock event listener.maybe no need.*/
           	DVB.GetInstance().SubScribeEvent(DVB.Event.KK_FILTER, new IDVBListener() {
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
        		
        	
        		byte [] DataBuf = new byte[arg5];
        		
        		mDemux.GetFilterData(arg3, DataBuf,arg4, arg5); 
        		
			   //   Log.v(TAG,".....................1.."+slo);
        		//if(null != onSectionFilterListener && used ==1 && slot >=0 && arg3==slot)
        		FilterInfo FilterInfo = null;
            	 int i = 0;
             	for( i = 0; i<FilterListInfo.size(); i++)
             	{
             		
             		FilterInfo = FilterListInfo.get(i);
             		if(FilterInfo != null)
             		{
             			Log.v(TAG,".....................FilterInfo.slot:"+FilterInfo.slot);
             			if(FilterInfo.slot == arg3 &&FilterInfo.used ==1)
             			{      
             				if(1==KK_CheckDataMask(FilterInfo,DataBuf))
             				  break;
             			}
             		}
             	}
             	
             	if(FilterInfo != null )
        		{
             		if(FilterInfo.slot == arg3 &&(1==KK_CheckDataMask(FilterInfo,DataBuf)))
             		{
        			   //Log.v(TAG,".....................2..\n");
        			   Section section = new Section();
        			
        			   if(section != null)
        			   {
        			       Log.v(TAG,"2......................."+(char)DataBuf[0]+","+(char)DataBuf[3]+","+(char)DataBuf[4]+","+(char)DataBuf[5]+","+(char)DataBuf[6]);
        				   section.SetData(DataBuf,arg5);        				
        				   DataBuf = null;        				 	       				
        			   }
        			
        			    if(FilterInfo.Listener != null) FilterInfo.Listener.SectionFilterUpdate(section);
             		}
        		}
	
        	}
        }, 0);
           
        }

        public static SectionFilter getInstance()
        {
        	if(mFilter == null)
        	{
            	mFilter = new SectionFilter();
            
        	}

            return mFilter;
        }
        
        public int getPid()
        {
        	
            return 0;
        }
/**设置过滤器的存储空间大小*/ 
        public boolean SetBufferSize(int size)
        {
        	//BufSize = size;
        	
            return true;
        }

/**过滤数据等使用*/ 
        public int  StartFiltering(int pID, byte[] ext, byte[] msk,SectionFilterListener  listener)
        {
        	int SlotId = -1;
        	int i = 0;
        	if(mDemux == null) 
        	{
        		Log.e("Stock SectionFilter","mDemux is null");
        		return SlotId;
        	}
        		
        	FilterInfo Info = new FilterInfo();
        	
            if(Info != null)
     	    {
            	Info.used =1;     	    
            	Info.pid = pID;
            	Info.Listener=listener;
            	Info.match = new byte[16]; 
            	Info.mask = new byte[16]; 
            	Info.match[0]= ext[0];
            	Info.mask[0]= msk[0];
            	Info.match[1]= 0;
            	Info.mask[1]= 0;
            	Info.match[2]= 0;
            	Info.mask[2]= 0;
 	    		
     	    	for(i = 3; i < 16;i++)
     	    	{
     	    		Info.match[i-2]= ext[i];
     	    		Info.mask[i-2]= msk[i];
     	    	}

     	    }
     	    
        	SlotId =  mDemux.StartFilterReq(pID,Info.match,Info.mask, 8, 1);
        	
        	if(SlotId >= 0)
        	{        	   
        	    if(FilterListInfo == null) FilterListInfo =new ArrayList<FilterInfo>();  
        	    
        		Info.slot = SlotId;
        	   
        	    FilterListInfo.add(Info);
         	}
        	
        	Log.e("Stock SectionFilter","StartFiltering is SlotId:"+SlotId+":"+Info.match[0]+Info.match[1]+Info.match[2]);
        	
        	return SlotId;
        }

/*ext 说明 
*ext[0]:对应于DVB标准子表中的tableID 
*ext[1]:对于DVB标准子表一般为空即可 
*ext[2]:对于DVB标准子表一般为空即可 
*ext[3]:对应于DVB标准子表中的ext table id的高8位 
*ext[4]:对应于DVB标准子表中的ext table id的低8位 
*ext[5]:对应于DVB标准子表中的version_number 
*ext[6]:对应于DVB标准子表中的section_number msk说明： 					 msk[0]—msk[15]一般为0xff 
*/ 
/**关闭过滤器: 将开启的过滤器通道关闭。*/ 
        public void CloseFiltering( int slot )
         {
        	Log.e("Stock SectionFilter","CloseFiltering is SlotId:"+slot);
        	
        	if(slot >= 0)mDemux.CancelFilterReq(slot);
        	
        	 FilterInfo FilterInfo = null;
        	 
        	for(int i = 0; i< FilterListInfo.size(); i++)
        	{
        		FilterInfo = FilterListInfo.get(i);
        		
        		if(FilterInfo != null)
        		{
        			if(FilterInfo.slot == slot)
        			{
        				
        				FilterListInfo.remove(i);
        			}
        		}
        	}

         }

        public void CloseAllFiltering( )
        {
        	
        	Log.e("Stock SectionFilter","CloseFiltering All SlotId:");
       	
        	FilterInfo FilterInfo = null;
       	 
        	for(int i = 0; i< FilterListInfo.size(); i++)
        	{
       		   FilterInfo = FilterListInfo.get(i);
       		
        		if(FilterInfo != null)
       		  {
       		
       			 if(FilterInfo.slot >= 0 )
       			 {
       			 	 mDemux.CancelFilterReq(FilterInfo.slot);
       			 	//FilterArrInfo[i].used =0;
    				//FilterArrInfo[i].slot =-1;
    				//FilterArrInfo[i].match = null;
    				//FilterArrInfo[i].mask = null;
       				 FilterListInfo.remove(i);
       			 }
       		  }
        	}

        }
        /**添加回调监听*/ 
        public SectionFilterListener addSectionFilterListener(SectionFilterListener  listener)
        {
        	/*
        	 FilterInfo FilterInfo = null;
        	 
         	for(int i = 0; i< FilterListInfo.size(); i++)
         	{
         		FilterInfo = FilterListInfo.get(i);
         		
         		if(FilterInfo != null)
         		{
         			if(FilterInfo.slot == slot)
         			{
         				FilterInfo Info = new FilterInfo();
         				if(Info != null)
         				{
         					Info.slot = FilterInfo.slot;
         					Info.used = FilterInfo.used;
         					Info.pid = FilterInfo.pid;
         					Info.Listener = listener;
         				}
         				FilterListInfo.remove(i);
         				if(Info != null)FilterListInfo.add(Info);
         			}
         		}
         	}
        	   onSectionFilterListener = listener;
        	   
        	   return FilterInfo.Listener;*/
        	return null;
        }

/**删除监听：暂停将数据发送到回调函数中*/ 
       public void RemoveSectionFilterListener( SectionFilterListener listener)
       {
    	  /* FilterInfo FilterInfo = null;
      	 
        	for(int i = 0; i< FilterListInfo.size(); i++)
        	{
        		FilterInfo = FilterListInfo.get(i);
        		if(FilterInfo != null)
        		{
        			if(FilterInfo.slot == slot)
        			{
        				FilterInfo Info = new FilterInfo();
         				if(Info != null)
         				{
         					Info.slot = FilterInfo.slot;
         					Info.used = FilterInfo.used;
         					Info.pid = FilterInfo.pid;
         					Info.Listener = null;
         				}
         				FilterListInfo.remove(i);
         				if(Info != null)FilterListInfo.add(Info);
        			}
        		}
        	}*/
    	    
       }
    
       private int KK_CheckDataMask(FilterInfo info, byte []Data)
       {
          int i = 0;
    	 
          if(info == null) return 0;
    	   
  
       	for(i = 0; i < 8; i++)
       	{
       		if(0 == i)
       		{
       			if((info.match[i]&info.mask[i]) 
       				!= (Data[i]&info.mask[i]))
       			{
       				//Log.e("0x%x i = %d \n",Data[i]+i);
       				return 0;
       			}
       		}
       		else 
       		{
       			if((info.match[i]&info.mask[i]) 
       				!= (Data[i + 2]&info.mask[i]))
       			{
       				//IVIP_DEMUX_ERROR_DEBUG(("0x%x i = %d \n",Dest[i + 2],i));
       				return 0;
       			}
       		}
       		
       	}

       	return 1;
       }

}