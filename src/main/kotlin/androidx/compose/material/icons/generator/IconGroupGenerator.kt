package androidx.compose.material.icons.generator

import com.squareup.kotlinpoet.*
import java.io.File

class IconGroupGenerator(
    private val groupPackage: String,
    private val groupName: String
) {

    fun createFileSpec(previousGroupObject: ClassName?): Pair<FileSpec.Builder, ClassName> {
        return createGroup(previousGroupObject)
    }

    private fun createGroup(previousGroupObject: ClassName?): Pair<FileSpec.Builder, ClassName> {
        // if there is not previous group object, is suppose that this group is the root group
        // if is the root group, it should not prefix with "Group"
        val objectName = "${groupName}${if(previousGroupObject != null) "Group" else ""}"

        val objectSpec = TypeSpec.objectBuilder(objectName).build()
        val createdObjectClassName = ClassName(groupPackage, objectName)

        return FileSpec.builder(
            packageName = groupPackage,
            fileName = "__$groupName"
        )
            .addType(objectSpec)
            .apply {
                if(previousGroupObject != null) {
                    addProperty(PropertySpec.builder(groupName, createdObjectClassName)
                        .receiver(previousGroupObject)
                        .getter(FunSpec.getterBuilder().addStatement("return $objectName").build())
                        .build()
                    )
                }
            } to createdObjectClassName
    }
}