
###########################################
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := serial-data-transmission
LOCAL_SRC_FILES := serial-data-transmission.c
LOCAL_LDLIBS :=  -L$(SYSROOT)/usr/lib -llog 
include $(BUILD_SHARED_LIBRARY)
