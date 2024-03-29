#include <FaceDetector_jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/core/mat.hpp>
#include <opencv2/objdetect/objdetect.hpp>

#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/imgproc/types_c.h>

#include <string>
#include <vector>

#include <android/log.h>

#define LOG_TAG "REST-O-GRAM/FACE-DETECTION"

#ifdef NDEBUG

#define LOGD(...)
#define LOGV(...)

#else

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)

#endif

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

using namespace std;
using namespace cv;

CascadeClassifier face_cascade;

inline void vector_Rect_to_Mat(vector<Rect>& v_rect, Mat& mat)
{
    mat = Mat(v_rect, true);
}

JNIEXPORT void JNICALL Java_rest_o_gram_openCV_OpenCVFaceDetector_nativeLoadClassifier
(JNIEnv * jenv, jclass, jstring jFileName, jint faceSize)
{
    LOGD("nativeLoadClassifier enter");
    const char* jnamestr = jenv->GetStringUTFChars(jFileName, NULL);
    string stdFileName(jnamestr);
    jlong result = 0;

    try
    {
        if (!face_cascade.load(jnamestr))
            LOGE("nativeLoadClassifier Error while loading classifier");
    }
    catch(cv::Exception& e)
    {
        LOGE("nativeLoadClassifier caught cv::Exception: %s", e.what());
        jclass je = jenv->FindClass("org/opencv/core/CvException");
        if(!je)
            je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGE("nativeLoadClassifier caught unknown exception");
        jclass je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, "Unknown exception in JNI code");
    }

    LOGD("nativeLoadClassifier exit");
}

JNIEXPORT void JNICALL Java_rest_o_gram_openCV_OpenCVFaceDetector_nativeDetectFaces
(JNIEnv * jenv, jclass, jlong imageGray, jlong faces, jlong minSize, jlong maxSize)
{
    LOGV("nativeDetect enter");
    try
    {
         std::vector<Rect> facesVec;
         Mat frame_gray;

         cvtColor( *((Mat*)imageGray), frame_gray, CV_BGR2GRAY );
         //-- equalizeHist( frame_gray, frame_gray );

         //-- Detect faces
         face_cascade.detectMultiScale( frame_gray, facesVec, 1.1, 2, 0, Size(minSize, minSize) , Size(maxSize, maxSize));
         vector_Rect_to_Mat(facesVec, *((Mat*)faces));
    }
    catch(cv::Exception& e)
    {
        LOGE("nativeDetectFaces caught cv::Exception: %s", e.what());
        jclass je = jenv->FindClass("org/opencv/core/CvException");
        if(!je)
            je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGE("nativeDetectFaces caught unknown exception");
        jclass je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, "Unknown exception in JNI code");
    }
    LOGV("nativeDetectFaces exit");
}