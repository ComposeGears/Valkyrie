package io.github.composegears.valkyrie.ui

import ai.grazie.utils.dropPostfix
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.generator.Icon
import androidx.compose.material.icons.generator.IconParser
import androidx.compose.material.icons.generator.VectorAssetGenerator
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.ide.common.vectordrawable.Svg2Vector
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.intellij.openapi.ide.CopyPasteManager
import com.squareup.kotlinpoet.ClassName
import java.awt.datatransfer.StringSelection
import java.io.File
import kotlin.io.path.createTempFile
import kotlin.io.path.outputStream
import kotlin.io.path.readText

@Composable
fun ValkyriePlugin() {
    var showFilePicker by remember { mutableStateOf(false) }
    var file by remember { mutableStateOf<File?>(null) }

    var content by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(file) {
        val file = file ?: return@LaunchedEffect

        val filename = file.name.orEmpty().replace("_", "").dropPostfix(".svg")
        val tmp = createTempFile(suffix = "valkyrie/")
        Svg2Vector.parseSvgToXml(file, tmp.outputStream())

        val icon = Icon(
            kotlinName = filename,
            xmlFileName = "tmp",
            fileContent = tmp.readText()
        )
        val vector = IconParser(icon).parse()

        val aa = VectorAssetGenerator(
            icon.kotlinName,
            "groupPackage",
            vector,
            false
        ).createFileSpec(ClassName("com.mycompany.package", "ProjectPack"))

        content = aa.toString()
    }

    PluginUI(
        content = content,
        onChooseFile = { showFilePicker = true },
        onCopy = {
            val text = content ?: return@PluginUI
            CopyPasteManager.getInstance().setContents(StringSelection(text))
        }
    )

    FilePicker(
        show = showFilePicker,
        fileExtensions = listOf("svg", "xml"),
        initialDirectory = System.getProperty("user.home"),
        onFileSelected = { mpFile ->
            if (mpFile != null) {
                file = File(mpFile.path)
                showFilePicker = false
            } else {
                showFilePicker = false
            }
        }
    )
}

