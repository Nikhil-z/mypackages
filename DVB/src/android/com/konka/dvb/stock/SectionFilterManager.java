
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
	 
   /**初始化SectionFilter*/ 
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

      /**获取所有开启的过滤器个数*/ 
     public int GetAllSectionNum ( )
     {
    	 if(FilterListInfo == null || FilterListInfo.SecFilterInfo==null)
    	 {
    		 return 0;
    	 }
    	 
    	    	 
	     return FilterListInfo.SecFilterInfo.size();
     } 

     /**获取所有开启的过滤器*/ 
     public  ArrayList<SectionFilter>  GetAllSectionFilter ( )
     {
    	 if(FilterListInfo == null || FilterListInfo.SecFilterInfo==null)
    	 {
    		 return null;
    	 }
    	 
    	 return FilterListInfo.get();
	    
     } 

/**关闭所有的过滤器*/ 
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

/**关闭当前的过滤器*/ 
      public  void  CloseSectionFilter (SectionFilter  mSectionFilter)
      {
    	 if(FilterListInfo == null || FilterListInfo.SecFilterInfo==null)
     	 {
     		 return ;
     	 }
    	
    	
    	  FilterListInfo.SecFilterInfo.remove(mSectionFilter);//??
    	
      }

/**获取SecitonFilter 请求的Pid*/ 
       public  int  GetPidBySectionFilter (SectionFilter  mSectionFilter )
       {
    	   if(mSectionFilter == null)return 0x1fff;
    	   
    	   return  mSectionFilter.getPid();
    
       } 
}
