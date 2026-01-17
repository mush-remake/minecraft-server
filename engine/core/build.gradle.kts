plugins {
    java
    `maven-publish`
}

group = "com.minecraft.core"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://libraries.minecraft.net/")
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("redis.clients:jedis:2.9.0")
    compileOnly("com.mojang:authlib:1.5.21")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

sourceSets {
    val main by getting {
        java.srcDir("src/main/java")
        resources.srcDir("src/main/resources")
    }
    val test by getting {
        java.srcDir("src/test/java")
        resources.srcDir("src/test/resources")
    }
}

tasks.test {
    useJUnitPlatform()
}
