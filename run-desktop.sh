#!/bin/bash

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== 掘金 Desktop 应用启动器 ===${NC}\n"

# 显示菜单
echo "请选择要运行的应用："
echo "1) juejin-main（完整版）"
echo "2) juejin-lite（轻量版）"
echo "3) 同时运行两个应用"
echo "4) 退出"
echo ""
read -p "请输入选择 (1-4): " choice

case $choice in
    1)
        echo -e "\n${YELLOW}启动 juejin-main（完整版）...${NC}"
        ./gradlew :apps:juejin-main:run
        ;;
    2)
        echo -e "\n${YELLOW}启动 juejin-lite（轻量版）...${NC}"
        ./gradlew :apps:juejin-lite:run
        ;;
    3)
        echo -e "\n${YELLOW}同时启动两个应用...${NC}"
        echo -e "${BLUE}提示：两个窗口会同时打开${NC}\n"
        
        # 在后台启动 juejin-main
        ./gradlew :apps:juejin-main:run &
        MAIN_PID=$!
        
        # 等待 2 秒
        sleep 2
        
        # 启动 juejin-lite
        ./gradlew :apps:juejin-lite:run &
        LITE_PID=$!
        
        echo -e "${GREEN}✅ 两个应用已启动${NC}"
        echo "juejin-main PID: $MAIN_PID"
        echo "juejin-lite PID: $LITE_PID"
        echo ""
        echo "按 Ctrl+C 停止所有应用"
        
        # 等待进程
        wait
        ;;
    4)
        echo -e "${GREEN}再见！${NC}"
        exit 0
        ;;
    *)
        echo -e "${YELLOW}无效的选择${NC}"
        exit 1
        ;;
esac
