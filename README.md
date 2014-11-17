**NativeSubprocess** is a linux native process for android bridge。

**Usage**

it creates a child process by NDK calling linux fork function. callback your android java code run in the child process inside. Such as [Watchdog](https://github.com/droidwolf/NativeSubprocess/blob/master/src/com/droidwolf/example/WatchDog.java "WatchDog") (sample [ProcessWatcher](https://github.com/droidwolf/NativeSubprocess/blob/master/src/com/droidwolf/example/ProcessWatcher.java "ProcessWatcher")) to monitor your android app service, call up the customer satisfaction survey feedback page (sample [UninstallWatcher](https://github.com/droidwolf/NativeSubprocess/blob/master/src/com/droidwolf/example/UninstallWatcher.java "UninstallWatcher")) when uninstalling your applications.

**How to use**

1. copy com.droidwolf.nativesubprocess package to your project;
2. copy libsubprocess.so library to your  libs/armeabi project directory.
3. implements Subprocess class and  override runOnSubprocess function.
4. Finally, create your child process call by Subprocess.create function.

**中文描述**
安全软件卸载后调出浏览器苦苦哀求"主人，为什么要抛弃我..."页面是怎么做到的？service经常莫名挂了肿么办？用 NativeSubprocess一切都很简单。

NativeSubprocess 是一个可以让你在android 程序中创建linux子进程并执行你的java代码的so库。由于市面上典型的内存清理工具值清理apk 包关联的进程，而不会处理linux原生进程，所以NativeSubprocess 可以做什么您懂滴！

不详细介绍，有兴趣的请看[两个典型应用场景](https://github.com/droidwolf/NativeSubprocess/blob/master/src/com/droidwolf/example/WatchDog.java "WatchDog")：
1.进程监控   [ProcessWatcher](https://github.com/droidwolf/NativeSubprocess/blob/master/src/com/droidwolf/example/ProcessWatcher.java "ProcessWatcher")；
2.卸载反馈  [UninstallWatcher](https://github.com/droidwolf/NativeSubprocess/blob/master/src/com/droidwolf/example/UninstallWatcher.java "UninstallWatcher")。

**Authors**

droidwolf [droidwolf2010@gmail.com](mailto:droidwolf2010@gmail.com "droidwolf2010@gmail.com")


**License**

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0 "Apache License, Version 2.0")
