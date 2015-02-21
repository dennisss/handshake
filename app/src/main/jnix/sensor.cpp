#include "sensor.h"
#include <android/log.h>
#include <jni.h>
#include <iostream>
#include <cctype>


#include <android/looper.h>
#include <android/sensor.h>

using namespace std;

#define LOG_TAG "libsensors"
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)

#ifdef __cplusplus
extern "C" {
#endif


ASensorManager* sensorManager;
ASensorEventQueue* eventQueue;

const ASensor* gyroSensor;
const ASensor* magnSensor;
const ASensor* accelSensor;

int accCounter = 0;
int64_t lastAccTime = 0;

int gyroCounter = 0;
int64_t lastGyroTime = 0;

int magCounter = 0;
int64_t lastMagTime = 0;


// Function to call when we get a sample
data_callback func;

bool gotGyro = false;
bool gotAccel = false;

vector<double> sample(6);

static int get_sensor_events(int fd, int events, void* data) {
	ASensorEvent event;


	while(ASensorEventQueue_getEvents(eventQueue, &event, 1) > 0) {

		if(event.type == ASENSOR_TYPE_ACCELEROMETER) {
            gotAccel = true;

		    sample[0] = event.acceleration.x;
		    sample[1] = event.acceleration.y;
		    sample[2] = event.acceleration.z;


			//LOGI("accl(x,y,z,t): %f %f %f %lld", event.acceleration.x, event.acceleration.y, event.acceleration.z, event.timestamp);
			if(accCounter == 0 || accCounter == 1000)
			{
				//LOGI("Acc-Time: %lld (%f)", event.timestamp,((double)(event.timestamp-lastAccTime))/1000000000.0);
				lastAccTime = event.timestamp;
				accCounter = 0;
			}

			accCounter++;
		}
		else if(event.type == ASENSOR_TYPE_GYROSCOPE) {
		    gotGyro = true;

            sample[3] = event.acceleration.x;
            sample[4] = event.acceleration.y;
            sample[5] = event.acceleration.z;

			//LOGI("gyro(x,y,z,t): %f %f %f %lld", event.acceleration.x, event.acceleration.y, event.acceleration.z, event.timestamp);
			if(gyroCounter == 0 || gyroCounter == 1000)
			{

				//LOGI("Gyro-Time: %lld (%f)", event.timestamp,((double)(event.timestamp-lastGyroTime))/1000000000.0);
				lastGyroTime = event.timestamp;
				gyroCounter = 0;
			}

			gyroCounter++;
		}
		else if(event.type == ASENSOR_TYPE_MAGNETIC_FIELD) {
		   // sample[6] = event.magnetic.x;
           // sample[7] = event.magnetic.y;
           // sample[8] = event.magnetic.z;

			//LOGI("magn(x,y,z,t): %f %f %f %lld", event.magnetic.x, event.magnetic.y, event.magnetic.z, event.timestamp);
			if(magCounter == 0 || magCounter == 1000)
			{
				LOGI("Mag-Time: %lld (%f)", event.timestamp,((double)(event.timestamp-lastMagTime))/1000000000.0);
				lastMagTime = event.timestamp;
				magCounter = 0;
			}

			magCounter++;
		}

	}

   // if(gotGyro && gotAccel){
	    func(sample);
	    gotGyro = false;
	    gotAccel = false;
//	}


	//should return 1 to continue receiving callbacks, or 0 to unregister
	return 1;
}



void init_sensors(data_callback f){

    func = f;

	ALooper* looper = ALooper_forThread();

	if(looper == NULL)
		looper = ALooper_prepare(ALOOPER_PREPARE_ALLOW_NON_CALLBACKS);


	sensorManager = ASensorManager_getInstance();

	gyroSensor = ASensorManager_getDefaultSensor(sensorManager, ASENSOR_TYPE_GYROSCOPE);
	accelSensor = ASensorManager_getDefaultSensor(sensorManager, ASENSOR_TYPE_ACCELEROMETER);
	magnSensor = ASensorManager_getDefaultSensor(sensorManager, ASENSOR_TYPE_MAGNETIC_FIELD);


	void* sensor_data = malloc(1024);

	eventQueue = ASensorManager_createEventQueue(sensorManager, looper, 1, get_sensor_events, sensor_data);

	ASensorEventQueue_enableSensor(eventQueue, accelSensor);
	ASensorEventQueue_enableSensor(eventQueue, gyroSensor);
	//ASensorEventQueue_enableSensor(eventQueue, magnSensor);

	//Sampling rate: 100Hz
	int a = ASensor_getMinDelay(accelSensor);
	int b = ASensor_getMinDelay(gyroSensor);
	int c = ASensor_getMinDelay(magnSensor);

	LOGI("min-delay: %d, %d, %d",a,b,c);
	ASensorEventQueue_setEventRate(eventQueue, accelSensor, 40000); // (1000L/SAMP_PER_SEC)*1000
	ASensorEventQueue_setEventRate(eventQueue, gyroSensor, 40000);
	//ASensorEventQueue_setEventRate(eventQueue, magnSensor, 40000);

	LOGI("sensorValue() - START");

}


void stop_sensors(){
    ASensorManager_destroyEventQueue(sensorManager, eventQueue);
    LOGI("Sensor queue stopped");
}


#ifdef __cplusplus
}
#endif