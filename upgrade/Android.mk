LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src) \
	src/com/konka/upgrade/service/IDownloadListener.aidl \
	src/com/konka/upgrade/service/IProcedureListener.aidl \
	src/com/konka/upgrade/service/IUpgradeService.aidl

LOCAL_PACKAGE_NAME := Upgrade

LOCAL_CERTIFICATE := platform

LOCAL_JAVA_LIBRARIES := com.konka.system

include $(BUILD_PACKAGE)