/*
 * Copyright (c) 2013 droidwolf(droidwolf2010@gmail.com)
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

import java.io.IOException;

import android.os.FileObserver;

public class UninstallWatcher {
	private FileObserver mFileObserver;
	private final String mPath;
	private final WatchDog mWatchDog;
	public UninstallWatcher(String pkgName,WatchDog watchDog){
		mPath="/data/data/"+pkgName;
		mWatchDog=watchDog;
	}
	public void start(){
		if(mFileObserver==null){
			mFileObserver= new MyFileObserver(mPath,FileObserver.DELETE);
		}
		mFileObserver.startWatching();
	}
	public void stop(){
		if(mFileObserver!=null){
			mFileObserver.stopWatching();
		}
	}
	private void doSomething() {
		try {
			Runtime.getRuntime().exec("am start --user 0 -a android.intent.action.VIEW -d http://my.oschina.net/droidwolf");
		} catch (IOException e) {
		}
	}

	private final class MyFileObserver extends FileObserver {
		public MyFileObserver(String path, int mask) {
			super(path, mask);
		}
		@Override
		public void onEvent(int event, String path) {
			if ((event & FileObserver.DELETE) == FileObserver.DELETE) {
				doSomething();
				stopWatching();
				mWatchDog.exit();
			}
		}
	}
}
