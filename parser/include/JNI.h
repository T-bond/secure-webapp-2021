#include <jni.h>

#ifndef _Included_bme_schonbrunn_backend_CAFF
#define _Included_bme_schonbrunn_backend_CAFF

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jobject JNICALL Java_bme_schonbrunn_backend_parser_NativeParserDriver_parse(JNIEnv* env, jobject thisObject, jstring file);

JNIEXPORT bool JNICALL Java_bme_schonbrunn_backend_parser_NativeParserDriver_preview(JNIEnv* env, jobject thisObject, jstring input, jstring output);

#ifdef __cplusplus
}
#endif
#endif
