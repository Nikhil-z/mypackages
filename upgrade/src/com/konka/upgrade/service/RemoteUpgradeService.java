package com.konka.upgrade.service;

import java.io.File;

import com.konka.upgrade.OneShotSignal;
import com.konka.upgrade.download.Downloads;
import com.konka.upgrade.utils.SystemUtils;
import com.konka.system.SystemSignatureNative;
import com.konka.system.SystemManufactureNative;
import com.konka.system.ManufactureData;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

public class RemoteUpgradeService extends IUpgradeService.Stub {
    
    private final String TAG = RemoteUpgradeService.class.getSimpleName();
    
    private RemoteCallbackList<IDownloadListener> downloadListeners;
    private RemoteCallbackList<IProcedureListener> procedureListeners;
    private Object callbacksLock;
    private UpgradeManager manager;
    
    private SystemSignatureNative mSignature;
    private SystemManufactureNative manufactureNative;
    
    private boolean isFinish;
    private boolean isUpgradeFound;
    private int currentState;
    
    public RemoteUpgradeService(UpgradeManager manager) {
    	downloadListeners = new RemoteCallbackList<IDownloadListener>();
    	procedureListeners = new RemoteCallbackList<IProcedureListener>();
        callbacksLock = new Object();
        this.manager = manager;
    }

    @Override
    public void checkUpgrade() throws RemoteException {
        // TODO Auto-generated method stub
        if(OneShotSignal.isUpgradeReady()) {
            if( !manager.checkUpgrade() ) {
                notifyClientStart();
            }
        }
    }
    
    public void notifyClientStart() {
        synchronized (callbacksLock) {
            int i = procedureListeners.beginBroadcast();
            while (i > 0) {
                i--;
                try {
                	procedureListeners.getBroadcastItem(i).onStart();
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                    e.printStackTrace();
                }
            }
            procedureListeners.finishBroadcast();
        }
    }
    
    private ContentValues toContentValues(UpgradeManager manager) {
        ContentValues values = new ContentValues();
        values.put(Downloads.Impl.COLUMN_MANUFACTURE, manager.getUpgradeDesc().getManufacture());
        values.put(Downloads.Impl.COLUMN_MODEL, manager.getUpgradeDesc().getModel());
        values.put(Downloads.Impl.COLUMN_HW_VERSION, manager.getUpgradeDesc().getHWVersion());
        values.put(Downloads.Impl.COLUMN_SW_VERSION, manager.getUpgradeDesc().getUpgradeVersion());
        values.put(Downloads.Impl.COLUMN_UPGRADE_URI, manager.mPackageUrl);
        return values;
    }
    