@Composable
private fun PluginUI(
    content: String?,
    onChooseFile: () -> Unit,
    onCopy: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = onChooseFile) {
                    Text(text = "Choose")
                }
            }

            if (content != null) {
                Box {
                    SelectionContainer {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .border(0.5.dp, Color.White)
                                .padding(16.dp),
                            text = content,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Thin
                            )
                        )
                    }
                    IconButton(
                        modifier = Modifier.padding(12.dp).align(Alignment.TopEnd),
                        onClick = onCopy
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun test() = MaterialTheme(colorScheme = darkColorScheme()) {
    var content by remember {
        mutableStateOf<String?>(
            "import androidx.compose.runtime.Composable\n" +
                    "import androidx.compose.foundation.Image\n" +
                    "import androidx.compose.ui.graphics.SolidColor\n" +
                    "import androidx.compose.ui.graphics.Color\n" +
                    "import androidx.compose.ui.graphics.StrokeCap\n" +
                    "import androidx.compose.ui.graphics.StrokeJoin\n" +
                    "import androidx.compose.ui.graphics.vector.ImageVector\n" +
                    "import androidx.compose.ui.graphics.PathFillType\n" +
                    "import androidx.compose.ui.graphics.vector.path\n" +
                    "import androidx.compose.ui.unit.dp\n" +
                    "import androidx.compose.ui.tooling.preview.Preview\n" +
                    "\n" +
                    "\n" +
                    "@Preview\n" +
                    "@Composable\n" +
                    "private fun VectorPreview() {\n" +
                    "    Image(Lovelist, null)\n" +
                    "}\n" +
                    "\n" +
                    "private var _Lovelist: ImageVector? = null\n" +
                    "\n" +
                    "public val Lovelist: ImageVector\n" +
                    "\t\tget() {\n" +
                    "\t\t\tif (_Lovelist != null) {\n" +
                    "\t\t\t\treturn _Lovelist!!\n" +
                    "\t\t\t}\n" +
                    "_Lovelist = ImageVector.Builder(\n" +
                    "                name = \"Lovelist\",\n" +
                    "                defaultWidth = 24.dp,\n" +
                    "                defaultHeight = 25.dp,\n" +
                    "                viewportWidth = 24f,\n" +
                    "                viewportHeight = 25f\n" +
                    "            ).apply {\n" +
                    "\t\t\t\tpath(\n" +
                    "    \t\t\t\tfill = SolidColor(Color(0xFF000000)),\n" +
                    "    \t\t\t\tfillAlpha = 1.0f,\n" +
                    "    \t\t\t\tstroke = null,\n" +
                    "    \t\t\t\tstrokeAlpha = 1.0f,\n" +
                    "    \t\t\t\tstrokeLineWidth = 1.0f,\n" +
                    "    \t\t\t\tstrokeLineCap = StrokeCap.Butt,\n" +
                    "    \t\t\t\tstrokeLineJoin = StrokeJoin.Miter,\n" +
                    "    \t\t\t\tstrokeLineMiter = 1.0f,\n" +
                    "    \t\t\t\tpathFillType = PathFillType.NonZero\n" +
                    "\t\t\t\t) {\n" +
                    "\t\t\t\t\tmoveTo(20.3325f, 4.79985f)\n" +
                    "\t\t\t\t\tcurveTo(22.5836f, 6.6341f, 22.5003f, 10.8029f, 20.4159f, 12.8873f)\n" +
                    "\t\t\t\t\tcurveTo(18.5816f, 15.2219f, 14.246f, 19.6408f, 12.6619f, 20.8914f)\n" +
                    "\t\t\t\t\tcurveTo(12.245f, 21.2249f, 11.7447f, 21.2249f, 11.3279f, 20.8914f)\n" +
                    "\t\t\t\t\tcurveTo(9.8271f, 19.6408f, 5.4915f, 15.2219f, 3.6572f, 12.8873f)\n" +
                    "\t\t\t\t\tcurveTo(1.4895f, 10.7196f, 1.4061f, 6.5507f, 3.6572f, 4.7999f)\n" +
                    "\t\t\t\t\tcurveTo(5.825f, 2.6321f, 10.0772f, 2.7988f, 11.9949f, 5.717f)\n" +
                    "\t\t\t\t\tcurveTo(13.9125f, 2.8822f, 18.0813f, 2.6321f, 20.3325f, 4.7999f)\n" +
                    "\t\t\t\t\tclose()\n" +
                    "\t\t\t\t\tmoveTo(19.7488f, 5.46686f)\n" +
                    "\t\t\t\t\tcurveTo(17.7478f, 3.4658f, 13.7458f, 3.7993f, 12.3284f, 6.7175f)\n" +
                    "\t\t\t\t\tcurveTo(12.245f, 6.9676f, 11.9949f, 7.051f, 11.7447f, 6.8842f)\n" +
                    "\t\t\t\t\tcurveTo(11.6614f, 6.8842f, 11.578f, 6.8009f, 11.578f, 6.7175f)\n" +
                    "\t\t\t\t\tcurveTo(10.244f, 3.7993f, 6.1585f, 3.3824f, 4.1575f, 5.4669f)\n" +
                    "\t\t\t\t\tcurveTo(2.3232f, 6.8842f, 2.4066f, 10.5528f, 4.2409f, 12.3871f)\n" +
                    "\t\t\t\t\tcurveTo(6.0752f, 14.6382f, 10.3273f, 19.0572f, 11.8281f, 20.2244f)\n" +
                    "\t\t\t\t\tcurveTo(11.9115f, 20.3078f, 11.9949f, 20.3078f, 12.0782f, 20.2244f)\n" +
                    "\t\t\t\t\tcurveTo(13.579f, 19.0572f, 17.8312f, 14.6382f, 19.6655f, 12.3037f)\n" +
                    "\t\t\t\t\tcurveTo(21.5831f, 10.5528f, 21.6665f, 6.8842f, 19.7488f, 5.4669f)\n" +
                    "\t\t\t\t\tclose()\n" +
                    "}\n" +
                    "}.build()\n" +
                    "return _Lovelist!!\n" +
                    "\t\t}\n" +
                    "\n"
        )
    }

    PluginUI(
        content = content,
        onChooseFile = {},
        onCopy = {}
    )
}