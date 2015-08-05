package com.konka.upgrade.utils;

import java.io.File;
import java.util.Locale;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.konka.upgrade.R;
import com.konka.upgrade.dialog.SystemAlertDialog;

public class DialogUtils {
    
    private static TextView textView;
    private static String hintStr;
    private static Context _context;
    
    private static int _timeout;
    private static Handler _Handler = new Handler();
    private static Runnable _Runnable = new Runnable() {
        
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if(_timeout != 0) {
                String timeStr = String.format(Locale.getDefault(),hintStr,_timeout--);
                textView.setText(timeStr);
                _Handler.postDelayed(this, 1000);
            } else {
                SystemUtils.rebootNormal(_context);
            }
        }
    };
    
    public static void showSystemDialog(final Context context) {
        SystemAlertDialog.Builder builder = new SystemAlertDialog.Builder(context);
        builder.setTitle(R.string.upgradeHintTitle);
        builder.setMessage(R.string.upgradeHintMsg);
        builder.setPositiveButtonListener(context.getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                SystemUtils.rebootNormal(context);
            }
        });
        builder.setNegativeButtonListener(context.getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    
    public static SystemAlertDialog createUSBAlertDialog(final Context context,final File packageFile) {
        SystemAlertDialog.Builder builder = new SystemAlertDialog.Builder(context);
        builder.setTitle(R.string.upgradeHintTitle);
        builder.setMessage(R.string.upgradeHintMsg);
        builder.setPositiveButtonListener(context.getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                SystemUtils.rebootInstallPackage(context, packageFile);
                dialog.dismiss();
            }
        });
        builder.setNegativeButtonListener(context.getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        return builder.create();
    }
    
    public static void showForceAlertDialog(Context context) {
        _timeout = 10;
        _context = context;
        hintStr = context.getResources().getString(R.string.upgradeForce);
        View view = View.inflate(context, R.layout.upgrade_alert_countdown, null);
        ((TextView)view.findViewById(R.id.title)).setText(context.getResources().getString(R.string.upgradeHintTitle));
        textView = (TextView)view.findViewById(R.id.hint);
        SystemAlertDialog.Builder builder = new SystemAlertDialog.Builder(context);
        builder.setContentView(view);
        builder.create().show();
        _Handler.post(_Runnable);
    }
}
