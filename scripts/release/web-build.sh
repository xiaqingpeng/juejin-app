#!/bin/bash

# ========================================
# Web (Wasm) 专用构建脚本
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
RELEASE_DIR="$PROJECT_ROOT/build/releases/web"

# 显示帮助信息
show_help() {
    echo -e "${BLUE}Web (Wasm) 构建脚本${NC}"
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
    echo ""
    echo "注意:"
    echo "  Web 平台需要在 build.gradle.kts 中配置 wasmJs 目标"
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
echo -e "${BLUE}    Web (Wasm) 构建脚本${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${CYAN}配置信息:${NC}"
echo -e "  版本号: ${VERSION}"
echo -e "  自动上传: ${AUTO_UPLOAD}"
echo -e "  项目根目录: ${PROJECT_ROOT}"
echo ""

# 检查是否配置了 wasmJs 目标
if ! grep -q "wasmJs" "$PROJECT_ROOT/composeApp/build.gradle.kts"; then
    echo -e "${YELLOW}⚠ 警告: 未在 build.gradle.kts 中找到 wasmJs 配置${NC}"
    echo -e "${CYAN}提示: 需要在 kotlin { } 块中添加 wasmJs() 目标${NC}"
    echo ""
    echo -e "${CYAN}示例配置:${NC}"
    echo -e "  kotlin {"
    echo -e "      wasmJs {"
    echo -e "          browser {"
    echo -e "              commonWebpackConfig {"
    echo -e "                  outputFileName = \"composeApp.js\""
    echo -e "              }"
    echo -e "          }"
    echo -e "          binaries.executable()"
    echo -e "      }"
    echo -e "  }"
    echo ""
    read -p "是否继续尝试构建? (y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# 创建发布目录
mkdir -p "$RELEASE_DIR"

cd "$PROJECT_ROOT"

# 清理之前的构建
echo -e "${YELLOW}清理之前的构建...${NC}"
./gradlew clean

# 构建 Web (Wasm)
echo -e "${YELLOW}构建 Web (Wasm) 应用...${NC}"
./gradlew :composeApp:wasmJsBrowserDistribution

if [ $? -ne 0 ]; then
    echo -e "${RED}✗ Web 构建失败${NC}"
    echo -e "${YELLOW}提示: 请确保已在 build.gradle.kts 中配置 wasmJs 目标${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Web 构建成功${NC}"

# 查找生成的 Web 文件
WEB_DIR="$PROJECT_ROOT/composeApp/build/dist/wasmJs/productionExecutable"

if [ ! -d "$WEB_DIR" ]; then
    echo -e "${RED}✗ 未找到 Web 构建目录${NC}"
    exit 1
fi

# 压缩 Web 文件
echo -e "${YELLOW}压缩 Web 文件...${NC}"
cd "$WEB_DIR"
ZIP_FILE="$RELEASE_DIR/${PROJECT_NAME}-${VERSION}-web.zip"
zip -r "$ZIP_FILE" .

if [ ! -f "$ZIP_FILE" ]; then
    echo -e "${RED}✗ 压缩失败${NC}"
    exit 1
fi

FILE_SIZE=$(du -h "$ZIP_FILE" | cut -f1)

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}    Web 构建完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${CYAN}构建产物:${NC}"
echo -e "  • Web ZIP: $ZIP_FILE ($FILE_SIZE)"
echo ""
echo -e "${CYAN}部署说明:${NC}"
echo -e "  1. 解压 ZIP 文件到 Web 服务器目录"
echo -e "  2. 确保服务器支持 WASM MIME 类型"
echo -e "  3. 访问 index.html 即可运行应用"
echo ""

# 自动上传
if [ "$AUTO_UPLOAD" = true ]; then
    echo -e "${YELLOW}开始上传到 GitHub Release...${NC}"
    
    if [ -f "$ZIP_FILE" ]; then
        echo -e "${CYAN}上传: $(basename "$ZIP_FILE")${NC}"
        gh release upload "$VERSION" "$ZIP_FILE" --clobber
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ 上传成功${NC}"
        else
            echo -e "${RED}✗ 上传失败${NC}"
        fi
    fi
fi

echo -e "${GREEN}✓ 完成！${NC}"
