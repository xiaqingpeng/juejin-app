#!/bin/bash

# 模块创建脚本
# 用法: ./scripts/create-module.sh <module-type> <module-name>
# 示例: ./scripts/create-module.sh feature notification

set -e

# 检查参数
if [ $# -ne 2 ]; then
    echo "用法: $0 <module-type> <module-name>"
    echo ""
    echo "module-type 可选值:"
    echo "  - core      (核心模块，如 shared/core/xxx)"
    echo "  - feature   (功能模块，如 shared/features/xxx)"
    echo "  - ui        (UI 模块，如 shared/ui/xxx)"
    echo "  - domain    (领域模块，如 shared/domain/xxx)"
    echo ""
    echo "示例:"
    echo "  $0 feature notification"
    echo "  $0 core analytics"
    echo "  $0 ui icons"
    exit 1
fi

MODULE_TYPE=$1
MODULE_NAME=$2

# 根据类型确定路径
case $MODULE_TYPE in
    core)
        MODULE_PATH="shared/core/$MODULE_NAME"
        PACKAGE_PATH="com/example/juejin/core/$MODULE_NAME"
        NAMESPACE="com.example.juejin.core.$MODULE_NAME"
        BASE_NAME="Core${MODULE_NAME^}"
        ;;
    feature)
        MODULE_PATH="shared/features/$MODULE_NAME"
        PACKAGE_PATH="com/example/juejin/feature/$MODULE_NAME"
        NAMESPACE="com.example.juejin.feature.$MODULE_NAME"
        BASE_NAME="Feature${MODULE_NAME^}"
        ;;
    ui)
        MODULE_PATH="shared/ui/$MODULE_NAME"
        PACKAGE_PATH="com/example/juejin/ui/$MODULE_NAME"
        NAMESPACE="com.example.juejin.ui.$MODULE_NAME"
        BASE_NAME="UI${MODULE_NAME^}"
        ;;
    domain)
        MODULE_PATH="shared/domain/$MODULE_NAME"
        PACKAGE_PATH="com/example/juejin/domain/$MODULE_NAME"
        NAMESPACE="com.example.juejin.domain.$MODULE_NAME"
        BASE_NAME="Domain${MODULE_NAME^}"
        ;;
    *)
        echo "错误: 未知的模块类型 '$MODULE_TYPE'"
        exit 1
        ;;
esac

echo "🚀 创建模块: $MODULE_PATH"

# 创建目录结构
mkdir -p "$MODULE_PATH/src/commonMain/kotlin/$PACKAGE_PATH"
mkdir -p "$MODULE_PATH/src/androidMain/kotlin/$PACKAGE_PATH"
mkdir -p "$MODULE_PATH/src/iosMain/kotlin/$PACKAGE_PATH"
mkdir -p "$MODULE_PATH/src/desktopMain/kotlin/$PACKAGE_PATH"

# 创建 build.gradle.kts
cat > "$MODULE_PATH/build.gradle.kts" << EOF
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "$BASE_NAME"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:common"))
            implementation(libs.kotlinx.coroutines.core)
        }
        
        androidMain.dependencies {
            // Android 特定依赖
        }
        
        iosMain.dependencies {
            // iOS 特定依赖
        }
        
        val desktopMain by getting {
            dependencies {
                // Desktop 特定依赖
            }
        }
    }
}

android {
    namespace = "$NAMESPACE"
    compileSdk = 35
    
    defaultConfig {
        minSdk = 24
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
EOF

# 创建 README.md
cat > "$MODULE_PATH/README.md" << EOF
# $MODULE_NAME

## 概述

TODO: 添加模块描述

## 功能

- TODO: 列出主要功能

## 使用

\`\`\`kotlin
// TODO: 添加使用示例
\`\`\`

## 依赖

- shared:core:common

## 架构

TODO: 描述模块架构

## 测试

\`\`\`bash
./gradlew :$MODULE_PATH:test
\`\`\`
EOF

# 创建示例文件
cat > "$MODULE_PATH/src/commonMain/kotlin/$PACKAGE_PATH/${MODULE_NAME^}.kt" << EOF
package ${NAMESPACE//.//}

/**
 * ${MODULE_NAME^} 模块入口
 */
object ${MODULE_NAME^} {
    fun initialize() {
        // TODO: 初始化逻辑
    }
}
EOF

echo "✅ 模块创建完成: $MODULE_PATH"
echo ""
echo "下一步:"
echo "1. 在 settings.gradle.kts 中添加: include(\":$MODULE_PATH\")"
echo "2. 编辑 $MODULE_PATH/build.gradle.kts 添加所需依赖"
echo "3. 编辑 $MODULE_PATH/README.md 完善文档"
echo "4. 开始开发你的模块！"
