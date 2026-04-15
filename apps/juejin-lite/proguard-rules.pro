# Kotlin serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.example.juejin.**$$serializer { *; }
-keepclassmembers class com.example.juejin.** {
    *** Companion;
}
-keepclasseswithmembers class com.example.juejin.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Ktor
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.atomicfu.**
-dontwarn io.netty.**
-dontwarn com.typesafe.**
-dontwarn org.slf4j.**

# Java Management (for Ktor)
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-dontwarn javax.management.**

# OkHttp (used by Ktor)
-dontwarn okhttp3.**
-dontwarn okio.**

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Coil
-keep class coil3.** { *; }
-dontwarn coil3.**
