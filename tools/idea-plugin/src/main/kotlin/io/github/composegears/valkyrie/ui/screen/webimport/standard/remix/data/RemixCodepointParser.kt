package io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.data

import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.data.RegexCssCodepointParser

class RemixCodepointParser :
    RegexCssCodepointParser(
        Regex("""\.ri-([a-z0-9-]+):before\s*\{\s*content:\s*"\\([a-fA-F0-9]+)";?\s*}"""),
    )
