package android.com.konka.dvb.casys.func;

import android.com.konka.dvb.CaManager;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.IDVBListener;
import android.com.konka.dvb.casys.data.CaACListInfo;
import android.com.konka.dvb.casys.data.CaCardSNInfo;
import android.com.konka.dvb.casys.data.CaDetitleChkNums;
import android.com.konka.dvb.casys.data.CaEmailContentInfo;
import android.com.konka.dvb.casys.data.CaEmailHeadInfo;
import android.com.konka.dvb.casys.data.CaEmailHeadsInfo;
import android.com.konka.dvb.casys.data.CaEmailSpaceInfo;
import android.com.konka.dvb.casys.data.CaEntitleIDs;
import android.com.konka.dvb.casys.data.CaFeedDataInfo;
import android.com.konka.dvb.casys.data.CaFingerExtMsgInfo;
import android.com.konka.dvb.casys.data.CaIPPVProgramInfo;
import android.com.konka.dvb.casys.data.CaIPPVProgramInfos;
import android.com.konka.dvb.casys.data.CaLockService;
import android.com.konka.dvb.casys.data.CaOSDMessageInfo;
import android.com.konka.dvb.casys.data.CaOperatorChildStatus;
import android.com.konka.dvb.casys.data.CaOperatorIds;
import android.com.konka.dvb.casys.data.CaOperatorInfo;
import android.com.konka.dvb.casys.data.CaPairedInfo;
import android.com.konka.dvb.casys.data.CaRatingInfo;
import android.com.konka.dvb.casys.data.CaSTBIDInfo;
import android.com.konka.dvb.casys.data.CaServiceEntitle;
import android.com.konka.dvb.casys.data.CaServiceEntitles;
import android.com.konka.dvb.casys.data.CaSlotIDs;
import android.com.konka.dvb.casys.data.CaSlotInfo;
import android.com.konka.dvb.casys.data.CaStartIPPVBuyDlgInfo;
import android.com.konka.dvb.casys.data.CaStopIPPVBuyDlgInfo;
import android.com.konka.dvb.casys.data.CaWorkTimeInfo;
import android.com.konka.dvb.casys.exception.CaCommonException;
//import android.DVB.CaDeskImpl.OnCaEventListener;
import android.util.Log;



public class CaDeskManager
{
	protected enum CA_EVENT
    {
		EV_CA_START_IPPV_BUY_DLG,
		EV_CA_HIDE_IPPV_DLG,
		EV_CA_EMAIL_NOTIFY_ICON,
		EV_CA_SHOW_OSD_MESSAGE,
		EV_CA_HIDE_OSD_MESSAGE,
		EV_CA_REQUEST_FEEDING,
		EV_CA_SHOW_BUY_MESSAGE,
		EV_CA_SHOW_FINGER_MESSAGE,
		EV_CA_SHOW_PROGRESS_STRIP,
		EV_CA_ACTION_REQUEST,
		EV_CA_ENTITLE_CHANGED,
		EV_CA_DETITLE_RECEVIED,
		EV_CA_LOCKSERVICE,
		EV_CA_UNLOCKSERVICE,
		EV_CA_OTASTATE,
		EV_CA_SHOWCURTAIN,
		EV_CA_ACTIONREGUESTEXT,
		EV_CA_FINGERMESSAGEEXT,
    }
	
	 public interface OnCaEventListener {
	        boolean onStartIppvBuyDlg(CaDeskManager mgr, int what, int arg1, int arg2, CaStartIPPVBuyDlgInfo arg3);

	        boolean onHideIPPVDlg(CaDeskManager mgr, int what, int arg1, int arg2);

	        boolean onEmailNotifyIcon(CaDeskManager mgr, int what, int arg1, int arg2);

	        boolean onShowOSDMessage(CaDeskManager mgr, int what, int arg1, int arg2, String arg3);

	        boolean onHideOSDMessage(CaDeskManager mgr, int what, int arg1, int arg2);

	        boolean onRequestFeeding(CaDeskManager mgr, int what, int arg1, int arg2);

	        boolean onShowBuyMessage(CaDeskManager mgr, int what, int arg1, int arg2);

	        boolean onShowFingerMessage(CaDeskManager mgr, int what, int arg1, int arg2);

	        boolean onShowProgressStrip(CaDeskManager mgr, int what, int arg1, int arg2);

