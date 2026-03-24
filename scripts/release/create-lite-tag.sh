#!/bin/bash

# ========================================
# 轻量级标签创建脚本（避免 GitHub Actions 超时）
# ========================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# 配置
TAG_NAME="v1.0.0"
REMOTE_NAME="origin"
PROJECT_NAME="juejin-app"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    轻量级标签创建（本地构建方案）${NC}"
echo -e "${BLUE}========================================${NC}"

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --tag)
            TAG_NAME="$2"
            shift 2
            ;;
        --help)
            echo -e "${BLUE}用法:${NC} $0 [选项]"
            echo ""
            echo -e "${YELLOW}选项:${NC}"
            echo -e "  --tag TAG_NAME       指定标签名 (默认: v1.0.0)"
            echo -e "  --help               显示此帮助信息"
            echo ""
            echo -e "${CYAN}说明:${NC}"
            echo -e "  此脚本创建轻量级标签，不触发 GitHub Actions 构建"
            echo -e "  适合本地构建后手动上传的场景"
            exit 0
            ;;
        *)
            echo -e "${RED}✗ 未知参数: $1${NC}"
            exit 1
            ;;
    esac
done

echo -e "${CYAN}配置信息:${NC}"
echo -e "  标签名称: ${TAG_NAME}"
echo -e "  项目名称: ${PROJECT_NAME}"
echo ""

# 获取脚本目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 检查是否在Git仓库中
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo -e "${RED}✗ 错误: 当前目录不是Git仓库${NC}"
    exit 1
fi

# 获取仓库信息
REMOTE_URL=$(git remote get-url "$REMOTE_NAME" 2>/dev/null)
if [ $? -ne 0 ]; then
    echo -e "${RED}✗ 错误: 无法获取远程仓库信息${NC}"
    exit 1
fi

# 提取GitHub仓库信息
if [[ "$REMOTE_URL" =~ github\.com[:/]([^/]+)/([^/]+)(\.git)?$ ]]; then
    GITHUB_USER="${BASH_REMATCH[1]}"
    GITHUB_REPO="${BASH_REMATCH[2]}"
    GITHUB_REPO="${GITHUB_REPO%.git}"
else
    echo -e "${RED}✗ 错误: 无法解析GitHub仓库信息${NC}"
    exit 1
fi

echo -e "${CYAN}GitHub仓库: ${GITHUB_USER}/${GITHUB_REPO}${NC}"
echo ""

# 检查 GitHub CLI
if ! command -v gh &> /dev/null; then
    echo -e "${YELLOW}⚠️  GitHub CLI 未安装${NC}"
    echo -e "${YELLOW}将只创建本地标签，需要手动创建 Release${NC}"
    HAS_GH=false
else
    if gh auth status &> /dev/null 2>&1; then
        HAS_GH=true
        echo -e "${GREEN}✓ GitHub CLI 已就绪${NC}"
    else
        echo -e "${YELLOW}⚠️  GitHub CLI 未认证${NC}"
        HAS_GH=false
    fi
fi

echo ""

# 步骤 1: 本地构建
echo -e "${CYAN}=== 步骤 1/4: 本地构建 ===${NC}"
read -p "是否执行本地构建? [Y/n]: " build_choice

if [[ ! "$build_choice" =~ ^[Nn]$ ]]; then
    echo -e "${YELLOW}选择构建平台:${NC}"
    echo -e "  1. Android"
    echo -e "  2. Desktop"
    echo -e "  3. 两者都构建"
    read -p "请选择 [1-3]: " platform_choice
    
    case $platform_choice in
        1)
            if [ -f "$SCRIPT_DIR/android-build.sh" ]; then
                "$SCRIPT_DIR/android-build.sh" --version "$TAG_NAME" --type all
            fi
            ;;
        2)
            if [ -f "$SCRIPT_DIR/desktop-build.sh" ]; then
                "$SCRIPT_DIR/desktop-build.sh" --version "$TAG_NAME" --platforms current
            fi
            ;;
        3)
            if [ -f "$SCRIPT_DIR/android-build.sh" ]; then
                "$SCRIPT_DIR/android-build.sh" --version "$TAG_NAME" --type all
            fi
            if [ -f "$SCRIPT_DIR/desktop-build.sh" ]; then
                "$SCRIPT_DIR/desktop-build.sh" --version "$TAG_NAME" --platforms current
            fi
            ;;
        *)
            echo -e "${YELLOW}跳过构建${NC}"
            ;;
    esac
else
    echo -e "${YELLOW}跳过构建${NC}"
fi

echo ""

# 步骤 2: 创建标签
echo -e "${CYAN}=== 步骤 2/4: 创建标签 ===${NC}"

# 检查本地标签
if git rev-parse "$TAG_NAME" > /dev/null 2>&1; then
    echo -e "${YELLOW}⚠️  本地标签已存在${NC}"
    read -p "是否删除并重新创建? [y/N]: " recreate_tag
    if [[ "$recreate_tag" =~ ^[Yy]$ ]]; then
        git tag -d "$TAG_NAME"
        echo -e "${GREEN}✓ 本地标签已删除${NC}"
    else
        echo -e "${YELLOW}使用现有标签${NC}"
    fi
fi

# 创建标签
if ! git rev-parse "$TAG_NAME" > /dev/null 2>&1; then
    if git tag "$TAG_NAME" -m "Release $TAG_NAME"; then
        echo -e "${GREEN}✓ 标签创建成功${NC}"
    else
        echo -e "${RED}✗ 标签创建失败${NC}"
        exit 1
    fi
fi

echo ""

# 步骤 3: 创建 GitHub Release
echo -e "${CYAN}=== 步骤 3/4: 创建 GitHub Release ===${NC}"

