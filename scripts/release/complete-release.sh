#!/bin/bash

# ========================================
# Kotlin Multiplatform 完整发布流程脚本
# ========================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
MAGENTA='\033[0;35m'
NC='\033[0m' # No Color

# 默认配置
VERSION="v1.0.0"
TAG_NAME="v1.0.0"
REMOTE_NAME="origin"
PROJECT_NAME="juejin-app"

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    Kotlin Multiplatform 完整发布流程${NC}"
echo -e "${BLUE}========================================${NC}"

# 显示当前配置
echo -e "${CYAN}配置信息:${NC}"
echo -e "  项目名称: ${PROJECT_NAME}"
echo -e "  版本标签: ${TAG_NAME}"
echo -e "  远程仓库: ${REMOTE_NAME}"
echo -e "  项目根目录: ${PROJECT_ROOT}"
echo ""

# 步骤选择菜单
echo -e "${YELLOW}请选择要执行的步骤:${NC}"
echo -e "  ${CYAN}1.${NC} 本地构建测试 ${YELLOW}(单平台快速验证)${NC}"
echo -e "  ${CYAN}2.${NC} 重建标签并推送 ${YELLOW}(触发GitHub Actions)${NC}"
echo -e "  ${CYAN}3.${NC} 检测流水线状态 ${YELLOW}(监控Actions运行)${NC}"
echo -e "  ${CYAN}4.${NC} 手动上传Release资产 ${YELLOW}(备用上传方案)${NC}"
echo -e "  ${CYAN}5.${NC} 完整流程 (1→2→3) ${YELLOW}(传统发布流程)${NC}"
echo -e "  ${CYAN}6.${NC} 创建轻量级标签 ${YELLOW}(避免Actions超时)${NC}"
echo -e "  ${CYAN}7.${NC} 跨平台构建和打包 ${YELLOW}(Android + Desktop)${NC}"
echo -e "  ${CYAN}7a.${NC} 全平台构建 ${YELLOW}(Android + iOS + Desktop + Linux + Windows + Web)${NC}"
echo -e "  ${CYAN}8.${NC} 快速构建上传 ${YELLOW}(基于现有构建快速发布)${NC}"
echo -e "  ${CYAN}9.${NC} Android 专用构建 ${YELLOW}(仅构建Android平台)${NC}"
echo -e "  ${CYAN}10.${NC} Desktop 专用构建 ${YELLOW}(仅构建Desktop平台)${NC}"
echo -e "  ${CYAN}11.${NC} iOS 专用构建 ${YELLOW}(仅构建iOS平台)${NC}"
echo -e "  ${CYAN}12.${NC} Linux 专用构建 ${YELLOW}(仅构建Linux平台)${NC}"
echo -e "  ${CYAN}13.${NC} Windows 专用构建 ${YELLOW}(仅构建Windows平台)${NC}"
echo -e "  ${CYAN}14.${NC} Web 专用构建 ${YELLOW}(仅构建Web/Wasm平台)${NC}"
echo -e "  ${CYAN}0.${NC} 退出"
echo ""
echo -e "${BLUE}💡 推荐选择:${NC}"
echo -e "  ${GREEN}• 选项 7${NC} - 完整的跨平台构建，适合正式发布"
echo -e "  ${GREEN}• 选项 8${NC} - 快速发布，适合基于现有构建的热修复"
echo -e "  ${GREEN}• 选项 6${NC} - 轻量级发布，避免GitHub Actions超时问题"
echo -e "  ${GREEN}• 选项 9-14${NC} - 单平台快速测试和发布"
echo ""

read -p "请输入选择 [0-14, 7a]: " choice

