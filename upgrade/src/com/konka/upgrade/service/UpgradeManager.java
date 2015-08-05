package com.konka.upgrade.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXParseException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.konka.system.SystemSignatureNative;
import com.konka.system.SystemManufactureNative;
import com.konka.system.ManufactureData;
import com.konka.upgrade.R;
import com.konka.upgrade.dialog.SystemAlertDialog;
import com.konka.upgrade.download.DownloadHandler;
import com.konka.upgrade.download.DownloadManager;
import com.konka.upgrade.download.DownloadListener;
import com.konka.upgrade.download.Downloads;
import com.konka.upgrade.utils.ConfigUtils;
import com.konka.upgrade.utils.DialogUtils;
import com.konka.upgrade.utils.FormatUtils;
import com.konka.upgrade.utils.StorageUtils;
import com.konka.upgrade.utils.SystemUtils;
import com.konka.upgrade.xml.XmlParser;
import com.konka.upgrade.xml.XmlParserListener;
import com.konka.upgrade.xml.XmlUpgradeInfo;
import com.konka.upgrade.xml.XmlUpgradeInfo.Scope;

public class UpgradeManager {
    
    private final String TAG = UpgradeManager.class.getSimpleName();
    private final static int OTA_UPGRADE = 0x1000;
    private final static int OTA_UPGRADE_FORCE = 0x1001;
    private final static int USB_UPGRADE = 0x1002;
    private final static int USB_UPGRADE_EJECT = 0x1003;
    private String packageFile;
    private String packageFilesn;

    
    private RemoteUpgradeService remoteService;
    private DownloadManager mDownloadManager;
    private SDCardReceiver receiver;
    private TimerTask task;
    private MyHandler mHandler;
    
    private static boolean isUpgrading;
    private static boolean userCanceled;

    private UpgradeDesc mUpgradeDesc;
    private SystemSignatureNative mSignature;
    private SystemManufactureNative manufactureNative;
    private Map<String, String> upgradeMap;
    private String mXmlUrl ;
    private String mXmlEncryption_url;
    protected String mPackageEncryptionUrl;
    protected String mPackageUrl;

    private Timer timer;
    private int count; 
    
    protected Context mContext;
    
    protected SharedPreferences preferences;
    
    public UpgradeManager(Context context) {
        mContext = context;
        count = 1;
        Log.i(TAG, "count---begin--------------"+count);
        remoteService = new RemoteUpgradeService(this);
        mXmlUrl = ConfigUtils.getDefaultXmlUrl(mContext);
        mXmlEncryption_url = ConfigUtils.getDefaultXmlVerifyUrl(mContext);
        Log.i(TAG,"peroid is "+ConfigUtils.getPeriod(mContext)+"  mDeley is "+ ConfigUtils.getDelay(mContext));
        mDownloadManager = new DownloadManager(context);
        timer = new Timer();
        task = new MyTimerTask();
        mUpgradeDesc = new UpgradeDesc();
        mUpgradeDesc.clear();
        mSignature = new SystemSignatureNative();
        manufactureNative = new SystemManufactureNative();
        upgradeMap = new HashMap<String, String>();
        mHandler = new MyHandler(this);
        receiver = new SDCardReceiver();
        userCanceled = false;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addDataScheme("file");
        preferences = PreferenceManager.getDefaultSharedPreferences(context);  
        mContext.registerReceiver(receiver, filter);
    }
    
    public RemoteUpgradeService getRemoteService() {
        return remoteService;
    }
    
    public synchronized void invoke() {
		invoke(ConfigUtils.getDelay(mContext), ConfigUtils.getPeriod(mContext));   
    }
    
