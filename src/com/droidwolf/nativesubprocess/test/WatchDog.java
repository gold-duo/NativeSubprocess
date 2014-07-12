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

package com.droidwolf.nativesubprocess.test;

import com.droidwolf.nativesubprocess.Subprocess;

public class WatchDog extends Subprocess {
	private final Object mSync = new Object();

	private ProcessWatcher mProcessWatcher;
	private UninstallWatcher mUninstallWatcher;

	@Override
	public void runOnSubprocess(int parentPid) {
		regWatchers(parentPid);
		holdMainThread();
		unregWatchers();
		System.exit(0);
	}

	private void regWatchers(int parentPid) {
		if (mProcessWatcher == null) {
			mProcessWatcher = new ProcessWatcher(parentPid,this);
		} else {
			mProcessWatcher.stop();
		}
		mProcessWatcher.start();

		if (mUninstallWatcher == null) {
			mUninstallWatcher = new UninstallWatcher("com.droidwolf.nativesubprocess",this);
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
