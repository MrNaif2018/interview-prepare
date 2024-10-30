#include <jni.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <android/log.h>

#define LOG_TAG "NativeLib"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

__attribute__((unused)) JNIEXPORT jboolean JNICALL
Java_com_mrnaif_interviewhelper_NativeLib_isUserValid(JNIEnv *env, jobject obj, jstring username, jstring password) {
    const char *username_c = (*env)->GetStringUTFChars(env, username, 0);
    const char *password_c = (*env)->GetStringUTFChars(env, password, 0);

    bool ok = true;
    if (strlen(username_c) == 0 || strlen(password_c) < 8 || !strstr(username_c, "@"))
        ok=false;
    if (ok)
        LOGI("Успешно проверен пользователь %s.",username_c);
    else
        LOGE("Не удалось проверить пользователя %s.", username_c);
    (*env)->ReleaseStringUTFChars(env, username, username_c);
    (*env)->ReleaseStringUTFChars(env, password, password_c);
    return ok ? JNI_TRUE: JNI_FALSE;
}

