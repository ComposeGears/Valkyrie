package io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font

enum class MaterialIconFontFamily(
    val cdnUrl: String,
    val displayName: String,
    val fontFamily: String,
) {
    Outlined(
        cdnUrl = "https://cdn.jsdelivr.net/gh/google/material-design-icons@master/variablefont/MaterialSymbolsOutlined%5BFILL%2CGRAD%2Copsz%2Cwght%5D.woff2",
        displayName = "Outlined",
        fontFamily = "materialsymbolsoutlined",
    ),
    Rounded(
        cdnUrl = "https://cdn.jsdelivr.net/gh/google/material-design-icons@master/variablefont/MaterialSymbolsRounded%5BFILL%2CGRAD%2Copsz%2Cwght%5D.woff2",
        displayName = "Rounded",
        fontFamily = "materialsymbolsrounded",
    ),
    Sharp(
        cdnUrl = "https://cdn.jsdelivr.net/gh/google/material-design-icons@master/variablefont/MaterialSymbolsSharp%5BFILL%2CGRAD%2Copsz%2Cwght%5D.woff2",
        displayName = "Sharp",
        fontFamily = "materialsymbolssharp",
    ),
}
