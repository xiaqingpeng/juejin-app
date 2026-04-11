package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.ui.Colors
import com.example.juejin.ui.theme.ThemeColors
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.input_placeholder
import org.jetbrains.compose.resources.stringResource

/**
 * 编辑单个字段页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFieldScreen(
    title: String,
    initialValue: String,
    onSave: (String) -> Unit,
    onBack: () -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }
    
    com.example.juejin.ui.theme.AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            title,
                            color = ThemeColors.Text.primary,
                            fontSize = 18.sp
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "返回",
                                tint = ThemeColors.Text.primary
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                onSave(text)
                            }
                        ) {
                            Text(
                                "保存",
                                color = ThemeColors.primaryBlue,
                                fontSize = 16.sp
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ThemeColors.primaryWhite
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(ThemeColors.Background.primary)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { 
                        Text(
                            "请输入$title",
                            color = ThemeColors.Text.placeholder
                        ) 
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ThemeColors.Text.primary,
                        unfocusedTextColor = ThemeColors.Text.primary,
                        focusedBorderColor = ThemeColors.primaryBlue,
                        unfocusedBorderColor = ThemeColors.UI.divider,
                        focusedContainerColor = ThemeColors.primaryWhite,
                        unfocusedContainerColor = ThemeColors.primaryWhite,
                        focusedPlaceholderColor = ThemeColors.Text.placeholder,
                        unfocusedPlaceholderColor = ThemeColors.Text.placeholder
                    ),
                    maxLines = if (title == "简介") 5 else 1,
                    minLines = if (title == "简介") 3 else 1
                )
            }
        }
    }
}
