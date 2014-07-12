**NativeSubprocess** is a linux native process for android bridge 。
It creates a child process by fork. By callbacks your android java code will run in the child process created inside.

it creates a child process by NDK calling linux fork function. By callbacks your android java code will run in the child process created inside. Such as Watchdog (sample ProcessWatcher) to monitor your android app service, call up the customer satisfaction survey feedback page (sample UninstallWatcher) when uninstalling your applications.

**How to use**

1. copy com.droidwolf.nativesubprocess package to your project;
2. copy libsubprocess.so library to your  libs/armeabi project directory.
3. implements Subprocess class and  override runOnSubprocess function.
4. Finally, create your child process call by Subprocess.create function.

**Authors**

droidwolf [droidwolf2010@gmail.com](mailto:droidwolf2010@gmail.com "droidwolf2010@gmail.com")


**License**

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0 "Apache License, Version 2.0")