    public void notifyClientProgress(int type) {
        currentState = type;
        mSignature = new SystemSignatureNative();
        manufactureNative = new SystemManufactureNative();
        String mPackageEncryptionPath = "/cache/update/KONKA_USB_UPDATE_FORCE.zip.sn"; 
        if(type == UpgradeType.UPGRADE_FOUND) {
            Cursor cursor = manager.mContext.getContentResolver().query(
                    Downloads.Impl.UPGRADE_CONTENT_URI, 
                    new String[] {Downloads.Impl.COLUMN_UPGRADE_STATUS,Downloads.Impl.COLUMN_UPGRADE_FILE}, 
                    Downloads.Impl.COLUMN_SW_VERSION+"='"+manager.getUpgradeDesc().getUpgradeVersion()+"'", 
                    null, null);
            try {
                if(cursor.moveToFirst()) {
                    int status = cursor.getInt(0);
                    Log.e(TAG, "status = " + status + "....................");
                    
                    String path = cursor.getString(1);
                    Log.e(TAG, "path = " + path );
                    if(status == 1) {
                        if(!TextUtils.isEmpty(path) && (new File(path).exists()) && 
                           !TextUtils.isEmpty(mPackageEncryptionPath) && (new File(mPackageEncryptionPath).exists()))
                        {
                        	if (mSignature.FileSignatureVerify(path,mPackageEncryptionPath) == 0) {
                                manager.getUpgradeDesc().setUpgradeFilePath(path);
                                try {
                                    if (manager.getUpgradeDesc().getControl().equals("3")){
                                        manager.procedure(UpgradeType.DOWNLOAD_COMPLETE);
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else {
                                manager.deleteAll();                   
                               
                            }
                            
                        }
                            else 
                        {
                        	Log.e(TAG, "start inside");
                            manager.startDownload();
                        }	
                    } else {
                    	Log.e(TAG, "start outside");
                        manager.startDownload();
                        
                    }
                } else {
                    manager.mContext.getContentResolver().insert(
                            Downloads.Impl.UPGRADE_CONTENT_URI, 
                            toContentValues(manager));
                    isUpgradeFound = true;
                }
            } finally {
                cursor.close();
            }
        } else if(type == UpgradeType.DOWNLOAD_COMPLETE) {
            ContentValues values = toContentValues(manager);
            values.put(Downloads.Impl.COLUMN_UPGRADE_FILE, manager.getUpgradeDesc().getUpgradeFilePath());
            Log.e(TAG, "complete file path = "+manager.getUpgradeDesc().getUpgradeFilePath());
            values.put(Downloads.Impl.COLUMN_UPGRADE_STATUS, true);
            manager.mContext.getContentResolver().update(
                    Downloads.Impl.UPGRADE_CONTENT_URI, 
                    values, 
                    Downloads.Impl.COLUMN_UPGRADE_URI + "='"+manager.mPackageUrl + "'", 
                    null);
            Log.e(TAG, "after update complete file path = " + manager.mPackageUrl);
            isFinish = true;
        }
        else if(type == UpgradeType.USER_CANCEL){
        	
        	isFinish = true;
        	
        }
        Log.i(TAG,"notifyClientProgress "+type);
        synchronized (callbacksLock) {
            int i = procedureListeners.beginBroadcast();
            while (i > 0) {
                i--;
                try {
                	procedureListeners.getBroadcastItem(i).onProcess(type);;
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                    e.printStackTrace();
                }
            }
            procedureListeners.finishBroadcast();
        }
    }
    
    public void notifyClientDownloadProgress(int type,long downloadSize,long totalSize) {
        synchronized (callbacksLock) {
            int i = downloadListeners.beginBroadcast();
            while (i > 0) {
                i--;
                try {
                	downloadListeners.getBroadcastItem(i).onDownloadProgress(type,downloadSize, totalSize);
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                    e.printStackTrace();
                }
            }
            downloadListeners.finishBroadcast();
        }
    }
    
    public void notifyClientError(int type,int errMsg) {
        currentState = errMsg;
        synchronized (callbacksLock) {
            int i = procedureListeners.beginBroadcast();
            while (i > 0) {
                i--;
                try {
                	procedureListeners.getBroadcastItem(i).onError(type, errMsg);
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                    e.printStackTrace();
                }
            }
            procedureListeners.finishBroadcast();
        }
    }
    
    private void deleteUpgradeDb() {
        manager.mContext.getContentResolver().delete(Downloads.Impl.UPGRADE_CONTENT_URI, null, null);
    }

	@Override
    public void startDownload() throws RemoteException {
        // TODO Auto-generated method stub
        if (isUpgradeFound) {
            manager.startDownload();
            isUpgradeFound = false;
        }
    }

	@Override
	public void installUpgrade() throws RemoteException {
		// TODO Auto-generated method stub
	    if(isFinish) {
	        deleteUpgradeDb();
	        manager.installUpgrade();
	        isFinish = false;
	    }
	}

	@Override
	public void cancel() throws RemoteException {
		// TODO Auto-generated method stub
	    if(isFinish) {
//	        deleteUpgradeDb();
	        manager.cancel();
	        notifyClientProgress(UpgradeType.USER_CANCEL);
//	        isUpgradeFound = false;
	    }
	}

	@Override
	public void neverRemind() throws RemoteException {
		// TODO Auto-generated method stub
	    if(isFinish) {
//	        deleteUpgradeDb();
	        manager.neverRemind();
	        notifyClientProgress(UpgradeType.USER_CANCEL);
//	        isUpgradeFound = false;
	    }
	}

	@Override
	public boolean registProcedureListener(IProcedureListener cb)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(cb != null) {
			return procedureListeners.register(cb);
		} else {
			return false;
		}
	}

	@Override
	public boolean unregistProcedureListener(IProcedureListener cb)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(cb != null) {
			return procedureListeners.unregister(cb);
		} else {
			return false;
		}
	}

	@Override
	public boolean registDownloadListener(IDownloadListener cb)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(cb != null) {
			return downloadListeners.register(cb);
		} else {
			return false;
		}
	}

	@Override
	public boolean unregistDownloadListener(IDownloadListener cb)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(cb != null) {
			return downloadListeners.unregister(cb);
		} else {
			return false;
		}
	}

    @Override
    public int getCurrentState() throws RemoteException {
        // TODO Auto-generated method stub
        return currentState;
    }

    @Override
    public UpgradeDesc getUpgradeDesc() throws RemoteException {
        // TODO Auto-generated method stub
        switch (currentState) {
        case UpgradeType.UPGRADE_FOUND:
        case UpgradeType.DOWNLOAD_COMPLETE:
        case UpgradeType.DOWNLOAD_UPGRADE_FILE:
        case UpgradeType.DOWNLOAD_UPGRADE_VERIFY_FILE:
        case UpgradeType.INSTALL_UPGRADE_FILE:
            return manager.getUpgradeDesc();
        default:
            return null;
        }
    }
}
