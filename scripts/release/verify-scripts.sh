#!/bin/bash

# ========================================
# 发布脚本验证工具
# ========================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    发布脚本验证${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

# 检查项计数
TOTAL_CHECKS=0
PASSED_CHECKS=0
FAILED_CHECKS=0

# 检查函数
check_item() {
    local name="$1"
    local command="$2"
    
    TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
    
    echo -n "检查 $name... "
    if eval "$command" &> /dev/null; then
        echo -e "${GREEN}✓${NC}"
        PASSED_CHECKS=$((PASSED_CHECKS + 1))
        return 0
    else
        echo -e "${RED}✗${NC}"
        FAILED_CHECKS=$((FAILED_CHECKS + 1))
        return 1
    fi
}

# 1. 检查必需工具
echo -e "${CYAN}=== 检查必需工具 ===${NC}"
check_item "Git" "command -v git"
check_item "Java" "command -v java"
check_item "Gradle Wrapper" "[ -f '$PROJECT_ROOT/gradlew' ]"
check_item "GitHub CLI" "command -v gh"
check_item "ADB (可选)" "command -v adb"
echo ""

# 2. 检查脚本文件
echo -e "${CYAN}=== 检查脚本文件 ===${NC}"
SCRIPTS=(
    "android-build.sh"
    "desktop-build.sh"
    "cross-platform-build.sh"
    "quick-build-upload.sh"
    "create-lite-tag.sh"
    "recreate-tag.sh"
    "upload-release.sh"
    "check-pipeline.sh"
    "test-build.sh"
    "complete-release.sh"
)

for script in "${SCRIPTS[@]}"; do
    check_item "$script" "[ -f '$SCRIPT_DIR/$script' ] && [ -x '$SCRIPT_DIR/$script' ]"
done
echo ""

# 3. 检查 Git 仓库
echo -e "${CYAN}=== 检查 Git 仓库 ===${NC}"
cd "$PROJECT_ROOT"
check_item "Git 仓库" "git rev-parse --git-dir"
check_item "远程仓库" "git remote get-url origin"

if git remote get-url origin &> /dev/null; then
    REMOTE_URL=$(git remote get-url origin)
    if [[ "$REMOTE_URL" == *"github.com"* ]]; then
        echo -e "  ${GREEN}✓${NC} GitHub 仓库: $REMOTE_URL"
    else
        echo -e "  ${YELLOW}⚠${NC}  非 GitHub 仓库: $REMOTE_URL"
    fi
fi
echo ""

# 4. 检查构建环境
echo -e "${CYAN}=== 检查构建环境 ===${NC}"
check_item "Android SDK" "[ -n '$ANDROID_HOME' ] || [ -n '$ANDROID_SDK_ROOT' ]"
check_item "Gradle 配置" "[ -f '$PROJECT_ROOT/build.gradle.kts' ]"
check_item "Compose App 模块" "[ -d '$PROJECT_ROOT/composeApp' ]"
echo ""

# 5. 检查 GitHub CLI 认证
echo -e "${CYAN}=== 检查 GitHub CLI ===${NC}"
if command -v gh &> /dev/null; then
    if gh auth status &> /dev/null 2>&1; then
        echo -e "  ${GREEN}✓${NC} GitHub CLI 已认证"
        PASSED_CHECKS=$((PASSED_CHECKS + 1))
    else
        echo -e "  ${YELLOW}⚠${NC}  GitHub CLI 未认证 (运行: gh auth login)"
        FAILED_CHECKS=$((FAILED_CHECKS + 1))
    fi
    TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
else
    echo -e "  ${YELLOW}⚠${NC}  GitHub CLI 未安装"
fi
echo ""

# 6. 检查构建目录
echo -e "${CYAN}=== 检查构建目录 ===${NC}"
BUILD_DIRS=(
    "build/releases"
    "build/releases/android"
    "build/releases/desktop"
)

for dir in "${BUILD_DIRS[@]}"; do
    if [ -d "$PROJECT_ROOT/$dir" ]; then
        echo -e "  ${GREEN}✓${NC} $dir 存在"
        
        # 检查是否有文件
        file_count=$(find "$PROJECT_ROOT/$dir" -type f | wc -l)
        if [ $file_count -gt 0 ]; then
            echo -e "    包含 $file_count 个文件"
        fi
    else
        echo -e "  ${YELLOW}⚠${NC}  $dir 不存在 (首次构建时会创建)"
    fi
done
echo ""

# 7. 测试脚本语法
echo -e "${CYAN}=== 测试脚本语法 ===${NC}"
for script in "${SCRIPTS[@]}"; do
    if [ -f "$SCRIPT_DIR/$script" ]; then
        if bash -n "$SCRIPT_DIR/$script" 2>/dev/null; then
            echo -e "  ${GREEN}✓${NC} $script 语法正确"
        else
            echo -e "  ${RED}✗${NC} $script 语法错误"
            FAILED_CHECKS=$((FAILED_CHECKS + 1))
        fi
    fi
done
echo ""

# 8. 显示总结
echo -e "${BLUE}========================================${NC}"
echo -e "${CYAN}验证总结:${NC}"
echo -e "  总检查项: $TOTAL_CHECKS"
echo -e "  ${GREEN}通过: $PASSED_CHECKS${NC}"
echo -e "  ${RED}失败: $FAILED_CHECKS${NC}"

if [ $FAILED_CHECKS -eq 0 ]; then
    echo -e "${GREEN}✓ 所有检查通过！${NC}"
    echo -e "${BLUE}========================================${NC}"
    exit 0
else
    echo -e "${YELLOW}⚠ 有 $FAILED_CHECKS 项检查失败${NC}"
    echo -e "${BLUE}========================================${NC}"
    echo ""
    echo -e "${CYAN}建议操作:${NC}"
    
    if ! command -v gh &> /dev/null; then
        echo -e "  1. 安装 GitHub CLI: ${YELLOW}brew install gh${NC}"
    fi
    
    if command -v gh &> /dev/null && ! gh auth status &> /dev/null 2>&1; then
        echo -e "  2. 认证 GitHub CLI: ${YELLOW}gh auth login${NC}"
    fi
    
    if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
        echo -e "  3. 配置 Android SDK 环境变量"
    fi
    
    exit 1
fi
