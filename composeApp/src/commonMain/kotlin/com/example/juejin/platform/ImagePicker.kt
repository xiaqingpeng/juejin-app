package com.example.juejin.platform

import androidx.compose.runtime.Composable

/**
 * 图片选择器选项
 */
enum class ImageSourceType {
    CAMERA,    // 拍照
    GALLERY    // 相册
}

/**
 * 图片选择器接口
 * 用于从相册选择图片或拍照
 */
expect class ImagePicker {
    /**
     * 显示图片选择器
     * @param sourceType 图片来源类型（相机或相册）
     * @param onImageSelected 选择图片后的回调，返回图片的 URI 或路径
     */
    fun pickImage(
        sourceType: ImageSourceType,
        onImageSelected: (String) -> Unit,
        onError: (String) -> Unit = {}
    )
}

/**
 * 创建图片选择器实例
 */
@Composable
expect fun rememberImagePicker(): ImagePicker
