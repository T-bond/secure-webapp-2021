#include "JNI.h"

#include "parser/CAFF.hpp"

#include <fstream>
#include <iostream>
#include <memory>

JNIEXPORT jobject JNICALL
Java_bme_schonbrunn_backend_parser_NativeParserDriver_parse(JNIEnv* env, jobject thisObject, jstring file) {
	std::unique_ptr<CAFF> caff;
	std::ifstream caffFile(env->GetStringUTFChars(file, nullptr), std::ios::binary);
	try {
		caff = std::make_unique<CAFF>(caffFile);
	} catch(std::exception& e) {
		std::cerr << "An exception has occurred: " << e.what() << std::endl;
		caffFile.close();
		return nullptr;
	}
	caffFile.close();

	
	jclass jSetClass = env->FindClass("java/util/LinkedHashSet");
	jmethodID jSetClassConstructor = env->GetMethodID(jSetClass, "<init>", "(I)V");

	jclass jListClass = env->FindClass("java/util/ArrayList");
	jmethodID jListClassConstructor = env->GetMethodID(jListClass, "<init>", "(I)V");

	jmethodID addSetElement = env->GetMethodID(jSetClass, "add", "(Ljava/lang/Object;)Z");
	jmethodID addListElement = env->GetMethodID(jListClass, "add", "(Ljava/lang/Object;)Z");

	jclass jCaffClass = env->FindClass("bme/schonbrunn/backend/parser/CAFF");
	jmethodID jCaffClassConstructor = env->GetMethodID(jCaffClass, "<init>", "(ZLjava/lang/String;JJJLjava/util/Set;Ljava/util/List;)V");

	bool isValid = caff->isValid();
	const char* creator = caff->getCreator().c_str();
	intmax_t createdAt = caff->getCreatedAt();
	int64_t width = caff->getWidth();
	int64_t height = caff->getHeight();
	const std::vector<std::string_view>& captions = caff->getCaptions();
	const std::vector<std::vector<std::string>>& tags = caff->getTags();
	jobject jCaptionsSet = env->NewObject(jSetClass, jSetClassConstructor, captions.size());
	jobject jTagsList = env->NewObject(jListClass, jListClassConstructor, tags.size());
	
	for (const auto &caption : captions) {
		env->CallVoidMethod(jCaptionsSet, addSetElement, env->NewStringUTF(std::string(caption).data()));
	}

	for (const auto &tagsForFrame : tags) {
		jobject jTagSet = env->NewObject(jSetClass, jSetClassConstructor, tagsForFrame.size());
		for (const auto &tag : tags) {
			env->CallVoidMethod(jTagSet, addSetElement, env->NewStringUTF(tag.data()->c_str()));
		}

		env->CallVoidMethod(jTagsList, addListElement, jTagSet);
	}

	jobject jCaff = env->NewObject(jCaffClass,
	                               jCaffClassConstructor,
	                               isValid,
	                               env->NewStringUTF(creator),
	                               createdAt,
	                               width,
	                               height,
	                               jCaptionsSet,
	                               jTagsList);

	return jCaff;
}

JNIEXPORT bool JNICALL
Java_bme_schonbrunn_backend_parser_NativeParserDriver_preview(JNIEnv* env, jobject thisObject, jstring input, jstring output) {
	std::unique_ptr<CAFF> caff;
	std::ifstream caffFile(env->GetStringUTFChars(input, nullptr), std::ios::binary);
	try {
		CAFF caff(caffFile);
		
		if(caff.isValid()) {
			caff.writePreview(env->GetStringUTFChars(output, nullptr));
		}
	} catch(std::exception& e) {
		std::cerr << "An exception has occurred: " << e.what() << std::endl;
		caffFile.close();
		return false;
	}
	caffFile.close();

	return true;
}
