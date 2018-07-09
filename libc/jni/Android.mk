LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := tc
LOCAL_SRC_FILES := test.cpp \
    __set_errno.cpp

ifeq ($(TARGET_ARCH),arm)
    LOCAL_SRC_FILES += arm/syscall.S
endif
ifeq ($(TARGET_ARCH),x86)
    LOCAL_SRC_FILES += x86/syscall.S
endif

ifeq ($(TARGET_ARCH),arm64)
    LOCAL_SRC_FILES += arm64/syscall.S
endif
LOCAL_C_INCLUDES := $(LOCAL_PATH)/private
include $(BUILD_SHARED_LIBRARY)
