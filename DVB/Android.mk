LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

#LOCAL_STATIC_JAVA_LIBRARIES  := DVBLIB

LOCAL_PACKAGE_NAME := KonkaDVB

#LOCAL_CERTIFICATE := platform
LOCAL_PROGUARD_ENABLED:= disabled

include $(BUILD_PACKAGE)
#include $(CLEAR_VARS)

#LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := DVBLIB:libs/DVB.jar
#include $(BUILD_MULTI_PREBUILT)
include $(call all-makefiles-under,$(LOCAL_PATH))