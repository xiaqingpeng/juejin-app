#!/bin/bash

# ========================================
# 标签重建脚本 (触发 GitHub Actions)
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
FORCE_PUSH=false

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    标签重建脚本${NC}"
echo -e "${BLUE}========================================${NC}"

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --tag)
            TAG_NAME="$2"
            shift 2
            ;;
        --remote)
            REMOTE_NAME="$2"
            shift 2
            ;;
        --force)
            FORCE_PUSH=true
            shift
            ;;
        --help)
            echo -e "${BLUE}用法:${NC} $0 [选项]"
            echo ""
            echo -e "${YELLOW}选项:${NC}"
            echo -e "  --tag TAG_NAME       指定标签名 (默认: v1.0.0)"
            echo -e "  --remote REMOTE_NAME 指定远程仓库名 (默认: origin)"
            echo -e "  --force             强制推送标签"
            echo -e "  --help              显示此帮助信息"
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
echo -e "  远程仓库: ${REMOTE_NAME}"
echo -e "  强制推送: ${FORCE_PUSH}"
echo ""

# 检查是否在Git仓库中
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo -e "${RED}✗ 错误: 当前目录不是Git仓库${NC}"
    exit 1
fi

# 检查远程仓库
if ! git remote get-url "$REMOTE_NAME" > /dev/null 2>&1; then
    echo -e "${RED}✗ 错误: 远程仓库 '$REMOTE_NAME' 不存在${NC}"
    exit 1
fi

# 获取当前分支
CURRENT_BRANCH=$(git branch --show-current)
echo -e "${CYAN}当前分支: ${CURRENT_BRANCH}${NC}"

# 检查是否有未提交的更改
if ! git diff --quiet || ! git diff --cached --quiet; then
    echo -e "${YELLOW}⚠️  检测到未提交的更改${NC}"
    echo -e "${YELLOW}建议先提交所有更改${NC}"
    read -p "是否继续? [y/N]: " continue_choice
    if [[ ! "$continue_choice" =~ ^[Yy]$ ]]; then
        echo -e "${YELLOW}操作已取消${NC}"
        exit 0
    fi
fi

# 获取远程仓库信息
REMOTE_URL=$(git remote get-url "$REMOTE_NAME")
echo -e "${CYAN}远程仓库: ${REMOTE_URL}${NC}"

# 检查是否为GitHub仓库
if [[ "$REMOTE_URL" != *"github.com"* ]]; then
    echo -e "${RED}✗ 错误: 不是GitHub仓库，无法触发GitHub Actions${NC}"
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

# 检查本地是否已存在该标签
if git rev-parse "$TAG_NAME" > /dev/null 2>&1; then
    echo -e "${YELLOW}⚠️  本地标签 '$TAG_NAME' 已存在${NC}"
    read -p "是否删除本地标签并重新创建? [y/N]: " delete_local
    if [[ "$delete_local" =~ ^[Yy]$ ]]; then
        git tag -d "$TAG_NAME"
        echo -e "${GREEN}✓ 本地标签已删除${NC}"
    else
        echo -e "${YELLOW}保留本地标签${NC}"
    fi
fi

# 检查远程是否已存在该标签
echo -e "${YELLOW}检查远程标签...${NC}"
if git ls-remote --tags "$REMOTE_NAME" | grep -q "refs/tags/$TAG_NAME$"; then
    echo -e "${YELLOW}⚠️  远程标签 '$TAG_NAME' 已存在${NC}"
    read -p "是否删除远程标签并重新创建? [y/N]: " delete_remote
    if [[ "$delete_remote" =~ ^[Yy]$ ]]; then
        git push "$REMOTE_NAME" --delete "$TAG_NAME"
        echo -e "${GREEN}✓ 远程标签已删除${NC}"
    else
        echo -e "${YELLOW}保留远程标签${NC}"
        if [ "$FORCE_PUSH" = false ]; then
            echo -e "${YELLOW}使用 --force 参数强制推送${NC}"
            exit 1
        fi
    fi
else
    echo -e "${GREEN}✓ 远程标签 '$TAG_NAME' 不存在${NC}"
fi

echo ""

# 创建本地标签
echo -e "${YELLOW}创建本地标签 '$TAG_NAME'...${NC}"

# 获取最新提交信息
LATEST_COMMIT=$(git rev-parse HEAD)
echo -e "${CYAN}基于提交: ${LATEST_COMMIT}${NC}"

# 创建标签
if git tag "$TAG_NAME" -m "Release $TAG_NAME"; then
    echo -e "${GREEN}✓ 本地标签创建成功${NC}"
else
    echo -e "${RED}✗ 本地标签创建失败${NC}"
    exit 1
fi

echo ""

# 推送标签到远程
echo -e "${YELLOW}推送标签到远程仓库...${NC}"

PUSH_ARGS=("$REMOTE_NAME" "$TAG_NAME")
if [ "$FORCE_PUSH" = true ]; then
    PUSH_ARGS+=("--force")
fi

if git push "${PUSH_ARGS[@]}"; then
    echo -e "${GREEN}✓ 标签推送成功${NC}"
else
    echo -e "${RED}✗ 标签推送失败${NC}"
    exit 1
fi

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}✓ 标签重建完成！${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${CYAN}标签信息:${NC}"
echo -e "  标签: ${TAG_NAME}"
echo -e "  仓库: ${GITHUB_USER}/${GITHUB_REPO}"
echo -e "  分支: ${CURRENT_BRANCH}"
echo -e "  提交: ${LATEST_COMMIT}"
echo ""
echo -e "${CYAN}GitHub Actions:${NC}"
echo -e "  状态: 已触发"
echo -e "  链接: https://github.com/${GITHUB_USER}/${GITHUB_REPO}/actions"
echo ""
echo -e "${CYAN}Release页面:${NC}"
echo -e "  链接: https://github.com/${GITHUB_USER}/${GITHUB_REPO}/releases/tag/${TAG_NAME}"
echo ""
echo -e "${CYAN}下一步操作:${NC}"
echo -e "  1. 等待GitHub Actions完成构建"
echo -e "  2. 检查构建状态和产物"
echo -e "  3. 测试下载的构建产物"
echo -e "  4. 如有问题，查看Actions日志"

# 可选：自动打开GitHub Actions页面
read -p "是否打开GitHub Actions页面? [y/N]: " open_actions
if [[ "$open_actions" =~ ^[Yy]$ ]]; then
    if command -v open &> /dev/null; then
        open "https://github.com/${GITHUB_USER}/${GITHUB_REPO}/actions"
    elif command -v xdg-open &> /dev/null; then
        xdg-open "https://github.com/${GITHUB_USER}/${GITHUB_REPO}/actions"
    else
        echo -e "${YELLOW}请手动访问: https://github.com/${GITHUB_USER}/${GITHUB_REPO}/actions${NC}"
    fi
fi
