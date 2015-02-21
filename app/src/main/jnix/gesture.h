#include <jni.h>

#include <vector>

using namespace std;


#ifndef GESTURE_H_
#define GESTURE_H_

#ifdef __cplusplus
extern "C" {
#endif

void record_samples(vector<double> sample);
void save_recorded_samples();
void record_setup();

JNIEXPORT void JNICALL Java_me_denniss_handshake_Gesture_start(JNIEnv *env, jobject obj);
JNIEXPORT void JNICALL Java_me_denniss_handshake_Gesture_stop(JNIEnv *env, jobject obj);



#ifdef __cplusplus
}
#endif

#endif