	        boolean onActionRequest(CaDeskManager mgr, int what, int arg1, int arg2);

	        boolean onEntitleChanged(CaDeskManager mgr, int what, int arg1, int arg2);

	        boolean onDetitleReceived(CaDeskManager mgr, int what, int arg1, int arg2);

	        boolean onLockService(CaDeskManager mgr, int what, int arg1, int arg2, CaLockService arg3);

	        boolean onUNLockService(CaDeskManager mgr, int what, int arg1, int arg2);
	        
			boolean onOtaState(CaDeskManager mgr, int what, int arg1, int arg2);

	        boolean onShowCurtainNotify(CaDeskManager mgr, int what, int arg1, int arg2);
	        
	        boolean onActionRequestExt(CaDeskManager mgr, int what, int arg1, int arg2);
	        
	        boolean onShowFingerMessageExt(CaDeskManager mgr, int what, int arg1, int arg2,CaFingerExtMsgInfo arg3);


	}
	
	private static CaDeskManager caDeskManager = null;
    private static CaManager mCaManager = null;
    private static OnCaEventListener onCaEventListner = null;
    private CaDeskManager mMSrv = null;
    
    /**record currvent msg*/
    private static int _current_event = 0;
    private static int _current_msg_type = 0;
    public static int _current_email_type = 0;
    public static int _current_detitle_type = 0;

    
    private CaDeskManager()
    {
		if(!DVB.IsInstanced())
		{
			DVB.GetInstance().create("unfdemo");
		}
		mMSrv = this;
       	mCaManager = DVB.GetInstance().GetCaManagerInstance();	
       	
       	DVB.GetInstance().SubScribeEvent(DVB.Event.KK_CA, new IDVBListener() {
			public void NotifyEvent(int arg0, int arg1, int arg2, int arg3, int arg4,
					int arg5, int arg6, int arg7) {
			// TODO Auto-generated method stub
			Log.v("CaManager", "arg0:" + arg0 
					+ "arg1:" + arg1 
					+ "arg2:" + arg2
					+ "arg3:" + arg3
					+ "arg4:" + arg4
					+ "arg5:" + arg5
					+ "arg6:" + arg6
					+ "arg7:" + arg7
					);
			
			int msg_type = arg3;
			if((6 != arg0)&&(14 != msg_type)){
				return;
			}
			
			if(mCaManager == null){
				return;
			}
			
			CaDeskManager.CA_EVENT ev[] = CaDeskManager.CA_EVENT.values();
			
			switch(ev[msg_type])
			{
                case EV_CA_START_IPPV_BUY_DLG:
                    if (onCaEventListner != null){
                    	//onCaEventListner.onStartIppvBuyDlg(mMSrv, msg.what, msg.arg1, msg.arg2, 
                        //        (CaStartIPPVBuyDlgInfo) msg.obj);
                    	Log.v("CaManager", "Java receive ippv info");
                    }
                    return;
                    
                case EV_CA_HIDE_IPPV_DLG:
                    if (onCaEventListner != null)
                        onCaEventListner.onHideIPPVDlg(mMSrv, msg_type, arg4, arg5);
                	Log.v("CaManager", "Java receive ippv info");
                    return;
                    
                case EV_CA_EMAIL_NOTIFY_ICON:
                    if (onCaEventListner != null)
                        onCaEventListner.onEmailNotifyIcon(mMSrv, msg_type, arg4, arg5);
                    Log.v("CaManager", "Java receive email notify info");
                    return;
                    
                case EV_CA_SHOW_OSD_MESSAGE:
                    Log.v("CaManager", "Java receive osd msg info");
                    if (onCaEventListner != null){
                    	CaOSDMessageInfo szMessage;
						try {
							szMessage = mCaManager.GetOSDMessage((short)arg4);				
		                     onCaEventListner.onShowOSDMessage(mMSrv, msg_type, arg4, arg5,
		                                (String) szMessage.pMesage);
		                     Log.v("CaManager", " osd msg info"+szMessage.pMesage);	
						} catch (CaCommonException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	
                     }
                    return;
                    
                case EV_CA_HIDE_OSD_MESSAGE:
                    if (onCaEventListner != null)
                        onCaEventListner.onHideOSDMessage(mMSrv, msg_type, arg4, arg5);
                    Log.v("CaManager", "Java receive hide osd msg info");
                    return;
                    
                case EV_CA_REQUEST_FEEDING:
                    if (onCaEventListner != null)
                        onCaEventListner.onRequestFeeding(mMSrv, msg_type, arg4, arg5);
                    Log.v("CaManager", "Java receive request feed info");
                    return;
                    
                case EV_CA_SHOW_BUY_MESSAGE:
                    if (onCaEventListner != null)
                        onCaEventListner.onShowBuyMessage(mMSrv, msg_type, arg4, arg5);
                    Log.v("CaManager", "Java receive buy message info");
                    
                    _current_event = CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal();
                    _current_msg_type = arg5;
             
                    return;
                    
                case EV_CA_SHOW_FINGER_MESSAGE:
                    if (onCaEventListner != null)
                        onCaEventListner.onShowFingerMessage(mMSrv, msg_type, arg4, arg5);
                    Log.v("CaManager", "Java receive finger msg info");
                    return;
                    
                case EV_CA_SHOW_PROGRESS_STRIP:
                    if (onCaEventListner != null)
                        onCaEventListner.onShowProgressStrip(mMSrv, msg_type, arg4, arg5);
                    Log.v("CaManager", "Java receive progress strip info");
                    return;
                case EV_CA_ACTION_REQUEST:
                    if (onCaEventListner != null)
                        onCaEventListner.onActionRequest(mMSrv, msg_type, arg4, arg5);
                    Log.v("CaManager", "Java receive action request info");
                    return;
                    
                case EV_CA_ENTITLE_CHANGED:
                    if (onCaEventListner != null)
                        onCaEventListner.onEntitleChanged(mMSrv, msg_type, arg4, arg5);
                    Log.v("CaManager", "Java receive entitle change info");
                    return;
                case EV_CA_DETITLE_RECEVIED:
                    if (onCaEventListner != null)
                        onCaEventListner.onDetitleReceived(mMSrv, msg_type, arg4, arg5);
                    Log.v("CaManager", "Java receive detitle msg info");
                    return;

                case EV_CA_LOCKSERVICE:
                    //if (onCaEventListner != null)
                    //    onCaEventListner.onLockService(mMSrv, msg.what, msg.arg1, msg.arg2,
                    //            (CaLockService) msg.obj);
                	Log.v("CaManager", "Java receive lock service info");
                    return;

                case EV_CA_UNLOCKSERVICE:
                    if (onCaEventListner != null)
                        onCaEventListner.onUNLockService(mMSrv, msg_type, arg4, arg5);
                    Log.v("CaManager", "Java receive unlock service info");
                    return;

                case EV_CA_SHOWCURTAIN:
                	return;
                	
                case EV_CA_ACTIONREGUESTEXT:
                	return;

                case EV_CA_FINGERMESSAGEEXT:
                    Log.v("CaManager", "Java receive finger ext msg info");
                    if (onCaEventListner != null){
                    	CaFingerExtMsgInfo fingerExtMsg;
						try {
							fingerExtMsg = mCaManager.GetFingerExtMessage();				
		                    onCaEventListner.onShowFingerMessageExt(mMSrv, msg_type, arg4, arg5,fingerExtMsg);
		                     Log.v("CaManager", " finger ext msg info"+fingerExtMsg.pMesage);	
						} catch (CaCommonException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	
                     }
                    return; 
                default:
                    System.err.println("Unknown message type " + msg_type);
                    return;
            }
		}
	}, 0);
       	
    }

    public static CaDeskManager getCaMgrInstance()
    {
        if (caDeskManager == null)
        	caDeskManager = new CaDeskManager();
        return caDeskManager;
    }
    /**
     * To Get CA Card Sn Infomation
     * 
     * @return CACardSNInfo
     * @throws CaCommonException
     */
    public CaCardSNInfo CaGetCardSN() throws CaCommonException
    {
    	if(mCaManager == null)
    		return null;
    	
    	return mCaManager.CaGetCardSN();
    	
    }
    /**
     *  To Change CA Pin Code
     * 
     * @param1 old Pin code
     * @param2 new pin code
     * @return short
     * @throws CaCommonException
     */
    public short CaChangePin(String pbyOldPin,String pbyNewPin) throws CaCommonException
    {
       	if(mCaManager == null)
    		return 0;
    	
    	return mCaManager.CaChangePin(pbyOldPin,pbyNewPin);
    }
    /**
     * To Set CA Rating
     * 
     * @param1 Pin Code
     * @param2 New Rating
     * @return short
     * @throws CaCommonException
     */
    public short CaSetRating(String pbyPin,short byRating) throws CaCommonException
    {
      	if(mCaManager == null)
    		return 0;
      	return  mCaManager.CaSetRating(pbyPin,byRating);
    }
    /**
     * To Get CA Rating Information
     * 
     * @return CARatingInfo
     * @throws CaCommonException
     */
    public CaRatingInfo CaGetRating() throws CaCommonException
    {
      	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetRating();

    }
    /**
     * To Modify CA Work Time
     * 
     * @param1 Pin Code
     * @param2 CA WorkTime Object
     * @return short
     * @throws CaCommonException
     */
    public short CaSetWorkTime(String pbyPin,CaWorkTimeInfo worktime) throws CaCommonException
    {
      	if(mCaManager == null)
    		return 0;
      	return  mCaManager.CaSetWorkTime(pbyPin,worktime);
    }
    /**
     * To Get CA WorkTime
     * 
     * @return CaWorkTimeInfo
     * @throws CaCommonException
     */
    public CaWorkTimeInfo CaGetWorkTime() throws CaCommonException
    {
      	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetWorkTime();
     }
    /**
     * To Get CA System Version
     * 
     * @return int
     * @throws CaCommonException
     */
    public int CaGetVer() throws CaCommonException
    {
       	if(mCaManager == null)
    		return 0;
      	return  mCaManager.CaGetVer();
    }
    /**
     * To Get all Ca operatorid
     * 
     * @return CaOperatorIds
     * @throws CaCommonException
     */
    public CaOperatorIds CaGetOperatorIds() throws CaCommonException
    {
      	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetOperatorIds();
     }
    /**
     * To Get Operator information by Id
     * 
     * @param CA operator id
     * @return CaOperatorInfo
     * @throws CaCommonException
     */
    public CaOperatorInfo CaGetOperatorInfo(short wTVSID) throws CaCommonException
    {
       	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetOperatorInfo(wTVSID);
    }
    /**
     * To Inquires the user features
     * 
     * @param CA operator id
     * @return CaACListInfo
     * @throws CaCommonException
     */
    public CaACListInfo CaGetACList(short wTVSID) throws CaCommonException
    {
      	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetACList(wTVSID);
    }
    /**
     * To Get all CA Slot Id by Operator Id
     * 
     * @param CA operator id
     * @return CaSlotIDs
     * @throws CaCommonException
     */
    public CaSlotIDs CaGetSlotIDs(short wTVSID) throws CaCommonException
    {
      	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetSlotIDs(wTVSID); 	
    }
    /**
     * To Get Slot information by OperatorId and SlotId
     * 
     * @param1 CA operator id
     * @param2 CA Slot id
     * @return CaSlotInfo
     * @throws CaCommonException
     */
    public CaSlotInfo CaGetSlotInfo(short wTVSID,short bySlotID) throws CaCommonException
    {
      	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetSlotInfo(wTVSID, bySlotID); 	
    }
    /**
     * To Get Ca Service Entitle Information
     * 
     * @param CA operator id
     * @return CaServiceEntitles
     * @throws CaCommonException
     */
    public CaServiceEntitles CaGetServiceEntitles(short wTVSID) throws CaCommonException
    {
       	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetServiceEntitles(wTVSID); 		
    }
    /**
     * To Get Ca Entitle Id List
     * 
     * @param CA operator id
     * @return CaEntitleIDs
     * @throws CaCommonException
     */
    public CaEntitleIDs CaGetEntitleIDs (short wTVSID) throws CaCommonException
    {
      	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetEntitleIDs(wTVSID); 
    }
    /**
     * To Get ca Detitle information
     * 
     * @param CA operator id
     * @return CaDetitleChkNums
     * @throws CaCommonException
     */
    public CaDetitleChkNums CaGetDetitleChkNums(short wTVSID) throws CaCommonException
    {
      	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetDetitleChkNums(wTVSID); 	
    }
    /**
     * To Get CA Detitle information Read State
     * 
     * @param CA operator id
     * @return boolean
     * @throws CaCommonException
     */
    public boolean CaGetDetitleReaded(short wTVSID) throws CaCommonException
    {
       	if(mCaManager == null)
    		return false;
      	return  mCaManager.CaGetDetitleReaded(wTVSID); 	
    }
    /**
     * To Delete Detitle Check Num
     * 
     * @param1 CA operator id
     * @param2 CA Detitle Check Num
     * @return boolean
     * @throws CaCommonException
     */
    public boolean CaDelDetitleChkNum(short wTVSID, int dwDetitleChkNum ) throws CaCommonException
    {
      	if(mCaManager == null)
    		return false;
      	return  mCaManager.CaDelDetitleChkNum(wTVSID,dwDetitleChkNum); 	
    }
    /**
     * To Get Machine card corresponding situation
     * 
     * @param1 Smart CARDS corresponding set-top box number(MaxNum=5)
     * @param2 set-top box List
     * @return short
     * @throws CaCommonException
     */
    public CaPairedInfo CaIsPaired() throws CaCommonException
    {
    	
      	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaIsPaired();	
      	/*
    	CaPairedInfo caPairedInfo = new CaPairedInfo();
    	String[] pSTBIdList = new String[5];
    	
    	pSTBIdList[0] = "81000123";
    	pSTBIdList[1] = "81000124";
    	pSTBIdList[2] = "81000125";
    	pSTBIdList[3] = "81000126";
    	pSTBIdList[4] = "81000127";
    	
    	caPairedInfo.sPairedState = 0;
    	caPairedInfo.pSTBIdList = pSTBIdList;
    	
    	return caPairedInfo;
    	*/
    }
    /**
     * To Get CA Platform Id
     * 
     * 
     * @return short
     * @throws CaCommonException
     */
    public short CaGetPlatformID() throws CaCommonException
    {
     	if(mCaManager == null)
    		return 0;
      	return  mCaManager.CaGetPlatformID();	
    }
	 /**
     * To Stop IPPV Buy Dialog Info
     * 
     * @param CA operator id
     * @return CaIPPVProgramInfo
     * @throws CaCommonException
     */
    public short CaStopIPPVBuyDlg(CaStopIPPVBuyDlgInfo IppvInfo) throws CaCommonException
    {
     	if(mCaManager == null)
    		return 0;
      	return  mCaManager.CaStopIPPVBuyDlg(IppvInfo);	
    }
    /**
     * To Get Ca IPPV Program Information
     * 
     * @param CA operator id
     * @return CaIPPVProgramInfo
     * @throws CaCommonException
     */
    public CaIPPVProgramInfos CaGetIPPVProgram(short wTvsID) throws CaCommonException
    {
    	
     	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetIPPVProgram(wTvsID);	
      	
//    	CaIPPVProgramInfos caIPPVProgramInfos = new CaIPPVProgramInfos();
    	
//    	caIPPVProgramInfos.sNumber = 2;
    	

//    	caIPPVProgramInfos.IPPVProgramInfo[0].wdwProductID = 0xFFFFFFFF;   /* 节目的ID */
//    	caIPPVProgramInfos.IPPVProgramInfo[0].sBookEdFlag=1;  /* 产品状态：BOOKING，VIEWED */ 
//    	caIPPVProgramInfos.IPPVProgramInfo[0].sCanTape=1;      /* 是否可以录像�?－可以录像；0－不可以录像 */
//    	caIPPVProgramInfos.IPPVProgramInfo[0].sPrice=0xFF;        /* 节目价格 */
//    	caIPPVProgramInfos.IPPVProgramInfo[0].sExpiredDate=3458;  /* 节目过期时间,IPPV�?*/
//    	caIPPVProgramInfos.IPPVProgramInfo[0].sSlotID=0xFF;      /* 钱包ID */
//
//    	caIPPVProgramInfos.IPPVProgramInfo[1].wdwProductID = 1234;   /* 节目的ID */
//    	caIPPVProgramInfos.IPPVProgramInfo[1].sBookEdFlag=3;  /* 产品状态：BOOKING，VIEWED */ 
//    	caIPPVProgramInfos.IPPVProgramInfo[1].sCanTape=1;      /* 是否可以录像�?－可以录像；0－不可以录像 */
//    	caIPPVProgramInfos.IPPVProgramInfo[1].sPrice=0xFF;        /* 节目价格 */
//    	caIPPVProgramInfos.IPPVProgramInfo[1].sExpiredDate=3458;  /* 节目过期时间,IPPV�?*/
//    	caIPPVProgramInfos.IPPVProgramInfo[1].sSlotID=0xFF;      /* 钱包ID */
    	
//    	return caIPPVProgramInfos;
    }
    /**
     * To Get Ca Email Head Info  
     * 
     * 
     * @return CaEmailHeadInfo
     * @throws CaCommonException
     */
    public CaEmailHeadsInfo CaGetEmailHeads(short byCount,short byFromIndex) throws CaCommonException
    {
     	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetEmailHeads(byCount,byFromIndex);
    }
    /**
     * To Get Ca Email Head Info  By Email Id
     * 
     * @param Email Id
     * @return CaEmailHeadInfo
     * @throws CaCommonException
     */
    public CaEmailHeadInfo CaGetEmailHead(int dwEmailID) throws CaCommonException
    {
     	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetEmailHead(dwEmailID);
    }
    /**
     * To Get Ca Email Content By Email Id
     * 
     * @param Email Id
     * @return CaEmailContentInfo
     * @throws CaCommonException
     */
    public CaEmailContentInfo CaGetEmailContent(int dwEmailID) throws CaCommonException
    {
      	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetEmailContent(dwEmailID);
    }
    /**
     * To Delete Email By Email Id
     * 
     * @param Email Id
     * 
     * @throws CaCommonException
     */
    public void CaDelEmail(int dwEmailID) throws CaCommonException
    {
     	if(mCaManager == null)
    		return ;
      	mCaManager.CaDelEmail(dwEmailID);
    }
    /**
     * To Inquires Email Space Info
     * 
     * 
     * @return CaEmailSpaceInfo
     * @throws CaCommonException
     */
    public CaEmailSpaceInfo CaGetEmailSpaceInfo() throws CaCommonException
    {
     	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetEmailSpaceInfo();
    }
    /**
     * To Reads mother card and Child Card match info
     * 
     * @param CA operator id
     * @return CaOperatorChildStatus
     * @throws CaCommonException
     */
    public CaOperatorChildStatus CaGetOperatorChildStatus(short wTVSID) throws CaCommonException
    {
     	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaGetOperatorChildStatus(wTVSID);
    }
    /**
     * To Read Feed Data From Parent Card
     * 
     * @param CA operator id
     * @return CaFeedDataInfo
     * @throws CaCommonException
     */
    public CaFeedDataInfo CaReadFeedDataFromParent(short wTVSID) throws CaCommonException
    {
     	if(mCaManager == null)
    		return null;
      	return  mCaManager.CaReadFeedDataFromParent(wTVSID);
    }
    /**
     * To Write Feed Data To Child Card
     * 
     * @param1 CA operator id
     * @param2 Feed Data Object
     * @return short
     * @throws CaCommonException
     */
    public short CaWriteFeedDataToChild(short wTVSID,CaFeedDataInfo FeedData) throws CaCommonException
    {
     	if(mCaManager == null)
    		return 0;
      	return mCaManager.CaWriteFeedDataToChild(wTVSID, FeedData);
    }
    /**
     * To Refresh Interface
     * 
     * @throws CaCommonException
     */
    public void CaRefreshInterface() throws CaCommonException
    {
     	if(mCaManager == null)
    		return ;
      	mCaManager.CaRefreshInterface();
    }
    /**
     * To Refresh Interface
     * 
     * @throws CaCommonException
     */
    public boolean CaOTAStateConfirm(int arg1,int arg2) throws CaCommonException
    {
     	if(mCaManager == null)
    		return false;
      	return mCaManager.CaOTAStateConfirm(arg1,arg2);
    }
    
    /**
     * To Get CA STBID
     * 
     * @throws CaCommonException
     */
    public CaSTBIDInfo CaGetSTBID() throws CaCommonException
    {
     	if(mCaManager == null)
    		return null;
      	return mCaManager.CaGetSTBID();
    }
    
    /**
     * Register setOnCaEventListener(OnCaEventListener listener), your listener
     * will be triggered when the events posted from native code.
     * 
     * @param listener OnCaEventListener
     */
    
    public void setOnCaEventListener(OnCaEventListener listener)
    {
    	onCaEventListner = listener;
    }
    
    
    public static int getCurrentEvent() {
        return _current_event;
    }

    public static int getCurrentMsgType() {
        return _current_msg_type;
    }

    public static void setCurrentEvent(int CurrentEvent) {
        _current_event = CurrentEvent;
    }

    public static void setCurrentMsgType(int MsgType) {
        _current_msg_type = MsgType;
    }
}
