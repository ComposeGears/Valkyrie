package io.github.composegears.valkyrie.ui.screen.settings.tabs.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import io.github.composegears.valkyrie.ui.screen.settings.tabs.about.domain.AvatarByteArray
import io.github.composegears.valkyrie.ui.screen.settings.tabs.about.domain.ContributorUiModel
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.util.result.runCatchingCancellable
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AboutSettingsViewModel : ViewModel() {

    private val httpClient = inject(NetworkModule.httpClient)

    private val _contributors = MutableStateFlow(initialContributors())
    val contributors = _contributors.asStateFlow()

    init {
        loadAvatars()
    }

    private fun loadAvatars() {
        viewModelScope.launch {
            val updated = _contributors.value.map { contributor ->
                async {
                    val bytes = runCatchingCancellable {
                        AvatarByteArray(httpClient.get(contributor.avatarUrl).bodyAsBytes())
                    }.getOrNull()
                    contributor.copy(avatarBytes = bytes)
                }
            }.awaitAll()
            _contributors.value = updated
        }
    }

    private fun initialContributors(): List<ContributorUiModel> = listOf(
        ContributorUiModel(
            login = "egorikftp",
            description = "Project creator & core maintainer",
            profileUrl = "https://github.com/egorikftp",
            avatarUrl = "https://github.com/egorikftp.png",
        ),
        ContributorUiModel(
            login = "t-regbs",
            description = "Web import & Figma plugin development, ImageVector to XML conversion, KMP tuning",
            profileUrl = "https://github.com/t-regbs",
            avatarUrl = "https://github.com/t-regbs.png",
        ),
        ContributorUiModel(
            login = "Goooler",
            description = "Build tooling, test infrastructure & dependency management",
            profileUrl = "https://github.com/Goooler",
            avatarUrl = "https://github.com/Goooler.png",
        ),
        ContributorUiModel(
            login = "LennartEgb",
            description = "KMP generator & parser modules, IR restructuring & Compose colors handling",
            profileUrl = "https://github.com/LennartEgb",
            avatarUrl = "https://github.com/LennartEgb.png",
        ),
        ContributorUiModel(
            login = "vkatz",
            description = "Fuzzy search, plugin state persistence & technical support",
            profileUrl = "https://github.com/vkatz",
            avatarUrl = "https://github.com/vkatz.png",
        ),
        ContributorUiModel(
            login = "jonapoul",
            description = "Initial Gradle plugin implementation",
            profileUrl = "https://github.com/jonapoul",
            avatarUrl = "https://github.com/jonapoul.png",
        ),
    )
}
