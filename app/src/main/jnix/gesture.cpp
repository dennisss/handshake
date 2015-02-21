#include "gesture.h"
#include "sensor.h"
#include <android/log.h>
#include <jni.h>

#include <stdio.h>

#define LOG_TAG "libgesture"
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)

#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT void JNICALL Java_me_denniss_handshake_Gesture_start(JNIEnv *env, jobject obj)
{
	LOGI("HHHHHHHHHH");

    record_setup();
    init_sensors(record_samples);
}

JNIEXPORT void JNICALL Java_me_denniss_handshake_Gesture_stop(JNIEnv *env, jobject obj)
{
    stop_sensors();

    LOGI("Saving data dfdfdfdf...");

    save_recorded_samples();

    fopen("/sdcard/hello.txt", "w");
}




#ifdef __cplusplus
}
#endif