case $choice in
    1)
        echo -e "${YELLOW}执行本地构建测试...${NC}"
        if [ -f "$SCRIPT_DIR/test-build.sh" ]; then
            "$SCRIPT_DIR/test-build.sh"
        else
            echo -e "${RED}✗ 错误: test-build.sh 不存在${NC}"
            exit 1
        fi
        ;;
    2)
        echo -e "${YELLOW}执行标签重建...${NC}"
        if [ -f "$SCRIPT_DIR/recreate-tag.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/recreate-tag.sh"
        else
            echo -e "${RED}✗ 错误: recreate-tag.sh 不存在${NC}"
            exit 1
        fi
        ;;
    3)
        echo -e "${YELLOW}检测流水线状态...${NC}"
        if [ -f "$SCRIPT_DIR/check-pipeline.sh" ]; then
            "$SCRIPT_DIR/check-pipeline.sh"
        else
            echo -e "${RED}✗ 错误: check-pipeline.sh 不存在${NC}"
            exit 1
        fi
        ;;
    4)
        echo -e "${YELLOW}手动上传Release资产...${NC}"
        if [ -f "$SCRIPT_DIR/upload-release.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/upload-release.sh"
        else
            echo -e "${RED}✗ 错误: upload-release.sh 不存在${NC}"
            exit 1
        fi
        ;;
    5)
        echo -e "${YELLOW}执行完整流程...${NC}"
        echo -e "${CYAN}步骤 1/3: 本地构建测试${NC}"
        if [ -f "$SCRIPT_DIR/test-build.sh" ]; then
            "$SCRIPT_DIR/test-build.sh"
        else
            echo -e "${RED}✗ 错误: test-build.sh 不存在${NC}"
            exit 1
        fi
        
        echo -e "${CYAN}步骤 2/3: 重建标签并推送${NC}"
        if [ -f "$SCRIPT_DIR/recreate-tag.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/recreate-tag.sh"
        else
            echo -e "${RED}✗ 错误: recreate-tag.sh 不存在${NC}"
            exit 1
        fi
        
        echo -e "${CYAN}步骤 3/3: 检测流水线状态${NC}"
        if [ -f "$SCRIPT_DIR/check-pipeline.sh" ]; then
            "$SCRIPT_DIR/check-pipeline.sh"
        else
            echo -e "${RED}✗ 错误: check-pipeline.sh 不存在${NC}"
            exit 1
        fi
        ;;
    6)
        echo -e "${YELLOW}创建轻量级标签...${NC}"
        if [ -f "$SCRIPT_DIR/create-lite-tag.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/create-lite-tag.sh"
        else
            echo -e "${RED}✗ 错误: create-lite-tag.sh 不存在${NC}"
            exit 1
        fi
        ;;
    7)
        echo -e "${YELLOW}执行跨平台构建和打包...${NC}"
        if [ -f "$SCRIPT_DIR/cross-platform-build.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/cross-platform-build.sh"
        else
            echo -e "${RED}✗ 错误: cross-platform-build.sh 不存在${NC}"
            exit 1
        fi
        ;;
    7a)
        echo -e "${YELLOW}执行全平台构建...${NC}"
        if [ -f "$SCRIPT_DIR/all-platforms-build.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/all-platforms-build.sh"
        else
            echo -e "${RED}✗ 错误: all-platforms-build.sh 不存在${NC}"
            exit 1
        fi
        ;;
    8)
        echo -e "${YELLOW}执行快速构建上传...${NC}"
        if [ -f "$SCRIPT_DIR/quick-build-upload.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/quick-build-upload.sh"
        else
            echo -e "${RED}✗ 错误: quick-build-upload.sh 不存在${NC}"
            exit 1
        fi
        ;;
    9)
        echo -e "${YELLOW}执行Android专用构建...${NC}"
        if [ -f "$SCRIPT_DIR/android-build.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/android-build.sh"
        else
            echo -e "${RED}✗ 错误: android-build.sh 不存在${NC}"
            exit 1
        fi
        ;;
    10)
        echo -e "${YELLOW}执行Desktop专用构建...${NC}"
        if [ -f "$SCRIPT_DIR/desktop-build.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/desktop-build.sh"
        else
            echo -e "${RED}✗ 错误: desktop-build.sh 不存在${NC}"
            exit 1
        fi
        ;;
    11)
        echo -e "${YELLOW}执行iOS专用构建...${NC}"
        if [ -f "$SCRIPT_DIR/ios-build.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/ios-build.sh"
        else
            echo -e "${RED}✗ 错误: ios-build.sh 不存在${NC}"
            exit 1
        fi
        ;;
    12)
        echo -e "${YELLOW}执行Linux专用构建...${NC}"
        if [ -f "$SCRIPT_DIR/linux-build.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/linux-build.sh"
        else
            echo -e "${RED}✗ 错误: linux-build.sh 不存在${NC}"
            exit 1
        fi
        ;;
    13)
        echo -e "${YELLOW}执行Windows专用构建...${NC}"
        if [ -f "$SCRIPT_DIR/windows-build.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/windows-build.sh"
        else
            echo -e "${RED}✗ 错误: windows-build.sh 不存在${NC}"
            exit 1
        fi
        ;;
    14)
        echo -e "${YELLOW}执行Web专用构建...${NC}"
        if [ -f "$SCRIPT_DIR/web-build.sh" ]; then
            cd "$PROJECT_ROOT"
            "$SCRIPT_DIR/web-build.sh"
        else
            echo -e "${RED}✗ 错误: web-build.sh 不存在${NC}"
            exit 1
        fi
        ;;
    0)
        echo -e "${YELLOW}退出${NC}"
        exit 0
        ;;
    *)
        echo -e "${RED}✗ 无效选择: $choice${NC}"
        exit 1
        ;;
esac

echo ""
echo -e "${GREEN}✓ 操作完成！${NC}"