    private synchronized void invoke(long delay,long period) {
        Log.i(TAG,"delay "+delay+" period "+period);
        try {
            timer.cancel();
            task.cancel();
            timer = new Timer();
            task = new MyTimerTask();
            timer.schedule(task,delay,period);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        Log.e(TAG,"on destroy");
        task.cancel();
        timer.cancel();
        mContext.unregisterReceiver(receiver);
        DownloadHandler.getInstance().stopAll();
    }
    
    public synchronized boolean checkUpgrade() {
        boolean ret = false;
        Log.i(TAG,"call checkUpgrade!!!!,flag is  "+isUpgrading);
        timer.cancel();
        task.cancel();
        timer = new Timer();
        task = new MyTimerTask();
        if(!isUpgrading && !userCanceled) {
            ret = isUpgrading = true;
            
            downloadXmlVerifyFile(mXmlEncryption_url);
        } else {
        }
        return ret;
    }
    
    protected synchronized UpgradeDesc getUpgradeDesc() {
        return mUpgradeDesc;
    }
    
    protected synchronized void startDownload() {
        downloadUpgradeVerifyFile(mPackageEncryptionUrl);
    }
    
    protected synchronized void installUpgrade() {
        procedure(UpgradeType.INSTALL_UPGRADE_FILE);
        mUpgradeDesc.setUpgradeFilePath(preferences.getString("saveFileP",""));
        SystemUtils.rebootInstallPackage(mContext, new File(mUpgradeDesc.getUpgradeFilePath()));
    }
    
    protected synchronized void cancel() {
//      deleteAll();
        isUpgrading = false;
        userCanceled = true;
        invoke(ConfigUtils.getPeriod(mContext), ConfigUtils.getPeriod(mContext));
    }
    
    protected synchronized void neverRemind() {
//        deleteAll();
        ConfigUtils.setNeverRemindVersion(mContext, mUpgradeDesc.getUpgradeVersion());
        isUpgrading = false;
        userCanceled = true;
        invoke(ConfigUtils.getPeriod(mContext), ConfigUtils.getPeriod(mContext));
    }
    
    public boolean procedure(int type) {
        Log.e(TAG,"procedure "+type);
        boolean ret = false;
        switch(type) {
        case UpgradeType.DOWNLOAD_XML:
        case UpgradeType.DOWNLOAD_XML_VERIFY_FILE:
        case UpgradeType.XML_VERIFY:
        case UpgradeType.PARSE_XML:
        case UpgradeType.UPGRADE_FOUND:
        case UpgradeType.USER_CANCEL:
        case UpgradeType.DOWNLOAD_COMPLETE:
            remoteService.notifyClientProgress(type);
            ret = true;
            break;
        default:
            if(mUpgradeDesc.getControl().equals("3")) {
                remoteService.notifyClientProgress(type);
                ret = true;
            } else {
                ret = false;
            }
            break;
        }
        return ret;
    }
    
    private void downloadProgress(int type,long downloadSize,long totalSize) {
        switch(type) {
        case UpgradeType.DOWNLOAD_XML:
        case UpgradeType.DOWNLOAD_XML_VERIFY_FILE:
            remoteService.notifyClientDownloadProgress(type,downloadSize,totalSize);
            break;
        default:
            if(mUpgradeDesc.getControl().equals("3")) {
                remoteService.notifyClientDownloadProgress(type,downloadSize,totalSize);
            }
            break;
        }
    }
    
    private void error(int type,int errMsg) {
    	Log.e(TAG,String.format(Locale.getDefault(), "type %d,errmsg %d",type,errMsg));
        remoteService.notifyClientError(type, errMsg);
        if( (type != UpgradeType.DOWNLOAD_IMGINFO) &&
                (errMsg != UpgradeType.UPGRADE_NOT_FOUND_FORCE)){
            mUpgradeDesc.clear();
            upgradeMap.clear();
            if(errMsg == UpgradeType.FILE_VERIFY_ERROR ||
            		errMsg == UpgradeType.UPGRADE_NOT_FOUND ||
            		errMsg == UpgradeType.XML_PARSE_ERROR ){
            	count = 1;
            	Log.i(TAG, "count1-----------------"+count);
                invoke(ConfigUtils.getPeriod(mContext), ConfigUtils.getPeriod(mContext));
            }
            else if(type == UpgradeType.DOWNLOAD_XML_VERIFY_FILE && errMsg == UpgradeType.DOWNLOAD_ERROR){
            	if(count == 1){
            		count++;
            		Log.i(TAG, "count1-----------------"+count);
            		invoke(ConfigUtils.getPeriod(mContext), ConfigUtils.getPeriod(mContext));
            	}
            	else if(count == 2){
            		count = 1;
            		Log.i(TAG, "count2-----------------"+count);
            		invoke(ConfigUtils.getPeriod(mContext), ConfigUtils.getPeriod(mContext));
            	}
            	else{
            		count = 1;
            		Log.i(TAG, "count-----------------else"+count);
            		invoke(ConfigUtils.getPeriod(mContext), ConfigUtils.getPeriod(mContext));
            	}
            }
            else{
            	invoke(ConfigUtils.getPeriod(mContext), ConfigUtils.getPeriod(mContext));
            }
            isUpgrading = false;
        }
    }
    
    private void downloadOrNotify() {
//        if(mUpgradeDesc.getControl().equals("3")) {
//            Log.i(TAG,String.format(Locale.getDefault(), "never remind version %d current %d",
//                    ConfigUtils.getNeverRemindVersion(mContext),mUpgradeDesc.getUpgradeVersion()));
//            if (ConfigUtils.getNeverRemindVersion(mContext) != mUpgradeDesc.getUpgradeVersion() ) {
//                procedure(UpgradeType.UPGRADE_FOUND);
//            } else {
//                error(UpgradeType.PARSE_XML, UpgradeType.UPGRADE_NOT_FOUND);
//            }
//        } else {
            //error(UpgradeType.PARSE_XML, UpgradeType.UPGRADE_NOT_FOUND_FORCE);
            downloadUpgradeVerifyFile(mPackageEncryptionUrl);
//        }
    }
    
    private void downloadXmlFile(String url) {
        mDownloadManager.startDownload(url, new DownloadListener() {
            
            @Override
            public void onError(Throwable error) {
                // TODO Auto-generated method stub
                error.printStackTrace();
                error(UpgradeType.DOWNLOAD_XML,UpgradeType.DOWNLOAD_ERROR);
            }
            
            @Override
            public void onDownloadStart(String url) {
                // TODO Auto-generated method stub
                procedure(UpgradeType.DOWNLOAD_XML);
            }
            
            @Override
            public void onDownloadSize(String url, long size, long totalSize) {
                // TODO Auto-generated method stub
                downloadProgress(UpgradeType.DOWNLOAD_XML,size,totalSize);
            }
            
            @Override
            public void onDownloadComplete(String url, String saveFile) {
                // TODO Auto-generated method stub
                procedure(UpgradeType.XML_VERIFY);
                Log.i(TAG,"saveFile "+saveFile);
                Log.i(TAG,"mXmlEncryption "+upgradeMap.get(mXmlEncryption_url));
                if (mSignature.FileSignatureVerify(saveFile,upgradeMap.get(mXmlEncryption_url)) == 0) {
                    try {
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser parser = factory.newSAXParser();
                        XmlParser handler = new XmlParser();
                        handler.setXmlParserListener(new UpgradeXmlParserListener());
                        parser.parse(new File(saveFile), handler);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        error(UpgradeType.PARSE_XML,UpgradeType.XML_PARSE_ERROR);
                    }
                } else {
                    deleteAll();
                    error(UpgradeType.XML_VERIFY,UpgradeType.FILE_VERIFY_ERROR);
                    
                }
            }
        });
    }
    
    private void downloadXmlVerifyFile(String url) {
        mDownloadManager.startDownload(url, new DownloadListener() {
            
            @Override
            public void onError(Throwable error) {
                // TODO Auto-generated method stub
                error.printStackTrace();
                error(UpgradeType.DOWNLOAD_XML_VERIFY_FILE,UpgradeType.DOWNLOAD_ERROR);
            }
            
            @Override
            public void onDownloadStart(String url) {
                // TODO Auto-generated method stub
                procedure(UpgradeType.DOWNLOAD_XML_VERIFY_FILE);
            }
            
            @Override
            public void onDownloadSize(String url, long size, long totalSize) {
                // TODO Auto-generated method stub
                downloadProgress(UpgradeType.DOWNLOAD_XML_VERIFY_FILE,size,totalSize);
            }
            
            @Override
            public void onDownloadComplete(String url, String saveFile) {
                // TODO Auto-generated method stub
                upgradeMap.put(url, saveFile);
                downloadXmlFile(mXmlUrl);
            }
        });
    }
    
    private void downloadUpgradeFile(String url) {
        mDownloadManager.startDownload(url, new DownloadListener() {
            
            @Override
            public void onError(Throwable error) {
                // TODO Auto-generated method stub
                error.printStackTrace();
                error(UpgradeType.DOWNLOAD_UPGRADE_FILE,UpgradeType.DOWNLOAD_ERROR);
            }
            
            @Override
            public void onDownloadStart(String url) {
                // TODO Auto-generated method stub
                procedure(UpgradeType.DOWNLOAD_UPGRADE_FILE);
            }
            
            @Override
            public void onDownloadSize(String url, long size, long totalSize) {
                // TODO Auto-generated method stub
                downloadProgress(UpgradeType.DOWNLOAD_UPGRADE_FILE,size,totalSize);
            }
            
            @Override
            public void onDownloadComplete(String url, String saveFile) {
                // TODO Auto-generated method stub
                procedure(UpgradeType.UPGRADE_FILE_VERIFY);
                if (mSignature.FilePublicKeyVerify(saveFile,upgradeMap.get(mPackageEncryptionUrl)) == 0) {
                    mUpgradeDesc.setUpgradeFilePath(saveFile);
                    preferences.edit().putString("saveFileP", saveFile).apply();
                    try {
                        if (mUpgradeDesc.getControl().equals("1")) {
                            SystemUtils.installPackageWithoutReboot(mContext, new File(saveFile));
                            Log.i(TAG, "Save file in control 1:" + saveFile);
                            mHandler.sendEmptyMessage(OTA_UPGRADE);
                        } else if(mUpgradeDesc.getControl().equals("0")) {
                            SystemUtils.installPackageWithoutReboot(mContext, new File(saveFile));
                            Log.i(TAG, "Save file in control 0:" + saveFile);
                            mHandler.sendEmptyMessage(OTA_UPGRADE_FORCE);
                        } else if(mUpgradeDesc.getControl().equals("3")) {
                            procedure(UpgradeType.DOWNLOAD_COMPLETE);
                            preferences.edit().putBoolean("download_complete", true).apply();
                            preferences.edit().putLong("curVersion", mUpgradeDesc.getUpgradeVersion()).apply();
                            isUpgrading = false;
                            Log.i(TAG, "download_complete:"+preferences.getBoolean("download_complete", true)+"curVersion:"+preferences.getLong("curVersion", 0));
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    deleteAll();                   
                    error(UpgradeType.UPGRADE_FILE_VERIFY,UpgradeType.FILE_VERIFY_ERROR);
                }
            }
        });
    }
    
    private void downloadUpgradeVerifyFile(String url) {
    	
    	if(preferences.getBoolean("download_complete", false) && (mUpgradeDesc.getUpgradeVersion() == preferences.getLong("curVersion", 0)) && (mUpgradeDesc.getControl().equals("3"))){
    		Log.i(TAG, "reboot complete:"+ConfigUtils.getNeverRemindVersion(mContext));
    		isUpgrading = false;
    		packageFile = preferences.getString("saveFileP", "");
    		packageFilesn = preferences.getString("saveFileP", "")+".sn";
    		File pacFile = new File(packageFile);
    		File snFile = new File(packageFilesn);
    		Log.i(TAG, "pacFile:"+packageFile);
    		Log.i(TAG, "pacFileSn:"+packageFilesn);
    		if(pacFile.exists() && snFile.exists() && (mSignature.FilePublicKeyVerify(packageFile,packageFilesn) == 0)){
	    		if(preferences.getLong("curVersion", 0) == ConfigUtils.getNeverRemindVersion(mContext))
	    		{
	    			procedure(UpgradeType.USER_CANCEL);
	    		}
	    		else
	    		{
	    			procedure(UpgradeType.DOWNLOAD_COMPLETE);
	    		}
    		}
    		else
    		{
    			deleteAll();
    			preferences.edit().putBoolean("download_complete", false).apply();
    			preferences.edit().putString("saveFileP", " ").apply();
    			invoke(ConfigUtils.getPeriod(mContext), ConfigUtils.getPeriod(mContext));

    		}
    		
    	}
	    else{
	    	
	    	
	    	Log.i(TAG, "not reboot complete");
	        Cursor cursor = mContext.getContentResolver().query(
	                Downloads.Impl.CONTENT_URI, 
	                null,
	                "("+Downloads.Impl.COLUMN_URI+"='"+mPackageUrl+"')", 
	                null, null, null);
	        try {
	            if(!cursor.moveToFirst()) {
	                deleteAll();
	                preferences.edit().putBoolean("download_complete", false).apply();
	                Log.e(TAG, "cursor not move to first!!!!!!!!!!!!!!!");
	            }
	        } finally {
	            cursor.close();
	        }
	    	
	        mDownloadManager.startDownload(url, new DownloadListener() {
	            
	            @Override
	            public void onError(Throwable error) {
	                // TODO Auto-generated method stub
	                error.printStackTrace();
	                error(UpgradeType.DOWNLOAD_UPGRADE_VERIFY_FILE,UpgradeType.DOWNLOAD_ERROR);
	            }
	            
	            @Override
	            public void onDownloadStart(String url) {
	                // TODO Auto-generated method stub
	                procedure(UpgradeType.DOWNLOAD_UPGRADE_VERIFY_FILE);
	            }
	            
	            @Override
	            public void onDownloadSize(String url, long size, long totalSize) {
	                // TODO Auto-generated method stub
	                downloadProgress(UpgradeType.DOWNLOAD_UPGRADE_VERIFY_FILE,size,totalSize);
	            }
	            
	            @Override
	            public void onDownloadComplete(String url, String saveFile) {
	                // TODO Auto-generated method stub
	                upgradeMap.put(url, saveFile);
	                downloadUpgradeFile(mPackageUrl);
	            }
	        });
	     }
    }
    
    private void downloadImageInfo(String url) {
        mDownloadManager.startDownload(url, new DownloadListener() {
            
            @Override
            public void onError(Throwable error) {
                // TODO Auto-generated method stub
                error.printStackTrace();
                error(UpgradeType.DOWNLOAD_IMGINFO,UpgradeType.DOWNLOAD_ERROR);
                downloadOrNotify();
            }
            
            @Override
            public void onDownloadStart(String url) {
                // TODO Auto-generated method stub
                procedure(UpgradeType.DOWNLOAD_IMGINFO);
            }
            
            @Override
            public void onDownloadSize(String url, long size, long totalSize) {
                // TODO Auto-generated method stub
                downloadProgress(UpgradeType.DOWNLOAD_IMGINFO,size,totalSize);
            }
            
            @Override
            public void onDownloadComplete(String url, String saveFile) {
                // TODO Auto-generated method stub
                int count;
                BufferedReader reader = null;
                FileInputStream is = null;
                BufferedInputStream bi = null;
                byte[] buff = new byte[1024];
                File file = new File(saveFile);
                try {
                    is = new FileInputStream(file);
                    bi = new BufferedInputStream(is);
                    bi.mark(4);
                    byte[] first3bytes = new byte[3];
                    bi.read(first3bytes);
                    bi.reset();
                    if (first3bytes[0] == (byte) 0xEF 
                            && first3bytes[1] == (byte) 0xBB 
                            && first3bytes[2] == (byte) 0xBF) {// utf-8
                        reader = new BufferedReader(new InputStreamReader(bi, "utf-8"));
                    } else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {
                        reader = new BufferedReader( new InputStreamReader(bi, "unicode"));
                    } else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte)0xFF) { 
                        reader = new BufferedReader(new InputStreamReader(bi,"utf-16be")); 
                    } else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFF) {
                        reader = new BufferedReader(new InputStreamReader(bi,"utf-16le"));
                    } else {
                        reader = new BufferedReader(new InputStreamReader(bi,"GBK"));
                    }
                    String line = "";
                    String info = "";
                    while( (line = reader.readLine()) != null) {
                        info = info + line + "\n";
                    }
                    mUpgradeDesc.setImgInfo(info);
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(is != null) 
                            is.close();
                        if(bi != null) 
                            bi.close();
                        if(reader != null)
                            reader.close();
                    } catch(Exception e) {}
                }
                downloadOrNotify();
            }
        });
    }
    
    public void deleteAll() {
        try {
            for(File file: new File(StorageUtils.FILE_ROOT).listFiles()) {
            	Log.i(TAG,"deleted file is "+file.getName());
                StorageUtils.delete(file);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        mContext.getContentResolver().delete(Downloads.Impl.CONTENT_URI, null, null);
    }
    
    private static class MyHandler extends Handler{
        
        private WeakReference<UpgradeManager> managerReference;
        private SystemAlertDialog mUsbDialog;
        
        public MyHandler(UpgradeManager manager) {
            managerReference = new WeakReference<UpgradeManager>(manager);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch(msg.what) {
            case OTA_UPGRADE:
                DialogUtils.showSystemDialog(managerReference.get().mContext);
                break;
            case OTA_UPGRADE_FORCE:
                DialogUtils.showForceAlertDialog(managerReference.get().mContext);
                break;
            case USB_UPGRADE:
                if ((mUsbDialog != null) && (mUsbDialog.isShowing())) {
                    mUsbDialog.dismiss();
                }
                mUsbDialog = DialogUtils.createUSBAlertDialog(managerReference.get().mContext, new File((String) msg.obj));
                mUsbDialog.show();
                break;
            case USB_UPGRADE_EJECT:
                if ((mUsbDialog != null) && (mUsbDialog.isShowing())) {
                    mUsbDialog.dismiss();
                }
                break;
            }
        }
    }
    
    
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.i(TAG,"timer task run");
            checkUpgrade();
        }
    }
    
    private class UpgradeXmlParserListener implements XmlParserListener {

        @Override
        public void onError(SAXParseException e) {
            // TODO Auto-generated method stub
            e.printStackTrace();
            error(UpgradeType.PARSE_XML,UpgradeType.XML_PARSE_ERROR);
        }

        @Override
        public void onStart() {
            // TODO Auto-generated method stub
            procedure(UpgradeType.PARSE_XML);
        }

        @Override
        public void onSuccess(XmlUpgradeInfo info) {
            // TODO Auto-generated method stub
            try {
                long period = Integer.parseInt(info.getPeriod());
                if(period <1) {
                    period = 1;
                }
                Log.e(TAG,"period is "+period);
                ConfigUtils.setPeriod(mContext, period*60*1000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if(parseXML(info,mUpgradeDesc)) {
                    if(mUpgradeDesc.getImgUrl().size() != 0) {
                        int index = Math.abs(new Random().nextInt()) % mUpgradeDesc.getImgUrl().size();
                        if(index == mUpgradeDesc.getImgUrl().size()) 
                            index = index -1;
                        mPackageUrl = mUpgradeDesc.getImgUrl().get(index);
                        mPackageEncryptionUrl = String.format(Locale.getDefault(), "%s%s",mPackageUrl,".sn");
                        Log.i(TAG,String.format(Locale.getDefault(), "index is %d,encrypt url is %s \n"
                                + "imginfo url is %s ",index,mPackageEncryptionUrl,mUpgradeDesc.getImgInfoUrl()));
                        if( (mUpgradeDesc.getImgInfoUrl() != null) &&
                                (mUpgradeDesc.getImgInfoUrl() != "") ){
                            downloadImageInfo(mUpgradeDesc.getImgInfoUrl());
                        } else {
                            downloadOrNotify();
                        }
                    } else {
                        error(UpgradeType.PARSE_XML,UpgradeType.XML_PARSE_ERROR);
                    }
                } else {
                    error(UpgradeType.PARSE_XML,UpgradeType.UPGRADE_NOT_FOUND);
                }
            } catch (Exception e) {
                e.printStackTrace();
                error(UpgradeType.PARSE_XML,UpgradeType.XML_PARSE_ERROR);
            }
        }
        
        private boolean parseXML(XmlUpgradeInfo xmlInfo,UpgradeDesc info) throws Exception{
            try{
                ManufactureData data = new ManufactureData();
                manufactureNative.Init();
                manufactureNative.GetManufactureData(data);
                long currentSN = data.SerialNumber;
                info.setManufacture(xmlInfo.getManufacture());
                info.setModel(xmlInfo.getModelID());
                info.setHWVersion(xmlInfo.getHwVersion());
                info.setControl(xmlInfo.getControl());
                info.setPeriod(xmlInfo.getPeriod());
                info.setImgInfoUrl(xmlInfo.getImgInfo());
                if(data.ManufactureID.equals(xmlInfo.getManufacture())){
                    if(xmlInfo.getModelID().equals(data.ModelID)) {
                        if(xmlInfo.getHwVersion().equals(data.HWVersion)) {
                            long stbVersion_xml = FormatUtils.fromString2Long(xmlInfo.getSTBVersion());
                            long stbVersion_system = FormatUtils.fromString2Long(data.STBVersion);
                            long serverVersion_xml = FormatUtils.fromString2Long(xmlInfo.getServerVersion());
                            Log.e(TAG,"serverVersion_xml "+serverVersion_xml);
                            Log.e(TAG,"stbVersion_system "+stbVersion_system);
                            Log.e(TAG,"stbVersion_xml "+stbVersion_xml);
                            info.setUpgradeVersion(serverVersion_xml);
                            info.setStbVersion(stbVersion_xml);
                            if( (serverVersion_xml > stbVersion_system) && 
                                    ( (stbVersion_xml == stbVersion_system) || (stbVersion_xml == 0))) {
                                for(Scope scope:xmlInfo.getScopeList()) {
                                    long startSN = FormatUtils.fromString2Long(scope.getStartSN());
                                    long endSN = FormatUtils.fromString2Long(scope.getEndSN());
                                    if(startSN >0xffffffffl || startSN <0) {
                                        startSN = 0xffffffffl;
                                    }
                                    if(endSN >0xffffffffl || endSN <0) {
                                        endSN = 0xffffffffl;
                                    }
                                    Log.e(TAG,"startSN "+startSN);
                                    Log.e(TAG,"endSN "+endSN);
                                    Log.e(TAG,"currentSN "+currentSN);
                                    if( (startSN <= currentSN) && (currentSN <= endSN) ) {
                                        List<String> urls = new ArrayList<String>();
                                        for(String index:scope.getIndex()) {
                                            int i = Integer.parseInt(index);
                                            if(i < xmlInfo.getUrlList().size()) {
                                                Log.i(TAG,"index is "+i);
                                                Log.i(TAG,"url is "+xmlInfo.getUrlList().get(i));
                                                urls.add(xmlInfo.getUrlList().get(i));
                                            }
                                        }
                                        info.setImgUrl(urls);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }

            } finally {
                manufactureNative.Destroy();
            }
            return false;
        }
    }
    
    private class SDCardReceiver extends BroadcastReceiver {
        
        private String mPath;

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action != null){
                Log.i(TAG,"\n\n###USB Broadcast Receive action="+action+"###\n\n");
            }
            if(action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                mPath = intent.getData().getPath();

                if (mPath != null){
                	Log.i(TAG,"\n\n###USB Broadcast Receive mPath="+mPath+"###\n\n");
                }
                File file = new File(mPath, ConfigUtils.getDefaultUsbUpgradeName(mContext.getApplicationContext()));
                if(file.exists() && file.isFile()) {
                    Log.i(TAG,"find the upgrade file");
                    Message msg = mHandler.obtainMessage();
                    msg.what = USB_UPGRADE;
                    msg.obj = file.getAbsolutePath();
                    msg.sendToTarget();
                }
            } else if(action.equals(Intent.ACTION_MEDIA_EJECT)) {
                if ((mPath != null) && (mPath.equals(intent.getData().getPath()))) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = USB_UPGRADE_EJECT;
                    msg.sendToTarget();
                }
            }
            
        }
    }
}
