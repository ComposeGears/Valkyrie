package io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font

enum class IconFontFamily(
    val githubUrl: String,
    val cdnUrl: String,
    val displayName: String,
    val fontFamily: String,
) {
    OUTLINED(
        githubUrl = "https://github.com/google/material-design-icons/raw/refs/heads/master/variablefont/MaterialSymbolsOutlined%5BFILL,GRAD,opsz,wght%5D.woff2",
        cdnUrl = "https://cdn.jsdelivr.net/gh/google/material-design-icons@master/variablefont/MaterialSymbolsOutlined%5BFILL%2CGRAD%2Copsz%2Cwght%5D.woff2",
        displayName = "Outlined",
        fontFamily = "materialsymbolsoutlined",
    ),
    ROUNDED(
        githubUrl = "https://github.com/google/material-design-icons/raw/refs/heads/master/variablefont/MaterialSymbolsRounded%5BFILL,GRAD,opsz,wght%5D.woff2",
        cdnUrl = "https://cdn.jsdelivr.net/gh/google/material-design-icons@master/variablefont/MaterialSymbolsRounded%5BFILL%2CGRAD%2Copsz%2Cwght%5D.woff2",
        displayName = "Rounded",
        fontFamily = "materialsymbolsrounded",
    ),
    SHARP(
        githubUrl = "https://github.com/google/material-design-icons/raw/refs/heads/master/variablefont/MaterialSymbolsSharp%5BFILL,GRAD,opsz,wght%5D.woff2",
        cdnUrl = "https://cdn.jsdelivr.net/gh/google/material-design-icons@master/variablefont/MaterialSymbolsSharp%5BFILL%2CGRAD%2Copsz%2Cwght%5D.woff2",
        displayName = "Sharp",
        fontFamily = "materialsymbolssharp",
    ),
}
