plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "Network"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:common"))
            
            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            
            // Serialization
            implementation(libs.kotlinx.serialization.json)
            
            // Logging
            implementation(libs.kermit)
        }
        
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }
        
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        
        val desktopMain by getting {
            dependencies {
                implementation(libs.ktor.client.java)
            }
        }
    }
}

android {
    namespace = "com.example.juejin.core.network"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
