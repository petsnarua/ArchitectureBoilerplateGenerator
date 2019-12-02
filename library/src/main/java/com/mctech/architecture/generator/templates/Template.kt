package com.mctech.architecture.generator.templates

import com.mctech.architecture.generator.class_contract.Package
import com.mctech.architecture.generator.generator.FileGenerator
import com.mctech.architecture.generator.generator.printPackage
import com.mctech.architecture.generator.generator.writeFile
import com.mctech.architecture.generator.path.FilePath
import com.mctech.architecture.generator.path.ModuleFilePath
import com.mctech.architecture.generator.settings.featureSegment
import java.io.PrintWriter

/**
 * @author MAYCON CARDOSO on 2019-11-30.
 */
abstract class Template(private val moduleFilePath: ModuleFilePath) : FilePath, FileGenerator {
    abstract val folder: String
    abstract val className: String
    protected abstract fun generate(output: PrintWriter)

    final override fun getPath(): String {
        if(folder.isEmpty()){
            return moduleFilePath.getPath() + className + ".kt"
        }
        return moduleFilePath.getPath() + featureSegment() + getFolderSegment() + className + ".kt"
    }

    fun getPackage() = Package(
        if(folder.isEmpty())
            moduleFilePath.packageValue.getPackageLine()
        else
            moduleFilePath.packageValue.getPackageLine() + "." + featureSegment() + getFolderPackageSegment()
    )

    private fun getFolderSegment(): String {
        return if(folder.isBlank())  "/" else "/${folder}/"
    }

    private fun getFolderPackageSegment(): String {
        return if(folder.isBlank()) "" else ".$folder"
    }

    final override fun generate() = writeFile(this) { output ->
        // Write Package
        output.printPackage(getPackage().getPackageLine().removeSuffix("."))

        // Write empty class
        output.println("")

        // Call the child class to generate it body.
        generate(output)
    }
}