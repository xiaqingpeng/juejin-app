#!/bin/bash

# ========================================
# Android 专用构建脚本
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
BUILD_DIR="build/releases/android"
BUILD_TYPE="debug"  # debug, release, all
UPLOAD_TO_RELEASE=false

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    Android 专用构建${NC}"
echo -e "${BLUE}========================================${NC}"

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --version)
            VERSION="$2"
            shift 2
            ;;
        --type)
            BUILD_TYPE="$2"
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
            echo -e "  --type TYPE          构建类型: debug|release|all (默认: debug)"
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
echo -e "  类型: ${BUILD_TYPE}"
echo -e "  项目: ${PROJECT_NAME}"
echo -e "  上传: ${UPLOAD_TO_RELEASE}"
echo ""

# 创建构建目录
mkdir -p "$BUILD_DIR"

# 检查 Android 环境
echo -e "${YELLOW}检查 Android 环境...${NC}"

if [ ! -d "$ANDROID_HOME" ] && [ ! -d "$ANDROID_SDK_ROOT" ]; then
    echo -e "${RED}✗ Android SDK 未配置${NC}"
    echo -e "${YELLOW}请设置 ANDROID_HOME 或 ANDROID_SDK_ROOT 环境变量${NC}"
    exit 1
fi

# 检查 Gradle
if [ ! -f "./gradlew" ]; then
    echo -e "${RED}✗ 未找到 gradlew${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Android 环境检查通过${NC}"
echo ""

# 清理之前的构建
echo -e "${YELLOW}清理之前的构建...${NC}"
./gradlew clean --no-daemon

# 根据构建类型执行构建
case $BUILD_TYPE in
    debug)
        echo -e "${CYAN}=== 构建 Debug APK ===${NC}"
        echo -e "${YELLOW}正在构建 Debug APK...${NC}"
        
        if ./gradlew :composeApp:assembleDebug --no-daemon; then
            echo -e "${GREEN}✓ Debug APK 构建成功${NC}"
            
            # 复制APK
            APK_SOURCE="composeApp/build/outputs/apk/debug/composeApp-debug.apk"
            APK_TARGET="$BUILD_DIR/${PROJECT_NAME}-${VERSION}-android-debug.apk"
            
            if [ -f "$APK_SOURCE" ]; then
                cp "$APK_SOURCE" "$APK_TARGET"
                echo -e "${GREEN}✓ APK 已复制到: $APK_TARGET${NC}"
                
                # 显示APK信息
                APK_SIZE=$(stat -f%z "$APK_TARGET" 2>/dev/null || stat -c%s "$APK_TARGET" 2>/dev/null)
                echo -e "${CYAN}APK 大小: $(( APK_SIZE / 1024 / 1024 )) MB${NC}"
            else
                echo -e "${RED}✗ 未找到生成的APK文件${NC}"
                exit 1
            fi
        else
            echo -e "${RED}✗ Debug APK 构建失败${NC}"
            exit 1
        fi
        ;;
        
    release)
        echo -e "${CYAN}=== 构建 Release APK ===${NC}"
        echo -e "${YELLOW}正在构建 Release APK...${NC}"
        
        if ./gradlew :composeApp:assembleRelease --no-daemon; then
            echo -e "${GREEN}✓ Release APK 构建成功${NC}"
            
            # 复制APK
            APK_SOURCE="composeApp/build/outputs/apk/release/composeApp-release.apk"
            APK_TARGET="$BUILD_DIR/${PROJECT_NAME}-${VERSION}-android-release.apk"
            
            if [ -f "$APK_SOURCE" ]; then
                cp "$APK_SOURCE" "$APK_TARGET"
                echo -e "${GREEN}✓ APK 已复制到: $APK_TARGET${NC}"
                
                APK_SIZE=$(stat -f%z "$APK_TARGET" 2>/dev/null || stat -c%s "$APK_TARGET" 2>/dev/null)
                echo -e "${CYAN}APK 大小: $(( APK_SIZE / 1024 / 1024 )) MB${NC}"
            else
                echo -e "${RED}✗ 未找到生成的APK文件${NC}"
                exit 1
            fi
        else
            echo -e "${RED}✗ Release APK 构建失败${NC}"
            exit 1
        fi
        ;;
        
    all)
        echo -e "${CYAN}=== 构建所有类型 ===${NC}"
        
        # Debug APK
        echo -e "${YELLOW}构建 Debug APK...${NC}"
        if ./gradlew :composeApp:assembleDebug --no-daemon; then
            echo -e "${GREEN}✓ Debug APK 构建成功${NC}"
            cp "composeApp/build/outputs/apk/debug/composeApp-debug.apk" \
               "$BUILD_DIR/${PROJECT_NAME}-${VERSION}-android-debug.apk"
        else
            echo -e "${RED}✗ Debug APK 构建失败${NC}"
        fi
        
        # Release APK
        echo -e "${YELLOW}构建 Release APK...${NC}"
        if ./gradlew :composeApp:assembleRelease --no-daemon; then
            echo -e "${GREEN}✓ Release APK 构建成功${NC}"
            cp "composeApp/build/outputs/apk/release/composeApp-release.apk" \
               "$BUILD_DIR/${PROJECT_NAME}-${VERSION}-android-release.apk"
        else
            echo -e "${RED}✗ Release APK 构建失败${NC}"
        fi
        
        # Release AAB
        echo -e "${YELLOW}构建 Release AAB...${NC}"
        if ./gradlew :composeApp:bundleRelease --no-daemon; then
            echo -e "${GREEN}✓ Release AAB 构建成功${NC}"
            cp "composeApp/build/outputs/bundle/release/composeApp-release.aab" \
               "$BUILD_DIR/${PROJECT_NAME}-${VERSION}-android-release.aab"
        else
            echo -e "${RED}✗ Release AAB 构建失败${NC}"
        fi
        ;;
        
    *)
        echo -e "${RED}✗ 无效的构建类型: $BUILD_TYPE${NC}"
        echo -e "${YELLOW}支持的类型: debug, release, all${NC}"
        exit 1
        ;;
