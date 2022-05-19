package uk.co.droidinactu.bddtestdox

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.thoughtworks.qdox.JavaProjectBuilder
import com.thoughtworks.qdox.model.JavaClass
import com.thoughtworks.qdox.model.JavaMethod
import com.thoughtworks.qdox.model.JavaSource
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.function.Consumer

class FileProcessor {

    fun processTestClassFiles(
        myState: ProjectSettingsState, currentProject: Project, srcRoot: VirtualFile
    ) {
        var isLicensed = CheckLicense.isLicensed()
        isLicensed = true
        val gen = MultiplexingGenerator()
        val fp = FileProcessor()
        val outputPath = (currentProject.basePath
                + File.separator
                + (if (myState.prependProjectName) currentProject.name + "_" else "")
                + myState.outputFilename)
        gen.addGenerator(ConsoleGenerator())
        if (myState.outputToHtml) {
            try {
                BufferedWriter(FileWriter("$outputPath.html")).use { writer ->
                    gen.addGenerator(HtmlDocumentGenerator(writer))
                    gen.startProject(currentProject.name)
                    fp.generateFile(isLicensed, gen, srcRoot)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            gen.removeGenerator(HtmlDocumentGenerator::class.java)
        }
        if (myState.outputToMarkdown) {
            try {
                BufferedWriter(FileWriter("$outputPath.md")).use { writer ->
                    gen.addGenerator(MarkdownDocumentGenerator(writer))
                    gen.startProject(currentProject.name)
                    fp.generateFile(isLicensed, gen, srcRoot)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun generateFile(isLicensed: Boolean, gen: DocumentGenerator, testSrcDir: VirtualFile) {
        /* Use qdoc for junit tests */
        val builder = JavaProjectBuilder()
        builder.addSourceTree(File(testSrcDir.path))
        builder.sources.forEach(Consumer { src: JavaSource -> processJUnitTestFile(gen, src) })
        if (isLicensed) {
            try {
                val files: MutableList<Path> = mutableListOf()
                Files.walk(Paths.get(testSrcDir.path))
                    .filter { path: Path? -> Files.isRegularFile(path) }
                    .filter { f: Path -> f.toString().endsWith(".feature") }
                    .forEach { e: Path -> files.add(e) }
                files.forEach(
                    Consumer { f: Path ->
                        try {
                            processGherkinTestFile(gen, f)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private fun processGherkinTestFile(gen: DocumentGenerator, f: Path) {
        val nameFormatter = NameFormatter()
        var lines = Files.lines(f)
        lines
            .filter { l -> l.trim { it <= ' ' }.lowercase(Locale.getDefault()).startsWith("feature:") }
            .forEach { l -> gen.startFeature(nameFormatter.prettifyFeatureName(l)) }
        lines = Files.lines(f)
        lines
            .filter { l -> l.trim { it <= ' ' }.lowercase(Locale.getDefault()).startsWith("scenario:") }
            .forEach { l -> gen.onTest(nameFormatter.prettifyScenarioName(l)) }
        lines.close()
    }

    private fun processJUnitTestFile(gen: DocumentGenerator, src: JavaSource) {
        src.classes.stream()
            .filter { cls: JavaClass -> UnitTestDetector.isTestClass(cls.name) }
            .forEach { classObj: JavaClass -> processTestClass(classObj, gen) }
    }

    private fun processTestClass(classObj: JavaClass, gen: DocumentGenerator) {
        val nameFormatter = NameFormatter()
        val prettyName = nameFormatter.prettifyTestClass(classObj.name)
        gen.startClass(prettyName)
        classObj.methods.stream()
            .filter { mthdName: JavaMethod -> UnitTestDetector.isTestMethod(mthdName.name) }
            .forEach { mthdName: JavaMethod -> gen.onTest(nameFormatter.prettifyTestMethod(mthdName.name)) }
        gen.endClass(prettyName)
    }
}