#!/bin/bash

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== 掘金应用一键安装脚本 ===${NC}\n"

# 检查设备连接
echo -e "${YELLOW}1. 检查设备连接...${NC}"
DEVICES=$(adb devices | grep -v "List" | grep "device" | wc -l)
if [ $DEVICES -eq 0 ]; then
    echo -e "${YELLOW}⚠️  未检测到连接的设备${NC}"
    echo "请确保："
    echo "  1. 设备已通过 USB 连接"
    echo "  2. 已启用 USB 调试"
    echo "  3. 已授权此电脑进行调试"
    exit 1
fi
echo -e "${GREEN}✅ 检测到 $DEVICES 个设备${NC}\n"
adb devices

# 编译应用
echo -e "\n${YELLOW}2. 编译应用...${NC}"
./gradlew assembleDebug --no-daemon
if [ $? -ne 0 ]; then
    echo -e "${YELLOW}❌ 编译失败${NC}"
    exit 1
fi
echo -e "${GREEN}✅ 编译成功${NC}"

# 安装完整版
echo -e "\n${YELLOW}3. 安装完整版（juejin-main）...${NC}"
./gradlew :apps:juejin-main:installDebug
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 完整版安装成功${NC}"
else
    echo -e "${YELLOW}❌ 完整版安装失败${NC}"
fi

# 安装轻量版
echo -e "\n${YELLOW}4. 安装轻量版（juejin-lite）...${NC}"
./gradlew :apps:juejin-lite:installDebug
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 轻量版安装成功${NC}"
else
    echo -e "${YELLOW}❌ 轻量版安装失败${NC}"
fi

# 验证安装
echo -e "\n${YELLOW}5. 验证安装...${NC}"
INSTALLED=$(adb shell pm list packages | grep juejin | wc -l)
echo -e "${GREEN}已安装 $INSTALLED 个应用：${NC}"
adb shell pm list packages | grep juejin

# 显示应用信息
echo -e "\n${BLUE}=== 安装完成 ===${NC}"
echo -e "${GREEN}✅ 完整版：${NC}com.example.juejin"
echo -e "   启动命令：adb shell am start -n com.example.juejin/.MainActivity"
echo -e "\n${GREEN}✅ 轻量版：${NC}com.example.juejin.lite"
echo -e "   启动命令：adb shell am start -n com.example.juejin.lite/.MainActivity"

echo -e "\n${BLUE}提示：${NC}两个应用可以同时运行在设备上"
