package com.example.juejin.screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.juejin.ui.Colors
import com.example.juejin.ui.theme.ThemeColors
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGetImage::class)
@Composable
actual fun QrScannerPreview(
    modifier: Modifier,
    onQrCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = context as? Activity
    val mainHandler = remember { Handler(Looper.getMainLooper()) }

    var hasCameraPermission by
        remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            hasCameraPermission = granted
        }

    if (!hasCameraPermission || activity == null) {
        Column(
            modifier = modifier.fillMaxSize().background(ThemeColors.Background.primary).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "需要相机权限以扫描二维码")
            TextButton(onClick = { requestPermissionLauncher.launch(Manifest.permission.CAMERA) }) {
                Text(text = "请求权限")
            }
        }
        return
    }

    val previewView = remember { PreviewView(context) }
    var cameraBound by remember { mutableStateOf(false) }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )

    // Initialize camera when the view is ready
    LaunchedEffect(previewView, lifecycleOwner) {
        if (cameraBound) return@LaunchedEffect
        
        val cameraProvider = ProcessCameraProvider.getInstance(context)
        val barcodeScanner = BarcodeScanning.getClient()
        val found = AtomicBoolean(false)

        val executor = Executors.newSingleThreadExecutor()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val analysis =
            ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

        analysis.setAnalyzer(executor) { imageProxy: ImageProxy ->
            if (found.get()) {
                imageProxy.close()
                return@setAnalyzer
            }

            val mediaImage = imageProxy.image
            if (mediaImage == null) {
                imageProxy.close()
                return@setAnalyzer
            }

            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)

            barcodeScanner
                .process(inputImage)
                .addOnSuccessListener { barcodes ->
                    val value = barcodes.firstOrNull()?.rawValue
                    if (!value.isNullOrBlank() && found.compareAndSet(false, true)) {
                        mainHandler.post { onQrCodeScanned(value) }
                    }
                }
                .addOnFailureListener {
                    // ignore
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }

        val cameraSelector =
            CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

        // Add a listener to handle the camera provider when it's available
        cameraProvider.addListener({
            try {
                val provider = cameraProvider.get()
                // Unbind all existing use cases before binding new ones
                provider.unbindAll()
                
                // Bind the camera to the lifecycle
                provider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, analysis)
                cameraBound = true
            } catch (e: Exception) {
                // Handle camera initialization errors
            }
        }, ContextCompat.getMainExecutor(context))
    }

    // Clean up resources when disposed
    DisposableEffect(Unit) {
        onDispose {
            val cameraProvider = ProcessCameraProvider.getInstance(context)
            cameraProvider.addListener({
                try {
                    val provider = cameraProvider.get()
                    provider.unbindAll()
                } catch (e: Exception) {
                    // Ignore cleanup errors
                }
            }, ContextCompat.getMainExecutor(context))
        }
    }
}

