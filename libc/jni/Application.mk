APP_CPPFLAGS += -fexceptions

APP_STL := gnustl_static
APP_CPPFLAGS += -frtti

APP_OPTIM:=release
APP_PLATFORM:=android-9
APP_ABI:=armeabi x86 arm64-v8a #x86_64
APP_PIE := true