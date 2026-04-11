plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "CoreStorage"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:common"))
        }
        
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation("androidx.datastore:datastore-preferences:1.1.1")
        }
        
        iosMain.dependencies {
        }
        
        val desktopMain by getting {
            dependencies {
            }
        }
    }
}

android {
    namespace = "com.example.juejin.core.storage"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
