-allowaccessmodification
-dontobfuscate
-keepattributes SourceFile, LineNumberTable

-keep class io.github.composegears.valkyrie.cli.MainKt {
  public static void main(java.lang.String[]);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class org.xmlpull.v1.** { *; }
-keep class org.xmlpull.mxp1.** { *; }

-dontwarn com.google.j2objc.annotations.**
-dontwarn org.graalvm.nativeimage.**
-dontwarn org.graalvm.word.**
-dontwarn com.oracle.svm.core.annotate.**
