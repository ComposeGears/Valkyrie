package io.github.composegears.valkyrie.ui.screen.webimport.standard.boxicons.data

import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.data.RegexCssCodepointParser

class BoxCodepointParser :
    RegexCssCodepointParser(
        Regex("""\.((?:bx|bxs|bxl)-[a-z0-9-]+)::?before\s*\{\s*content\s*:\s*["']\\([A-Fa-f0-9]+)["']\s*;?\s*}"""),
    )
