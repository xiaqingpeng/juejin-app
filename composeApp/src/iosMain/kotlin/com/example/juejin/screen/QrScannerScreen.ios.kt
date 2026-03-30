@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import com.example.juejin.ui.Colors
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.AVMetadataObjectTypeQRCode
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVAuthorizationStatusAuthorized

import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_async
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.UIKit.bounds
import platform.UIKit.backgroundColor
import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import platform.CoreGraphics.CGRect


import platform.QuartzCore.CATransaction


private class QrScannerUIView : UIView(frame = cValue<CGRect> { CGRectMake(0.0, 0.0, 0.0, 0.0) }) {
    var onQrCodeScanned: ((String) -> Unit)? = null

    private val session = AVCaptureSession()
    private val previewLayer: AVCaptureVideoPreviewLayer =
        AVCaptureVideoPreviewLayer().also { it.session = session }
    private val metadataOutput = AVCaptureMetadataOutput()

    private var didScan = false
    private var isSessionConfigured = false

    private val delegate =
        object : NSObject(), AVCaptureMetadataOutputObjectsDelegateProtocol {
            override fun captureOutput(
                output: AVCaptureOutput,
                didOutputMetadataObjects: List<*>,
                fromConnection: AVCaptureConnection
            ) {
                if (didScan) return
                val first =
                    didOutputMetadataObjects.firstOrNull() as?
                        AVMetadataMachineReadableCodeObject
                val value = first?.stringValue
                if (!value.isNullOrBlank()) {
                    didScan = true
                    onQrCodeScanned?.invoke(value)
                    // 找到后停止，避免重复触发
                    session.stopRunning()
                }
            }
        }

    init {
        backgroundColor = UIColor.blackColor
        previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
        this.layer.addSublayer(previewLayer)

        configureSession()
    }

    private fun configureSession() {
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
        val input = device?.let {
            try {
                AVCaptureDeviceInput.deviceInputWithDevice(it, null) as? AVCaptureDeviceInput
            } catch (e: Exception) {
                null
            }
        }

        if (input != null && session.canAddInput(input)) {
            session.addInput(input)
        } else {
            // 没有相机输入，无法工作
            return
        }

        if (session.canAddOutput(metadataOutput)) {
            session.addOutput(metadataOutput)
        }

        metadataOutput.setMetadataObjectsDelegate(delegate, dispatch_get_main_queue())
        
        // 标记配置完成
        isSessionConfigured = true
    }

    fun start() {
        if (!session.running && isSessionConfigured) {
            // 使用异步主队列，确保不阻塞当前 Compose 布局帧
            dispatch_async(dispatch_get_main_queue()) {
                session.startRunning()
                
                // 重要：session 启动后，获取实际支持的元数据类型
                val availableTypes = metadataOutput.availableMetadataObjectTypes
                // 检查 QR 码是否被支持
                if (availableTypes.contains(AVMetadataObjectTypeQRCode)) {
                    metadataOutput.metadataObjectTypes = listOf(AVMetadataObjectTypeQRCode)
                } else {
                    // 如果 QR 码不被支持，尝试设置所有支持的条形码类型
                    val barcodeTypes = availableTypes.filter { type ->
                        // 过滤出条形码相关的类型
                        type.toString().contains("Code") || 
                        type.toString().contains("QR") ||
                        type.toString().contains("bar")
                    }
                    if (barcodeTypes.isNotEmpty()) {
                        metadataOutput.metadataObjectTypes = barcodeTypes
                    }
                    println("QR Code not supported, using: $barcodeTypes")
                }
            }
        }
    }

    fun stop() {
        if (session.running) {
            session.stopRunning()
        }
    }
    
    override fun layoutSubviews() {
        super.layoutSubviews()
        // 确保布局刷新时 layer 跟随 view 尺寸
        CATransaction.begin()
        CATransaction.setDisableActions(true)
        previewLayer.frame = bounds
        CATransaction.commit()
    }
}

@Composable
actual fun QrScannerPreview(
    modifier: Modifier,
    onQrCodeScanned: (String) -> Unit
) {
    // 为了先保证可编译：这里先不做权限查询（权限被拒时相机会无法启动）。
    // 后续你如果要完整请求权限，我可以再按你当前 K/N 版本的 API 映射补全。
    var hasPermission by remember { mutableStateOf(true) }

    if (!hasPermission) {
        Box(
            modifier = modifier.fillMaxSize().background(Colors.Background.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "需要相机权限以扫描二维码")
        }
        return
    }

    val scannerView = remember { QrScannerUIView() }

    // 当 scannerView 准备好并进入 Composition 时启动
    LaunchedEffect(scannerView) {
        scannerView.start()
    }

    DisposableEffect(scannerView) {
        onDispose {
            scannerView.stop()
        }
    }

    UIKitView(
        factory = {
            scannerView.onQrCodeScanned = onQrCodeScanned
            scannerView
        },
        modifier = modifier,
        update = { view ->
            view.onQrCodeScanned = onQrCodeScanned
        }
    )
}

