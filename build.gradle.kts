plugins {
    java
    scala
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")

    implementation("io.github.casually-blue:cucm-11:1.6")
    implementation("com.sun.xml.ws:jaxws-rt:2.3.3")

    implementation("com.microsoft.graph:microsoft-graph:5.0.0")
    implementation("com.azure:azure-identity:1.3.5")

    implementation(group="com.typesafe.akka", name="akka-actor_2.13", version="2.6.15")
    implementation(group="com.typesafe.akka", name="akka-actor-typed_2.13", version="2.6.15")
    implementation(group="com.typesafe.akka", name="akka-http_2.13", version="10.2.6")
    implementation(group="com.typesafe.akka", name="akka-http-spray-json_2.13", version="10.2.6")
    implementation(group="com.typesafe.akka", name="akka-http-xml_2.13", version="10.2.6")
    implementation(group="com.typesafe.akka", name="akka-stream_2.13", version="2.6.15")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

dependencies {
    compileOnly("org.scala-lang:scala-library:2.13.6")
}