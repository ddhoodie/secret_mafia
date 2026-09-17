# Release minify. Keep reflection used by prefs / enums.
-keepattributes *Annotation*, InnerClasses, Signature, Exception
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-dontwarn kotlinx.coroutines.**
