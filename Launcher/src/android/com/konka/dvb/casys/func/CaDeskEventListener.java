
package android.com.konka.dvb.casys.func;


import android.com.konka.dvb.CaManager;
import android.com.konka.dvb.casys.data.CaFingerExtMsgInfo;
import android.com.konka.dvb.casys.data.CaLockService;
import android.com.konka.dvb.casys.data.CaStartIPPVBuyDlgInfo;
import android.com.konka.dvb.casys.func.CaDeskManager.OnCaEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class CaDeskEventListener implements CaDeskManager.OnCaEventListener
{
	public enum CA_EVENT
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

    private Handler m_handler = null;
    static private CaDeskEventListener caEventListener = null;

    public CaDeskEventListener()
    {
        m_handler = null;
    }

    public void attachHandler(Handler handler)
    {
        m_handler = handler;
    }

    public void releaseHandler()
    {
        m_handler = null;
    }

    public static CaDeskEventListener getInstance()
    {
        if (caEventListener == null)
        {
            caEventListener = new CaDeskEventListener();
        }
        return caEventListener;
    }

    /**
    * IPPV Program Notify Message
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onStartIppvBuyDlg(CaDeskManager mgr, int what,int arg1, int arg2,CaStartIPPVBuyDlgInfo arg3)
    {
        if (m_handler == null)
        {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_START_IPPV_BUY_DLG.ordinal();
        b.putInt("MessageType", arg1);
        b.putInt("MessageType2", arg2);
        msg.setData(b);
        msg.obj = arg3;
        m_handler.sendMessage(msg);
        return false;
    }

    /**
    * Hide IPPV Program Dialog
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onHideIPPVDlg(CaDeskManager mgr, int what,int arg1, int arg2)
    {
        if (m_handler == null)
        {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_HIDE_IPPV_DLG.ordinal();
        b.putInt("MessageType", arg1);
        b.putInt("MessageType2", arg2);
        msg.setData(b);
        m_handler.sendMessage(msg);
        return false;
    }

    /**
    * Email Notify
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onEmailNotifyIcon(CaDeskManager mgr, int what,int arg1, int arg2)
    {

        if (m_handler == null)
        {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_EMAIL_NOTIFY_ICON.ordinal();
        b.putInt("MessageType", arg1);
        b.putInt("MessageType2", arg2);
        msg.setData(b);
        m_handler.sendMessage(msg);
        return false;
    }

    /**
    * Show OSD Message
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onShowOSDMessage(CaDeskManager mgr, int what,int arg1, int arg2,String arg3)
    {
        if (m_handler == null)
        {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_SHOW_OSD_MESSAGE.ordinal();
        b.putInt("MessageType", arg1);
        b.putInt("MessageType2", arg2);
        b.putString("StringType", arg3);
        System.out.print("onShowOSDMessage:"+arg3);
        msg.setData(b);
        m_handler.sendMessage(msg);
        return false;
    }

    /**
    * Hide OSD Message
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onHideOSDMessage(CaDeskManager mgr, int what,int arg1, int arg2)
    {

        if (m_handler == null)
        {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_HIDE_OSD_MESSAGE.ordinal();
        b.putInt("MessageType", arg1);
        b.putInt("MessageType2", arg2);
        msg.setData(b);
        m_handler.sendMessage(msg);
        return false;
    }

    /**
    * Request Feeding Data Message
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onRequestFeeding(CaDeskManager mgr, int what,int arg1, int arg2)
    {

        if (m_handler == null)
        {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_REQUEST_FEEDING.ordinal();
        b.putInt("MessageType", arg1);
        b.putInt("MessageType2", arg2);
        msg.setData(b);
        m_handler.sendMessage(msg);
        return false;
    }

    /**
    * Notify Message when Can't watch the shows
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onShowBuyMessage(CaDeskManager mgr, int what,int arg1, int arg2)
    {

        if (m_handler == null)
        {
            return false;
        }
        Log.i("DeskCaEventListener", "//////////////////////////////////EV_CA_SHOW_BUY_MESSAGE/////////////////////////////////////////////////");
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal();
        b.putInt("MessageType", arg2);
        b.putInt("MessageFrom", 0);
        msg.setData(b);
        m_handler.sendMessage(msg);
//        System.out.print("SN Post Event:"+CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.toString()+"\n");
//        System.out.print("SN Post Message Type:"+arg2);
        return false;
    }

    /**
    * For Finger Message Show
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onShowFingerMessage(CaDeskManager mgr, int what,int arg1, int arg2)
    {

        if (m_handler == null)
        {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_SHOW_FINGER_MESSAGE.ordinal();
        b.putInt("MessageType", arg1);
        b.putInt("MessageType2", arg2);
        msg.setData(b);
        m_handler.sendMessage(msg);
        return false;
    }

    /**
    * For Progress Strip Show
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onShowProgressStrip(CaDeskManager mgr, int what,int arg1, int arg2)
    {

        if (m_handler == null)
        {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_SHOW_PROGRESS_STRIP.ordinal();
        b.putInt("MessageType", arg1);
        b.putInt("MessageType2", arg2);
        msg.setData(b);
        m_handler.sendMessage(msg);
        return false;
    }

    /**
    * For STB Action Request
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onActionRequest(CaDeskManager mgr, int what,int arg1, int arg2)
    {
        if (m_handler == null)
        {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_ACTION_REQUEST.ordinal();
        b.putInt("MessageType", arg1);
        b.putInt("MessageType2", arg2);
        msg.setData(b);
        m_handler.sendMessage(msg);
        return false;
    }

    /**
    * For STB Entitle Changed Notify
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onEntitleChanged(CaDeskManager mgr, int what,int arg1, int arg2)
    {

        if (m_handler == null)
        {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_ENTITLE_CHANGED.ordinal();
        b.putInt("MessageType", arg1);
        b.putInt("MessageType2", arg2);
        msg.setData(b);
        m_handler.sendMessage(msg);
        return false;
    }

    /**
    * For Notify the Status of STB Detitle Check Num Space
    *
    * @param1 mgr the CaManager the info pertains to.
    * @param2 what the type of info or the Event.
    * @param3 Reserves(Default=0).
    * @param4 what the type of the Message.
    *
    * @return True if the method handled the info, false if it didn't.
    * Returning false, or not having an OnErrorListener at all, will cause the
    * info to be discarded.
    */
    @Override
    public boolean onDetitleReceived(CaDeskManager mgr, int what,int arg1, int arg2)
    {

        if (m_handler == null)
        {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = m_handler.obtainMessage();
        msg.what = CA_EVENT.EV_CA_DETITLE_RECEVIED.ordinal();
        b.putInt("MessageType", arg1);
        b.putInt("MessageType2", arg2);
        msg.setData(b);
        m_handler.sendMessage(msg);
        return false;
    }

    /**
     * For Notify the Status of STB Detitle Check Num Space
     *
     * @param1 mgr the CaManager the info pertains to.
     * @param2 what the type of info or the Event.
     * @param3 Reserves(Default=0).
     * @param4 what the type of the Message.
     *
     * @return True if the method handled the info, false if it didn't.
     * Returning false, or not having an OnErrorListener at all, will cause the
     * info to be discarded.
     */
     @Override
     public boolean onLockService(CaDeskManager mgr, int what,int arg1, int arg2,CaLockService arg3)
     {

         if (m_handler == null)
         {
             return false;
         }
         Bundle b = new Bundle();
         Message msg = m_handler.obtainMessage();
         msg.what = CA_EVENT.EV_CA_LOCKSERVICE.ordinal();
         b.putInt("MessageType", arg1);
         b.putInt("MessageType2", arg2);
         msg.setData(b);
         msg.obj = arg3;
         m_handler.sendMessage(msg);
         return false;
     }


     /**
      * For Notify the Status of STB Detitle Check Num Space
      *
      * @param1 mgr the CaManager the info pertains to.
      * @param2 what the type of info or the Event.
      * @param3 Reserves(Default=0).
      * @param4 what the type of the Message.
      *
      * @return True if the method handled the info, false if it didn't.
      * Returning false, or not having an OnErrorListener at all, will cause the
      * info to be discarded.
      */
      @Override
      public boolean onUNLockService(CaDeskManager mgr, int what,int arg1, int arg2)
      {

          if (m_handler == null)
          {
              return false;
          }
          Bundle b = new Bundle();
          Message msg = m_handler.obtainMessage();
          msg.what = CA_EVENT.EV_CA_LOCKSERVICE.ordinal();
          b.putInt("MessageType", arg1);
          b.putInt("MessageType2", arg2);
          msg.setData(b);
          m_handler.sendMessage(msg);
          return false;
      }
      
      /**
       * For Notify the Status of STB Detitle Check Num Space
       *
       * @param1 mgr the CaManager the info pertains to.
       * @param2 what the type of info or the Event.
       * @param3 Reserves(Default=0).
       * @param4 what the type of the Message.
       *
       * @return True if the method handled the info, false if it didn't.
       * Returning false, or not having an OnErrorListener at all, will cause the
       * info to be discarded.
       */
       @Override
       public boolean onOtaState(CaDeskManager mgr, int what,int arg1, int arg2)
       {

           if (m_handler == null)
           {
               return false;
           }
           Bundle b = new Bundle();
           Message msg = m_handler.obtainMessage();
           msg.what = CA_EVENT.EV_CA_OTASTATE.ordinal();
           b.putInt("MessageType", arg1);
           b.putInt("MessageType2", arg2);
           msg.setData(b);
           m_handler.sendMessage(msg);
           return false;
       }

       @Override
       public boolean onShowCurtainNotify(CaDeskManager mgr, int what, int arg1,int arg2) 
       {
    	   // TODO Auto-generated method stub
    	   return false;
       }

       @Override
       public boolean onActionRequestExt(CaDeskManager mgr, int what, int arg1,int arg2) 
       {
    	   // TODO Auto-generated method stub
    	   return false;
       }

       @Override
       public boolean onShowFingerMessageExt(CaDeskManager mgr, int what,int arg1, int arg2, CaFingerExtMsgInfo arg3) 
       {

           if (m_handler == null)
           {
               return false;
           }
           
           if (arg3 == null)
           {
               return false;
           }
           
           Bundle b = new Bundle();
           Message msg = m_handler.obtainMessage();
           msg.what = CA_EVENT.EV_CA_FINGERMESSAGEEXT.ordinal();
           b.putInt("MessageType", arg1);
           b.putInt("MessageType2", arg2);
           b.putString("FingerMsg", arg3.pMesage);
           msg.setData(b);
           m_handler.sendMessage(msg);
           return false;
       }
       
}

