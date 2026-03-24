#!/bin/bash

# ========================================
# 快速构建和上传脚本
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
PLATFORM="android"  # android, desktop, all

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    快速构建和上传${NC}"
echo -e "${BLUE}========================================${NC}"

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --version)
            VERSION="$2"
            shift 2
            ;;
        --platform)
            PLATFORM="$2"
            shift 2
            ;;
        --help)
            echo -e "${BLUE}用法:${NC} $0 [选项]"
            echo ""
            echo -e "${YELLOW}选项:${NC}"
            echo -e "  --version VERSION    指定版本号 (默认: v1.0.0)"
            echo -e "  --platform PLATFORM  平台: android|desktop|all (默认: android)"
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
echo -e "  平台: ${PLATFORM}"
echo ""

# 创建构建目录
mkdir -p "$BUILD_DIR"

# 获取脚本目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 构建函数
build_android() {
    echo -e "${CYAN}=== 构建 Android ===${NC}"
    if [ -f "$SCRIPT_DIR/android-build.sh" ]; then
        "$SCRIPT_DIR/android-build.sh" --version "$VERSION" --type all
    else
        echo -e "${RED}✗ android-build.sh 不存在${NC}"
        return 1
    fi
}

build_desktop() {
    echo -e "${CYAN}=== 构建 Desktop ===${NC}"
    if [ -f "$SCRIPT_DIR/desktop-build.sh" ]; then
        "$SCRIPT_DIR/desktop-build.sh" --version "$VERSION" --platforms current
    else
        echo -e "${RED}✗ desktop-build.sh 不存在${NC}"
        return 1
    fi
}

# 执行构建
case $PLATFORM in
    android)
        build_android
        ;;
    desktop)
        build_desktop
        ;;
    all)
        build_android
        build_desktop
        ;;
    *)
        echo -e "${RED}✗ 无效的平台: $PLATFORM${NC}"
        exit 1
        ;;
esac

echo ""

# 检查构建产物
echo -e "${CYAN}=== 检查构建产物 ===${NC}"
BUILD_FILES=()

# 查找所有构建文件
if [ -d "$BUILD_DIR" ]; then
    while IFS= read -r -d '' file; do
        BUILD_FILES+=("$file")
        filename=$(basename "$file")
        size=$(stat -f%z "$file" 2>/dev/null || stat -c%s "$file" 2>/dev/null)
        size_mb=$(( size / 1024 / 1024 ))
        echo -e "  ${GREEN}✓${NC} $filename (${size_mb} MB)"
    done < <(find "$BUILD_DIR" -type f \( -name "*.apk" -o -name "*.aab" -o -name "*.dmg" -o -name "*.deb" -o -name "*.exe" \) -print0)
fi

if [ ${#BUILD_FILES[@]} -eq 0 ]; then
    echo -e "${RED}✗ 未找到构建文件${NC}"
    exit 1
fi

echo ""

# 询问是否上传
read -p "是否上传到 GitHub Release? [y/N]: " upload_choice

if [[ "$upload_choice" =~ ^[Yy]$ ]]; then
    echo -e "${CYAN}=== 上传到 GitHub Release ===${NC}"
    
    # 检查 upload-release.sh
    if [ ! -f "$SCRIPT_DIR/upload-release.sh" ]; then
        echo -e "${RED}✗ upload-release.sh 不存在${NC}"
        exit 1
    fi
    
    # 上传每个文件
    for file in "${BUILD_FILES[@]}"; do
        echo -e "${YELLOW}上传: $(basename "$file")${NC}"
        if "$SCRIPT_DIR/upload-release.sh" "$file" --tag "$VERSION"; then
            echo -e "${GREEN}✓ 上传成功${NC}"
        else
            echo -e "${RED}✗ 上传失败${NC}"
        fi
        echo ""
    done
    
    echo -e "${GREEN}✓ 所有文件上传完成${NC}"
else
    echo -e "${YELLOW}跳过上传${NC}"
fi

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}✓ 操作完成！${NC}"
echo -e "${BLUE}========================================${NC}"
