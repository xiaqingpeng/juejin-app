#!/bin/bash

# ========================================
# 全平台构建脚本 (Android, iOS, Desktop, Linux, Windows, Web)
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
AUTO_UPLOAD=false
PROJECT_NAME="juejin-app"
PLATFORMS="all"

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

# 显示帮助信息
show_help() {
    echo -e "${BLUE}全平台构建脚本${NC}"
    echo ""
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  --version VERSION    版本号 (默认: v1.0.0)"
    echo "  --platforms LIST     要构建的平台，逗号分隔 (默认: all)"
    echo "                       可选: android,desktop,ios,linux,windows,web,all"
    echo "  --upload             构建后自动上传到 GitHub Release"
    echo "  -h, --help           显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 --version v1.0.0"
    echo "  $0 --version v1.0.0 --platforms android,desktop,ios"
    echo "  $0 --version v1.0.0 --platforms all --upload"
}

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --version)
            VERSION="$2"
            shift 2
            ;;
        --platforms)
            PLATFORMS="$2"
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
echo -e "${BLUE}    全平台构建脚本${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${CYAN}配置信息:${NC}"
echo -e "  版本号: ${VERSION}"
echo -e "  构建平台: ${PLATFORMS}"
echo -e "  自动上传: ${AUTO_UPLOAD}"
echo -e "  项目根目录: ${PROJECT_ROOT}"
echo ""

# 解析平台列表
BUILD_ANDROID=false
BUILD_DESKTOP=false
BUILD_IOS=false
BUILD_LINUX=false
BUILD_WINDOWS=false
BUILD_WEB=false

if [[ "$PLATFORMS" == "all" ]]; then
    BUILD_ANDROID=true
    BUILD_DESKTOP=true
    BUILD_IOS=true
    BUILD_LINUX=true
    BUILD_WINDOWS=true
    BUILD_WEB=true
else
    IFS=',' read -ra PLATFORM_ARRAY <<< "$PLATFORMS"
    for platform in "${PLATFORM_ARRAY[@]}"; do
        case $platform in
            android) BUILD_ANDROID=true ;;
            desktop) BUILD_DESKTOP=true ;;
            ios) BUILD_IOS=true ;;
            linux) BUILD_LINUX=true ;;
            windows) BUILD_WINDOWS=true ;;
            web) BUILD_WEB=true ;;
            *)
                echo -e "${RED}✗ 未知平台: $platform${NC}"
                exit 1
                ;;
        esac
    done
fi

# 显示将要构建的平台
echo -e "${CYAN}将要构建的平台:${NC}"
[[ "$BUILD_ANDROID" == true ]] && echo -e "  • Android"
[[ "$BUILD_DESKTOP" == true ]] && echo -e "  • Desktop (macOS)"
[[ "$BUILD_IOS" == true ]] && echo -e "  • iOS"
[[ "$BUILD_LINUX" == true ]] && echo -e "  • Linux"
[[ "$BUILD_WINDOWS" == true ]] && echo -e "  • Windows"
[[ "$BUILD_WEB" == true ]] && echo -e "  • Web (Wasm)"
echo ""

# 构建计数器
TOTAL_PLATFORMS=0
CURRENT_PLATFORM=0
[[ "$BUILD_ANDROID" == true ]] && ((TOTAL_PLATFORMS++))
[[ "$BUILD_DESKTOP" == true ]] && ((TOTAL_PLATFORMS++))
[[ "$BUILD_IOS" == true ]] && ((TOTAL_PLATFORMS++))
[[ "$BUILD_LINUX" == true ]] && ((TOTAL_PLATFORMS++))
[[ "$BUILD_WINDOWS" == true ]] && ((TOTAL_PLATFORMS++))
[[ "$BUILD_WEB" == true ]] && ((TOTAL_PLATFORMS++))

cd "$PROJECT_ROOT"

# 1. 构建 Android
if [[ "$BUILD_ANDROID" == true ]]; then
    ((CURRENT_PLATFORM++))
    echo -e "${CYAN}[$CURRENT_PLATFORM/$TOTAL_PLATFORMS] 构建 Android 平台...${NC}"
    if [ -f "$SCRIPT_DIR/android-build.sh" ]; then
        "$SCRIPT_DIR/android-build.sh" --version "$VERSION" --type all
        if [ $? -ne 0 ]; then
            echo -e "${RED}✗ Android 构建失败${NC}"
            exit 1
        fi
    else
        echo -e "${RED}✗ 错误: android-build.sh 不存在${NC}"
        exit 1
    fi
    echo ""
fi

# 2. 构建 Desktop (macOS)
if [[ "$BUILD_DESKTOP" == true ]]; then
    ((CURRENT_PLATFORM++))
    echo -e "${CYAN}[$CURRENT_PLATFORM/$TOTAL_PLATFORMS] 构建 Desktop 平台...${NC}"
    if [ -f "$SCRIPT_DIR/desktop-build.sh" ]; then
        "$SCRIPT_DIR/desktop-build.sh" --version "$VERSION" --platform current
        if [ $? -ne 0 ]; then
            echo -e "${RED}✗ Desktop 构建失败${NC}"
            exit 1
        fi
    else
        echo -e "${RED}✗ 错误: desktop-build.sh 不存在${NC}"
        exit 1
    fi
    echo ""
