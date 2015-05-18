# SimpleLeakCanary
LeakCanary simple version

抽取了LeakCanary检测部分的代码，放在了这里，有兴趣的可以看下  
原理简单点说就是： 在onDestroy的时候为Activity加上WeakReference，然后在合适的时机调用Runtime.getRuntime().gc()触发GC,之后检测下WeakReference还是否存在。  
如果存在，那么Activity就泄露了；如果不存在，那么表示回收正常。  
具体的细节部分请看抽取代码。  
RefWatcher是引用监听,也是监听部分最核心的代码。  
## LeakCanary源码工程导读
- leakcanary-analyzer : 如果检测到内存泄露，就dump出hprof分析，这个库主要用于分析hprof。 代码利用了MAT
- leakcanary-android-no-op : 空实现，为了方便在release时调用
- leakcanary-android : debug时调用，组织监听、分析、展示结果等逻辑。
- leakcanary-sample : 官方调用示例
- leakcanary-watcher : 引用监听
# 声明
本库只是将LeakCanary监听部分做了简化处理（源码中还包含了排除SDK本身泄漏的部分，本库没有包含进来），方便大家以最小的成本学习原理。  
开发中还请使用LeakCanary。 
