#!/bin/bash

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== 配置 juejin-lite iOS 项目 ===${NC}\n"

# 检查是否在 macOS 上
if [[ "$OSTYPE" != "darwin"* ]]; then
    echo -e "${RED}❌ 错误: iOS 开发只能在 macOS 上进行${NC}"
    exit 1
fi

# 步骤 1: 编译 Framework
echo -e "${YELLOW}步骤 1: 编译 JuejinLite Framework...${NC}"
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64 --no-daemon

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Framework 编译失败${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Framework 编译成功${NC}\n"

# 步骤 2: 检查 Framework 是否存在
FRAMEWORK_PATH="apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/JuejinLite.framework"

if [ ! -d "$FRAMEWORK_PATH" ]; then
    echo -e "${RED}❌ Framework 未找到: $FRAMEWORK_PATH${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Framework 位置: $FRAMEWORK_PATH${NC}\n"

# 步骤 3: 检查 iosApp-lite 目录
if [ ! -d "iosApp-lite" ]; then
    echo -e "${YELLOW}创建 iosApp-lite 目录...${NC}"
    cp -r iosApp iosApp-lite
    echo -e "${GREEN}✅ 已复制 iosApp 到 iosApp-lite${NC}\n"
fi

# 步骤 4: 更新 ContentView.swift
echo -e "${YELLOW}步骤 2: 更新 ContentView.swift...${NC}"

CONTENT_VIEW="iosApp-lite/iosApp/ContentView.swift"

if [ -f "$CONTENT_VIEW" ]; then
    # 替换 import ComposeApp 为 import JuejinLite
    sed -i '' 's/import ComposeApp/import JuejinLite/g' "$CONTENT_VIEW"
    
    # 替换应用名称
    sed -i '' 's/掘金 APP/掘金轻量版/g' "$CONTENT_VIEW"
    
    echo -e "${GREEN}✅ ContentView.swift 已更新${NC}\n"
else
    echo -e "${RED}❌ ContentView.swift 未找到${NC}"
    exit 1
fi

# 步骤 5: 显示下一步操作
echo -e "${BLUE}=== 配置完成 ===${NC}\n"
echo -e "${GREEN}✅ juejin-lite iOS 项目已准备就绪！${NC}\n"

echo -e "${YELLOW}下一步操作：${NC}"
echo ""
echo "1. 打开 Xcode 项目："
echo "   ${BLUE}cd iosApp-lite && open iosApp.xcodeproj${NC}"
echo ""
echo "2. 在 Xcode 中配置 Framework："
echo "   a. 选择项目 → Target → General"
echo "   b. 找到 'Frameworks, Libraries, and Embedded Content'"
echo "   c. 删除旧的 ComposeApp.framework"
echo "   d. 点击 '+' → 'Add Other...' → 'Add Files...'"
echo "   e. 导航到: ${BLUE}$FRAMEWORK_PATH${NC}"
echo "   f. 选择 'Embed & Sign'"
echo ""
echo "3. 配置 Build Phase（自动重新编译 Framework）："
echo "   a. 选择 Target → Build Phases"
echo "   b. 点击 '+' → 'New Run Script Phase'"
echo "   c. 添加脚本："
echo "      ${BLUE}cd \"\$SRCROOT/..\"${NC}"
echo "      ${BLUE}./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64${NC}"
echo ""
echo "4. 运行应用："
echo "   ${BLUE}Cmd + R${NC} 或点击运行按钮 (▶️)"
echo ""
echo -e "${YELLOW}提示：${NC} 也可以使用脚本运行："
echo "   ${BLUE}./run-ios.sh${NC}"
echo ""
