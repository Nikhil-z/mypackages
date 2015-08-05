package com.konka.upgrade.dialog;

import com.konka.upgrade.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SystemAlertDialog extends Dialog{

    private SystemAlertDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    private SystemAlertDialog(Context context,int theme) {
        super(context, theme);
    }
    
    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveBtnTxt;
        private String negativeBtbTxt;
        private View contentView;
        
        private DialogInterface.OnClickListener positiveBtnOnClickListener,negativeBtnOnClickListener;
        
        public Builder(Context context) {
            this.context = context;
        }
        
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        
        public Builder setTitle(int titleID) {
            this.title = context.getResources().getString(titleID);
            return this;
        }
        
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
        
        public Builder setMessage(int msgID) {
            this.message = context.getResources().getString(msgID);
            return this;
        }
        
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }
        
        public Builder setPositiveButtonListener(String text,DialogInterface.OnClickListener listener) {
            this.positiveBtnTxt = text;
            this.positiveBtnOnClickListener = listener;
            return this;
        }
        
        public Builder setNegativeButtonListener(String text,DialogInterface.OnClickListener listener) {
            this.negativeBtbTxt = text;
            this.negativeBtnOnClickListener = listener;
            return this;
        }
        
        public SystemAlertDialog create() {
            View view = View.inflate(context, R.layout.upgrade_alert, null);
            final SystemAlertDialog dialog = new SystemAlertDialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            
            ((TextView)view.findViewById(R.id.upgradeTitle)).setText(title);
            
            if(positiveBtnTxt != null) {
                ((Button)view.findViewById(R.id.btn_ok)).setText(positiveBtnTxt);
                if(positiveBtnOnClickListener != null) {
                    ((Button)view.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            positiveBtnOnClickListener.onClick(dialog, R.id.btn_ok);
                        }
                    });
                } 
            } else {
                view.findViewById(R.id.btn_ok).setVisibility(View.GONE);
            }
            
            if(negativeBtbTxt != null) {
                Button btnCancel = (Button)view.findViewById(R.id.btn_cancel);
                btnCancel.setText(negativeBtbTxt);
                if(negativeBtnOnClickListener != null) {
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            negativeBtnOnClickListener.onClick(dialog, R.id.btn_cancel);
                        }
                    });
                    btnCancel.requestFocus();
                    btnCancel.requestFocusFromTouch();
                }
            } else {
                view.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
            }
            
            if(message != null) {
                ((TextView)view.findViewById(R.id.upgradeHint)).setText(message);
            } else if(contentView != null) {
                ((RelativeLayout)view.findViewById(R.id.content)).removeAllViews();
                ((RelativeLayout)view.findViewById(R.id.content)).addView(contentView,
                        new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            return dialog;
        }
    }
}
