/*
 * Copyright (c) 2014 droidwolf(droidwolf2010@gmail.com)
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

package com.droidwolf.example;

import android.content.Context;
import android.content.SharedPreferences;

import com.droidwolf.nativesubprocess.Subprocess;

public class WatchDog extends Subprocess {
	private final Object mSync = new Object();

	private ProcessWatcher mProcessWatcher;
	private UninstallWatcher mUninstallWatcher;

	@Override
	public void runOnSubprocess() {
		killPreviousProcess();
		regWatchers(getParentPid());
		holdMainThread();
		unregWatchers();
//		System.exit(0);
	}
	
	private void killPreviousProcess(){
		try{
			final String KEY="previous_pid";
			final SharedPreferences spf=getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
			final int pid =spf.getInt(KEY, 0);
			if(pid!=0){
				android.os.Process.killProcess(pid);
			}
			spf.edit().putInt(KEY, android.os.Process.myPid()).commit();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void regWatchers(int parentPid) {
		if (mProcessWatcher == null) {
			mProcessWatcher = new ProcessWatcher(parentPid,this);
		} else {
			mProcessWatcher.stop();
		}
		mProcessWatcher.start();

		if (mUninstallWatcher == null) {
			mUninstallWatcher = new UninstallWatcher(getContext().getPackageName(),this);
		} else {
			mUninstallWatcher.stop();
		}
		mUninstallWatcher.start();
	}

	private void unregWatchers(){
		if (mProcessWatcher != null) {
			mProcessWatcher.stop();
		}
		if (mUninstallWatcher != null) {
			mUninstallWatcher.stop();
		}
	}
	private void holdMainThread() {
		try {
			synchronized (mSync) {
				mSync.wait();
			}
		} catch (InterruptedException e) {
		}
	}

	public void exit() {
		try {
			mSync.notify();
		} catch (Exception e) {
		}
	}
}// end class WatchDog
