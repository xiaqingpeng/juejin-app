package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.juejin.model.User
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBarWithBack
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.confirm
import juejin.composeapp.generated.resources.input_placeholder
import juejin.composeapp.generated.resources.save
import juejin.composeapp.generated.resources.saving
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * 编辑资料详情页面（底部弹窗编辑版本）
 * 点击字段弹出底部编辑弹窗
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDetailScreen(
    onLeftClick: () -> Unit,
    viewModel: com.example.juejin.viewmodel.UserViewModel = com.example.juejin.viewmodel.UserViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var editingField by remember { mutableStateOf<EditField?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Colors.Background.primary,
            surface = Colors.Background.surface
        )
    ) {
        Scaffold(
            topBar = {
                TopNavigationBarWithBack(
                    title = "资料修改",
                    onLeftClick = onLeftClick
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Colors.Background.primary)
            ) {
                // 头像
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Colors.primaryWhite)
                            .padding(16.dp)
                            .clickable { /* TODO: 选择头像 */ },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "头像",
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp,
                            color = Colors.Text.primary
                        )
                        
                        if (user.avatar.isNotEmpty()) {
                            AsyncImage(
                                model = user.avatar,
                                contentDescription = "头像",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Colors.UI.avatar)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = Colors.Text.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    HorizontalDivider(color = Colors.Background.primary, thickness = 8.dp)
                }
                
                // 用户名
                item {
                    ProfileItem(
                        label = "用户名",
                        value = user.username,
                        onClick = {
                            editingField = EditField("用户名", user.username, 20) { newValue ->
                                viewModel.updateUsername(newValue)
                            }
                        }
                    )
                }
                
                // 职位
                item {
                    ProfileItem(
                        label = "职位",
                        value = user.position,
                        onClick = {
                            editingField = EditField("职位", user.position, 30) { newValue ->
                                viewModel.updatePosition(newValue)
                            }
                        }
                    )
                }
                
                // 公司
                item {
                    ProfileItem(
                        label = "公司",
                        value = user.company,
                        onClick = {
                            editingField = EditField("公司", user.company, 30) { newValue ->
                                viewModel.updateCompany(newValue)
                            }
                        }
                    )
                }
                
                // 简介
                item {
                    ProfileItem(
                        label = "简介",
                        value = user.bio,
                        onClick = {
                            editingField = EditField("简介", user.bio, 100, isMultiLine = true) { newValue ->
                                viewModel.updateBio(newValue)
                            }
                        }
                    )
                }
                
                // 博客地址
                item {
                    ProfileItem(
                        label = "博客地址",
                        value = user.blogUrl,
                        onClick = {
                            editingField = EditField("博客地址", user.blogUrl, 100) { newValue ->
                                viewModel.updateBlogUrl(newValue)
                            }
                        }
                    )
                }
                
                // GitHub
                item {
                    ProfileItem(
                        label = "GitHub",
                        value = user.github,
                        onClick = {
                            editingField = EditField("GitHub", user.github, 50) { newValue ->
                                viewModel.updateGithub(newValue)
                            }
                        }
                    )
                }
                
                // 微博
                item {
                    ProfileItem(
                        label = "微博",
                        value = user.weibo,
                        onClick = {
                            editingField = EditField("微博", user.weibo, 50) { newValue ->
                                viewModel.updateWeibo(newValue)
                            }
                        },
                        isLast = true
                    )
                }
                
                // 保存按钮
                item {
                    Button(
                        onClick = {
                            viewModel.saveUserInfo(
                                onSuccess = {
                                    println("保存成功")
                                    // 可以显示 Toast 提示
                                },
                                onError = { error ->
                                    println("保存失败: $error")
                                    // 可以显示错误提示
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Colors.primaryBlue
                        ),
                        enabled = !isLoading
                    ) {
                        Text(
                            if (isLoading) stringResource(Res.string.saving) 
                            else stringResource(Res.string.save),
                            color = Colors.primaryWhite,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
        
        // 底部编辑弹窗
        if (editingField != null) {
            ModalBottomSheet(
                onDismissRequest = { editingField = null },
                sheetState = sheetState,
                containerColor = Colors.primaryWhite,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                EditBottomSheet(
                    field = editingField!!,
                    onConfirm = { newValue ->
                        editingField!!.onValueChange(newValue)
                        scope.launch {
                            sheetState.hide()
                            editingField = null
                        }
                    },
                    onDismiss = {
                        scope.launch {
                            sheetState.hide()
                            editingField = null
                        }
                    }
                )
            }
        }
    }
}

/**
 * 资料列表项组件
 */
@Composable
private fun ProfileItem(
    label: String,
    value: String,
    onClick: () -> Unit,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Colors.primaryWhite)
                .padding(16.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                label,
                fontSize = 16.sp,
                color = Colors.Text.primary,
                modifier = Modifier.weight(1f)
            )
            
            Text(
                value.ifEmpty { "未填写" },
                color = if (value.isEmpty()) Colors.Text.secondary else Colors.Text.primary,
                fontSize = 14.sp,
                maxLines = 1
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Colors.Text.secondary,
                modifier = Modifier.size(16.dp)
            )
        }
        
        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Colors.UI.divider
            )
        }
    }
}

/**
 * 底部编辑弹窗
 */
@Composable
private fun EditBottomSheet(
    field: EditField,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(field.initialValue) }
    val charCount = text.length
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding() // 键盘顶起
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "编辑${field.label}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.Text.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 输入框
        OutlinedTextField(
            value = text,
            onValueChange = { 
                if (it.length <= field.maxLength) {
                    text = it
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(if (field.isMultiLine) 120.dp else 56.dp),
            placeholder = { Text("请输入${field.label}") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Colors.primaryBlue,
                unfocusedBorderColor = Colors.UI.divider,
                focusedContainerColor = Colors.Background.surface,
                unfocusedContainerColor = Colors.Background.input
            ),
            maxLines = if (field.isMultiLine) 5 else 1,
            singleLine = !field.isMultiLine
        )
        
        // 字数统计
        Text(
            text = "$charCount/${field.maxLength}",
            fontSize = 12.sp,
            color = if (charCount >= field.maxLength) Color.Red else Colors.Text.secondary,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 确定按钮
        Button(
            onClick = { onConfirm(text) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Colors.primaryBlue
            )
        ) {
            Text(stringResource(Res.string.confirm), color = Colors.primaryWhite, fontSize = 16.sp)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * 编辑字段数据类
 */
private data class EditField(
    val label: String,
    val initialValue: String,
    val maxLength: Int,
    val isMultiLine: Boolean = false,
    val onValueChange: (String) -> Unit
)
