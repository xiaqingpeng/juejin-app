plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "UITheme"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:common"))
            implementation(project(":shared:core:storage"))
            
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
        }
        
        androidMain.dependencies {
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
    namespace = "com.example.juejin.ui.theme"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