fi

# 3. 构建 iOS
if [[ "$BUILD_IOS" == true ]]; then
    ((CURRENT_PLATFORM++))
    echo -e "${CYAN}[$CURRENT_PLATFORM/$TOTAL_PLATFORMS] 构建 iOS 平台...${NC}"
    if [[ "$OSTYPE" != "darwin"* ]]; then
        echo -e "${YELLOW}⚠ 跳过 iOS 构建（需要 macOS）${NC}"
    elif [ -f "$SCRIPT_DIR/ios-build.sh" ]; then
        "$SCRIPT_DIR/ios-build.sh" --version "$VERSION" --type release
        if [ $? -ne 0 ]; then
            echo -e "${YELLOW}⚠ iOS 构建失败（可能需要配置签名）${NC}"
        fi
    else
        echo -e "${RED}✗ 错误: ios-build.sh 不存在${NC}"
    fi
    echo ""
fi

# 4. 构建 Linux
if [[ "$BUILD_LINUX" == true ]]; then
    ((CURRENT_PLATFORM++))
    echo -e "${CYAN}[$CURRENT_PLATFORM/$TOTAL_PLATFORMS] 构建 Linux 平台...${NC}"
    if [ -f "$SCRIPT_DIR/linux-build.sh" ]; then
        "$SCRIPT_DIR/linux-build.sh" --version "$VERSION"
        if [ $? -ne 0 ]; then
            echo -e "${YELLOW}⚠ Linux 构建失败${NC}"
        fi
    else
        echo -e "${RED}✗ 错误: linux-build.sh 不存在${NC}"
    fi
    echo ""
fi

# 5. 构建 Windows
if [[ "$BUILD_WINDOWS" == true ]]; then
    ((CURRENT_PLATFORM++))
    echo -e "${CYAN}[$CURRENT_PLATFORM/$TOTAL_PLATFORMS] 构建 Windows 平台...${NC}"
    if [ -f "$SCRIPT_DIR/windows-build.sh" ]; then
        "$SCRIPT_DIR/windows-build.sh" --version "$VERSION"
        if [ $? -ne 0 ]; then
            echo -e "${YELLOW}⚠ Windows 构建失败${NC}"
        fi
    else
        echo -e "${RED}✗ 错误: windows-build.sh 不存在${NC}"
    fi
    echo ""
fi

# 6. 构建 Web
if [[ "$BUILD_WEB" == true ]]; then
    ((CURRENT_PLATFORM++))
    echo -e "${CYAN}[$CURRENT_PLATFORM/$TOTAL_PLATFORMS] 构建 Web 平台...${NC}"
    if [ -f "$SCRIPT_DIR/web-build.sh" ]; then
        "$SCRIPT_DIR/web-build.sh" --version "$VERSION"
        if [ $? -ne 0 ]; then
            echo -e "${YELLOW}⚠ Web 构建失败（可能需要配置 wasmJs）${NC}"
        fi
    else
        echo -e "${RED}✗ 错误: web-build.sh 不存在${NC}"
    fi
    echo ""
fi

# 构建总结
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}    全平台构建完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 显示所有构建产物
echo -e "${CYAN}构建产物:${NC}"
RELEASE_BASE="$PROJECT_ROOT/build/releases"

for platform_dir in "$RELEASE_BASE"/*; do
    if [ -d "$platform_dir" ]; then
        platform_name=$(basename "$platform_dir")
        echo -e "${YELLOW}$platform_name:${NC}"
        for file in "$platform_dir"/*; do
            if [ -f "$file" ]; then
                filename=$(basename "$file")
                filesize=$(du -h "$file" | cut -f1)
                echo -e "  • $filename ($filesize)"
            fi
        done
    fi
done

echo ""

# 自动上传
if [ "$AUTO_UPLOAD" = true ]; then
    echo -e "${YELLOW}开始上传到 GitHub Release...${NC}"
    
    # 收集所有文件
    FILES_TO_UPLOAD=()
    for platform_dir in "$RELEASE_BASE"/*; do
        if [ -d "$platform_dir" ]; then
            for file in "$platform_dir"/*; do
                if [ -f "$file" ] && [[ ! "$file" =~ \.txt$ ]]; then
                    FILES_TO_UPLOAD+=("$file")
                fi
            done
        fi
    done
    
    # 上传文件
    for file in "${FILES_TO_UPLOAD[@]}"; do
        echo -e "${CYAN}上传: $(basename "$file")${NC}"
        gh release upload "$VERSION" "$file" --clobber
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ 上传成功${NC}"
        else
            echo -e "${RED}✗ 上传失败${NC}"
        fi
    done
    
    echo ""
    echo -e "${GREEN}✓ 所有文件上传完成！${NC}"
fi

echo -e "${GREEN}✓ 完成！${NC}"
