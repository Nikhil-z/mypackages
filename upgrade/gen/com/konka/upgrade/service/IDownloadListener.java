/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: \\\\172.22.6.249\\zhaoyuxiang\\A20\\HAN350\\android4.2\\packages\\konka\\upgrade\\src\\com\\konka\\upgrade\\service\\IDownloadListener.aidl
 */
package com.konka.upgrade.service;
public interface IDownloadListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.konka.upgrade.service.IDownloadListener
{
private static final java.lang.String DESCRIPTOR = "com.konka.upgrade.service.IDownloadListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.konka.upgrade.service.IDownloadListener interface,
 * generating a proxy if needed.
 */
public static com.konka.upgrade.service.IDownloadListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.konka.upgrade.service.IDownloadListener))) {
return ((com.konka.upgrade.service.IDownloadListener)iin);
}
return new com.konka.upgrade.service.IDownloadListener.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onDownloadProgress:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
long _arg1;
_arg1 = data.readLong();
long _arg2;
_arg2 = data.readLong();
this.onDownloadProgress(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.konka.upgrade.service.IDownloadListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void onDownloadProgress(int type, long downloadSize, long totalSize) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
_data.writeLong(downloadSize);
_data.writeLong(totalSize);
mRemote.transact(Stub.TRANSACTION_onDownloadProgress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onDownloadProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void onDownloadProgress(int type, long downloadSize, long totalSize) throws android.os.RemoteException;
}
