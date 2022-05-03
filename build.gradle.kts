import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.springframework.boot") version "2.5.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    id("com.google.protobuf") version "0.8.15" // 플러그인 추가 (gRPC를 활용하기 위한 메시지)
}

group = "io.wisoft"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
    // protobuf 에서 제공해주는 기본 메시지를 자바로 컴파일된 것들
    api("com.google.protobuf:protobuf-java-util:3.17.0")

    // 코틀린으로 컴파일한것들을 사용하기 위한 것
    api("io.grpc:grpc-kotlin-stub:1.0.0")

    // gRPC를 사용하기 위한 기본 라이브러리
    api("io.grpc:grpc-protobuf:1.39.0")
    api("io.grpc:grpc-netty-shaded:1.39.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.1")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

//    implementation("com.linecorp.armeria:armeria:1.9.2")
//    implementation("com.linecorp.armeria:armeria-grpc:1.9.2")
}

protobuf {
    generatedFilesBaseDir = "$projectDir/build/generated/source" // 컴파일된 저장되는 위치
    protoc { // proto 컴파일
        artifact = "com.google.protobuf:protoc:3.17.0"
    }
    plugins {
        id("grpc") {
            // java로 컴파일
            artifact = "io.grpc:protoc-gen-grpc-java:1.39.0"
        }
        id("grpckt") {
            // kotlin로 컴파일
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.0.0:jdk7@jar"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }

            it.generateDescriptorSet = true
            it.descriptorSetOptions.includeSourceInfo = true
            it.descriptorSetOptions.includeImports = true
            it.descriptorSetOptions.path = "$buildDir/resources/META-INF/armeria/grpc/service-name.dsc"
        }
    }
}

sourceSets {
    main {
        java.srcDir("build/generated/source/main/grpckt")
        java.srcDir("build/generated/source/main/grpc")
        java.srcDir("build/generated/source/main/java")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
