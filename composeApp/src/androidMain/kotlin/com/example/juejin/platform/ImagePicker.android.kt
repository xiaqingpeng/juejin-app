package com.example.juejin.platform

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.juejin.util.Logger
import java.io.File

private const val TAG = "ImagePicker"

/**
 * Android 平台图片选择器实现
 */
actual class ImagePicker(
    private val context: Context
) {
    var galleryLauncher: ManagedActivityResultLauncher<String, Uri?>? = null
    var cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>? = null
    private var tempPhotoUri: Uri? = null
    private var onImageSelectedCallback: ((String) -> Unit)? = null
    private var onErrorCallback: ((String) -> Unit)? = null
    
    actual fun pickImage(
        sourceType: ImageSourceType,
        onImageSelected: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        onImageSelectedCallback = onImageSelected
        onErrorCallback = onError
        
        when (sourceType) {
            ImageSourceType.GALLERY -> {
                Logger.d(TAG, "启动相册选择")
                try {
                    galleryLauncher?.launch("image/*")
                } catch (e: Exception) {
                    Logger.e(TAG, "启动相册失败", e)
                    onError("启动相册失败: ${e.message}")
                }
            }
            ImageSourceType.CAMERA -> {
                Logger.d(TAG, "准备启动相机")
                try {
                    // 创建临时文件用于存储拍照结果
                    val photoFile = File.createTempFile(
                        "avatar_${System.currentTimeMillis()}",
                        ".jpg",
                        context.cacheDir
                    )
                    
                    tempPhotoUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        photoFile
                    )
                    
                    Logger.d(TAG, "临时文件创建成功: ${photoFile.absolutePath}")
                    Logger.d(TAG, "URI: $tempPhotoUri")
                    
                    tempPhotoUri?.let { uri ->
                        cameraLauncher?.launch(uri)
                        Logger.d(TAG, "相机启动器已调用")
                    } ?: run {
                        Logger.e(TAG, "URI 为空")
                        onError("创建临时文件失败")
                    }
                } catch (e: Exception) {
                    Logger.e(TAG, "启动相机异常", e)
                    onError("启动相机失败: ${e.message}")
                }
            }
        }
    }
    
    fun handleGalleryResult(uri: Uri?) {
        Logger.d(TAG, "相册选择结果: $uri")
        if (uri != null) {
            onImageSelectedCallback?.invoke(uri.toString())
        } else {
            onErrorCallback?.invoke("未选择图片")
        }
    }
    
    fun handleCameraResult(success: Boolean) {
        Logger.d(TAG, "拍照结果: success=$success, tempPhotoUri=$tempPhotoUri")
        if (success && tempPhotoUri != null) {
            onImageSelectedCallback?.invoke(tempPhotoUri.toString())
        } else {
            onErrorCallback?.invoke("拍照失败")
        }
    }
}

/**
 * 创建 Android 图片选择器
 */
@Composable
actual fun rememberImagePicker(): ImagePicker {
    val context = LocalContext.current
    val picker = remember { ImagePicker(context) }
    
    Logger.d(TAG, "创建 ImagePicker 实例")
    
    // 相册选择器
    picker.galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        Logger.d(TAG, "相册 launcher 回调: $uri")
        picker.handleGalleryResult(uri)
    }
    
    // 相机启动器
    picker.cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        Logger.d(TAG, "相机 launcher 回调: $success")
        picker.handleCameraResult(success)
    }
    
    return picker
}
