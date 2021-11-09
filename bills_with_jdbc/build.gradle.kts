plugins {
    java
    application
}


application {
    mainClassName = "Application"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.flywaydb:flyway-core:8.0.1")
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.22")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.22")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.intellij:annotations:12.0")
    implementation("org.postgresql:postgresql:42.2.9")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}