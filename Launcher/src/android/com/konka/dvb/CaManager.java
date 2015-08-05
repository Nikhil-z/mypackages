package android.com.konka.dvb;

import java.lang.ref.WeakReference;

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
import android.com.konka.dvb.casys.data.CaServiceEntitles;
import android.com.konka.dvb.casys.data.CaSlotIDs;
import android.com.konka.dvb.casys.data.CaSlotInfo;
import android.com.konka.dvb.casys.data.CaStartIPPVBuyDlgInfo;
import android.com.konka.dvb.casys.data.CaStopIPPVBuyDlgInfo;
import android.com.konka.dvb.casys.data.CaWorkTimeInfo;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
//import android.os.SystemProperties;

public final class CaManager {

    private DVB mDVB;
    private int mNativeContext; // accessed by native methods
    private final static String TAG = "DVB";

    public CaManager(DVB dvb)
    {
        mDVB = dvb;
        mNativeContext = dvb.mNativeContext;
        Log.d(TAG, "CaManager  construct"+ mNativeContext);
    }
	
    public native final CaCardSNInfo CaGetCardSN() throws CaCommonException;

    public native final short CaChangePin(String pbyOldPin, String pbyNewPin) throws CaCommonException;

    public native final short CaSetRating(String pbyPin, short byRating) throws CaCommonException;

    public native final CaRatingInfo CaGetRating() throws CaCommonException;

    public native final short CaSetWorkTime(String pbyPin, CaWorkTimeInfo worktime) throws CaCommonException;

    public native final CaWorkTimeInfo CaGetWorkTime() throws CaCommonException;

    public native final int CaGetVer() throws CaCommonException;

    public native final CaOperatorIds CaGetOperatorIds() throws CaCommonException;

    public native final CaOperatorInfo CaGetOperatorInfo(short wTVSID) throws CaCommonException;

    public native final CaACListInfo CaGetACList(short wTVSID) throws CaCommonException;

    public native final CaSlotIDs CaGetSlotIDs(short wTVSID) throws CaCommonException;

    public native final CaSlotInfo CaGetSlotInfo(short wTVSID, short bySlotID) throws CaCommonException;

    public native final CaServiceEntitles CaGetServiceEntitles(short wTVSID)throws CaCommonException;

    public native final CaEntitleIDs CaGetEntitleIDs(short wTVSID) throws CaCommonException;

    public native final CaDetitleChkNums CaGetDetitleChkNums(short wTVSID) throws CaCommonException;

    public native final boolean CaGetDetitleReaded(short wTvsID) throws CaCommonException;

    public native final boolean CaDelDetitleChkNum(short wTvsID, int dwDetitleChkNum)throws CaCommonException;

    public native final CaPairedInfo CaIsPaired()throws CaCommonException;

    public native final short CaGetPlatformID() throws CaCommonException;

    public native final short CaStopIPPVBuyDlg(CaStopIPPVBuyDlgInfo IppvInfo) throws CaCommonException;

    public native final CaIPPVProgramInfos CaGetIPPVProgram(short wTvsID) throws CaCommonException;

    public native final CaEmailHeadsInfo CaGetEmailHeads(short byCount, short byFromIndex)  throws CaCommonException;

    public native final CaEmailHeadInfo CaGetEmailHead(int dwEmailID) throws CaCommonException;

    public native final CaEmailContentInfo CaGetEmailContent(int dwEmailID) throws CaCommonException;

    public native final void CaDelEmail(int dwEmailID) throws CaCommonException;

    public native final CaEmailSpaceInfo CaGetEmailSpaceInfo() throws CaCommonException;

    public native final CaOperatorChildStatus CaGetOperatorChildStatus(short wTVSID) throws CaCommonException;

    public native final CaFeedDataInfo CaReadFeedDataFromParent(short wTVSID) throws CaCommonException;

    public native final short CaWriteFeedDataToChild(short wTVSID, CaFeedDataInfo FeedData) throws CaCommonException;

    public native final void CaRefreshInterface() throws CaCommonException;

    public native final boolean CaOTAStateConfirm(int arg1, int arg2)  throws CaCommonException;
    
    public native final CaSTBIDInfo CaGetSTBID() throws CaCommonException;
    
    public native final CaOSDMessageInfo  GetOSDMessage(short byStyle) throws CaCommonException;
    
    public native final CaStartIPPVBuyDlgInfo GetIPPVBuyDlgInfo() throws CaCommonException;
    
    public native final CaLockService GetLockServiceInfo() throws CaCommonException;

    public native final CaFingerExtMsgInfo GetFingerExtMessage() throws CaCommonException;


}


