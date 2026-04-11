package com.example.juejin.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

/**
 * JVM/Desktop 平台图片选择器实现
 */
actual class ImagePicker {
    actual fun pickImage(
        sourceType: ImageSourceType,
        onImageSelected: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        when (sourceType) {
            ImageSourceType.GALLERY -> {
                try {
                    val fileDialog = FileDialog(null as Frame?, "选择图片", FileDialog.LOAD)
                    fileDialog.setFilenameFilter { _, name ->
                        name.lowercase().endsWith(".jpg") ||
                        name.lowercase().endsWith(".jpeg") ||
                        name.lowercase().endsWith(".png") ||
                        name.lowercase().endsWith(".gif")
                    }
                    fileDialog.isVisible = true
                    
                    val directory = fileDialog.directory
                    val file = fileDialog.file
                    
                    if (directory != null && file != null) {
                        val selectedFile = File(directory, file)
                        onImageSelected(selectedFile.absolutePath)
                    } else {
                        onError("未选择图片")
                    }
                } catch (e: Exception) {
                    onError("选择图片失败: ${e.message}")
                }
            }
            ImageSourceType.CAMERA -> {
                // Desktop 平台不支持相机
                onError("Desktop 平台不支持相机功能")
            }
        }
    }
}

/**
 * 创建 JVM 图片选择器
 */
@Composable
actual fun rememberImagePicker(): ImagePicker {
    return remember { ImagePicker() }
}
