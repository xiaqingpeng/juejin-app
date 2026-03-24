#!/bin/bash

# ========================================
# iOS 专用构建脚本
# ========================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# 默认配置
VERSION="v1.0.0"
BUILD_TYPE="release"
AUTO_UPLOAD=false
PROJECT_NAME="juejin-app"

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
RELEASE_DIR="$PROJECT_ROOT/build/releases/ios"

# 显示帮助信息
show_help() {
    echo -e "${BLUE}iOS 构建脚本${NC}"
    echo ""
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  --version VERSION    版本号 (默认: v1.0.0)"
    echo "  --type TYPE          构建类型: debug|release (默认: release)"
    echo "  --upload             构建后自动上传到 GitHub Release"
    echo "  -h, --help           显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 --version v1.0.0 --type release"
    echo "  $0 --version v1.0.0 --upload"
}

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
            AUTO_UPLOAD=true
            shift
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            echo -e "${RED}✗ 未知选项: $1${NC}"
            show_help
            exit 1
            ;;
    esac
done

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    iOS 构建脚本${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${CYAN}配置信息:${NC}"
echo -e "  版本号: ${VERSION}"
echo -e "  构建类型: ${BUILD_TYPE}"
echo -e "  自动上传: ${AUTO_UPLOAD}"
echo -e "  项目根目录: ${PROJECT_ROOT}"
echo ""

# 检查是否在 macOS 上运行
if [[ "$OSTYPE" != "darwin"* ]]; then
    echo -e "${RED}✗ 错误: iOS 构建只能在 macOS 上运行${NC}"
    exit 1
fi

# 检查 Xcode 是否安装
if ! command -v xcodebuild &> /dev/null; then
    echo -e "${RED}✗ 错误: 未找到 xcodebuild，请安装 Xcode${NC}"
    exit 1
fi

# 创建发布目录
mkdir -p "$RELEASE_DIR"

cd "$PROJECT_ROOT"

# 清理之前的构建
echo -e "${YELLOW}清理之前的构建...${NC}"
./gradlew clean

# 构建 iOS Framework
echo -e "${YELLOW}构建 iOS Framework...${NC}"
if [[ "$BUILD_TYPE" == "debug" ]]; then
    ./gradlew :composeApp:linkDebugFrameworkIosArm64
    FRAMEWORK_PATH="composeApp/build/bin/iosArm64/debugFramework/ComposeApp.framework"
else
    ./gradlew :composeApp:linkReleaseFrameworkIosArm64
    FRAMEWORK_PATH="composeApp/build/bin/iosArm64/releaseFramework/ComposeApp.framework"
fi

if [ $? -ne 0 ]; then
    echo -e "${RED}✗ Framework 构建失败${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Framework 构建成功${NC}"

# 构建 iOS App
echo -e "${YELLOW}构建 iOS App...${NC}"
cd "$PROJECT_ROOT/iosApp"

if [[ "$BUILD_TYPE" == "debug" ]]; then
    xcodebuild -scheme iosApp \
        -configuration Debug \
        -sdk iphoneos \
        -archivePath "$RELEASE_DIR/iosApp.xcarchive" \
        archive
else
    xcodebuild -scheme iosApp \
        -configuration Release \
        -sdk iphoneos \
        -archivePath "$RELEASE_DIR/iosApp.xcarchive" \
        archive
fi

if [ $? -ne 0 ]; then
    echo -e "${RED}✗ iOS App 构建失败${NC}"
    exit 1
fi

echo -e "${GREEN}✓ iOS App 构建成功${NC}"

# 导出 IPA
echo -e "${YELLOW}导出 IPA...${NC}"

# 创建 ExportOptions.plist
cat > "$RELEASE_DIR/ExportOptions.plist" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>method</key>
    <string>development</string>
    <key>teamID</key>
    <string>YOUR_TEAM_ID</string>
</dict>
</plist>
EOF

xcodebuild -exportArchive \
    -archivePath "$RELEASE_DIR/iosApp.xcarchive" \
    -exportPath "$RELEASE_DIR" \
    -exportOptionsPlist "$RELEASE_DIR/ExportOptions.plist"

if [ $? -ne 0 ]; then
    echo -e "${YELLOW}⚠ IPA 导出失败（可能需要配置签名）${NC}"
    echo -e "${CYAN}提示: 请在 Xcode 中配置签名后手动导出 IPA${NC}"
fi

# 查找生成的文件
IPA_FILE=$(find "$RELEASE_DIR" -name "*.ipa" -type f | head -n 1)

if [ -n "$IPA_FILE" ]; then
    # 重命名文件
    NEW_NAME="$RELEASE_DIR/${PROJECT_NAME}-${VERSION}-ios-${BUILD_TYPE}.ipa"
    mv "$IPA_FILE" "$NEW_NAME"
    IPA_FILE="$NEW_NAME"
    
    FILE_SIZE=$(du -h "$IPA_FILE" | cut -f1)
    echo -e "${GREEN}✓ IPA 文件: $IPA_FILE ($FILE_SIZE)${NC}"
else
    echo -e "${YELLOW}⚠ 未找到 IPA 文件${NC}"
fi

# 压缩 Framework
echo -e "${YELLOW}压缩 Framework...${NC}"
cd "$PROJECT_ROOT"
FRAMEWORK_ZIP="$RELEASE_DIR/${PROJECT_NAME}-${VERSION}-ios-framework.zip"
zip -r "$FRAMEWORK_ZIP" "$FRAMEWORK_PATH"

if [ -f "$FRAMEWORK_ZIP" ]; then
    FILE_SIZE=$(du -h "$FRAMEWORK_ZIP" | cut -f1)
    echo -e "${GREEN}✓ Framework 文件: $FRAMEWORK_ZIP ($FILE_SIZE)${NC}"
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}    iOS 构建完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${CYAN}构建产物:${NC}"
if [ -n "$IPA_FILE" ]; then
    echo -e "  • IPA: $IPA_FILE"
fi
echo -e "  • Framework: $FRAMEWORK_ZIP"
echo ""

# 自动上传
if [ "$AUTO_UPLOAD" = true ]; then
    echo -e "${YELLOW}开始上传到 GitHub Release...${NC}"
    
    FILES_TO_UPLOAD=()
    [ -n "$IPA_FILE" ] && FILES_TO_UPLOAD+=("$IPA_FILE")
    FILES_TO_UPLOAD+=("$FRAMEWORK_ZIP")
    
    for file in "${FILES_TO_UPLOAD[@]}"; do
        if [ -f "$file" ]; then
            echo -e "${CYAN}上传: $(basename "$file")${NC}"
            gh release upload "$VERSION" "$file" --clobber
            if [ $? -eq 0 ]; then
                echo -e "${GREEN}✓ 上传成功${NC}"
            else
                echo -e "${RED}✗ 上传失败${NC}"
            fi
        fi
    done
fi

echo -e "${GREEN}✓ 完成！${NC}"
