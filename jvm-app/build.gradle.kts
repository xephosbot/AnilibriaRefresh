import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.desktop.application.tasks.AbstractJPackageTask

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

dependencies {
    implementation(projects.sharedUi)
    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutines.swing)
    implementation(libs.koin.compose)
    implementation(libs.slf4j)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Anilibria Refresh"
            packageVersion = "1.0.0"
            modules("java.instrument", "java.management", "jdk.unsupported")

            macOS {
                bundleID = "com.xbot.anilibriarefresh"
                dockName = "Anilibria"
                iconFile.set(project.file("src/main/resources/icons/AppIcon.icns"))
                jvmArgs("-Dapple.awt.application.appearance=system")
            }
            windows {
                upgradeUuid = "7f0323f1-8f05-44b4-9884-33dbc7aff7af"
                iconFile.set(project.file("src/main/resources/icons/AppIcon.ico"))
            }
            linux {
                iconFile.set(project.file("src/main/resources/icons/AppIcon.png"))
            }
        }
    }
}

val generateAssetsCar by tasks.registering(Exec::class) {
    group = "build"
    description = "Generate Assets.car for macOS"

    val scriptPath = rootProject.file("actool")
    val iconPath = project.file("src/main/resources/icons/AppIcon.icon")
    val outputPath = project.file("src/main/resources/icons")

    commandLine(
        "bash",
        scriptPath.absolutePath,
        iconPath.absolutePath,
        outputPath.absolutePath,
        "AppIcon"
    )

    inputs.dir(iconPath)
    outputs.file(outputPath.resolve("Assets.car"))

    onlyIf { System.getProperty("os.name").contains("Mac") }
}

if (System.getProperty("os.name").contains("Mac")) {
    project.afterEvaluate {
        listOf(
            "createDistributable",
            "createReleaseDistributable",
        ).forEach { name ->
            tasks.named<AbstractJPackageTask>(name) {
                configureMacOSPackageTask(
                    packageName = packageName,
                    iconFile = iconFile,
                    destinationDir = destinationDir,
                    replaceIconFile = true,
                )
            }
        }
    }
}

fun Task.configureMacOSPackageTask(
    packageName: Property<String>,
    iconFile: RegularFileProperty,
    destinationDir: DirectoryProperty,
    replaceIconFile: Boolean = false,
) {
    val assetsCarFile = project.file("src/main/resources/icons/Assets.car")
    inputs.file(assetsCarFile)

    doLast {
        val appContentDir = destinationDir.get().asFile
            .resolve("${packageName.get()}.app")
            .resolve("Contents")
        val bundleResourceDir = appContentDir.resolve("Resources")
        val infoListFile = appContentDir.resolve("Info.plist")

        val infoListFileContent = infoListFile.readText()
        if (!infoListFileContent.contains("CFBundleIconName")) {
            infoListFile.writeText(
                infoListFileContent.replace(
                    "<key>CFBundleIconFile</key>",
                    """
                        <key>CFBundleIconName</key>
                        <string>${iconFile.get().asFile.nameWithoutExtension}</string>
                        <key>CFBundleIconFile</key>
                    """.trimIndent()
                )
            )
        }
        assetsCarFile.copyTo(
            target = bundleResourceDir.resolve(assetsCarFile.name),
            overwrite = true
        )
        if (replaceIconFile) {
            val oldIconFile = bundleResourceDir.resolve("${packageName}.icns")
            oldIconFile.renameTo(bundleResourceDir.resolve(iconFile.get().asFile.name))
        }
    }
}
