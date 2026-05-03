package io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.data

import io.github.composegears.valkyrie.ui.screen.webimport.common.data.RegexCssCodepointParser

class EvaCodepointParser :
    RegexCssCodepointParser(
        Regex("""\.eva-([a-z0-9-]+)::before\s*\{\s*content\s*:\s*"\\([A-Fa-f0-9]+)"\s*;?\s*}"""),
    )
