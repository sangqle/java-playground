#include "com_example_HelloWorldJNI.h"
#include <iostream>
/*
 * Class:     com_baeldung_jni_HelloWorldJNI
 * Method:    sayHello
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT void JNICALL Java_com_example_HelloWorldJNI_sayHello (JNIEnv* env, jobject thisObject) {
	std::string hello = "Hello from C++ !!";
    std::cout << hello << std::endl;
}

JNIEXPORT jint JNICALL Java_com_example_HelloWorldJNI_add(JNIEnv *env, jobject obj, jint a, jint b) {
  return a + b;
}
