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

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.os.FileObserver;

public class ProcessWatcher {
	private FileObserver mFileObserver;
	private final String mPath;
	private final File mFile;
	private final WatchDog mWatchDog;
	public ProcessWatcher(int pid,WatchDog watchDog){
		mPath="/proc/"+pid;
		mFile= new File(mPath);
		mWatchDog=watchDog;
	}
	public void start(){
		if(mFileObserver==null){
			mFileObserver= new MyFileObserver(mPath,FileObserver.CLOSE_NOWRITE);
		}
		mFileObserver.startWatching();
	}
	public void stop(){
		if(mFileObserver!=null){
			mFileObserver.stopWatching();
		}
	}
	private void doSomething() {
//		try {
//			Runtime.getRuntime().exec("am start --user 0 -n com.droidwolf.example/com.droidwolf.example.WatchDogActivity");
//		} catch (IOException e) {}
	    mWatchDog.getContext().startActivity(new Intent(mWatchDog.getContext(), com.droidwolf.example.WatchDogActivity.class));
	}

	private final class MyFileObserver extends FileObserver {
		private final Object mWaiter=new Object();
		public MyFileObserver(String path, int mask) {
			super(path, mask);
		}
		@Override
		public void onEvent(int event, String path) {
			if ((event & FileObserver.CLOSE_NOWRITE) == FileObserver.CLOSE_NOWRITE) {
				try {
					synchronized (mWaiter) {
						mWaiter.wait(3000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!mFile.exists()) {
					doSomething();
					stopWatching();
					mWatchDog.exit();
				}
			}
		}
	}
}