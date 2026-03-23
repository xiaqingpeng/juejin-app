#!/bin/bash

# ========================================
# 测试构建脚本
# ========================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    测试构建${NC}"
echo -e "${BLUE}========================================${NC}"

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

echo -e "${CYAN}项目根目录: ${PROJECT_ROOT}${NC}"
echo ""

# 检查 Gradle
if [ ! -f "$PROJECT_ROOT/gradlew" ]; then
    echo -e "${RED}✗ 未找到 gradlew${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Gradle 环境检查通过${NC}"
echo ""

# 测试编译
echo -e "${YELLOW}测试 Kotlin 编译...${NC}"
cd "$PROJECT_ROOT"

if ./gradlew :composeApp:compileKotlinJvm --no-daemon; then
    echo -e "${GREEN}✓ Kotlin 编译成功${NC}"
else
    echo -e "${RED}✗ Kotlin 编译失败${NC}"
    exit 1
fi

echo ""

# 测试资源编译
echo -e "${YELLOW}测试资源编译...${NC}"
if ./gradlew :composeApp:compileDebugKotlinAndroid --no-daemon; then
    echo -e "${GREEN}✓ Android 资源编译成功${NC}"
else
    echo -e "${RED}✗ Android 资源编译失败${NC}"
    exit 1
fi

echo ""

# 测试 Desktop 构建
echo -e "${YELLOW}测试 Desktop 构建...${NC}"
if ./gradlew :composeApp:packageDmg --no-daemon; then
    echo -e "${GREEN}✓ Desktop 构建成功${NC}"
else
    echo -e "${RED}✗ Desktop 构建失败${NC}"
    exit 1
fi

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}✓ 所有测试通过！${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${CYAN}下一步操作:${NC}"
echo -e "  1. 运行完整构建: ./scripts/release/cross-platform-build.sh"
echo -e "  2. 运行菜单: ./scripts/release/complete-release.sh"
echo -e "  3. Android 构建: ./scripts/release/android-build.sh"
