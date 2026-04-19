package io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.data

import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.data.RegexCssCodepointParser

class TablerCodepointParser :
    RegexCssCodepointParser(
        Regex("""\.ti-([a-z0-9-]+):before\s*\{\s*content:\s*"\\([A-Fa-f0-9]+)"\s*;?\s*}"""),
    )
