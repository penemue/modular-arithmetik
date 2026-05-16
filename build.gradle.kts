import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import java.util.Calendar

plugins {
    kotlin("jvm") version "2.3.21"
    id("com.github.hierynomus.license") version "0.16.1"
    id("me.champeau.jmh") version "0.7.3"
}

group = "com.github.penemue"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

tasks.test {
    testLogging {
        showStandardStreams = true
        events("failed")
        exceptionFormat = TestExceptionFormat.SHORT
    }
}

jmh {
    jmhVersion.set("1.37")
    jvmArgsPrepend.set(listOf("-Xmx1g", "-Xms1g"))
    duplicateClassesStrategy.set(DuplicatesStrategy.WARN)
    forceGC.set(true)
}

license {
    header = rootProject.file("copyright.ftl")
    strictCheck = true
    include("**/*.kt")
    include("**/*.java")
    mapping("kt", "JAVADOC_STYLE")
}

(license as ExtensionAware).extra.apply {
    set("inceptionYear", 2016)
    set("year", Calendar.getInstance().get(Calendar.YEAR))
    set("owner", "Vyacheslav Lukianov")
    set("ownerURL", "https://github.com/penemue")
}

tasks.wrapper {
    gradleVersion = "9.5.1"
}

defaultTasks("clean", "build")
