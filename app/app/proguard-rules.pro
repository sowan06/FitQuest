# R8/proguard rules
-keepattributes Signature, *Annotation*, EnclosingMethod, InnerClasses

# Moshi reflection
-keep class com.fitquest.app.data.remote.dto.** { *; }
