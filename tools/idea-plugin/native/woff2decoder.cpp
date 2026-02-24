#include <jni.h>
#include <string>
#include <cstdio>
#include <woff2/decode.h>

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_io_github_composegears_valkyrie_util_font_Woff2Decoder_decodeBytes(
    JNIEnv *env, jobject thiz, jbyteArray input_bytes) {

    auto *input_data = reinterpret_cast<uint8_t *>(env->GetByteArrayElements(input_bytes, nullptr));
    if (input_data == nullptr || env->ExceptionCheck()) {
        return nullptr;
    }

    jsize input_len = env->GetArrayLength(input_bytes);
    if (env->ExceptionCheck()) {
        env->ReleaseByteArrayElements(input_bytes, reinterpret_cast<jbyte *>(input_data), JNI_ABORT);
        return nullptr;
    }

    std::string output(
        std::min(woff2::ComputeWOFF2FinalSize(input_data, input_len),
                 woff2::kDefaultMaxSize),
        0);

    woff2::WOFF2StringOut out(&output);

    if (!woff2::ConvertWOFF2ToTTF(input_data, input_len, &out)) {
        fprintf(stderr, "woff2decoder: decompression error\n");
        env->ReleaseByteArrayElements(input_bytes, reinterpret_cast<jbyte *>(input_data), JNI_ABORT);
        return nullptr;
    }

    output.resize(out.Size());

    jbyteArray arr = env->NewByteArray(static_cast<jsize>(out.Size()));
    if (arr == nullptr || env->ExceptionCheck()) {
        env->ReleaseByteArrayElements(input_bytes, reinterpret_cast<jbyte *>(input_data), JNI_ABORT);
        return nullptr;
    }

    env->SetByteArrayRegion(arr, 0, static_cast<jsize>(out.Size()),
                            reinterpret_cast<const jbyte *>(output.data()));
    if (env->ExceptionCheck()) {
        env->ReleaseByteArrayElements(input_bytes, reinterpret_cast<jbyte *>(input_data), JNI_ABORT);
        return nullptr;
    }

    env->ReleaseByteArrayElements(input_bytes, reinterpret_cast<jbyte *>(input_data), JNI_ABORT);

    return arr;
}
