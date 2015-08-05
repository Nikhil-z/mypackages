package android.test.ui;


import android.app.Activity;
import android.com.konka.dvb.casys.func.CaDeskEventListener;
import android.com.konka.dvb.casys.func.CaDeskManager;
import android.com.konka.dvb.casys.func.CaDeskEventListener.CA_EVENT;
import android.com.konka.dvb.prog.ProgManager;
import android.test.ui.CaErrorType.CA_NOTIFY;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CaViewSmallWindow {

	private CaDeskManager caDesk = null;
	private TextView noSignalTextView;
	private MyCaHandler myCaHandler = null;
	private Activity caActivity = null;
	private RelativeLayout relativeLayout= null;
	private ProgManager pProgManager = null;
	private int iShowNoProgMsg = 0;
	private CaDeskEventListener deskCaLister = null;

	public CaViewSmallWindow(Activity caActivity) {

		Log.d("smallwin", "smallwin caActivity :" + caActivity);

		this.caActivity = caActivity;

		relativeLayout = (RelativeLayout) caActivity.findViewById(R.id.ca_viewholder_layout);

		myCaHandler = new MyCaHandler();
		caDesk = CaDeskManager.getCaMgrInstance();
		pProgManager = ProgManager.getInstance();
		
		deskCaLister = CaDeskEventListener.getInstance();
		deskCaLister.attachHandler(myCaHandler);
		caDesk.setOnCaEventListener(deskCaLister);	
		
		Log.d("smallwin", "getCurrentMsgType :" + caDesk.getCurrentMsgType());
		Log.d("smallwin", "getCurrentEvent :" + caDesk.getCurrentEvent());
		this.sendCaNotifyMsg( caDesk.getCurrentEvent(),caDesk.getCurrentMsgType(),0);
		
		if(null == pProgManager.getCurrverntProgListInfo())
		{
			Log.d("smallwin", "no prog");
			//this.sendCaNotifyMsg( CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),caDesk.getCurrentEvent(),0);
			iShowNoProgMsg = 1;
			this.showNoSignalTextView(R.string.top_no_prog);
		}
	}

	public void showNoSignalTextView(int resid) {

		Log.d("smallwin", "smallwin showNoSignalTextView resid " + resid + " " + caActivity);
		
		if((iShowNoProgMsg ==1)&&(0 == resid)){
			
			if(null == pProgManager.getCurrverntProgListInfo())
			{
				resid = R.string.top_no_prog;
				Log.d("smallwin", "now no prog " + caActivity);
			}
			else{
				iShowNoProgMsg = 0;
			}
		}

		if (relativeLayout == null) {
			return;
		}
		if (noSignalTextView == null) {
			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT);

			noSignalTextView = new TextView(caActivity);
			relativeLayout.addView(noSignalTextView, param);
			noSignalTextView.setGravity(Gravity.CENTER);
			noSignalTextView.setTextSize(22);
			noSignalTextView.setTextColor(Color.WHITE);

			Log.d("smallwin", "smallwin showNoSignalTextView 2");
		}
		if (resid == 0) {
			Log.d("smallwin", "smallwin showNoSignalTextView 3");
			noSignalTextView.setVisibility(View.GONE);
		} else {
			Log.d("smallwin", "smallwin showNoSignalTextView 4");
			noSignalTextView.setVisibility(View.VISIBLE);
			noSignalTextView.setText(resid);
		}
	}

	public boolean isCaEnable() {

		return true;

	}
	
    public void cancelAllSmallWinShowMsg() {
        if (relativeLayout == null) {
            return;
        }
        
        iShowNoProgMsg = 0;
        showNoSignalTextView(0);
    }


	protected class MyCaHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			int what = msg.what;

			if (what == CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal()) {
				int MessageType = b.getInt("MessageType");
				int MsgFrom = b.getInt("MessageFrom");// 0 for SN Post;1 for
														// Activity Send
				CA_NOTIFY notify = null;
				Log.d("smallwin", "smallwin show buy message");

				for (CA_NOTIFY c : CA_NOTIFY.values()) {
					// Log.d("MessageType", "show buy message MessageType " +
					// MessageType + " c.ordinal() " + c.ordinal());
					if (MessageType == c.ordinal()) {
						notify = c;
						break;
					}
				}
				if (notify == null)
					return;

				Log.d("smallwin", notify.toString());
				switch (notify) {
				case ST_CA_MESSAGE_CANCEL_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_CANCEL_TYPE.ordinal(), 0,
							MsgFrom);
					break;
				case ST_CA_MESSAGE_BADCARD_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_BADCARD_TYPE.ordinal(),
							R.string.st_ca_message_badcard_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_EXPICARD_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_EXPICARD_TYPE.ordinal(),
							R.string.st_ca_message_expicard_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_INSERTCARD_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_INSERTCARD_TYPE.ordinal(),
							R.string.st_ca_message_insertcard_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_NOOPER_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_NOOPER_TYPE.ordinal(),
							R.string.st_ca_message_nooper_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_BLACKOUT_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_BLACKOUT_TYPE.ordinal(),
							R.string.st_ca_message_blackout_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_OUTWORKTIME_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_OUTWORKTIME_TYPE.ordinal(),
							R.string.st_ca_message_outworktime_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_WATCHLEVEL_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_WATCHLEVEL_TYPE.ordinal(),
							R.string.st_ca_message_watchlevel_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_PAIRING_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_PAIRING_TYPE.ordinal(),
							R.string.st_ca_message_pairing_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_NOENTITLE_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_NOENTITLE_TYPE.ordinal(),
							R.string.st_ca_message_noentitle_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_DECRYPTFAIL_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_DECRYPTFAIL_TYPE.ordinal(),
							R.string.st_ca_message_decryptfail_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_NOMONEY_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_NOMONEY_TYPE.ordinal(),
							R.string.st_ca_message_nomoney_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_ERRREGION_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_ERRREGION_TYPE.ordinal(),
							R.string.st_ca_message_errregion_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_NEEDFEED_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_NEEDFEED_TYPE.ordinal(),
							R.string.st_ca_message_needfeed_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_ERRCARD_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_ERRCARD_TYPE.ordinal(),
							R.string.st_ca_message_errcard_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_UPDATE_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_UPDATE_TYPE.ordinal(),
							R.string.st_ca_message_update_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_LOWCARDVER_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_LOWCARDVER_TYPE.ordinal(),
							R.string.st_ca_message_lowcardver_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_VIEWLOCK_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_VIEWLOCK_TYPE.ordinal(),
							R.string.st_ca_message_viewlock_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_MAXRESTART_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_MAXRESTART_TYPE.ordinal(),
							R.string.st_ca_message_maxrestart_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_FREEZE_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_FREEZE_TYPE.ordinal(),
							R.string.st_ca_message_freeze_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_CALLBACK_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_CALLBACK_TYPE.ordinal(),
							R.string.st_ca_message_callback_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_CURTAIN_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_CURTAIN_TYPE.ordinal(),
							R.string.st_ca_message_curtain_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_CARDTESTSTART_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_CARDTESTSTART_TYPE
									.ordinal(),
							R.string.st_ca_message_cardteststart_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_CARDTESTFAILD_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_CARDTESTFAILD_TYPE
									.ordinal(),
							R.string.st_ca_message_cardtestfaild_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_CARDTESTSUCC_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_CARDTESTSUCC_TYPE.ordinal(),
							R.string.st_ca_message_cardtestsucc_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_NOCALIBOPER_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_NOCALIBOPER_TYPE.ordinal(),
							R.string.st_ca_message_nocaliboper_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_STBLOCKED_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_STBLOCKED_TYPE.ordinal(),
							R.string.st_ca_message_stblocked_type, MsgFrom);
					break;
				case ST_CA_MESSAGE_STBFREEZE_TYPE:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_CA_MESSAGE_STBFREEZE_TYPE.ordinal(),
							R.string.st_ca_message_stbfreeze_type, MsgFrom);
					break;

				case ST_STB_MESSAGE_NOSIGNAL:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_STB_MESSAGE_NOSIGNAL.ordinal(),
							R.string.top_no_singal, MsgFrom);
					break;
				
				case ST_STB_MESSAGE_NOPROG:
					caNotifyMsgHandler(
							CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal(),
							CA_NOTIFY.ST_STB_MESSAGE_NOPROG.ordinal(),
							R.string.top_no_prog, MsgFrom);
					break;

				}
			}
			super.handleMessage(msg);
		}

		/* For Handle CA Notify Message */
		private void caNotifyMsgHandler(int caEventType, int caMsgType,
				int msgResId, int MsgFrom) {

			Log.d("smallwin", "caNotifyMsgHandler caMsgType :" + caEventType);
			Log.d("smallwin", "caNotifyMsgHandler caMsgType :" + caMsgType);

			showNoSignalTextView(msgResId);

		}

	}

	/* For Activity Send CA Notify Message */

	protected void sendCaNotifyMsg(int caEventType, int caMsgType, int MsgFrom) {
		if (!isCaEnable()) {
			return;
		}
		Log.d("smallwin", "------>smallwin caEventType :" + caEventType
				+ " caMsgType " + caMsgType);
		Bundle b = new Bundle();
		Message msg = myCaHandler.obtainMessage();
		msg.what = caEventType;
		b.putInt("MessageType", caMsgType);
		b.putInt("MessageFrom", MsgFrom);
		msg.setData(b);
		myCaHandler.sendMessage(msg);

	}

}