if [ "$HAS_GH" = true ]; then
    # 检查 Release 是否存在
    if gh release view "$TAG_NAME" --repo "${GITHUB_USER}/${GITHUB_REPO}" &> /dev/null; then
        echo -e "${YELLOW}⚠️  Release 已存在${NC}"
        read -p "是否删除并重新创建? [y/N]: " recreate_release
        if [[ "$recreate_release" =~ ^[Yy]$ ]]; then
            gh release delete "$TAG_NAME" --repo "${GITHUB_USER}/${GITHUB_REPO}" --yes
            echo -e "${GREEN}✓ Release 已删除${NC}"
        fi
    fi
    
    # 创建 Release
    if ! gh release view "$TAG_NAME" --repo "${GITHUB_USER}/${GITHUB_REPO}" &> /dev/null; then
        RELEASE_NOTES="## ${PROJECT_NAME} ${TAG_NAME}

### 📦 下载说明

本版本通过本地构建生成，包含以下平台的安装包：

- **Android**: APK 和 AAB 格式
- **Desktop**: macOS DMG / Linux DEB / Windows EXE

### 🔧 系统要求

- **Android**: Android 5.0+ (API 21+)
- **Desktop**: Java 17+ 运行环境

### 📱 安装说明

**Android**:
- Debug APK: 直接安装测试
- Release APK: 正式版本
- AAB: Google Play 发布格式

**Desktop**:
- macOS: 双击 DMG 安装
- Linux: \`sudo dpkg -i <file>.deb\`
- Windows: 双击 EXE 安装

### 🔗 链接

- 项目主页: https://github.com/${GITHUB_USER}/${GITHUB_REPO}
- 问题反馈: https://github.com/${GITHUB_USER}/${GITHUB_REPO}/issues
"
        
        if gh release create "$TAG_NAME" \
            --repo "${GITHUB_USER}/${GITHUB_REPO}" \
            --title "${PROJECT_NAME} ${TAG_NAME}" \
            --notes "$RELEASE_NOTES"; then
            echo -e "${GREEN}✓ Release 创建成功${NC}"
        else
            echo -e "${RED}✗ Release 创建失败${NC}"
            exit 1
        fi
    fi
else
    echo -e "${YELLOW}⚠️  GitHub CLI 不可用，请手动创建 Release${NC}"
    echo -e "${CYAN}手动创建步骤:${NC}"
    echo -e "  1. 访问: https://github.com/${GITHUB_USER}/${GITHUB_REPO}/releases/new"
    echo -e "  2. 选择标签: ${TAG_NAME}"
    echo -e "  3. 填写 Release 信息"
    echo -e "  4. 上传构建文件"
fi

echo ""

# 步骤 4: 上传构建文件
echo -e "${CYAN}=== 步骤 4/4: 上传构建文件 ===${NC}"

if [ "$HAS_GH" = true ]; then
    # 查找构建文件
    BUILD_FILES=()
    
    # Android 文件
    if [ -d "build/releases/android" ]; then
        while IFS= read -r -d '' file; do
            BUILD_FILES+=("$file")
        done < <(find build/releases/android -type f \( -name "*.apk" -o -name "*.aab" \) -print0)
    fi
    
    # Desktop 文件
    if [ -d "build/releases/desktop" ]; then
        while IFS= read -r -d '' file; do
            BUILD_FILES+=("$file")
        done < <(find build/releases/desktop -type f \( -name "*.dmg" -o -name "*.deb" -o -name "*.exe" \) -print0)
    fi
    
    if [ ${#BUILD_FILES[@]} -eq 0 ]; then
        echo -e "${YELLOW}⚠️  未找到构建文件${NC}"
        echo -e "${CYAN}请先执行构建，或手动上传文件${NC}"
    else
        echo -e "${CYAN}找到 ${#BUILD_FILES[@]} 个构建文件:${NC}"
        for file in "${BUILD_FILES[@]}"; do
            echo -e "  - $(basename "$file")"
        done
        echo ""
        
        read -p "是否上传这些文件? [Y/n]: " upload_choice
        if [[ ! "$upload_choice" =~ ^[Nn]$ ]]; then
            for file in "${BUILD_FILES[@]}"; do
                echo -e "${YELLOW}上传: $(basename "$file")${NC}"
                if gh release upload "$TAG_NAME" "$file" \
                    --repo "${GITHUB_USER}/${GITHUB_REPO}" \
                    --clobber; then
                    echo -e "${GREEN}✓ 上传成功${NC}"
                else
                    echo -e "${RED}✗ 上传失败${NC}"
                fi
            done
        fi
    fi
else
    echo -e "${YELLOW}⚠️  GitHub CLI 不可用，请手动上传文件${NC}"
    echo -e "${CYAN}手动上传步骤:${NC}"
    echo -e "  1. 访问: https://github.com/${GITHUB_USER}/${GITHUB_REPO}/releases/tag/${TAG_NAME}"
    echo -e "  2. 点击 'Edit release'"
    echo -e "  3. 拖拽文件到上传区域"
fi

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}✓ 轻量级发布完成！${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${CYAN}Release 信息:${NC}"
echo -e "  标签: ${TAG_NAME}"
echo -e "  仓库: ${GITHUB_USER}/${GITHUB_REPO}"
echo -e "  链接: https://github.com/${GITHUB_USER}/${GITHUB_REPO}/releases/tag/${TAG_NAME}"
echo ""
echo -e "${CYAN}下一步:${NC}"
echo -e "  1. 访问 Release 页面验证"
echo -e "  2. 测试下载和安装"
echo -e "  3. 通知用户更新"
