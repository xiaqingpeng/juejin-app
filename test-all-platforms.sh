#!/bin/bash

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== 多平台编译测试 ===${NC}\n"

# 测试结果数组
declare -a results

# 测试函数
test_build() {
    local app=$1
    local platform=$2
    local task=$3
    local description=$4
    
    echo -e "${YELLOW}测试: $description${NC}"
    echo "命令: ./gradlew $task --no-daemon"
    
    start_time=$(date +%s)
    if ./gradlew $task --no-daemon > /dev/null 2>&1; then
        end_time=$(date +%s)
        duration=$((end_time - start_time))
        echo -e "${GREEN}✅ 成功 (${duration}秒)${NC}\n"
        results+=("✅ $description - ${duration}秒")
        return 0
    else
        echo -e "${RED}❌ 失败${NC}\n"
        results+=("❌ $description - 失败")
        return 1
    fi
}

echo -e "${BLUE}=== juejin-main（完整版）===${NC}\n"

test_build "juejin-main" "Android" ":apps:juejin-main:assembleDebug" "juejin-main - Android Debug"
test_build "juejin-main" "Android" ":apps:juejin-main:assembleRelease" "juejin-main - Android Release"
test_build "juejin-main" "Desktop" ":apps:juejin-main:jvmJar" "juejin-main - Desktop JAR"
test_build "juejin-main" "iOS" ":apps:juejin-main:linkDebugFrameworkIosSimulatorArm64" "juejin-main - iOS Simulator"

echo -e "${BLUE}=== juejin-lite（轻量版）===${NC}\n"

test_build "juejin-lite" "Android" ":apps:juejin-lite:assembleDebug" "juejin-lite - Android Debug"
test_build "juejin-lite" "Android" ":apps:juejin-lite:assembleRelease" "juejin-lite - Android Release"
test_build "juejin-lite" "Desktop" ":apps:juejin-lite:jvmJar" "juejin-lite - Desktop JAR"
test_build "juejin-lite" "iOS" ":apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64" "juejin-lite - iOS Simulator"

# 显示总结
echo -e "\n${BLUE}=== 测试总结 ===${NC}\n"
for result in "${results[@]}"; do
    echo -e "$result"
done

# 统计成功/失败
success_count=$(echo "${results[@]}" | grep -o "✅" | wc -l | tr -d ' ')
total_count=${#results[@]}

echo -e "\n${BLUE}总计: $success_count/$total_count 成功${NC}"

if [ $success_count -eq $total_count ]; then
    echo -e "${GREEN}🎉 所有平台编译成功！${NC}"
    exit 0
else
    echo -e "${RED}⚠️  部分平台编译失败${NC}"
    exit 1
fi
