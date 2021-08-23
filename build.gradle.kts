plugins {
    scala
    application
}

group = "io.casually-blue"
version = "1.0-SNAPSHOT"

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("HTTPMain")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}


dependencies {
    compileOnly(group="org.scala-lang", name="scala-library", version="2.13.6")

    testImplementation(group="org.junit.jupiter", name="junit-jupiter-api", version="5.7.2")
    testRuntimeOnly(group="org.junit.jupiter", name="junit-jupiter-engine", version="5.7.2")

    implementation(group="com.lihaoyi", name="scalatags_2.13", version="0.8.2")

    implementation(group="io.github.casually-blue", name="cucm-11", version="1.6")
    implementation(group="com.sun.xml.ws", name="jaxws-rt", version="2.3.3")

    implementation(group="com.microsoft.graph", name="microsoft-graph", version="5.0.0")
    implementation(group="com.azure", name="azure-identity", version="1.3.5")

    implementation(group="org.slf4j", name="slf4j-api", version="1.7.32")
    implementation(group="org.slf4j", name="slf4j-simple", version="1.7.32")

    implementation(group="com.typesafe.akka", name="akka-actor_2.13", version="2.6.15")
    implementation(group="com.typesafe.akka", name="akka-actor-typed_2.13", version="2.6.15")
    implementation(group="com.typesafe.akka", name="akka-http_2.13", version="10.2.6")
    implementation(group="com.typesafe.akka", name="akka-http-caching_2.13", version="10.2.6")
    implementation(group="com.typesafe.akka", name="akka-http-spray-json_2.13", version="10.2.6")
    implementation(group="com.typesafe.akka", name="akka-http-xml_2.13", version="10.2.6")
    implementation(group="com.typesafe.akka", name="akka-stream_2.13", version="2.6.15")

    implementation(group="com.fasterxml.jackson.core", name="jackson-core", version="2.8.8")
    implementation(group="com.fasterxml.jackson.core", name="jackson-annotations", version="2.8.8")
    implementation(group="com.fasterxml.jackson.core", name="jackson-databind", version="2.8.8")
}

repositories {
    mavenCentral()
}