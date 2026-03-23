#!/bin/bash

# ========================================
# GitHub Actions 流水线状态检测脚本
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
MAX_CHECKS=20
CHECK_INTERVAL=30

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    GitHub Actions 流水线状态检测${NC}"
echo -e "${BLUE}========================================${NC}"

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --tag)
            TAG_NAME="$2"
            shift 2
            ;;
        --max-checks)
            MAX_CHECKS="$2"
            shift 2
            ;;
        --interval)
            CHECK_INTERVAL="$2"
            shift 2
            ;;
        --help)
            echo -e "${BLUE}用法:${NC} $0 [选项]"
            echo ""
            echo -e "${YELLOW}选项:${NC}"
            echo -e "  --tag TAG_NAME       指定标签名 (默认: v1.0.0)"
            echo -e "  --max-checks NUM    最大检查次数 (默认: 20)"
            echo -e "  --interval SEC     检查间隔秒数 (默认: 30)"
            echo -e "  --help              显示此帮助信息"
            exit 0
            ;;
        *)
            echo -e "${RED}✗ 未知参数: $1${NC}"
            exit 1
            ;;
    esac
done

echo -e "${CYAN}检测配置:${NC}"
echo -e "  标签: ${TAG_NAME}"
echo -e "  最大检查次数: ${MAX_CHECKS}"
echo -e "  检查间隔: ${CHECK_INTERVAL} 秒"
echo ""

# 检查 GitHub CLI
if ! command -v gh &> /dev/null; then
    echo -e "${RED}✗ 错误: 需要安装 GitHub CLI${NC}"
    echo -e "${YELLOW}安装方法:${NC}"
    echo -e "  macOS: brew install gh${NC}"
    exit 1
fi

# 检查 GitHub CLI 认证
if ! gh auth status &> /dev/null 2>&1; then
    echo -e "${RED}✗ 错误: GitHub CLI 未认证${NC}"
    echo -e "${YELLOW}请运行: gh auth login${NC}"
    exit 1
fi

# 获取仓库信息
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo -e "${RED}✗ 错误: 当前目录不是 Git 仓库${NC}"
    exit 1
fi

REMOTE_URL=$(git remote get-url "$REMOTE_NAME" 2>/dev/null)
if [[ "$REMOTE_URL" =~ github\.com[:/]([^/]+)/([^/]+)(\.git)?$ ]]; then
    GITHUB_USER="${BASH_REMATCH[1]}"
    GITHUB_REPO="${BASH_REMATCH[2]}"
    GITHUB_REPO="${GITHUB_REPO%.git}"
else
    echo -e "${RED}✗ 错误: 无法解析 GitHub 仓库信息${NC}"
    exit 1
fi

echo -e "${CYAN}仓库: ${GITHUB_USER}/${GITHUB_REPO}${NC}"
echo ""

# 检测流水线状态
echo -e "${YELLOW}开始检测 GitHub Actions 状态...${NC}"
echo ""

check_count=0
while [ $check_count -lt $MAX_CHECKS ]; do
    check_count=$((check_count + 1))
    echo -e "${CYAN}检查 #${check_count}/${MAX_CHECKS}...${NC}"
    
    # 获取工作流运行状态
    if gh run list --repo "${GITHUB_USER}/${GITHUB_REPO}" --limit 1 --json status,conclusion,createdAt,headBranch,headSha --jq '.[0]' 2>/dev/null; then
        # 解析状态
        run_info=$(gh run list --repo "${GITHUB_USER}/${GITHUB_REPO}" --limit 1 --json status,conclusion,createdAt,headBranch,headSha --jq '.[0]' 2>/dev/null)
        
        if [ -n "$run_info" ]; then
            status=$(echo "$run_info" | jq -r '.status // "unknown"')
            conclusion=$(echo "$run_info" | jq -r '.conclusion // "null"')
            created_at=$(echo "$run_info" | jq -r '.createdAt // "unknown"')
            branch=$(echo "$run_info" | jq -r '.headBranch // "unknown"')
            head_sha=$(echo "$run_info" | jq -r '.headSha // "unknown"')
            
            echo -e "  状态: ${status}"
            echo -e "  结论: ${conclusion}"
            echo -e "  分支: ${branch}"
            echo -e "  提交: ${head_sha:0:8}"
            echo -e "  创建时间: ${created_at}"
            
            # 判断状态
            case "$status" in
                "queued")
                    echo -e "  ${YELLOW}⏳ 等待中${NC}"
                    ;;
                "in_progress")
                    echo -e "  ${YELLOW}🔄 进行中${NC}"
                    ;;
                "completed")
                    case "$conclusion" in
                        "success")
                            echo -e "  ${GREEN}✅ 成功完成${NC}"
                            echo ""
                            echo -e "${BLUE}========================================${NC}"
                            echo -e "${GREEN}✓ 流水线执行成功！${NC}"
                            echo -e "${BLUE}========================================${NC}"
                            echo ""
                            echo -e "${CYAN}查看详情:${NC}"
                            echo -e "  Actions: https://github.com/${GITHUB_USER}/${GITHUB_REPO}/actions"
                            echo -e "  Release: https://github.com/${GITHUB_USER}/${GITHUB_REPO}/releases/tag/${TAG_NAME}"
                            exit 0
                            ;;
                        "failure")
                            echo -e "  ${RED}❌ 执行失败${NC}"
                            echo ""
                            echo -e "${BLUE}========================================${NC}"
                            echo -e "${RED}✗ 流水线执行失败！${NC}"
                            echo -e "${BLUE}========================================${NC}"
                            echo ""
                            echo -e "${CYAN}查看失败日志:${NC}"
                            echo -e "  Actions: https://github.com/${GITHUB_USER}/${GITHUB_REPO}/actions"
                            echo -e "  请检查构建日志以了解失败原因"
                            exit 1
                            ;;
                        "cancelled")
                            echo -e "  ${YELLOW}⚠️  已取消${NC}"
                            exit 2
                            ;;
                        *)
                            echo -e "  ${YELLOW}⚠️  未知结论: ${conclusion}${NC}"
                            ;;
                    esac
                    ;;
                *)
                    echo -e "  ${YELLOW}⚠️  未知状态: ${status}${NC}"
                    ;;
            esac
        else
            echo -e "  ${YELLOW}⚠️  未找到工作流运行记录${NC}"
        fi
    else
        echo -e "  ${RED}✗ 获取工作流状态失败${NC}"
    fi
    
    echo ""
    
    if [ $check_count -lt $MAX_CHECKS ]; then
        echo -e "${YELLOW}等待 ${CHECK_INTERVAL} 秒后再次检查...${NC}"
        sleep $CHECK_INTERVAL
    fi
done

echo ""
echo -e "${YELLOW}⚠️  达到最大检查次数 (${MAX_CHECKS})${NC}"
echo -e "${CYAN}请手动检查 GitHub Actions 状态:${NC}"
echo -e "  https://github.com/${GITHUB_USER}/${GITHUB_REPO}/actions"
echo ""
echo -e "${CYAN}或使用以下命令查看最新状态:${NC}"
echo -e "  gh run list --repo ${GITHUB_USER}/${GITHUB_REPO} --limit 5"
