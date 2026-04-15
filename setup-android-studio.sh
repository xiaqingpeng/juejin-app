#!/bin/bash

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Android Studio 多平台配置工具 ===${NC}\n"

# 检查 .idea 目录
if [ ! -d ".idea" ]; then
    echo -e "${RED}❌ 错误: .idea 目录不存在${NC}"
    echo "请先在 Android Studio 中打开项目"
    exit 1
fi

# 创建 runConfigurations 目录
echo -e "${YELLOW}创建运行配置目录...${NC}"
mkdir -p .idea/runConfigurations

# 检查配置文件
echo -e "\n${BLUE}=== 检查运行配置 ===${NC}\n"

configs=(
    "juejin_main.xml:Android"
    "juejin_lite.xml:Android"
    "juejin_main_Desktop.xml:Desktop"
    "juejin_lite_Desktop.xml:Desktop"
    "juejin_main_iOS.xml:iOS"
    "juejin_lite_iOS.xml:iOS"
)

all_exist=true
for config in "${configs[@]}"; do
    IFS=':' read -r file platform <<< "$config"
    if [ -f ".idea/runConfigurations/$file" ]; then
        echo -e "${GREEN}✅ $platform: $file${NC}"
    else
        echo -e "${RED}❌ $platform: $file (缺失)${NC}"
        all_exist=false
    fi
done

if [ "$all_exist" = true ]; then
    echo -e "\n${GREEN}🎉 所有运行配置已就绪！${NC}"
else
    echo -e "\n${YELLOW}⚠️  部分配置缺失，请检查文件${NC}"
fi

# 显示使用说明
echo -e "\n${BLUE}=== 使用说明 ===${NC}\n"

echo "1. 重启 Android Studio"
echo "2. 在顶部工具栏选择运行配置："
echo ""
echo "   📱 Android:"
echo "      - juejin-main"
echo "      - juejin-lite"
echo ""
echo "   🖥️  Desktop:"
echo "      - juejin-main (Desktop)"
echo "      - juejin-lite (Desktop)"
echo ""
echo "   🍎 iOS:"
echo "      - juejin-main (iOS)"
echo "      - juejin-lite (iOS)"
echo ""
echo "3. 点击运行按钮 (▶️) 或按 Shift + F10"

# 显示快捷键
echo -e "\n${BLUE}=== 快捷键 ===${NC}\n"
echo "运行:   Shift + F10"
echo "调试:   Shift + F9"
echo "停止:   Ctrl + F2"
echo "选择:   Alt + Shift + F10"

# 显示推荐工作流
echo -e "\n${BLUE}=== 推荐工作流 ===${NC}\n"
echo "1. Desktop 开发（最快）"
echo "   - 快速启动，无需模拟器"
echo "   - 完整调试功能"
echo "   - 热重载支持"
echo ""
echo "2. Android 测试"
echo "   - 验证移动端交互"
echo "   - 测试 Android 特定功能"
echo ""
echo "3. iOS 验证"
echo "   - 编译 Framework"
echo "   - 在 Xcode 中运行"

# 显示文档
echo -e "\n${BLUE}=== 相关文档 ===${NC}\n"
echo "📖 ANDROID_STUDIO_MULTIPLATFORM.md - 完整指南"
echo "📖 QUICK_COMMANDS.md - 快速命令"
echo "📖 README.md - 项目文档"

echo -e "\n${GREEN}✅ 配置检查完成！${NC}"
