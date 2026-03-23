#!/bin/bash

# ========================================
# Kotlin Multiplatform 跨平台构建脚本
# ========================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# 配置
VERSION="v1.0.0"
PROJECT_NAME="juejin-app"
BUILD_DIR="build/releases"
UPLOAD_TO_RELEASE=false

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    Kotlin Multiplatform 跨平台构建${NC}"
echo -e "${BLUE}========================================${NC}"

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --version)
            VERSION="$2"
            shift 2
            ;;
        --upload)
            UPLOAD_TO_RELEASE=true
            shift
            ;;
        --help)
            echo -e "${BLUE}用法:${NC} $0 [选项]"
            echo ""
            echo -e "${YELLOW}选项:${NC}"
            echo -e "  --version VERSION    指定版本号 (默认: v1.0.0)"
            echo -e "  --upload             构建完成后上传到Release"
            echo -e "  --help               显示此帮助信息"
            exit 0
            ;;
        *)
            echo -e "${RED}✗ 未知参数: $1${NC}"
            exit 1
            ;;
    esac
done

echo -e "${CYAN}构建配置:${NC}"
echo -e "  版本: ${VERSION}"
echo -e "  项目: ${PROJECT_NAME}"
echo -e "  上传: ${UPLOAD_TO_RELEASE}"
echo ""

# 创建构建目录
mkdir -p "$BUILD_DIR"

# 检查环境
echo -e "${YELLOW}检查构建环境...${NC}"

# 检查 Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}✗ Java 未安装${NC}"
    exit 1
fi

# 检查 Gradle
if ! command -v gradle &> /dev/null && [ ! -f "./gradlew" ]; then
    echo -e "${RED}✗ Gradle 未安装且未找到 gradlew${NC}"
    exit 1
fi

# 检查 Android SDK (仅Android构建)
if [ -d "$ANDROID_HOME" ] || [ -d "$ANDROID_SDK_ROOT" ]; then
    echo -e "${GREEN}✓ Android SDK 环境就绪${NC}"
else
    echo -e "${YELLOW}⚠️  Android SDK 环境未配置，将跳过Android构建${NC}"
fi

echo -e "${GREEN}✓ 环境检查完成${NC}"
echo ""

# 开始构建
echo -e "${YELLOW}开始跨平台构建...${NC}"
echo ""

# 1. Android 构建
echo -e "${CYAN}=== Android 构建 ===${NC}"
if [ -d "$ANDROID_HOME" ] || [ -d "$ANDROID_SDK_ROOT" ]; then
    echo -e "${YELLOW}构建 Android Debug APK...${NC}"
    if ./gradlew :composeApp:assembleDebug --no-daemon; then
        echo -e "${GREEN}✓ Android Debug APK 构建成功${NC}"
        
        # 复制APK到发布目录
        cp composeApp/build/outputs/apk/debug/composeApp-debug.apk "$BUILD_DIR/${PROJECT_NAME}-${VERSION}-android-debug.apk"
        
        echo -e "${YELLOW}构建 Android Release APK...${NC}"
        if ./gradlew :composeApp:assembleRelease --no-daemon; then
            echo -e "${GREEN}✓ Android Release APK 构建成功${NC}"
            
            # 查找实际的 APK 文件
            APK_FILE=$(find composeApp/build/outputs/apk/release -name "*.apk" | head -1)
            if [ -n "$APK_FILE" ]; then
                cp "$APK_FILE" "$BUILD_DIR/${PROJECT_NAME}-${VERSION}-android-release.apk"
                echo -e "${GREEN}✓ APK 已复制: $(basename "$APK_FILE")${NC}"
            else
                echo -e "${YELLOW}⚠️  未找到 Release APK 文件${NC}"
            fi
            
            echo -e "${YELLOW}构建 Android AAB...${NC}"
            if ./gradlew :composeApp:bundleRelease --no-daemon; then
                echo -e "${GREEN}✓ Android AAB 构建成功${NC}"
                cp composeApp/build/outputs/bundle/release/composeApp-release.aab "$BUILD_DIR/${PROJECT_NAME}-${VERSION}-android-release.aab"
            else
                echo -e "${RED}✗ Android AAB 构建失败${NC}"
            fi
        else
            echo -e "${RED}✗ Android Release 构建失败${NC}"
        fi
    else
        echo -e "${RED}✗ Android Debug 构建失败${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  跳过 Android 构建 (环境未配置)${NC}"
fi
echo ""

