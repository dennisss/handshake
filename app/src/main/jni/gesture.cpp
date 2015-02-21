#include "gesture.h"
#include "sensor.h"
#include <android/log.h>
#include <jni.h>

#include "GRT.h"
using namespace GRT;


#define LOG_TAG "libgesture"
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)

#ifdef __cplusplus
extern "C" {
#endif



int test(){
        //Create some input data for the PCA algorithm - this data comes from the Matlab PCA example
    	MatrixDouble data(13,4);

    	data[0][0] = 7; data[0][1] = 26; data[0][2] = 6; data[0][3] = 60;
    	data[1][0] = 1; data[1][1] = 29; data[1][2] = 15; data[1][3] = 52;
    	data[2][0] = 11; data[2][1] = 56; data[2][2] = 8; data[2][3] = 20;
    	data[3][0] = 11; data[3][1] = 31; data[3][2] = 8; data[3][3] = 47;
    	data[4][0] = 7; data[4][1] = 52; data[4][2] = 6; data[4][3] = 33;
    	data[5][0] = 11; data[5][1] = 55; data[5][2] = 9; data[5][3] = 22;
    	data[6][0] = 3; data[6][1] = 71; data[6][2] = 17; data[6][3] = 6;
    	data[7][0] = 1; data[7][1] = 31; data[7][2] = 22; data[7][3] = 44;
    	data[8][0] = 2; data[8][1] = 54; data[8][2] = 18; data[8][3] = 22;
    	data[9][0] = 21; data[9][1] = 47; data[9][2] = 4; data[9][3] = 26;
    	data[10][0] = 1; data[10][1] = 40; data[10][2] = 23; data[10][3] = 34;
    	data[11][0] = 11; data[11][1] = 66; data[11][2] = 9; data[11][3] = 12;
    	data[12][0] = 10; data[12][1] = 68; data[12][2] = 8; data[12][3] = 12;

        //Print the input data
       // data.print("Input Data:");

        //Create a new principal component analysis instance
    	PrincipalComponentAnalysis pca;

        //Run pca on the input data, setting the maximum variance value to 95% of the variance
    	if( !pca.computeFeatureVector( data, 0.95 ) ){
    	//	cout << "ERROR: Failed to compute feature vector!\n";
    		return EXIT_FAILURE;
    	}

        //Get the number of principal components
        UINT numPrincipalComponents = pca.getNumPrincipalComponents();
       // cout << "Number of Principal Components: " << numPrincipalComponents << endl;

        //Project the original data onto the principal subspace
    	MatrixDouble prjData;
    	if( !pca.project( data, prjData ) ){
    //		cout << "ERROR: Failed to project data!\n";
    		return EXIT_FAILURE;
    	}

        //Print out the pca info
        //Print our
  //      pca.print("PCA Info:");

        //Print the projected data
      /*  cout << "ProjectedData:\n";
    	for(UINT i=0; i<prjData.getNumRows(); i++){
    		for(UINT j=0; j<prjData.getNumCols(); j++){
    			cout << prjData[i][j] << "\t";
    		}cout << endl;
    	}
*/
        LOGI("SUCCESS");

        return EXIT_SUCCESS;


}



JNIEXPORT void JNICALL Java_me_denniss_handshake_Gesture_start(JNIEnv *env, jobject obj)
{
	LOGI("Hello World!");
	test();

    init_sensors();
}

JNIEXPORT void JNICALL Java_me_denniss_handshake_Gesture_stop(JNIEnv *env, jobject obj)
{
    stop_sensors();
}


#ifdef __cplusplus
}
#endif