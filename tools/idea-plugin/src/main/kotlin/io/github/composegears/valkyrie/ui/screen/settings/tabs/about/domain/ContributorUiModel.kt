package io.github.composegears.valkyrie.ui.screen.settings.tabs.about.domain

data class ContributorUiModel(
    val login: String,
    val description: String,
    val profileUrl: String,
    val avatarUrl: String,
    val avatarBytes: AvatarByteArray? = null,
)
