package io.github.composegears.valkyrie.jewel.banner

sealed interface BannerMessage {
    val text: String
    val duration: BannerDuration

    data class InfoBanner(
        override val text: String,
        override val duration: BannerDuration = BannerDuration.Short,
    ) : BannerMessage

    data class WarningBanner(
        override val text: String,
        override val duration: BannerDuration = BannerDuration.Short,
    ) : BannerMessage

    data class ErrorBanner(
        override val text: String,
        override val duration: BannerDuration = BannerDuration.Short,
    ) : BannerMessage
}

enum class BannerDuration {
    Short,
    Long,
    Indefinite,
}
