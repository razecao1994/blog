# 说明
`LeakCanary`利用了`StartUp`库的能力（基于ContentProvider的初始化在Application初始化之前的特性），保证在程序刚启动时就执行了`AppWatcher#manualInstall`，后续的逻辑就此展开
# 需要关注的类
- `AppWatcherStartupInitializer`
- `AppWatcher`
- `ObjectWatcher`
- `ReachabilityWatcher`
- `ActivityWatcher`
- `ReferenceQueueRetainedObjectWatcher`
- `LeakCanaryDelegate`
- `InternalLeakCanary`
- `HeapDumpTrigger`

# 需要关注的方法&逻辑
- `AppWatcherStartupInitializer#onCreate()`
- `AppWatcher#manualInstall()`
- `AppWatcher#appDefaultWatchers()`
- `ObjectWatcher#expectWeaklyReachable()`
- `ReachabilityWatcher#asDeletableObjectReporter()`
- `ActivityWatcher#lifecycleCallbacks`
- `ReferenceQueueRetainedObjectWatcher#expectDeletionOnTriggerFor()`
- `TriggeredDeletableObjectReporter$RetainTrigger`

# 重点代码
位于`AppWatcher`，在程序刚启动时调用，用于初始化环境
```java
  @JvmOverloads
  fun manualInstall(
    application: Application,
    retainedDelayMillis: Long = TimeUnit.SECONDS.toMillis(5),
    watchersToInstall: List<InstallableWatcher> = appDefaultWatchers(application)
  ) {
    checkMainThread()
    if (isInstalled) {
      throw IllegalStateException(
        "AppWatcher already installed, see exception cause for prior install call", installCause
      )
    }
    check(retainedDelayMillis >= 0) {
      "retainedDelayMillis $retainedDelayMillis must be at least 0 ms"
    }
    this.retainedDelayMillis = retainedDelayMillis
    if (application.isDebuggableBuild) {
      LogcatSharkLog.install()
    }
    // Requires AppWatcher.objectWatcher to be set
    LeakCanaryDelegate.loadLeakCanary(application)

    watchersToInstall.forEach {
      it.install()
    }
    // Only install after we're fully done with init.
    installCause = RuntimeException("manualInstall() first called here")
  }
```
位于`ReferenceQueueRetainedObjectTracker.java`，用以判断目标的引用是否可达
```java
override fun expectDeletionOnTriggerFor(
    target: Any,
    reason: String
  ): RetainTrigger {
    removeWeaklyReachableObjects()
    val key = UUID.randomUUID()
      .toString()
    val watchUptime = clock.uptime()
    val reference =
      KeyedWeakReference(target, key, reason, watchUptime.inWholeMilliseconds, queue)
    SharkLog.d {
      "Watching " +
        (if (target is Class<*>) target.toString() else "instance of ${target.javaClass.name}") +
        (if (reason.isNotEmpty()) " ($reason)" else "") +
        " with key $key"
    }

    watchedObjects[key] = reference
    return object : RetainTrigger {
      override val isStronglyReachable: Boolean
        get() {
          removeWeaklyReachableObjects()
          val weakRef = watchedObjects[key]
          return weakRef != null
        }

      override val isRetained: Boolean
        get() {
          removeWeaklyReachableObjects()
          val weakRef = watchedObjects[key]
          return weakRef?.retained ?: false
        }

      override fun markRetainedIfStronglyReachable() {
        moveToRetain[从trace分析厂商做的降帧优化.md](%B4%D3trace%B7%D6%CE%F6%B3%A7%C9%CC%D7%F6%B5%C4%BD%B5%D6%A1%D3%C5%BB%AF.md)ed(key)
      }
    }
  }
```
# 时序图
![image.png]([https://github.com/razecao1994/blog/blob/main/assets/leak_canary_sequence.png](https://github.com/razecao1994/blog/blob/main/assets/leak_canary_sequence.jpg))
