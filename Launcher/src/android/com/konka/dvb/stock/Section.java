package android.com.konka.dvb.stock;

import java.util.Arrays;

import android.util.Log;

    /**���ع�������*/ 
 public class Section { 
	
public byte[] BufData = new byte[4096]; 


 /**��ȡsection versionNum*/ 
 public int GetVersionNumber()
 {
	 if(BufData.length <= 0) return 0;
	 
	 return (BufData[5]>>1)&0x1f;	
 }
 
/**��ȡ��ǰsection��number*/ 
public int GetSectionNumber()
 {
	if(BufData.length <= 0) return 0;

	return BufData[6];	
 }

/**��ȡ����section number*/ 
public int GetLastSectionNumber()
{
	if(BufData.length <= 0) return 0;
	
	return BufData[7];
    	
}

public  void SetData(byte[]Buf,int len)
{
	if(BufData.length <= 0) return ;
	
	 BufData = Arrays.copyOf(Buf, len);
	// Log.v("Section","1......................GetData:"+BufData.length+BufData[0]+BufData[1]);
	 return ;
}

/**��ȡ����*/ 
public byte[] GetData()
{
	
	//Log.v("Section","2......................GetData:"+BufData.length+BufData[0]+BufData[1]);
	if(BufData.length <= 0) return null;
	
	 return BufData;
}

/**��ջ�������*/ 
public boolean SetEmty()
{
	
	return true;
}

} 