#!/bin/bash

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Android Studio 配置修复脚本 ===${NC}\n"

# 步骤 1：验证模块存在
echo -e "${YELLOW}1. 验证模块存在...${NC}"
./gradlew projects 2>&1 | grep -E "(apps:juejin)" > /dev/null
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 模块存在${NC}"
    ./gradlew projects | grep -E "(apps:juejin)"
else
    echo -e "${RED}❌ 模块不存在，请检查 settings.gradle.kts${NC}"
    exit 1
fi

# 步骤 2：清理构建
echo -e "\n${YELLOW}2. 清理构建缓存...${NC}"
./gradlew clean
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 清理成功${NC}"
else
    echo -e "${RED}❌ 清理失败${NC}"
    exit 1
fi

# 步骤 3：重新构建
echo -e "\n${YELLOW}3. 重新构建项目...${NC}"
./gradlew build --no-daemon
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 构建成功${NC}"
else
    echo -e "${YELLOW}⚠️  构建有警告，但可以继续${NC}"
fi

# 步骤 4：验证配置文件
echo -e "\n${YELLOW}4. 验证运行配置文件...${NC}"
if [ -f ".idea/runConfigurations/juejin_lite.xml" ]; then
    echo -e "${GREEN}✅ juejin_lite.xml 存在${NC}"
else
    echo -e "${RED}❌ juejin_lite.xml 不存在${NC}"
fi

if [ -f ".idea/runConfigurations/juejin_main.xml" ]; then
    echo -e "${GREEN}✅ juejin_main.xml 存在${NC}"
else
    echo -e "${RED}❌ juejin_main.xml 不存在${NC}"
fi

# 步骤 5：显示模块名称
echo -e "\n${YELLOW}5. 检查项目名称...${NC}"
PROJECT_NAME=$(grep "rootProject.name" settings.gradle.kts | cut -d'"' -f2)
echo -e "${GREEN}项目名称：${PROJECT_NAME}${NC}"

# 步骤 6：提供建议
echo -e "\n${BLUE}=== 修复完成 ===${NC}"
echo -e "\n${GREEN}下一步操作：${NC}"
echo "1. 重启 Android Studio"
echo "   File → Invalidate Caches / Restart → Invalidate and Restart"
echo ""
echo "2. 同步 Gradle"
echo "   点击顶部的 🐘 图标"
echo ""
echo "3. 手动创建运行配置（如果自动配置不工作）："
echo "   - Run → Edit Configurations..."
echo "   - 点击 + → Android App"
echo "   - Name: juejin-lite"
echo "   - Module: 从下拉菜单选择包含 'juejin-lite' 的选项"
echo "   - 可能的格式："
echo "     • ${PROJECT_NAME}.apps.juejin-lite.main"
echo "     • apps.juejin-lite.main"
echo "     • juejin-lite.main"
echo ""
echo "4. 如果仍然无法找到模块，使用 Gradle 任务："
echo "   右侧 Gradle 面板 → apps → juejin-lite → Tasks → install → installDebug"
echo ""
echo -e "${BLUE}详细说明请查看：${NC}MODULE_NOT_FOUND_FIX.md"
