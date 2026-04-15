import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "juejin.lite.generated.resources"
    generateResClass = always
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    // iOS 平台配置
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "JuejinLite"
            isStatic = true
        }
    }
    
    // Desktop (JVM) 平台配置
    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
            implementation("androidx.core:core-splashscreen:1.0.1")
            implementation("com.google.android.material:material:1.9.0")
        }
        
        commonMain.dependencies {
            // 共享模块依赖
            implementation(project(":shared:core:common"))
            implementation(project(":shared:core:storage"))
            implementation(project(":shared:core:network"))
            implementation(project(":shared:domain"))
            implementation(project(":shared:ui:theme"))
            implementation(project(":shared:ui:components"))
            
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kermit)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
        }
        
        // iOS 平台依赖
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        
        // Desktop (JVM) 平台依赖
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.java)
        }
        
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.example.juejin.lite"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.juejin.lite"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0-lite"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

// Desktop 应用配置
compose.desktop {
    application {
        mainClass = "com.example.juejin.lite.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Juejin Lite"
            packageVersion = "1.0.0"
            
            macOS {
                bundleID = "com.example.juejin.lite"
            }
            windows {
                menuGroup = "Juejin"
                upgradeUuid = "juejin-lite-upgrade-uuid"
            }
            linux {
                packageName = "juejin-lite"
            }
        }
    }
}
