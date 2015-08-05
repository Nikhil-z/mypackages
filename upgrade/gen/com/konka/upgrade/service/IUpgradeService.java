/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: \\\\172.22.6.249\\zhaoyuxiang\\A20\\HAN350\\android4.2\\packages\\konka\\upgrade\\src\\com\\konka\\upgrade\\service\\IUpgradeService.aidl
 */
package com.konka.upgrade.service;
public interface IUpgradeService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.konka.upgrade.service.IUpgradeService
{
private static final java.lang.String DESCRIPTOR = "com.konka.upgrade.service.IUpgradeService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.konka.upgrade.service.IUpgradeService interface,
 * generating a proxy if needed.
 */
public static com.konka.upgrade.service.IUpgradeService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.konka.upgrade.service.IUpgradeService))) {
return ((com.konka.upgrade.service.IUpgradeService)iin);
}
return new com.konka.upgrade.service.IUpgradeService.Stub.Proxy(obj);
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
case TRANSACTION_checkUpgrade:
{
data.enforceInterface(DESCRIPTOR);
this.checkUpgrade();
reply.writeNoException();
return true;
}
case TRANSACTION_getCurrentState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCurrentState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getUpgradeDesc:
{
data.enforceInterface(DESCRIPTOR);
com.konka.upgrade.service.UpgradeDesc _result = this.getUpgradeDesc();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_startDownload:
{
data.enforceInterface(DESCRIPTOR);
this.startDownload();
reply.writeNoException();
return true;
}
case TRANSACTION_installUpgrade:
{
data.enforceInterface(DESCRIPTOR);
this.installUpgrade();
reply.writeNoException();
return true;
}
case TRANSACTION_cancel:
{
data.enforceInterface(DESCRIPTOR);
this.cancel();
reply.writeNoException();
return true;
}
case TRANSACTION_neverRemind:
{
data.enforceInterface(DESCRIPTOR);
this.neverRemind();
reply.writeNoException();
return true;
}
case TRANSACTION_registProcedureListener:
{
data.enforceInterface(DESCRIPTOR);
com.konka.upgrade.service.IProcedureListener _arg0;
_arg0 = com.konka.upgrade.service.IProcedureListener.Stub.asInterface(data.readStrongBinder());
boolean _result = this.registProcedureListener(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_unregistProcedureListener:
{
data.enforceInterface(DESCRIPTOR);
com.konka.upgrade.service.IProcedureListener _arg0;
_arg0 = com.konka.upgrade.service.IProcedureListener.Stub.asInterface(data.readStrongBinder());
boolean _result = this.unregistProcedureListener(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_registDownloadListener:
{
data.enforceInterface(DESCRIPTOR);
com.konka.upgrade.service.IDownloadListener _arg0;
_arg0 = com.konka.upgrade.service.IDownloadListener.Stub.asInterface(data.readStrongBinder());
boolean _result = this.registDownloadListener(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_unregistDownloadListener:
{
data.enforceInterface(DESCRIPTOR);
com.konka.upgrade.service.IDownloadListener _arg0;
_arg0 = com.konka.upgrade.service.IDownloadListener.Stub.asInterface(data.readStrongBinder());
boolean _result = this.unregistDownloadListener(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.konka.upgrade.service.IUpgradeService
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
@Override public void checkUpgrade() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_checkUpgrade, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int getCurrentState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public com.konka.upgrade.service.UpgradeDesc getUpgradeDesc() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.konka.upgrade.service.UpgradeDesc _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUpgradeDesc, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.konka.upgrade.service.UpgradeDesc.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void startDownload() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startDownload, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void installUpgrade() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_installUpgrade, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void cancel() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_cancel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void neverRemind() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_neverRemind, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean registProcedureListener(com.konka.upgrade.service.IProcedureListener cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registProcedureListener, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean unregistProcedureListener(com.konka.upgrade.service.IProcedureListener cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregistProcedureListener, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean registDownloadListener(com.konka.upgrade.service.IDownloadListener cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registDownloadListener, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean unregistDownloadListener(com.konka.upgrade.service.IDownloadListener cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregistDownloadListener, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_checkUpgrade = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getCurrentState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getUpgradeDesc = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_startDownload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_installUpgrade = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_cancel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_neverRemind = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_registProcedureListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_unregistProcedureListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_registDownloadListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_unregistDownloadListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
}
public void checkUpgrade() throws android.os.RemoteException;
public int getCurrentState() throws android.os.RemoteException;
public com.konka.upgrade.service.UpgradeDesc getUpgradeDesc() throws android.os.RemoteException;
public void startDownload() throws android.os.RemoteException;
public void installUpgrade() throws android.os.RemoteException;
public void cancel() throws android.os.RemoteException;
public void neverRemind() throws android.os.RemoteException;
public boolean registProcedureListener(com.konka.upgrade.service.IProcedureListener cb) throws android.os.RemoteException;
public boolean unregistProcedureListener(com.konka.upgrade.service.IProcedureListener cb) throws android.os.RemoteException;
public boolean registDownloadListener(com.konka.upgrade.service.IDownloadListener cb) throws android.os.RemoteException;
public boolean unregistDownloadListener(com.konka.upgrade.service.IDownloadListener cb) throws android.os.RemoteException;
}
