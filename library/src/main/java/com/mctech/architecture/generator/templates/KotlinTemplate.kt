package com.mctech.architecture.generator.templates

import com.mctech.architecture.generator.class_contract.Package
import com.mctech.architecture.generator.generator.printPackage
import com.mctech.architecture.generator.generator.writeFile
import com.mctech.architecture.generator.path.ModuleFilePath
import com.mctech.architecture.generator.settings.baseProjectPath
import com.mctech.architecture.generator.settings.featurePackage
import java.io.PrintWriter

/**
 * @author MAYCON CARDOSO on 2019-11-30.
 */
abstract class KotlinTemplate(
    protected open var moduleFilePath: ModuleFilePath,
    protected open var doesTheClassHaveBody : Boolean = true
) : Template() {
    abstract val folder: String
    abstract val className: String

    override fun getPath(): String {
        if (folder.isEmpty()) {
            return baseProjectPath + moduleFilePath.getPath() + className + ".kt"
        }
        return baseProjectPath + moduleFilePath.getPath() + featurePackage() + getFolderSegment() + className + ".kt"
    }

    fun getPackage() = Package(
        if (folder.isEmpty())
            moduleFilePath.packageValue.getPackageLine()
        else
            moduleFilePath.packageValue.getPackageLine() + "." + featurePackage() + getFolderPackageSegment()
    )

    private fun getFolderSegment(): String {
        return if (folder.isBlank()) "/" else "/${folder}/"
    }

    private fun getFolderPackageSegment(): String {
        return if (folder.isBlank()) "" else ".$folder"
    }

    final override fun generate() = writeFile(this) { output ->
        // Write Package
        generatePackage(output)

        // Write empty class
        output.println("")

        // Call the child class to generate its imports.
        generateImports(output)

        // Call the child class to generate its class name.
        generateClassName(output)

        // Call the child class to generate its body.
        generateClassBody(output)

        // If the class has body so we finish it.
        if(doesTheClassHaveBody){
            output.println("}")
        }
    }

    protected open fun generatePackage(output: PrintWriter) {
        output.printPackage(getPackage().getPackageLine().removeSuffix("."))
    }

    abstract fun generateImports(output: PrintWriter)

    abstract fun generateClassName(output: PrintWriter)

    abstract fun generateClassBody(output: PrintWriter)
}