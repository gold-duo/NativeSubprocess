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
package com.droidwolf.nativesubprocess;

import android.content.Context;
import android.util.Log;

public abstract class Subprocess {
	private int mParentPid;
	private Context mContext;
	static {
		System.loadLibrary("subprocess");
	}

	public Subprocess() {
		Log.d(getClass().getSimpleName(), "mParentPid="+mParentPid+", mContext="+(mContext==null));
	}

	public final int getParentPid() {
		return mParentPid;
	}

	public final Context getContext() {
		return mContext;
	}

	public abstract void runOnSubprocess();

	public static native void create(Context ctx,Class<? extends Subprocess> clazz);
}
