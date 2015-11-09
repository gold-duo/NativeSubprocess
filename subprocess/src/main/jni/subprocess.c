/*
 * Copyright (c) 2014 droidwolf(droidwolf2006@gmail.com)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

#include <stdlib.h>
#include <unistd.h>
#include <android/log.h>
#include "subprocess.h"

#define LOGTAG "subprocess"
#define LOG_D(tag, msg) __android_log_write(ANDROID_LOG_DEBUG, tag, msg)

static void runOnSubprocess(JNIEnv *env, jclass clazz, pid_t ppid,jobject context) {
	if (clazz != NULL) {
		jobject subprocess = (*env)->AllocObject(env,clazz);
		if (subprocess != NULL) {
			(*env)->SetIntField(env,subprocess, (*env)->GetFieldID(env,clazz,"mParentPid","I"), ppid);
			(*env)->SetObjectField(env,subprocess, (*env)->GetFieldID(env,clazz,"mContext","Landroid/content/Context;"), context);
			(*env)->CallVoidMethod(env,subprocess, (*env)->GetMethodID(env,clazz,  "<init>", "()V"));
			(*env)->CallVoidMethod(env,subprocess, (*env)->GetMethodID(env,clazz, "runOnSubprocess","()V"));
			(*env)->DeleteLocalRef(env,subprocess);
		} else {
			LOG_D(LOGTAG, "create--NewObject Subprocess failed!");
		}
		(*env)->DeleteLocalRef(env,clazz);
	}
}

static void JNICALL create (JNIEnv *env, jclass thiz, jobject context, jclass clazz){
	pid_t ppid = getpid();
	pid_t pid = fork();
	if (pid < 0) {
		LOG_D(LOGTAG, "create--fork failed!");
	} else if (pid == 0) {
		LOG_D(LOGTAG, "create--runOnSubprocess start...");
		runOnSubprocess(env,clazz,ppid,context);
		LOG_D(LOGTAG, "create--runOnSubprocess finished!");
		exit(1);
	} else {
		LOG_D(LOGTAG, "create--run on parent process!");
	}
}

static JNINativeMethod METHODS[]={
		{"create", "(Landroid/content/Context;Ljava/lang/Class;)V", (void *)create}
};

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	int retval = -1;
	JNIEnv *env;
	if ( (*vm)->GetEnv(vm,(void **) &env, JNI_VERSION_1_4) == JNI_OK) {
		jclass clazz = (*env)->FindClass(env,"com/droidwolf/nativesubprocess/Subprocess");
		if (clazz != NULL) {
			if ((*env)->RegisterNatives(env,clazz, METHODS,sizeof(METHODS) / sizeof(METHODS[0])) >= 0) {
				retval = JNI_VERSION_1_4;
			}else{
				 LOG_D(LOGTAG,"RegisterNatives Subprocess.create method failed!");
			}
			(*env)->DeleteLocalRef(env,clazz);
		}else{
			 LOG_D(LOGTAG,"com/droidwolf/nativesubprocess/Subprocess not found!");
		}
	}
	return retval;
}

JNIEXPORT void JNI_OnUnload(JavaVM* vm, void* reserved) {
}
