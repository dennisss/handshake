#include <jni.h>

#ifndef GESTURE_H_
#define GESTURE_H_

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_me_denniss_handshake_Gesture_start(JNIEnv *env, jobject obj);
JNIEXPORT void JNICALL Java_me_denniss_handshake_Gesture_stop(JNIEnv *env, jobject obj);



#ifdef __cplusplus
}
#endif

#endif