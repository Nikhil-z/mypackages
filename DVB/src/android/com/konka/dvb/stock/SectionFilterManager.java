
package android.com.konka.dvb.stock;

import java.util.ArrayList;


import android.com.konka.dvb.DVB;
import android.com.konka.dvb.Proglib.ProgInfo;
import android.com.konka.dvb.stock.SectionFilter;
import android.util.Log;

public class SectionFilterManager { 
			
	private static SectionFilterManager SecFilterManager = null;
	private static SectionFilter	mSecFilter = null;
     public static class SectionFilterListInfo {
			
	
        public ArrayList<SectionFilter> SecFilterInfo;
             
        public SectionFilterListInfo()
        {
        	SecFilterInfo = new ArrayList<SectionFilter>();
        }
        
        public boolean add(SectionFilter info)
        {
            this.SecFilterInfo.add(info);
            return true;
        }
        
        public ArrayList<SectionFilter> get()
        {
            return this.SecFilterInfo;
        }
	}
	     
    SectionFilterListInfo FilterListInfo;
 	
	private SectionFilterManager()
    {
		if(!DVB.IsInstanced())
		{
			DVB.GetInstance().create("unfdemo");
		}
		
    }
	
	 public static SectionFilterManager getInstance()
	 {
	        if (SecFilterManager == null)
	        	SecFilterManager = new SectionFilterManager();
	        
	        return SecFilterManager;
	 }
	 
   /**��ʼ��SectionFilter*/ 
    public SectionFilter OpenSectionFilter ()
    {
    	
	     if(FilterListInfo == null)
	     {
	    	 FilterListInfo = new SectionFilterListInfo();	    	
	     }
	     mSecFilter = SectionFilter.getInstance();
	     return mSecFilter;
	     /*
	     SectionFilter  Filter = new SectionFilter();
	 
	     if(Filter != null) 
	        FilterListInfo.add(Filter);

	     return Filter;*/
    }

      /**��ȡ���п����Ĺ���������*/ 
     public int GetAllSectionNum ( )
     {
    	 if(FilterListInfo == null || FilterListInfo.SecFilterInfo==null)
    	 {
    		 return 0;
    	 }
    	 
    	    	 
	     return FilterListInfo.SecFilterInfo.size();
     } 

     /**��ȡ���п����Ĺ�����*/ 
     public  ArrayList<SectionFilter>  GetAllSectionFilter ( )
     {
    	 if(FilterListInfo == null || FilterListInfo.SecFilterInfo==null)
    	 {
    		 return null;
    	 }
    	 
    	 return FilterListInfo.get();
	    
     } 

/**�ر����еĹ�����*/ 
     public  void  CloseAllSectionFilter ( )
     {
    	 if(FilterListInfo == null || FilterListInfo.SecFilterInfo==null)
    	 {
    		 return ;
    	 }
    	 
    	 FilterListInfo.SecFilterInfo.clear();
    	 mSecFilter.CloseAllFiltering();
    	// FilterListInfo.SecFilterInfo.removeAll(FilterListInfo.SecFilterInfo);//??
     }

/**�رյ�ǰ�Ĺ�����*/ 
      public  void  CloseSectionFilter (SectionFilter  mSectionFilter)
      {
    	 if(FilterListInfo == null || FilterListInfo.SecFilterInfo==null)
     	 {
     		 return ;
     	 }
    	
    	
    	  FilterListInfo.SecFilterInfo.remove(mSectionFilter);//??
    	
      }

/**��ȡSecitonFilter �����Pid*/ 
       public  int  GetPidBySectionFilter (SectionFilter  mSectionFilter )
       {
    	   if(mSectionFilter == null)return 0x1fff;
    	   
    	   return  mSectionFilter.getPid();
    
       } 
}
