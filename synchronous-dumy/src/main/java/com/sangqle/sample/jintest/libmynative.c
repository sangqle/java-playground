#include <jni.h>

JNIEXPORT jint JNICALL Java_com_example_MainActivity_add(JNIEnv *env, jobject obj, jint a, jint b) {
    return a + b;
}
