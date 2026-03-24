#!/bin/bash

# ========================================
# Linux 专用构建脚本
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

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
RELEASE_DIR="$PROJECT_ROOT/build/releases/linux"

# 显示帮助信息
show_help() {
    echo -e "${BLUE}Linux 构建脚本${NC}"
    echo ""
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  --version VERSION    版本号 (默认: v1.0.0)"
    echo "  --upload             构建后自动上传到 GitHub Release"
    echo "  -h, --help           显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 --version v1.0.0"
    echo "  $0 --version v1.0.0 --upload"
}

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --version)
            VERSION="$2"
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
echo -e "${BLUE}    Linux 构建脚本${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${CYAN}配置信息:${NC}"
echo -e "  版本号: ${VERSION}"
echo -e "  自动上传: ${AUTO_UPLOAD}"
echo -e "  项目根目录: ${PROJECT_ROOT}"
echo ""

# 创建发布目录
mkdir -p "$RELEASE_DIR"

cd "$PROJECT_ROOT"

# 清理之前的构建
echo -e "${YELLOW}清理之前的构建...${NC}"
./gradlew clean

# 构建 Linux DEB
echo -e "${YELLOW}构建 Linux DEB 安装包...${NC}"
./gradlew :composeApp:packageDeb

if [ $? -ne 0 ]; then
    echo -e "${RED}✗ Linux DEB 构建失败${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Linux DEB 构建成功${NC}"

# 查找生成的 DEB 文件
DEB_FILE=$(find "$PROJECT_ROOT/composeApp/build/compose/binaries/main/deb" -name "*.deb" -type f | head -n 1)

if [ -z "$DEB_FILE" ]; then
    echo -e "${RED}✗ 未找到 DEB 文件${NC}"
    exit 1
fi

# 复制并重命名文件
NEW_NAME="$RELEASE_DIR/${PROJECT_NAME}-${VERSION}-linux.deb"
cp "$DEB_FILE" "$NEW_NAME"
DEB_FILE="$NEW_NAME"

FILE_SIZE=$(du -h "$DEB_FILE" | cut -f1)

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}    Linux 构建完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${CYAN}构建产物:${NC}"
echo -e "  • DEB: $DEB_FILE ($FILE_SIZE)"
echo ""

# 自动上传
if [ "$AUTO_UPLOAD" = true ]; then
    echo -e "${YELLOW}开始上传到 GitHub Release...${NC}"
    
    if [ -f "$DEB_FILE" ]; then
        echo -e "${CYAN}上传: $(basename "$DEB_FILE")${NC}"
        gh release upload "$VERSION" "$DEB_FILE" --clobber
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ 上传成功${NC}"
        else
            echo -e "${RED}✗ 上传失败${NC}"
        fi
    fi
fi

echo -e "${GREEN}✓ 完成！${NC}"
