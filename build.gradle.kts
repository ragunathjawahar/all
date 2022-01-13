plugins {
  kotlin("jvm") version "1.6.10"
}

group = "xyz.ragunath"
version = "0.1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val junitVersion = "5.8.2"

dependencies {
  implementation(kotlin("stdlib"))

  testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

  testImplementation("com.approvaltests:approvaltests:12.4.1")

  testImplementation("com.google.truth:truth:1.1.3")
}

tasks.test {
  useJUnitPlatform()
}
