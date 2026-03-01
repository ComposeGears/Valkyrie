package io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.data

import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.data.RegexCssCodepointParser

class BootstrapCodepointParser :
    RegexCssCodepointParser(
        Regex("""\.bi-([a-z0-9-]+)::before\s*\{\s*content\s*:\s*"\\([A-Fa-f0-9]+)"\s*;?\s*}"""),
    )
