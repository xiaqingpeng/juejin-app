#!/bin/bash

# ========================================
# Desktop 专用构建脚本
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
BUILD_DIR="build/releases/desktop"
PLATFORMS="all"  # all, current, windows, macos, linux
UPLOAD_TO_RELEASE=false

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    Desktop 专用构建${NC}"
echo -e "${BLUE}========================================${NC}"

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
            UPLOAD_TO_RELEASE=true
            shift
            ;;
        --help)
            echo -e "${BLUE}用法:${NC} $0 [选项]"
            echo ""
            echo -e "${YELLOW}选项:${NC}"
            echo -e "  --version VERSION    指定版本号 (默认: v1.0.0)"
            echo -e "  --platforms PLATFORMS 平台: all|current|windows|macos|linux (默认: all)"
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
echo -e "  平台: ${PLATFORMS}"
echo -e "  项目: ${PROJECT_NAME}"
echo -e "  上传: ${UPLOAD_TO_RELEASE}"
echo ""

# 创建构建目录
mkdir -p "$BUILD_DIR"

# 检测当前操作系统
OS=$(uname -s)
echo -e "${YELLOW}检测操作系统: $OS${NC}"

# 检查 Gradle
if [ ! -f "./gradlew" ]; then
    echo -e "${RED}✗ 未找到 gradlew${NC}"
    exit 1
fi

echo -e "${GREEN}✓ 环境检查通过${NC}"
echo ""

# 清理之前的构建
echo -e "${YELLOW}清理之前的构建...${NC}"
./gradlew clean --no-daemon

# 根据平台参数执行构建
case $PLATFORMS in
    all)
        echo -e "${CYAN}=== 构建所有平台 ===${NC}"
        
        # macOS
        echo -e "${YELLOW}构建 macOS DMG...${NC}"
        if ./gradlew :composeApp:packageDmg --no-daemon; then
            echo -e "${GREEN}✓ macOS DMG 构建成功${NC}"
            find composeApp/build/compose/binaries/main/dmg -name "*.dmg" -exec cp {} "$BUILD_DIR/" \;
        else
            echo -e "${RED}✗ macOS DMG 构建失败${NC}"
        fi
        
        # Linux (仅在Linux/macOS上)
        if [[ "$OS" == "Linux" ]] || [[ "$OS" == "Darwin" ]]; then
            echo -e "${YELLOW}构建 Linux DEB...${NC}"
            if ./gradlew :composeApp:packageDeb --no-daemon; then
                echo -e "${GREEN}✓ Linux DEB 构建成功${NC}"
                find composeApp/build/compose/binaries/main/deb -name "*.deb" -exec cp {} "$BUILD_DIR/" \;
            else
                echo -e "${RED}✗ Linux DEB 构建失败${NC}"
            fi
        fi
        
        # Windows (仅在Windows上)
        if [[ "$OS" == CYGWIN* ]] || [[ "$OS" == MINGW* ]] || [[ "$OS" == MSYS* ]]; then
            echo -e "${YELLOW}构建 Windows EXE...${NC}"
            if ./gradlew :composeApp:packageExe --no-daemon; then
                echo -e "${GREEN}✓ Windows EXE 构建成功${NC}"
                find composeApp/build/compose/binaries/main/exe -name "*.exe" -exec cp {} "$BUILD_DIR/" \;
            else
                echo -e "${RED}✗ Windows EXE 构建失败${NC}"
            fi
        fi
        ;;
        
    current)
        echo -e "${CYAN}=== 构建当前平台 ===${NC}"
        
        case $OS in
            Darwin*)
                echo -e "${YELLOW}构建 macOS DMG...${NC}"
                if ./gradlew :composeApp:packageDmg --no-daemon; then
                    echo -e "${GREEN}✓ macOS DMG 构建成功${NC}"
                    find composeApp/build/compose/binaries/main/dmg -name "*.dmg" -exec cp {} "$BUILD_DIR/" \;
                else
                    echo -e "${RED}✗ macOS DMG 构建失败${NC}"
                fi
                ;;
            Linux*)
                echo -e "${YELLOW}构建 Linux DEB...${NC}"
                if ./gradlew :composeApp:packageDeb --no-daemon; then
                    echo -e "${GREEN}✓ Linux DEB 构建成功${NC}"
                    find composeApp/build/compose/binaries/main/deb -name "*.deb" -exec cp {} "$BUILD_DIR/" \;
                else
                    echo -e "${RED}✗ Linux DEB 构建失败${NC}"
                fi
                ;;
            CYGWIN*|MINGW*|MSYS*)
                echo -e "${YELLOW}构建 Windows EXE...${NC}"
                if ./gradlew :composeApp:packageExe --no-daemon; then
                    echo -e "${GREEN}✓ Windows EXE 构建成功${NC}"
                    find composeApp/build/compose/binaries/main/exe -name "*.exe" -exec cp {} "$BUILD_DIR/" \;
                else
                    echo -e "${RED}✗ Windows EXE 构建失败${NC}"
                fi
                ;;
            *)
                echo -e "${RED}✗ 不支持的操作系统: $OS${NC}"
                exit 1
                ;;
        esac
        ;;
        
    macos)
        echo -e "${CYAN}=== 构建 macOS ===${NC}"
        echo -e "${YELLOW}构建 macOS DMG...${NC}"
        if ./gradlew :composeApp:packageDmg --no-daemon; then
            echo -e "${GREEN}✓ macOS DMG 构建成功${NC}"
            find composeApp/build/compose/binaries/main/dmg -name "*.dmg" -exec cp {} "$BUILD_DIR/" \;
        else
            echo -e "${RED}✗ macOS DMG 构建失败${NC}"
        fi
        ;;
        
    linux)
        echo -e "${CYAN}=== 构建 Linux ===${NC}"
        echo -e "${YELLOW}构建 Linux DEB...${NC}"
        if ./gradlew :composeApp:packageDeb --no-daemon; then
            echo -e "${GREEN}✓ Linux DEB 构建成功${NC}"
            find composeApp/build/compose/binaries/main/deb -name "*.deb" -exec cp {} "$BUILD_DIR/" \;
        else
            echo -e "${RED}✗ Linux DEB 构建失败${NC}"
        fi
        ;;
        
    windows)
        echo -e "${CYAN}=== 构建 Windows ===${NC}"
        echo -e "${YELLOW}构建 Windows EXE...${NC}"
        if ./gradlew :composeApp:packageExe --no-daemon; then
            echo -e "${GREEN}✓ Windows EXE 构建成功${NC}"
            find composeApp/build/compose/binaries/main/exe -name "*.exe" -exec cp {} "$BUILD_DIR/" \;
        else
            echo -e "${RED}✗ Windows EXE 构建失败${NC}"
        fi
        ;;
        
    *)
        echo -e "${RED}✗ 无效的平台参数: $PLATFORMS${NC}"
        echo -e "${YELLOW}支持的平台: all, current, macos, linux, windows${NC}"
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

