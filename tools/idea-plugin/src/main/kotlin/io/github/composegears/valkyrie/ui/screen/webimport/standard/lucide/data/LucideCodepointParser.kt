package io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.data

import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.data.RegexCssCodepointParser

class LucideCodepointParser :
    RegexCssCodepointParser(
        Regex("""\.icon-([a-z0-9-]+)::?before\s*\{\s*content:\s*["']\\([a-fA-F0-9]+)["'];\s*}"""),
    )
