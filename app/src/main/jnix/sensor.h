#include <jni.h>

#include <vector>

using namespace std;

#ifndef SENSORS_H_
#define SENSORS_H_

#ifdef __cplusplus
extern "C" {
#endif


typedef void (*data_callback)(vector<double> sample);


void init_sensors(data_callback f);
void stop_sensors();



#ifdef __cplusplus
}
#endif

#endif