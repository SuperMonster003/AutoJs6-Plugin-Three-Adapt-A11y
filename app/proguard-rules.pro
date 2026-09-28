-keep class org.autojs.plugin.** { *; }
-keepnames class com.google.android.accessibility.selecttospeak.SelectToSpeakService
-dontwarn kotlinx.parcelize.Parcelize

# The Shizuku server instantiates the user service reflectively by class name.
-keep class io.github.supermonster003.autojs6.plugin.three.adapt.a11y.shizuku.ShizukuShellService { <init>(...); *; }
-keep class io.github.supermonster003.autojs6.plugin.three.adapt.a11y.shizuku.IShizukuShell** { *; }
