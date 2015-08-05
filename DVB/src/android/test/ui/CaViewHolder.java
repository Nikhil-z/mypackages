
package android.test.ui;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import android.com.konka.dvb.casys.data.CaStartIPPVBuyDlgInfo;
import android.com.konka.dvb.casys.exception.CaCommonException;
import android.com.konka.dvb.casys.func.CaDeskEventListener;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.com.konka.dvb.casys.func.CaDeskEventListener.CA_EVENT;
import android.test.ui.CaErrorType.CA_NOTIFY;
import android.test.util.Constant;
import android.test.util.Tools;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CaViewHolder{

    private ProgressBar update_progressBar;

    private TextView update_textview;

    private CaDeskManager caDesk = null;

    private View updateView;

    private View installView;

    private int screenWidth;

    private int screenHeight;

    private TextView disturbTextView;

    private TextView noSignalTextView;

    private AutoScrollTextView osdTextViewUp;

    private AutoScrollTextView osdTextViewDown;

    private ImageView emailNotifyIcon;

    private ImageView detitleNotifyIcon;

    private static final int UPDATE_PROGRESS = 1000000010;

    private static final int UPDATE_START = 1000000011;

    private MyCaHandler myCaHandler = null;

    private Activity caActivity;

    private RelativeLayout relativeLayout;

    private AlertDialog.Builder builder;
	private CaDeskEventListener deskCaLister = null; 
       

    public CaViewHolder(Activity caActivity){
        screenWidth = caActivity.getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = caActivity.getWindowManager().getDefaultDisplay().getHeight();
        this.caActivity=caActivity;
               
        relativeLayout = (RelativeLayout)caActivity.findViewById(R.id.ca_viewholder_layout);
		
        myCaHandler = new MyCaHandler();
        caDesk = CaDeskManager.getCaMgrInstance();
        
		deskCaLister = CaDeskEventListener.getInstance();
		deskCaLister.attachHandler(myCaHandler);
		caDesk.setOnCaEventListener(deskCaLister);	
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
        	
            super.handleMessage(msg);

            if (relativeLayout == null) {
                return;
            }
            switch (msg.what) {
                case UPDATE_START:
                    if (installView == null) {
                        installView = caActivity.getLayoutInflater().inflate(
                                R.layout.ca_show_install_progressbar, null);
                        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                        relativeLayout.addView(installView, param);
                        installView.setX(screenWidth / 2 - 100);
                        installView.setY(screenHeight / 2 - 200);
                    }
                    break;
                case UPDATE_PROGRESS:
                    if(installView!=null){
                        installView.setVisibility(View.GONE);
                    }
                        installView = null;
                    break;
            }
        };
    };

    /**
     * @param num
     */
    protected void showDisturbTextView(String num) {

    	Log.v("camanager", "----1---finger msg screenWidth " + screenWidth + "screenHeight "+screenHeight);
        if (relativeLayout == null) {
            return;
        }
        if (disturbTextView == null) {
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            disturbTextView = new TextView(caActivity);
            relativeLayout.addView(disturbTextView, param);
            Log.v("camanager", "----2---finger msg");
        }
        if ("0".equals(num)) {
            disturbTextView.setVisibility(View.GONE);
        } else {
            disturbTextView.setText(num);
            disturbTextView.setX((float) (Math.random() * (screenWidth - 100)));
            disturbTextView.setY((float) (Math.random() * (screenHeight - 100)));
            disturbTextView.setTextSize(25);
            disturbTextView.setVisibility(View.VISIBLE);
            Log.v("camanager", "----3---finger msg");
        }

    }

    /**
     * @param Activity
     * @param position the position of the view ,1 is up ,2 is down
     */
    protected void showScrollBar(String Activity, int position) {

        Log.v("camanager", "showScrollBar");
        if (relativeLayout == null) {
            return;
        }
        Log.v("camanager", "----2---showScrollBar");
        if (position == 1) {
            if (osdTextViewUp == null) {
                osdTextViewUp = new AutoScrollTextView(caActivity);
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                relativeLayout.addView(osdTextViewUp, param);
            }
            osdTextViewUp.setHeight(50);
            osdTextViewUp.setTextSize(30);
            osdTextViewUp.setText(Activity);
            osdTextViewUp.init(caActivity.getWindowManager());
            osdTextViewUp.setX(0);
            osdTextViewUp.setY(50);
            osdTextViewUp.setVisibility(View.VISIBLE);
            osdTextViewUp.startScroll();

        } else if (position == 2) {
            if (osdTextViewDown == null) {
                osdTextViewDown = new AutoScrollTextView(caActivity);
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                relativeLayout.addView(osdTextViewDown, param);
            }
            osdTextViewDown.setHeight(50);
            osdTextViewDown.setTextSize(30);
            osdTextViewDown.setText(Activity);
            osdTextViewDown.init(caActivity.getWindowManager());
            osdTextViewDown.setX(0);
            osdTextViewDown.setY(screenHeight - 50);
            osdTextViewDown.setVisibility(View.VISIBLE);
            osdTextViewDown.startScroll();

        }

    }

    protected void hideScrollBar(int position) {

    	Log.v("camanager", "----1---hideScrollBar");
        if (relativeLayout == null) {
            return;
        }
        if (position == 1 && osdTextViewUp != null) {
            osdTextViewUp.stopScroll();
            osdTextViewUp.setVisibility(View.GONE);
        } else if (position == 2 && osdTextViewDown != null) {
            osdTextViewDown.stopScroll();
            osdTextViewDown.setVisibility(View.GONE);
        }
    }

    /**
     * @param byshow 0:hide icon 1:show icon 2:Disk full, Icon flash
     */
    protected void emailNotify(int byshow) {

        if (relativeLayout == null) {
            return;
        }
        if (emailNotifyIcon == null) {
            emailNotifyIcon = new ImageView(caActivity);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout.addView(emailNotifyIcon, param);
            emailNotifyIcon.setImageResource(R.drawable.ic_dialog_email);
            emailNotifyIcon.setX(screenWidth - 150);
            emailNotifyIcon.setY(100);
        }
        switch (byshow) {
            case 0:
                emailNotifyIcon.setVisibility(View.GONE);
                break;
            case 1:
                emailNotifyIcon.setVisibility(View.VISIBLE);
                break;
            case 2:
                emailNotifyIcon.setVisibility(View.VISIBLE);
                setFlickerAnimation(emailNotifyIcon);
                break;

        }

    }
    
    /**
     * @param byshow 0:hide icon 1:show icon 2:Disk full, Icon flash
     */
    protected void requestFeedingInfoNotify(int bReadStatus) {

        if (relativeLayout == null) {
            return;
        }
       
        Toast toast = new Toast(caActivity);    
        TextView MsgShow=new TextView(caActivity);
    	MsgShow.setTextColor(Color.RED);
    	MsgShow.setTextSize(25);
    	toast.setView(MsgShow);
    	
        switch (bReadStatus) {
            case 0:
            	MsgShow.setText("read failed");
                break;
            case 1:
            	MsgShow.setText("read success");
                break;
            default:
            	return;

        }
        
        toast.setGravity(Gravity.CENTER, 0, 0);
        showMyToast(toast,5);
 

    }

    /**
     * @param byshow 0:hide icon 1:show icon 2:Disk full, Icon flash
     */
    protected void detitleNotify(int byshow) {

        if (relativeLayout == null) {
            return;
        }
        if (detitleNotifyIcon == null) {
            detitleNotifyIcon = new ImageView(caActivity);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout.addView(detitleNotifyIcon, param);
            detitleNotifyIcon.setImageResource(R.drawable.ca_detitle);
            detitleNotifyIcon.setX(screenWidth - 75);
            detitleNotifyIcon.setY(100);
        }
        switch (byshow) {
            case Constant.CDCA_Detitle_All_Read:
                detitleNotifyIcon.setVisibility(View.GONE);
                break;
            case Constant.CDCA_Detitle_Received:
                detitleNotifyIcon.setVisibility(View.VISIBLE);
                break;
            case Constant.CDCA_Detitle_Space_Small:
                detitleNotifyIcon.setVisibility(View.VISIBLE);
                setFlickerAnimation(detitleNotifyIcon);
                break;

        }

    }

    protected void setFlickerAnimation(ImageView iv_chat_head) {
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        iv_chat_head.setAnimation(animation);
    }

    protected void showProgressBar(int progress) {

        if (relativeLayout == null) {
            return;
        }
        if (updateView == null) {
            updateView = caActivity.getLayoutInflater().inflate(
                    R.layout.ca_show_update_progressbar, null);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            relativeLayout.addView(updateView, param);
            updateView.setX(screenWidth / 2 - 100);
            updateView.setY(screenHeight / 2 - 200);
        }
        Constant.lockKey = false;
        update_progressBar = (ProgressBar) updateView.findViewById(R.id.update_progressBar);
        update_textview = (TextView) updateView.findViewById(R.id.update_textview);
        update_textview.setText(progress + "%");
        update_progressBar.setProgress(progress);
        if (progress == 100) {
            updateView.setVisibility(View.GONE);
            Constant.lockKey = true;
        }
    }

    
    
    public void showNoSignalTextView(int resid) {

        if (relativeLayout == null) {
            return;
        }
        if (noSignalTextView == null) {
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            noSignalTextView = new TextView(caActivity);
            relativeLayout.addView(noSignalTextView, param);
            noSignalTextView.setX(screenWidth / 2 - 100);
            noSignalTextView.setY(screenHeight / 2 + 100);
            noSignalTextView.setTextSize(30);
            noSignalTextView.setTextColor(Color.WHITE);
        }
        if (resid == 0) {
            noSignalTextView.setVisibility(View.GONE);
        } else {
            noSignalTextView.setVisibility(View.VISIBLE);
            noSignalTextView.setText(resid);
        }
    }
    

    public boolean isCaEnable() {

    	return true;

    }

    protected void cancelAllCANotify() {
        if (relativeLayout == null) {
            return;
        }
        detitleNotify(0);
        emailNotify(0);
        hideScrollBar(1);
        hideScrollBar(2);
        showDisturbTextView("0");

    }

    protected class MyCaHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            int what = msg.what;

            if (what == CA_EVENT.EV_CA_START_IPPV_BUY_DLG.ordinal()) {
            	/*
                int wEcmPid = b.getInt("MessageType");
                final CaStartIPPVBuyDlgInfo StIppvInfo = (CaStartIPPVBuyDlgInfo) msg.obj;
                Intent intent = new Intent();
                intent.setClass(caActivity, CaStartIppvBuyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("StIppvInfo", StIppvInfo);
                intent.putExtras(bundle);
                caActivity.startActivity(intent);
                */

            } else if (what == CA_EVENT.EV_CA_HIDE_IPPV_DLG.ordinal()) {
            } else if (what == CA_EVENT.EV_CA_EMAIL_NOTIFY_ICON.ordinal()) {
                int byShow = b.getInt("MessageType");
                int dwEmailID = b.getInt("MessageType2");
                emailNotify(byShow);
                Log.d("camanage", "email notify, byShow :" + byShow + ",dwEmailID: " + dwEmailID);
            } else if (what == CA_EVENT.EV_CA_SHOW_OSD_MESSAGE.ordinal()) {
                int byStyle = b.getInt("MessageType");
                int szMessage = b.getInt("MessageType2");
                String sOsdData = null;
                try {
                    sOsdData = new String(b.getString("StringType").getBytes(), "GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.d("camanage", "osd show notify, byStyle :" + byStyle + ",szMessage: "
                        + szMessage);
                Log.d("camanage", "osd show notify, String is :" + sOsdData);

                showScrollBar(sOsdData, byStyle);

            } else if (what == CA_EVENT.EV_CA_HIDE_OSD_MESSAGE.ordinal()) {
                int byStyle = b.getInt("MessageType");
                hideScrollBar(byStyle);
                Log.d("camanage", "osd hide notify " + byStyle);
            } else if (what == CA_EVENT.EV_CA_REQUEST_FEEDING.ordinal()) {
                int bReadStatus = b.getInt("MessageType");
                requestFeedingInfoNotify(bReadStatus);
                Log.d("camanage", "request notify, bReadStatus :" + bReadStatus );

            } else if (what == CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal()) {
                int MessageType = b.getInt("MessageType");
                int MsgFrom = b.getInt("MessageFrom");// 0 for SN Post;1 for
                                                      // Activity Send
                CA_NOTIFY notify = null;
                Log.d("MessageType", "show buy message");
                
                for (CA_NOTIFY c : CA_NOTIFY.values()) {
                	// Log.d("MessageType", "show buy message MessageType " + MessageType + " c.ordinal() " +  c.ordinal());
                    if (MessageType == c.ordinal()) {
                        notify = c;
                        break;
                    }
                }
                if (notify == null)
                    return;

                Log.d("MessageType", notify.toString());
                switch (notify) {
                    case ST_CA_MESSAGE_CANCEL_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_CANCEL_TYPE.ordinal(), 0, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_BADCARD_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_BADCARD_TYPE.ordinal(),
                                R.string.st_ca_message_badcard_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_EXPICARD_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_EXPICARD_TYPE.ordinal(),
                                R.string.st_ca_message_expicard_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_INSERTCARD_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_INSERTCARD_TYPE.ordinal(),
                                R.string.st_ca_message_insertcard_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_NOOPER_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_NOOPER_TYPE.ordinal(),
                                R.string.st_ca_message_nooper_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_BLACKOUT_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_BLACKOUT_TYPE.ordinal(),
                                R.string.st_ca_message_blackout_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_OUTWORKTIME_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_OUTWORKTIME_TYPE.ordinal(),
                                R.string.st_ca_message_outworktime_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_WATCHLEVEL_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_WATCHLEVEL_TYPE.ordinal(),
                                R.string.st_ca_message_watchlevel_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_PAIRING_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_PAIRING_TYPE.ordinal(),
                                R.string.st_ca_message_pairing_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_NOENTITLE_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_NOENTITLE_TYPE.ordinal(),
                                R.string.st_ca_message_noentitle_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_DECRYPTFAIL_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_DECRYPTFAIL_TYPE.ordinal(),
                                R.string.st_ca_message_decryptfail_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_NOMONEY_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_NOMONEY_TYPE.ordinal(),
                                R.string.st_ca_message_nomoney_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_ERRREGION_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_ERRREGION_TYPE.ordinal(),
                                R.string.st_ca_message_errregion_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_NEEDFEED_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_NEEDFEED_TYPE.ordinal(),
                                R.string.st_ca_message_needfeed_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_ERRCARD_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_ERRCARD_TYPE.ordinal(),
                                R.string.st_ca_message_errcard_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_UPDATE_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_UPDATE_TYPE.ordinal(),
                                R.string.st_ca_message_update_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_LOWCARDVER_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_LOWCARDVER_TYPE.ordinal(),
                                R.string.st_ca_message_lowcardver_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_VIEWLOCK_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_VIEWLOCK_TYPE.ordinal(),
                                R.string.st_ca_message_viewlock_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_MAXRESTART_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_MAXRESTART_TYPE.ordinal(),
                                R.string.st_ca_message_maxrestart_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_FREEZE_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_FREEZE_TYPE.ordinal(),
                                R.string.st_ca_message_freeze_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_CALLBACK_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_CALLBACK_TYPE.ordinal(),
                                R.string.st_ca_message_callback_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_CURTAIN_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_CURTAIN_TYPE.ordinal(),
                                R.string.st_ca_message_curtain_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_CARDTESTSTART_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_CARDTESTSTART_TYPE.ordinal(),
                                R.string.st_ca_message_cardteststart_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_CARDTESTFAILD_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_CARDTESTFAILD_TYPE.ordinal(),
                                R.string.st_ca_message_cardtestfaild_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_CARDTESTSUCC_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_CARDTESTSUCC_TYPE.ordinal(),
                                R.string.st_ca_message_cardtestsucc_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_NOCALIBOPER_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_NOCALIBOPER_TYPE.ordinal(),
                                R.string.st_ca_message_nocaliboper_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_STBLOCKED_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_STBLOCKED_TYPE.ordinal(),
                                R.string.st_ca_message_stblocked_type, MsgFrom);
                        break;
                    case ST_CA_MESSAGE_STBFREEZE_TYPE:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_CA_MESSAGE_STBFREEZE_TYPE.ordinal(),
                                R.string.st_ca_message_stbfreeze_type, MsgFrom);
                        break;
                        
                    case ST_STB_MESSAGE_NOSIGNAL:
                        caNotifyMsgHandler(CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
                                CA_NOTIFY.ST_STB_MESSAGE_NOSIGNAL.ordinal(),
                                R.string.top_no_singal, MsgFrom);
                        break;
                        
                }
            } else if (what == CA_EVENT.EV_CA_SHOW_FINGER_MESSAGE.ordinal()) {
                int num = b.getInt("MessageType");
                int num2 = b.getInt("MessageType2");
                showDisturbTextView(num2 + "");
                Log.d("camanage", "EV_CA_SHOW_FINGER_MESSAGE ");
            } else if (what == CA_EVENT.EV_CA_SHOW_PROGRESS_STRIP.ordinal()) {
                Log.d("camanage", "EV_CA_SHOW_PROGRESS_STRIP ");
            } else if (what == CA_EVENT.EV_CA_ACTION_REQUEST.ordinal()) {
                Log.d("camanage", "EV_CA_ACTION_REQUEST ");
            } else if (what == CA_EVENT.EV_CA_ENTITLE_CHANGED.ordinal()) {
                Log.d("camanage", "EV_CA_ENTITLE_CHANGED ");
            } else if (what == CA_EVENT.EV_CA_DETITLE_RECEVIED.ordinal()) {
                Log.d("camanage", "EV_CA_ENTITLE_CHANGED ");
                int byShow = b.getInt("MessageType");
                Log.d("camanage", "byShow===" + byShow);
                detitleNotify(byShow);

            } else if (what == CA_EVENT.EV_CA_LOCKSERVICE.ordinal()) {
                Log.d("camanage", "EV_CA_LOCKSERVICE ");
                //Constant.lockKey = false;
            } else if (what == CA_EVENT.EV_CA_UNLOCKSERVICE.ordinal()) {
                Log.d("camanage", "EV_CA_UNLOCKSERVICE ");
                //Constant.lockKey = true;
            } else if (what == CA_EVENT.EV_CA_OTASTATE.ordinal()) {
            

            }else if (what == CA_EVENT.EV_CA_FINGERMESSAGEEXT.ordinal()) {
                int num = b.getInt("MessageType");
                int num2 = b.getInt("MessageType2");
                String fingermsg = b.getString("FingerMsg");
                showDisturbTextView(fingermsg + "");
                Log.d("camanage", "EV_CA_SHOW_FINGER_MESSAGE ");
            } 
            super.handleMessage(msg);
        }

        /* For Handle CA Notify Message */
        private void caNotifyMsgHandler(int caEventType, int caMsgType, int msgResId, int MsgFrom) {

            Log.d("camanage", "caMsgType :" + caMsgType);
            /**
            * konka.dvb.EXCEPTION
			*key锛歝ontent , 瀛樻彁绀哄唴�?
			*key锛歷isibility 锛屽瓨boolean锛屾樉绀烘垨鑰呮秷澶?
            * */
            showNoSignalTextView(msgResId);
            //Intent intent = new Intent("konka.dvb.EXCEPTION");
            //intent.putExtra(COUNTER_VALUE, counter); 
            
            //caActivity.sendBroadcast(i);
           
        }
        
    }
    /* For Activity Send CA Notify Message */
    
    protected void sendCaNotifyMsg(int caEventType, int caMsgType, int MsgFrom) {
        if(!isCaEnable()){
            return;
        }
        Log.d("camanage", "caEventType :" + caEventType + " caMsgType "+ caMsgType);
        Bundle b = new Bundle();
        Message msg = myCaHandler.obtainMessage();
        msg.what = caEventType;
        b.putInt("MessageType", caMsgType);
        b.putInt("MessageFrom", MsgFrom);
        msg.setData(b);
        myCaHandler.sendMessage(msg);

    }
    
    private void showMyToast(Toast toast, int cnt) {
		if (cnt < 0)
			return;
		toast.show();
		execToast(toast, cnt);
	}


    private void execToast(final Toast toast, final int cnt) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				showMyToast(toast, cnt - 1);
			}
		}, 1000);
	}
    
	

}
