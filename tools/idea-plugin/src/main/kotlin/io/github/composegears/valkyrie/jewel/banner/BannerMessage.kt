package io.github.composegears.valkyrie.jewel.banner

sealed interface BannerMessage {
    val text: String
    val duration: BannerDuration
    val actions: List<BannerAction>

    data class SuccessBanner(
        override val text: String,
        override val duration: BannerDuration = BannerDuration.Short,
        override val actions: List<BannerAction> = emptyList(),
    ) : BannerMessage

    data class WarningBanner(
        override val text: String,
        override val duration: BannerDuration = BannerDuration.Short,
        override val actions: List<BannerAction> = emptyList(),
    ) : BannerMessage

    data class ErrorBanner(
        override val text: String,
        override val duration: BannerDuration = BannerDuration.Short,
        override val actions: List<BannerAction> = emptyList(),
    ) : BannerMessage
}

data class BannerAction(
    val label: String,
    val onClick: () -> Unit,
)

enum class BannerDuration {
    Short,
    Long,
    Indefinite,
}
