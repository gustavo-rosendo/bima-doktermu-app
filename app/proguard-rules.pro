# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/apradanas/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#Use 5 step of optimization
-optimizationpasses 5

#When not preverifing in a case-insensitive filing system, such as Windows. This tool will unpack your processed jars,(if using windows you should then use):
-dontusemixedcaseclassnames

#Specifies not to ignore non-public library classes. As of version 4.5, this is the default setting
-dontskipnonpubliclibraryclasses

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-dontpreverify

-dontwarn android.support.**

#Specifies to write out some more information during processing. If the program terminates with an exception, this option will print out the entire stack trace, instead of just the exception message.
-verbose

#The -optimizations option disables some arithmetic simplifications that Dalvik 1.0 and 1.5 can't handle. Note that the Dalvik VM also can't handle aggressive overloading (of static fields).
#To understand or change this check http://proguard.sourceforge.net/index.html#/manual/optimizations.html
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

#To repackage classes on a single package
#-repackageclasses ''

# Uncomment if using annotations to keep them.
-keepattributes EnclosingMethod, Signature, SourceFile, LineNumberTable, *Annotation*

# Keep classes that are referenced on the AndroidManifest
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
# Compatibility library
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
 -keepclassmembers public class * extends android.view.View {
  void set*(***);
  *** get*();
}

# To remove debug logs:
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** w(...);
}

#To avoid changing names of methods invoked on layout's onClick.
# Uncomment and add specific method names if using onClick on layouts
#-keepclassmembers class * {
# public void onClickButton(android.view.View);
#}

# Maintain java native methods
-keepclasseswithmembers class * {
    native <methods>;
}

# To maintain custom components names that are used on layouts XML:
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Maintain enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# To keep parcelable classes (to serialize - deserialize objects to sent through Intents)
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Keep the R
-keepclassmembers class **.R$* {
    public static <fields>;
}

# OKHttp
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontwarn rx.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Keep the pojos used by GSON
-keep class com.bima.dokterpribadimu.model.** { *; }

# Crashlytics
-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.android.**

# RxJava
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Billing
-keep class com.android.vending.billing.**

###### ADDITIONAL OPTIONS NOT USED NORMALLY

#To keep callback calls. Uncomment if using any
#http://proguard.sourceforge.net/index.html#/manual/examples.html#callback
#-keep class mypackage.MyCallbackClass {
#   void myCallbackMethod(java.lang.String);
#}

#Uncomment if using Serializable
#-keepclassmembers class * implements java.io.Serializable {
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}