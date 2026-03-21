package com.example.juejin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.juejin.storage.PrivacyStorage

class MainActivity : ComponentActivity() {
    companion object {
        var instance: MainActivity? = null
            private set
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        instance = this

        // 初始化隐私政策存储
        PrivacyStorage.init(application)

        setContent {
            App()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}