# 2. Desktop 构建
echo -e "${CYAN}=== Desktop 构建 ===${NC}"
echo -e "${YELLOW}构建 macOS DMG...${NC}"
if ./gradlew :composeApp:packageDmg --no-daemon; then
    echo -e "${GREEN}✓ macOS DMG 构建成功${NC}"
    cp composeApp/build/compose/binaries/main/dmg/*.dmg "$BUILD_DIR/"
else
    echo -e "${RED}✗ macOS DMG 构建失败${NC}"
fi

# 检测操作系统并构建对应平台
OS=$(uname -s)
case $OS in
    Linux*)
        echo -e "${YELLOW}构建 Linux DEB...${NC}"
        if ./gradlew :composeApp:packageDeb --no-daemon; then
            echo -e "${GREEN}✓ Linux DEB 构建成功${NC}"
            cp composeApp/build/compose/binaries/main/deb/*.deb "$BUILD_DIR/"
        else
            echo -e "${RED}✗ Linux DEB 构建失败${NC}"
        fi
        ;;
    Darwin*)
        echo -e "${YELLOW}macOS 环境，跳过 Linux 构建${NC}"
        ;;
    CYGWIN*|MINGW*|MSYS*)
        echo -e "${YELLOW}构建 Windows EXE...${NC}"
        if ./gradlew :composeApp:packageExe --no-daemon; then
            echo -e "${GREEN}✓ Windows EXE 构建成功${NC}"
            cp composeApp/build/compose/binaries/main/exe/*.exe "$BUILD_DIR/"
        else
            echo -e "${RED}✗ Windows EXE 构建失败${NC}"
        fi
        ;;
esac
echo ""

# 3. Web 构建 (可选)
echo -e "${CYAN}=== Web 构建 ===${NC}"
echo -e "${YELLOW}构建 Web WASM...${NC}"
if ./gradlew :composeApp:wasmJsBrowserDistribution --no-daemon; then
    echo -e "${GREEN}✓ Web WASM 构建成功${NC}"
    
    # 打包Web文件
    WEB_PACKAGE="$BUILD_DIR/${PROJECT_NAME}-${VERSION}-web.zip"
    cd composeApp/build/dist/wasmJs/productionExecutable
    zip -r "../../../../../$WEB_PACKAGE" .
    cd - > /dev/null
else
    echo -e "${YELLOW}⚠️  Web WASM 构建失败 (项目可能未配置 wasmJs 目标)${NC}"
    echo -e "${YELLOW}   如需 Web 支持，请在 build.gradle.kts 中配置 wasmJs 目标${NC}"
fi
echo ""

# 4. iOS 构建 (仅在macOS上)
if [[ "$OS" == "Darwin" ]]; then
    echo -e "${CYAN}=== iOS 构建 ===${NC}"
    echo -e "${YELLOW}构建 iOS Framework...${NC}"
    if ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode --no-daemon; then
        echo -e "${GREEN}✓ iOS Framework 构建成功${NC}"
        
        # 创建Xcode项目说明
        cat > "$BUILD_DIR/ios-build-instructions.txt" << EOF
iOS 构建说明
============

1. 使用 Xcode 打开项目:
   open iosApp/iosApp.xcodeproj

2. 选择目标设备或模拟器

3. 构建和运行:
   - Product → Run (Cmd+R)
   - Product → Archive (Cmd+Shift+B) 用于发布

4. 发布到 App Store:
   - Product → Archive
   - Distribute App

注意: iOS 构建需要 Xcode 环境和开发者证书
EOF
    else
        echo -e "${YELLOW}⚠️  iOS Framework 构建失败 (需要 Xcode 环境配置)${NC}"
        echo -e "${YELLOW}   请确保已安装 Xcode 并配置了 iOS 开发环境${NC}"
        
        # 创建iOS构建说明文件
        cat > "$BUILD_DIR/ios-build-instructions.txt" << EOF
iOS 构建说明
============

当前构建失败，可能的原因:
1. 未安装 Xcode
2. 未配置 iOS 开发环境
3. 未设置开发者证书

解决方法:
1. 安装 Xcode: xcode-select --install
2. 打开 Xcode 并接受许可协议
3. 配置开发者账号和证书

手动构建步骤:
1. 使用 Android Studio 打开项目
2. 选择 iOS 目标
3. 运行构建
EOF
    fi
else
    echo -e "${YELLOW}⚠️  跳过 iOS 构建 (需要macOS环境)${NC}"
fi
echo ""

# 5. 构建总结
echo -e "${CYAN}=== 构建总结 ===${NC}"
echo -e "${YELLOW}生成的文件:${NC}"

if [ -d "$BUILD_DIR" ]; then
    ls -la "$BUILD_DIR" | while read line; do
        if [[ "$line" != "total"* && "$line" != "drwx"* ]]; then
            filename=$(echo "$line" | awk '{print $9}')
            size=$(echo "$line" | awk '{print $5}')
            echo -e "  ${GREEN}✓${NC} $filename ($size bytes)"
        fi
    done
else
    echo -e "${RED}✗ 没有找到构建文件${NC}"
fi
echo ""

# 6. 上传到 Release (如果启用)
if [ "$UPLOAD_TO_RELEASE" = true ]; then
    echo -e "${CYAN}=== 上传到 Release ===${NC}"
    
    # 查找所有构建文件
    for file in "$BUILD_DIR"/*; do
        if [ -f "$file" ]; then
            echo -e "${YELLOW}上传: $(basename "$file")${NC}"
            if [ -f "$SCRIPT_DIR/upload-release.sh" ]; then
                "$SCRIPT_DIR/upload-release.sh" "$file" "$VERSION"
            else
                echo -e "${RED}✗ upload-release.sh 不存在${NC}"
            fi
        fi
    done
fi

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}✓ 跨平台构建完成！${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${CYAN}构建产物位置:${NC}"
echo -e "  目录: $BUILD_DIR"
echo -e "  版本: $VERSION"
echo ""
echo -e "${CYAN}下一步操作:${NC}"
echo -e "  1. 测试各平台构建产物"
echo -e "  2. 上传到Release (使用 --upload 参数)"
echo -e "  3. 创建GitHub Release"