# 测试运行 (可选)
if [[ "$PLATFORMS" == "current" ]] || [[ "$PLATFORMS" == "all" ]]; then
    echo -e "${CYAN}=== 测试运行 ===${NC}"
    read -p "是否运行测试? [y/N]: " run_choice
    
    if [[ "$run_choice" =~ ^[Yy]$ ]]; then
        echo -e "${YELLOW}启动应用测试...${NC}"
        
        case $OS in
            Darwin*)
                DMG_FILE=$(find "$BUILD_DIR" -name "*.dmg" | head -1)
                if [ -n "$DMG_FILE" ]; then
                    echo -e "${CYAN}挂载 DMG: $DMG_FILE${NC}"
                    open "$DMG_FILE"
                else
                    echo -e "${RED}✗ 未找到 DMG 文件${NC}"
                fi
                ;;
            Linux*)
                DEB_FILE=$(find "$BUILD_DIR" -name "*.deb" | head -1)
                if [ -n "$DEB_FILE" ]; then
                    echo -e "${CYAN}安装 DEB: $DEB_FILE${NC}"
                    sudo dpkg -i "$DEB_FILE" || echo -e "${YELLOW}需要手动安装${NC}"
                else
                    echo -e "${RED}✗ 未找到 DEB 文件${NC}"
                fi
                ;;
            CYGWIN*|MINGW*|MSYS*)
                EXE_FILE=$(find "$BUILD_DIR" -name "*.exe" | head -1)
                if [ -n "$EXE_FILE" ]; then
                    echo -e "${CYAN}运行 EXE: $EXE_FILE${NC}"
                    "$EXE_FILE" &
                else
                    echo -e "${RED}✗ 未找到 EXE 文件${NC}"
                fi
                ;;
        esac
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
echo -e "${GREEN}✓ Desktop 构建完成！${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${CYAN}构建产物位置:${NC}"
echo -e "  目录: $BUILD_DIR"
echo -e "  版本: $VERSION"
echo ""
echo -e "${CYAN}安装说明:${NC}"
echo -e "  macOS: 双击 DMG 文件安装"
echo -e "  Linux: sudo dpkg -i <file>.deb"
echo -e "  Windows: 双击 EXE 文件安装"
