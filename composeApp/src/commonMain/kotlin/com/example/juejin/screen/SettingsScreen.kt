package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.model.UserInfo
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys
import com.example.juejin.ui.components.TopNavigationBarWithBack
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_home
import juejin.composeapp.generated.resources.tab_profile_setting
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    userInfo: UserInfo? = null
) {
    Scaffold(
        topBar = {
            TopNavigationBarWithBack(
                title = stringResource(Res.string.tab_profile_setting),
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier =
                    Modifier.size(64.dp)
                        .background(Colors.primaryBlue, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(Res.string.tab_home),
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(text= stringResource(Res.string.tab_profile_setting), style = Typographys.screenTitle, modifier = Modifier.padding(top = 16.dp))
            // Display user info
            userInfo?.let {
                Text(text = "ID: ${it.id}", style = Typographys.screenTitle, modifier = Modifier.padding(top = 8.dp))
                Text(text = "Name: ${it.name}", style = Typographys.screenTitle, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}
