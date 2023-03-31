#include "com_example_HelloWorldJNI.h"
#include <iostream>
#include <string>
#include <sys/types.h>
#include <sys/sysctl.h>
#include <unistd.h>
#include <mach/vm_statistics.h>
#include <mach/host_priv.h>
#include <mach/host_info.h>
#include <mach/mach_host.h>
#include <thread>

using namespace std;

/*
 * Class:     com_baeldung_jni_HelloWorldJNI
 * Method:    sayHello
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT void JNICALL Java_com_example_HelloWorldJNI_sayHello(JNIEnv *env, jobject thisObject)
{
    std::string hello = "Update new text";
    std::cout << hello << std::endl;
}

JNIEXPORT jint JNICALL Java_com_example_HelloWorldJNI_add(JNIEnv *env, jobject obj, jint a, jint b)
{
    return a + b;
}

JNIEXPORT jstring JNICALL Java_com_example_HelloWorldJNI_getUserInfo(JNIEnv *env, jobject obj, jint userId)
{
    // Create a JSON string with the userId
    std::string userInfo = "{ \"userId\": " + std::to_string(userId) + " }";

    // Convert the string to a jstring and return it
    return env->NewStringUTF(userInfo.c_str());
}

JNIEXPORT jstring JNICALL Java_com_example_HelloWorldJNI_sayHelloToMe(JNIEnv *env, jobject thisObject, jstring name, jboolean isFemale)
{
    const char *nameCharPointer = env->GetStringUTFChars(name, NULL);
    std::string title;
    if (isFemale)
    {
        title = "Ms. ";
    }
    else
    {
        title = "Mr. ";
    }

    std::string fullName = title + nameCharPointer;
    env->ReleaseStringUTFChars(name, nameCharPointer); // Release the string pointer
    return env->NewStringUTF(fullName.c_str());
}

void print_cpu_stats()
{
    host_cpu_load_info_data_t cpuLoad;
    mach_msg_type_number_t count = HOST_CPU_LOAD_INFO_COUNT;
    if (host_statistics(mach_host_self(), HOST_CPU_LOAD_INFO, (host_info_t)&cpuLoad, &count) != KERN_SUCCESS)
    {
        cout << "Failed to get CPU load" << endl;
        return;
    }

    unsigned int totalTicks = 0;
    for (int i = 0; i < CPU_STATE_MAX; i++)
    {
        totalTicks += cpuLoad.cpu_ticks[i];
    }

    cout << "Total CPU usage: " << (double)cpuLoad.cpu_ticks[CPU_STATE_USER] / totalTicks * 100 << "% user, "
         << (double)cpuLoad.cpu_ticks[CPU_STATE_SYSTEM] / totalTicks * 100 << "% system, "
         << (double)cpuLoad.cpu_ticks[CPU_STATE_IDLE] / totalTicks * 100 << "% idle" << endl;

    natural_t numCPUs;
    processor_cpu_load_info_t cpuLoadInfo;
    mach_msg_type_number_t numCPUInfo;
    kern_return_t err;

    err = host_processor_info(mach_host_self(), PROCESSOR_CPU_LOAD_INFO, &numCPUs, (processor_info_array_t *)&cpuLoadInfo, &numCPUInfo);
    if (err == KERN_SUCCESS)
    {
        for (int i = 0; i < numCPUs; i++)
        {
            totalTicks = 0;
            for (int j = 0; j < CPU_STATE_MAX; j++)
            {
                totalTicks += cpuLoadInfo[i].cpu_ticks[j];
            }

            cout << "Core " << i << ": " << (double)cpuLoadInfo[i].cpu_ticks[CPU_STATE_USER] / totalTicks * 100 << "% user, "
                 << (double)cpuLoadInfo[i].cpu_ticks[CPU_STATE_SYSTEM] / totalTicks * 100 << "% system, "
                 << (double)cpuLoadInfo[i].cpu_ticks[CPU_STATE_IDLE] / totalTicks * 100 << "% idle" << endl;
        }
    }

    // Thread and process information
    int numThreads = thread::hardware_concurrency();
    cout << "Number of threads: " << numThreads << endl;
}

void print_info()
{
    FILE *pipe = popen("ps -A", "r");
    if (!pipe)
    {
        perror("Failed to run command");
    }

    char buffer[128];
    while (fgets(buffer, sizeof(buffer), pipe) != nullptr)
    {
        printf("%s", buffer);
    }

    pclose(pipe);
}

JNIEXPORT void JNICALL Java_com_example_HelloWorldJNI_getCpuInfo(JNIEnv *env, jobject thisObject)
{
    while (true)
    {
        print_info();
        // Sleep 2 seconds
        sleep(2);

        // Clean the console
        system("clear");
    }
}
