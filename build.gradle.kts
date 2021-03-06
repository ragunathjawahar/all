import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jlleitschuh.gradle.ktlint.KtlintExtension

buildscript {
  apply(from = ".git-hooks/manage.gradle")
}

plugins {
  kotlin("jvm") version "1.6.10"
  `java-library`

  id("io.gitlab.arturbosch.detekt") version "1.19.0"
  id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
  id("jacoco")
}

group = "xyz.ragunath"
version = "0.1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val junitVersion = "5.8.2"

dependencies {
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))

  testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

  testImplementation("com.approvaltests:approvaltests:12.4.1")

  testImplementation("com.google.truth:truth:1.1.3")

  testImplementation("io.arrow-kt:arrow-core-jvm:1.0.1")
}

tasks.test {
  useJUnitPlatform()
}

detekt {
  buildUponDefaultConfig = true
  allRules = false
  baseline = file("$projectDir/detekt/baseline.xml")
}

tasks.withType<Detekt>().configureEach {
  reports {
    xml.required.set(true)
    sarif.required.set(true)
  }
}

tasks.withType<Detekt>().configureEach {
  jvmTarget = "1.8"
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
  jvmTarget = "1.8"
}

configure<KtlintExtension> {
  verbose.set(true)
  outputToConsole.set(true)
  outputColorName.set("RED")
  baseline.set(file("$projectDir/ktlint/baseline.xml"))
}
