-allowaccessmodification
-dontobfuscate
-keepattributes SourceFile, LineNumberTable

-keep class io.github.composegears.valkyrie.MainKt {
  public static void main(java.lang.String[]);
}

-dontwarn com.google.j2objc.annotations.**
-dontwarn org.graalvm.nativeimage.**
-dontwarn org.graalvm.word.**
-dontwarn com.oracle.svm.core.annotate.**

# TODO: it should be possible to keep less classes under this package.
-keep class org.xmlpull.**
