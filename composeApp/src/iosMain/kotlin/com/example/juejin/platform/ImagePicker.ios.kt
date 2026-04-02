package com.example.juejin.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.juejin.util.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

private const val TAG = "ImagePicker"

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
        
        Logger.d(TAG, "启动图片选择器: $sourceType")
        
        val picker = UIImagePickerController()
        picker.sourceType = when (sourceType) {
            ImageSourceType.CAMERA -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            ImageSourceType.GALLERY -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        }
        
        // TODO: 设置 delegate 处理图片选择结果
        // 这需要实现 UIImagePickerControllerDelegate 协议
        // 由于 Kotlin/Native 的限制，这部分需要更复杂的实现
        
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        if (rootViewController != null) {
            rootViewController.presentViewController(picker, animated = true, completion = null)
            Logger.d(TAG, "图片选择器已显示")
        } else {
            Logger.e(TAG, "无法获取 rootViewController")
            onError("无法打开图片选择器")
        }
    }
}

/**
 * 创建 iOS 图片选择器
 */
@Composable
actual fun rememberImagePicker(): ImagePicker {
    return remember { ImagePicker() }
}
