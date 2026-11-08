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

-keep class org.xmlpull.v1.XmlPullParser { *; }
-keep class * extends org.xmlpull.v1.XmlPullParser {
    <init>(...);
    <methods>;
}

-dontwarn org.xmlpull.mxp1.**

# ProtoXmlPullParser handles compiled/binary AAPT2 XML, not exercised when parsing plain text SVG/XML
-dontwarn com.android.aapt.Resources$**

-dontwarn com.google.auto.service.AutoService
-dontwarn com.sun.activation.registries.LogSupport
-dontwarn com.sun.activation.registries.MailcapFile