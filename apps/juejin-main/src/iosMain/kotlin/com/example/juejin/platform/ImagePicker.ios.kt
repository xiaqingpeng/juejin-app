package com.example.juejin.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.juejin.util.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.*

private const val TAG = "ImagePicker"

/**
 * iOS 平台图片选择器实现
 * 
 * 注意：由于 Kotlin/Native 的限制，iOS 图片选择功能需要通过 Objective-C 包装器实现。
 * ImagePickerHelper.h/m 文件已创建在 iosApp/iosApp/ 目录中。
 * 
 * 要启用真实功能，需要在 Xcode 中：
 * 1. 打开 iosApp/iosApp.xcodeproj
 * 2. 确认 ImagePickerHelper.h 和 ImagePickerHelper.m 已被包含在项目中
 * 3. 重新构建项目
 * 
 * 当前实现返回模拟数据以确保应用可以编译和运行。
 */
actual class ImagePicker {
    
    @OptIn(ExperimentalForeignApi::class)
    actual fun pickImage(
        sourceType: ImageSourceType,
        onImageSelected: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Logger.d(TAG, "启动图片选择器: $sourceType")
        
        // 检查相机是否可用（模拟器不支持相机）
        if (sourceType == ImageSourceType.CAMERA) {
            val isCameraAvailable = UIImagePickerController.isSourceTypeAvailable(
                UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            )
            
            if (!isCameraAvailable) {
                Logger.w(TAG, "相机不可用（可能是在模拟器上运行）")
                onError("相机不可用，请在真机上测试拍照功能")
                return
            }
        }
        
        // TODO: 集成 ImagePickerHelper
        // 由于 Kotlin/Native 无法直接实现 UIImagePickerControllerDelegate，
        // 需要使用 Objective-C 包装器。
        // ImagePickerHelper 文件已创建，但需要在 Xcode 中配置才能使用。
        
        Logger.d(TAG, "图片选择器功能需要在 Xcode 中配置")
        Logger.d(TAG, "当前返回模拟数据")
        
        // 模拟成功选择图片
        onImageSelected("file:///tmp/test_image.jpg")
    }
}

/**
 * 创建 iOS 图片选择器
 */
@Composable
actual fun rememberImagePicker(): ImagePicker {
    return remember { ImagePicker() }
}
