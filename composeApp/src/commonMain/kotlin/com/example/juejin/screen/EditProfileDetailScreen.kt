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
import com.example.juejin.platform.rememberImagePicker
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
    onSaveSuccess: () -> Unit = {},
    viewModel: com.example.juejin.viewmodel.UserViewModel = com.example.juejin.viewmodel.UserViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var editingField by remember { mutableStateOf<EditField?>(null) }
    var showImageSourceSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val imagePicker = rememberImagePicker()
    
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
                            .clickable { showImageSourceSheet = true },
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
                            editingField = EditField(
                                label = "用户名",
                                initialValue = user.username,
                                maxLength = 20,
                                validate = com.example.juejin.util.ValidationUtil::validateUsername
                            ) { newValue ->
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
                            editingField = EditField(
                                label = "职位",
                                initialValue = user.position,
                                maxLength = 30,
                                validate = com.example.juejin.util.ValidationUtil::validatePosition
                            ) { newValue ->
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
                            editingField = EditField(
                                label = "公司",
                                initialValue = user.company,
                                maxLength = 30,
                                validate = com.example.juejin.util.ValidationUtil::validateCompany
                            ) { newValue ->
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
                            editingField = EditField(
                                label = "简介",
                                initialValue = user.bio,
                                maxLength = 100,
                                isMultiLine = true,
                                validate = com.example.juejin.util.ValidationUtil::validateBio
                            ) { newValue ->
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
                            editingField = EditField(
                                label = "博客地址",
                                initialValue = user.blogUrl,
                                maxLength = 100,
                                validate = com.example.juejin.util.ValidationUtil::validateBlogUrl
                            ) { newValue ->
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
                            editingField = EditField(
                                label = "GitHub",
                                initialValue = user.github,
                                maxLength = 50,
                                validate = com.example.juejin.util.ValidationUtil::validateGithub
                            ) { newValue ->
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
                            editingField = EditField(
                                label = "微博",
                                initialValue = user.weibo,
                                maxLength = 50,
                                validate = com.example.juejin.util.ValidationUtil::validateWeibo
                            ) { newValue ->
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
                                    // 保存成功后跳转回"我的"页面
                                    onSaveSuccess()
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
        
        // 图片来源选择弹窗
        if (showImageSourceSheet) {
            com.example.juejin.ui.components.ImageSourceBottomSheet(
                onDismiss = { 
                    println("[EditProfile] 关闭图片来源选择弹窗")
                    showImageSourceSheet = false 
                },
                onSourceSelected = { sourceType ->
                    println("[EditProfile] 选择了图片来源: $sourceType")
                    imagePicker.pickImage(
                        sourceType = sourceType,
                        onImageSelected = { imageUri ->
                            println("[EditProfile] 选择的图片: $imageUri")
                            viewModel.updateAvatar(imageUri)
                        },
                        onError = { error ->
                            println("[EditProfile] 选择图片失败: $error")
                        }
                    )
                }
            )
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
    var errorMessage by remember { mutableStateOf("") }
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
                    // 实时验证
                    errorMessage = field.validate(it).errorMessage
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(if (field.isMultiLine) 120.dp else 56.dp),
            placeholder = { Text("请输入${field.label}") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (errorMessage.isNotEmpty()) Color.Red else Colors.primaryBlue,
                unfocusedBorderColor = if (errorMessage.isNotEmpty()) Color.Red else Colors.UI.divider,
                focusedContainerColor = Colors.Background.surface,
                unfocusedContainerColor = Colors.Background.input
            ),
            maxLines = if (field.isMultiLine) 5 else 1,
            singleLine = !field.isMultiLine,
            isError = errorMessage.isNotEmpty()
        )
        
        // 错误提示或字数统计
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        } else {
            Text(
                text = "$charCount/${field.maxLength}",
                fontSize = 12.sp,
                color = if (charCount >= field.maxLength) Color.Red else Colors.Text.secondary,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 确定按钮
        Button(
            onClick = { 
                val validationResult = field.validate(text)
                if (validationResult.isValid) {
                    onConfirm(text)
                } else {
                    errorMessage = validationResult.errorMessage
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Colors.primaryBlue
            ),
            enabled = errorMessage.isEmpty()
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
    val validate: (String) -> com.example.juejin.util.ValidationResult = { com.example.juejin.util.ValidationResult(true) },
    val onValueChange: (String) -> Unit
)
