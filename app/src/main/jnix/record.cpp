#include "gesture.h"

#include "GRT.h"
using namespace GRT;


//Create a new instance of the ClassificationData
ClassificationData trainingData;

void record_setup(){
    trainingData.setNumDimensions(6);
}

void record_samples(vector<double> sample){
    UINT gestureLabel = 1;

    trainingData.addSample(gestureLabel, sample);
}


void save_recorded_samples(){



    trainingData.saveDatasetToFile( "/sdcard/data.txt" );
//    trainingData.clear();
}



/*


//Create a new GestureRecognitionPipeline
GestureRecognitionPipeline pipeline;

//Add a moving average filter (with a buffer size of 5 and for a 1 dimensional signal) as a pre-processing module to the start of the pipeline
pipeline.addPreProcessingModule( MovingAverageFilter(5,1) );

//Add an FFT (with a window size of 512) as a feature-extraction module to the pipeline, the input to this module will consist of the output of the moving average filter.
pipeline.addFeatureExtractionModule( FFT(512) );

//Add a custom feature module to the pipeline, its easy to integrate your own custom feature-extraction (and preprocessing or postprocessing) modules into any pipeline
//This custom feature-extraction module might, for example, take the output of the FFT module as its input and compute some features from the FFT signal, such as the top N frequency values
pipeline.addFeatureExtractionModule( MyOwnFeatureMethod() );

//Set the classifier at the core of the pipeline, in this case we are using an Adaptive Naive Bayes Classifier
pipeline.setClassifier( ANBC() );

//Add a class label timeout filter to the end of the pipeline (with a timeout value of 1 second), this will filter the predicted class output from the ANBC algorithm
pipeline.addPostProcessingModule( ClassLabelTimeoutFilter(1000) );

void test(){


    //Set the dimensionality of the data
    trainingData.setNumDimensions( 3 );

    //Here you would grab some data from your sensor and label it with the corresponding gesture it belongs to
    UINT gestureLabel = 1;
    vector< double > sample(3);
    sample[0] = //....Data from sensor
    sample[1] = //....Data from sensor
    sample[2] = //....Data from sensor

    //Add the sample to the training data
    trainingData.addSample( gestureLabel, sample );

    //After recording your training data you can then save it to a file
    bool saveResult = trainingData.saveDatasetToFile( "TrainingData.txt" );

    //This can then be loaded later
    bool loadResult = trainingData.loadDatasetFromFile( "TrainingData.txt" );

}





//Train the pipeline
bool trainSuccess = pipeline.train( trainingData );







//Perform the prediction
bool predictionSuccess = pipeline.predict( inputVector );

//You can then get the predicted class label from the pipeline
UINT predictedClassLabel = pipeline.getPredictedClassLabel();

//Along with some other results such as the likelihood of the most likely class or the likelihood of all the classes in the model
double bestLoglikelihood = pipeline.getMaximumLikelihood();
vector<double> classLikelihoods = pipeline.getClassLikelihoods();

//You can then use the predicted class label to trigger the action associated with that gesture
if( predictedClassLabel == 1 ){
    //Trigger the action associated with gesture 1
}
if( predictedClassLabel == 2 ){
    //Trigger the action associated with gesture 2
}




*/