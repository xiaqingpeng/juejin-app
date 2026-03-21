package com.example.juejin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.model.LogStatsItem
import com.example.juejin.screen.components.DetailScreenCard
import com.example.juejin.screen.components.DetailScreenCardItem
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys
import com.example.juejin.ui.components.TopNavigationBarWithBack
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.course_detail_basic_info
import juejin.composeapp.generated.resources.course_detail_id
import juejin.composeapp.generated.resources.course_detail_ip_address
import juejin.composeapp.generated.resources.course_detail_network_info
import juejin.composeapp.generated.resources.course_detail_performance_info
import juejin.composeapp.generated.resources.course_detail_platform
import juejin.composeapp.generated.resources.course_detail_platform_name
import juejin.composeapp.generated.resources.course_detail_request_method
import juejin.composeapp.generated.resources.course_detail_request_path
import juejin.composeapp.generated.resources.course_detail_request_time
import juejin.composeapp.generated.resources.course_detail_response_time
import juejin.composeapp.generated.resources.no_data
import juejin.composeapp.generated.resources.tab_profile_request
import org.jetbrains.compose.resources.stringResource

/** 课程详情页面（二级页面） 展示单个日志统计项的详细数据 */
@Composable
fun CourseDetailScreen(logStat: LogStatsItem?, onBackClick: () -> Unit = {}) {
    Scaffold(
            topBar = {
                TopNavigationBarWithBack(
                        title = stringResource(Res.string.tab_profile_request),
                        onBackClick = onBackClick,
                        backgroundColor = Colors.white
                )
            }
    ) { paddingValues ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (logStat != null) {
                // 基本信息卡片
                DetailScreenCard(title = stringResource(Res.string.course_detail_basic_info)) {
                    DetailScreenCardItem(label = stringResource(Res.string.course_detail_id), value = "${logStat.id ?: "N/A"}")
                    DetailScreenCardItem(label = stringResource(Res.string.course_detail_request_path), value = logStat.path ?: "N/A")
                    DetailScreenCardItem(label = stringResource(Res.string.course_detail_request_method), value = logStat.method ?: "N/A")
                    DetailScreenCardItem(label = stringResource(Res.string.course_detail_request_time), value = logStat.requestTime ?: "N/A")
                }

                // 网络信息卡片
                DetailScreenCard(title = stringResource(Res.string.course_detail_network_info)) {
                    DetailScreenCardItem(label = stringResource(Res.string.course_detail_ip_address), value = logStat.ip ?: "N/A")
                    DetailScreenCardItem(label = stringResource(Res.string.course_detail_platform), value = logStat.platform ?: "N/A")
                    DetailScreenCardItem(label = stringResource(Res.string.course_detail_platform_name), value = logStat.platformName ?: "N/A")
                }

                // 性能信息卡片
                DetailScreenCard(title = stringResource(Res.string.course_detail_performance_info)) {
                    DetailScreenCardItem(label = stringResource(Res.string.course_detail_response_time), value = "${logStat.durationMs ?: 0} ms")
                }
            } else {
                // 数据为空提示
                Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                ) { Text(text = stringResource(Res.string.no_data), style = Typographys.screenTitle, color = Color.Gray) }
            }
        }
    }
}
