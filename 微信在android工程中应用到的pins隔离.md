之前在业务迭代过程中，发现随着需求不断增多，原来按照细粒度划分的模块逐渐臃肿，加之维护人员比较多，不再清晰的模块划分会导致开发人员将新增的类没有规则的放置，之前还合理的修改，可能过几个月就看不懂了。当时有人提出了将现有的module拆分出更小的几个module，但是这些业务又存在关联，拆分出来可能会交叉依赖，而且也会增加工程的编译速度。

在这种情况下，我们就找到一个比较合适且大厂也在应用的方案，[微信的pins隔离](https://mp.weixin.qq.com/s/mkhCzeoLdev5TyO6DqHEdw)，它主要是以p_xx的文件夹将module内更内聚的业务以及资源放在一起，形成显示隔离，方便维护，如下图所示：
![[pins_1.png]]

整体实现上也比较简单，依赖于强大的gralde构建，下面主要记录下gradle的修改：
```groovy
android {
	sourceSets {
		main {
			def src_dir = new File(projectDir, "src")
			def dirs = src_dir
					.listFiles()
					.toList()
					.stream()
					.filter(new Predicate<File>() {
						@Override
						boolean test(File file) {
							return file.getName().startsWith("p_")
						}
					})
					.map {
						return it.getName()
					}
					.collect(Collections.toList())
			println("pins-module: " + dirs)
			dirs.each { dirs ->
					java.srcDir("src/$dir/java")
					java.srcDir("src/$dir/res")
			}
		}
	}
}
```