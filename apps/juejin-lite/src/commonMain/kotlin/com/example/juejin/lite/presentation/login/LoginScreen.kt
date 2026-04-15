package com.example.juejin.lite.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.juejin.core.common.CryptoUtil
import com.example.juejin.lite.di.AppContainer
import com.example.juejin.ui.theme.ThemeColors

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onClose: () -> Unit,
    viewModel: LoginViewModel = viewModel { AppContainer.getInstance().provideLoginViewModel() }
) {
    var account by remember { mutableStateOf("17304472875") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isRememberAccount by remember { mutableStateOf(true) }
    var isAgreeProtocol by remember { mutableStateOf(true) }  // 默认勾选协议
    
    val uiState by viewModel.uiState.collectAsState()
    
    var showProtocolWarning by remember { mutableStateOf(false) }
    var showEmptyWarning by remember { mutableStateOf(false) }
    
    // 监听登录成功
    LaunchedEffect(uiState.loginResult) {
        if (uiState.loginResult != null) {
            onLoginSuccess()
        }
    }
    
    // 显示错误提示
    if (uiState.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("登录失败") },
            text = { Text(uiState.errorMessage ?: "") },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("确定")
                }
            }
        )
    }
    
    // 显示协议提示
    if (showProtocolWarning) {
        AlertDialog(
            onDismissRequest = { showProtocolWarning = false },
            title = { Text("提示") },
            text = { Text("请先阅读并同意用户协议") },
            confirmButton = {
                TextButton(onClick = { showProtocolWarning = false }) {
                    Text("确定")
                }
            }
        )
    }
    
    // 显示空值提示
    if (showEmptyWarning) {
        AlertDialog(
            onDismissRequest = { showEmptyWarning = false },
            title = { Text("提示") },
            text = { Text("请输入账号和密码") },
            confirmButton = {
                TextButton(onClick = { showEmptyWarning = false }) {
                    Text("确定")
                }
            }
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF6900))
            .statusBarsPadding()
    ) {
        // 顶部关闭/注册按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 关闭按钮
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "关闭",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onClose() }
            )
            
            // 注册按钮
            Text(
                text = "注册",
                fontSize = 17.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* 跳转注册 */ }
            )
        }
        
        // 登录卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 标题
                Text(
                    text = "账号密码登录",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = ThemeColors.Text.primary
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 账号输入框
                OutlinedTextField(
                    value = account,
                    onValueChange = { account = it },
                    label = { Text("账号") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF6900),
                        focusedLabelColor = Color(0xFFFF6900)
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 密码输入框
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("密码") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) 
                        VisualTransformation.None 
                    else 
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (isPasswordVisible) 
                            Icons.Default.Visibility 
                        else 
                            Icons.Default.VisibilityOff
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(icon, contentDescription = "密码可见性")
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF6900),
                        focusedLabelColor = Color(0xFFFF6900)
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 记住账号 + 找回密码
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 记住账号
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isRememberAccount = !isRememberAccount }
                    ) {
                        Checkbox(
                            checked = isRememberAccount,
                            onCheckedChange = { isRememberAccount = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFFF6900)
                            )
                        )
                        Text(
                            text = "保存账号密码",
                            fontSize = 14.sp,
                            color = ThemeColors.Text.primary
                        )
                    }
                    
                    // 找回密码
                    Text(
                        text = "找回账号密码",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.clickable { /* 跳转找回密码 */ }
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 登录按钮
                Button(
                    onClick = {
                        if (!isAgreeProtocol) {
                            showProtocolWarning = true
                            return@Button
                        }
                        if (account.isEmpty() || password.isEmpty()) {
                            showEmptyWarning = true
                            return@Button
                        }
                        // MD5 加密密码
                        val encryptedPassword = CryptoUtil.md5(password)
                        viewModel.login(account, encryptedPassword)
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6900),
                        disabledContainerColor = Color(0xFFFFB380)
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "登录",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 协议同意
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isAgreeProtocol = !isAgreeProtocol }
                ) {
                    Checkbox(
                        checked = isAgreeProtocol,
                        onCheckedChange = { isAgreeProtocol = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFFFF6900)
                        )
                    )
                    Text(
                        text = "已认真阅读并同意以下协议:服务协议，隐私声明",
                        fontSize = 13.sp,
                        color = Color(0xFF666666),
                        lineHeight = 18.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 第三方登录标题
                Text(
                    text = "其他登录方式",
                    fontSize = 14.sp,
                    color = Color(0xFF999999),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // 第三方登录按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 微信登录
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onLoginSuccess() }
                    ) {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            color = Color(0xFF07C160)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = "微信",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "微信登录",
                            fontSize = 13.sp,
                            color = ThemeColors.Text.secondary
                        )
                    }
                    
                    // 一键登录
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onLoginSuccess() }
                    ) {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            color = Color(0xFF1890FF)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "一键登录",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "本机号码一键登录",
                            fontSize = 13.sp,
                            color = ThemeColors.Text.secondary
                        )
                    }
                }
            }
        }
    }
}
