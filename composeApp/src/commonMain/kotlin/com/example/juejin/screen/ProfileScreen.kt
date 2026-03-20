package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.model.UserInfo
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_profile
import juejin.composeapp.generated.resources.tab_profile_setting
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onSettingsClick: (UserInfo) -> Unit = {}) {
    Scaffold(
        topBar = {
            Surface(
                color = TopAppBarDefaults.topAppBarColors().containerColor,
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Title in center
//                    Text(
//                        text = stringResource(Res.string.tab_profile)
//                    )
                    // Settings icon on right
                    IconButton(
                        onClick = { onSettingsClick(UserInfo(id = 1, name = "我的")) },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(Res.string.tab_profile_setting),
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier =
                    Modifier.size(64.dp)
                        .background(Colors.primaryBlue, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = stringResource(Res.string.tab_profile),
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(stringResource(Res.string.tab_profile), style = Typographys.screenTitle, modifier = Modifier.padding(top = 16.dp))
        }
    }
}
