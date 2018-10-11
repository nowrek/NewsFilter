#include <jni.h>
#include <cstring>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_nowrek_newsfilter_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

std::string convertJString(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_nowrek_newsfilter_SlidingActivity_contains(JNIEnv *env, jobject instance, jstring article,
                                                jstring keyword) {
        std::string articleString = convertJString(env, article);
    std::string keywordString = convertJString(env, keyword);
    unsigned long kPos = articleString.find(keywordString);

    return static_cast<jboolean>(kPos != std::string::npos);
}