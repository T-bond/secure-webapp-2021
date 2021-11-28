#include <jni.h>

#ifndef BACKEND_JAVABINDINGS_H
#define BACKEND_JAVABINDINGS_H

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jobject JNICALL Java_bme_schonbrunn_backend_parser_NativeParserDriver_parse(JNIEnv* env, jobject thisObject, jstring file);

JNIEXPORT bool JNICALL Java_bme_schonbrunn_backend_parser_NativeParserDriver_preview(JNIEnv* env, jobject thisObject, jstring input, jstring output);

#ifdef __cplusplus
}
#endif
#endif // BACKEND_JAVABINDINGS_H
