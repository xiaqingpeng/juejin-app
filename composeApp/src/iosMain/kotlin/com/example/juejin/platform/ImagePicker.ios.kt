package com.example.juejin.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

/**
 * iOS 平台图片选择器实现
 */
actual class ImagePicker {
    private var onImageSelectedCallback: ((String) -> Unit)? = null
    private var onErrorCallback: ((String) -> Unit)? = null
    
    @OptIn(ExperimentalForeignApi::class)
    actual fun pickImage(
        sourceType: ImageSourceType,
        onImageSelected: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        onImageSelectedCallback = onImageSelected
        onErrorCallback = onError
        
        val picker = UIImagePickerController()
        picker.sourceType = when (sourceType) {
            ImageSourceType.CAMERA -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            ImageSourceType.GALLERY -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        }
        
        // TODO: 设置 delegate 处理图片选择结果
        // 这需要实现 UIImagePickerControllerDelegate 协议
        // 由于 Kotlin/Native 的限制，这部分需要更复杂的实现
        
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(picker, animated = true, completion = null)
    }
}

/**
 * 创建 iOS 图片选择器
 */
@Composable
actual fun rememberImagePicker(): ImagePicker {
    return remember { ImagePicker() }
}