esac

echo ""

# 显示构建结果
echo -e "${CYAN}=== 构建结果 ===${NC}"
echo -e "${YELLOW}生成的文件:${NC}"

for file in "$BUILD_DIR"/*; do
    if [ -f "$file" ]; then
        filename=$(basename "$file")
        size=$(stat -f%z "$file" 2>/dev/null || stat -c%s "$file" 2>/dev/null)
        size_mb=$(( size / 1024 / 1024 ))
        echo -e "  ${GREEN}✓${NC} $filename (${size_mb} MB)"
    fi
done

echo ""

# 安装到设备 (可选)
if [ "$BUILD_TYPE" = "debug" ] && command -v adb &> /dev/null; then
    echo -e "${CYAN}=== 设备安装 ===${NC}"
    
    # 检查连接的设备
    DEVICE_COUNT=$(adb devices | grep -c "device$")
    if [ $DEVICE_COUNT -gt 0 ]; then
        echo -e "${YELLOW}发现 $DEVICE_COUNT 个设备${NC}"
        read -p "是否安装到设备? [y/N]: " install_choice
        
        if [[ "$install_choice" =~ ^[Yy]$ ]]; then
            APK_FILE="$BUILD_DIR/${PROJECT_NAME}-${VERSION}-android-debug.apk"
            if [ -f "$APK_FILE" ]; then
                echo -e "${YELLOW}正在安装到设备...${NC}"
                if adb install -r "$APK_FILE"; then
                    echo -e "${GREEN}✓ 安装成功${NC}"
                else
                    echo -e "${RED}✗ 安装失败${NC}"
                fi
            fi
        fi
    else
        echo -e "${YELLOW}未发现连接的设备${NC}"
    fi
fi

echo ""

# 上传到 Release (如果启用)
if [ "$UPLOAD_TO_RELEASE" = true ]; then
    echo -e "${CYAN}=== 上传到 Release ===${NC}"
    
    for file in "$BUILD_DIR"/*; do
        if [ -f "$file" ]; then
            echo -e "${YELLOW}上传: $(basename "$file")${NC}"
            if [ -f "./upload-release.sh" ]; then
                ./upload-release.sh "$file" "$VERSION"
            else
                echo -e "${RED}✗ upload-release.sh 不存在${NC}"
            fi
        fi
    done
fi

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}✓ Android 构建完成！${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${CYAN}构建产物位置:${NC}"
echo -e "  目录: $BUILD_DIR"
echo -e "  版本: $VERSION"
echo ""
echo -e "${CYAN}测试命令:${NC}"
echo -e "  安装: adb install -r $BUILD_DIR/${PROJECT_NAME}-${VERSION}-android-debug.apk"
echo -e "  日志: adb logcat -s \"juejin-app\""
