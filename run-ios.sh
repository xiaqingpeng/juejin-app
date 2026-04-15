#!/bin/bash

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== 掘金 iOS 应用启动器 ===${NC}\n"

# 检查是否在 macOS 上
if [[ "$OSTYPE" != "darwin"* ]]; then
    echo -e "${RED}❌ 错误: iOS 开发只能在 macOS 上进行${NC}"
    exit 1
fi

# 检查 Xcode 是否安装
if ! command -v xcodebuild &> /dev/null; then
    echo -e "${RED}❌ 错误: 未找到 Xcode${NC}"
    echo "请从 App Store 安装 Xcode"
    exit 1
fi

# 显示菜单
echo "请选择要运行的应用："
echo "1) juejin-main（完整版）"
echo "2) juejin-lite（轻量版）- 需要创建 iOS 项目"
echo "3) 只编译 Framework"
echo "4) 清理并重新编译"
echo "5) 退出"
echo ""
read -p "请输入选择 (1-5): " choice

case $choice in
    1)
        echo -e "\n${YELLOW}步骤 1: 编译 juejin-main Framework...${NC}"
        ./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64 --no-daemon
        
        if [ $? -ne 0 ]; then
            echo -e "${RED}❌ Framework 编译失败${NC}"
            exit 1
        fi
        
        echo -e "${GREEN}✅ Framework 编译成功${NC}\n"
        
        echo -e "${YELLOW}步骤 2: 启动 iOS 模拟器...${NC}"
        
        # 查找可用的模拟器（提取 UUID）
        SIMULATOR=$(xcrun simctl list devices available | grep "iPhone" | head -1 | grep -oE '[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}')
        
        if [ -z "$SIMULATOR" ]; then
            echo -e "${RED}❌ 未找到可用的 iOS 模拟器${NC}"
            echo "请在 Xcode 中创建一个 iPhone 模拟器"
            exit 1
        fi
        
        SIMULATOR_NAME=$(xcrun simctl list devices available | grep "$SIMULATOR" | sed 's/^ *//;s/ (.*//')
        echo -e "${BLUE}使用模拟器: $SIMULATOR_NAME${NC}"
        echo -e "${BLUE}UUID: $SIMULATOR${NC}"
        
        # 启动模拟器
        xcrun simctl boot "$SIMULATOR" 2>/dev/null || true
        open -a Simulator
        
        echo -e "\n${YELLOW}步骤 3: 构建并运行应用...${NC}"
        
        # 使用 xcodebuild 构建并运行
        cd iosApp
        xcodebuild -project iosApp.xcodeproj \
                   -scheme iosApp \
                   -configuration Debug \
                   -destination "id=$SIMULATOR" \
                   -allowProvisioningUpdates \
                   build
        
        if [ $? -eq 0 ]; then
            echo -e "\n${GREEN}✅ 应用构建成功${NC}"
            
            # 安装应用
            APP_PATH=$(find ~/Library/Developer/Xcode/DerivedData -name "iosApp.app" | head -1)
            if [ -n "$APP_PATH" ]; then
                xcrun simctl install "$SIMULATOR" "$APP_PATH"
                
                # 获取 Bundle ID
                BUNDLE_ID=$(defaults read "$APP_PATH/Info.plist" CFBundleIdentifier)
                
                # 启动应用
                xcrun simctl launch "$SIMULATOR" "$BUNDLE_ID"
                
                echo -e "${GREEN}✅ 应用已启动${NC}"
            else
                echo -e "${YELLOW}⚠️  应用已构建，请在模拟器中手动启动${NC}"
            fi
        else
            echo -e "${RED}❌ 应用构建失败${NC}"
            echo -e "\n${YELLOW}建议：在 Xcode 中打开项目进行调试${NC}"
            echo "cd iosApp && open iosApp.xcodeproj"
            exit 1
        fi
        ;;
        
    2)
        echo -e "\n${YELLOW}步骤 1: 编译 juejin-lite Framework...${NC}"
        ./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64 --no-daemon
        
        if [ $? -ne 0 ]; then
            echo -e "${RED}❌ Framework 编译失败${NC}"
            exit 1
        fi
        
        echo -e "${GREEN}✅ Framework 编译成功${NC}\n"
        
        # 检查 iosApp-lite 是否存在
        if [ ! -d "iosApp-lite" ]; then
            echo -e "${RED}❌ iosApp-lite 项目不存在${NC}"
            echo -e "${YELLOW}请先运行配置脚本：${NC}"
            echo "./setup-ios-lite.sh"
            exit 1
        fi
        
        echo -e "${YELLOW}步骤 2: 启动 iOS 模拟器...${NC}"
        
        # 查找可用的模拟器（提取 UUID）
        SIMULATOR=$(xcrun simctl list devices available | grep "iPhone" | head -1 | grep -oE '[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}')
        
        if [ -z "$SIMULATOR" ]; then
            echo -e "${RED}❌ 未找到可用的 iOS 模拟器${NC}"
            echo "请在 Xcode 中创建一个 iPhone 模拟器"
            exit 1
        fi
        
        SIMULATOR_NAME=$(xcrun simctl list devices available | grep "$SIMULATOR" | sed 's/^ *//;s/ (.*//')
        echo -e "${BLUE}使用模拟器: $SIMULATOR_NAME${NC}"
        echo -e "${BLUE}UUID: $SIMULATOR${NC}"
        
        # 启动模拟器
        xcrun simctl boot "$SIMULATOR" 2>/dev/null || true
        open -a Simulator
        
        echo -e "\n${YELLOW}步骤 3: 构建并运行应用...${NC}"
        
        # 使用 xcodebuild 构建并运行
        cd iosApp-lite
        xcodebuild -project iosApp.xcodeproj \
                   -scheme iosApp \
                   -configuration Debug \
                   -destination "id=$SIMULATOR" \
                   -allowProvisioningUpdates \
                   build
        
        if [ $? -eq 0 ]; then
            echo -e "\n${GREEN}✅ 应用构建成功${NC}"
            
            # 安装应用
            APP_PATH=$(find ~/Library/Developer/Xcode/DerivedData -name "iosApp.app" -path "*/iosApp-lite/*" | head -1)
            if [ -n "$APP_PATH" ]; then
                xcrun simctl install "$SIMULATOR" "$APP_PATH"
                
                # 获取 Bundle ID
                BUNDLE_ID=$(defaults read "$APP_PATH/Info.plist" CFBundleIdentifier)
                
                # 启动应用
                xcrun simctl launch "$SIMULATOR" "$BUNDLE_ID"
                
                echo -e "${GREEN}✅ 应用已启动${NC}"
            else
                echo -e "${YELLOW}⚠️  应用已构建，请在模拟器中手动启动${NC}"
            fi
        else
            echo -e "${RED}❌ 应用构建失败${NC}"
            echo -e "\n${YELLOW}建议：在 Xcode 中打开项目进行调试${NC}"
            echo "cd iosApp-lite && open iosApp.xcodeproj"
            exit 1
        fi
        
        cd ..
        ;;
        
    3)
        echo -e "\n${YELLOW}选择要编译的 Framework:${NC}"
        echo "1) juejin-main"
        echo "2) juejin-lite"
        read -p "请选择 (1-2): " framework_choice
        
        case $framework_choice in
            1)
                echo -e "\n${YELLOW}编译 juejin-main Framework...${NC}"
                ./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64 --no-daemon
                echo -e "\n${GREEN}✅ Framework 位置:${NC}"
                echo "apps/juejin-main/build/bin/iosSimulatorArm64/debugFramework/ComposeApp.framework"
                ;;
            2)
                echo -e "\n${YELLOW}编译 juejin-lite Framework...${NC}"
                ./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64 --no-daemon
                echo -e "\n${GREEN}✅ Framework 位置:${NC}"
                echo "apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/JuejinLite.framework"
                ;;
            *)
                echo -e "${RED}无效的选择${NC}"
                exit 1
                ;;
        esac
        ;;
        
    4)
        echo -e "\n${YELLOW}清理并重新编译...${NC}"
        
        echo "1. 清理 Gradle 构建"
        ./gradlew clean
        
        echo "2. 删除 Kotlin Native 缓存"
        rm -rf ~/.konan
        
        echo "3. 清理 iOS 模拟器"
        xcrun simctl delete unavailable
        
        echo "4. 清理 Xcode DerivedData"
        rm -rf ~/Library/Developer/Xcode/DerivedData
        
        echo -e "\n${GREEN}✅ 清理完成${NC}"
        echo "现在可以重新编译和运行"
        ;;
        
    5)
        echo -e "${GREEN}再见！${NC}"
        exit 0
        ;;
        
    *)
        echo -e "${RED}无效的选择${NC}"
        exit 1
        ;;
esac

echo -e "\n${BLUE}=== 完成 ===${NC}"
