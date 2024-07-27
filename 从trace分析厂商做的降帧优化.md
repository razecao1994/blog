# 背景
早在`iphone 13 Pro`发布的时候，苹果提到了它们在高刷屏幕上的一项技术应用`ProMotion`，表示可以根据显示内容动态的调节刷新率，利用低刷新率来节省电量(`Use lower refresh rates whenever possible to save power.`)。支持的刷新率如下：
```xml
120Hz (8ms)
80Hz (12ms)
60Hz (16ms)
48Hz (20ms)
40Hz (25ms)
30Hz (33ms)
24Hz (41ms)
20Hz (50ms)
16Hz (62ms)
15Hz (66ms)
12Hz (83ms)
10Hz (100ms)
```
需要详细了解的读者，可以通过这篇文章查阅[优化 iPhone 13 Pro 和 iPad Pro 的 ProMotion 刷新率 |Apple 开发者文档 --- Optimizing ProMotion refresh rates for iPhone 13 Pro and iPad Pro | Apple Developer Documentation](https://developer.apple.com/documentation/quartzcore/optimizing_promotion_refresh_rates_for_iphone_13_pro_and_ipad_pro/#3885322)。

# 原理推测
我们都知道，为了让界面保持流畅，需要屏幕按照稳定帧率刷新内容，那么ios的这种ProMotion技术是如何在保障界面流畅的情况下又动态降低刷新率的呢？

由于本人没有用`iphone`，想着国产厂商也有提到过他们的动态降帧，所以利用自己的机子(机型：vivo，系统版本：Origin OS 4)抓了在微博界面滑动的trace。
![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/d1a1f6b1ff704b0bba1115e36848c230~tplv-73owjymdk6-watermark.image?policy=eyJ2bSI6MywidWlkIjoiMzg3ODczMjc1NTExNDgxNCJ9&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1722662327&x-orig-sign=Rlmv46u5jwoZHrJFUZo51yEiZDo%3D)
从`trace`的`VSYNC-APP`泳道的`vsync`信号间距可以看到：
```
在滑动初期，帧间距为11ms（90Hz）
运动了一会儿，帧间距为13.xxxms(72~75Hz)
接着帧间局为16ms(60Hz)
最后帧间局达到了33ms(30Hz)
```
通过`uiautomatorviewer`查看微博的首页面的基础控件是`RecyclerView`，结合`trace`可以确定整个降帧过程都是在微博列表的`fling`运动阶段发生的。

如果了解过`RecyclerView`，应该知道在`ACTION_UP`之后的动画是由OverScroller实现的。

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/5bbbfbf635154ad8bd1f6223364b9851~tplv-73owjymdk6-watermark.image?policy=eyJ2bSI6MywidWlkIjoiMzg3ODczMjc1NTExNDgxNCJ9&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1722662327&x-orig-sign=B3I5xmksnXGpOXvlz4ZjCuVpiX0%3D)

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/9201cc136ab54665a19c67753a1d0fa0~tplv-73owjymdk6-watermark.image?policy=eyJ2bSI6MywidWlkIjoiMzg3ODczMjc1NTExNDgxNCJ9&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1722662327&x-orig-sign=bJBQ5LeLFQz7XdJWRDEY%2Fq39im0%3D)

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/1972991e3d2345dda4bc04c5978963c7~tplv-73owjymdk6-watermark.image?policy=eyJ2bSI6MywidWlkIjoiMzg3ODczMjc1NTExNDgxNCJ9&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1722662327&x-orig-sign=g3xfWWtyA5rRQHo%2FqysUypUOKq8%3D)

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/ab21da422aec40d599188001c122fd6a~tplv-73owjymdk6-watermark.image?policy=eyJ2bSI6MywidWlkIjoiMzg3ODczMjc1NTExNDgxNCJ9&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1722662327&x-orig-sign=f8ny0ZRkpF3jXgItX4BCgpHE4Yo%3D)

通过源码可以看到，OverScroller$SplineOverScroller#fling的逻辑中，通过源码，可以知道，每次刷新时的速度都在减少。

**推测**：`vivo`的动态降帧是根据移动速度来调节的，可以想象，当移动速度较小时，同一段时间内的唯一距离较短，位移变化不明显，而这个时间内降帧不会影响用户体验。不好理解的小伙伴可以查询`ease缓动曲线`，在线查看缓动曲线的位移变化帮助理解。

# 原理印证
没有验证猜想的推理是不完整的，在借到了机子（发现通过xcode装的虚拟机不能触发降帧，应该与硬件相关）的情况下，简单利用SwiftUI基于TableView写了个滚动页面，打印出来每一帧的时间，根据时间间隔来绘制一下ios上的动态降帧曲线。如下：

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/f4c4dad7e6d44cad9d21b588fc5b7a33~tplv-73owjymdk6-watermark.image?policy=eyJ2bSI6MywidWlkIjoiMzg3ODczMjc1NTExNDgxNCJ9&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1722662327&x-orig-sign=NkavRIwphCECeJqLVCUsEXWR4e4%3D)

# 结论
1. Android厂商在做通用的动态降帧方案，应该是一种根据速度变化而调节刷新率的降帧方案
2. ios在具有高刷的机型上已经逻辑了ProMotion方案# 背景
