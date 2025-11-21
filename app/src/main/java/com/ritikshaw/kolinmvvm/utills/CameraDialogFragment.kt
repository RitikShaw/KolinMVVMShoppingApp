package com.ritikshaw.kolinmvvm.utills

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log.e
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ritikshaw.kolinmvvm.R
import com.ritikshaw.kolinmvvm.activity.viewModel.CameraSharedViewModel
import com.ritikshaw.kolinmvvm.databinding.FragmentCameraDialogBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraDialogFragment: DialogFragment() {
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageCapture: ImageCapture
    private var lensFacing = CameraSelector.LENS_FACING_BACK

    private var _binding: FragmentCameraDialogBinding? = null
    private val binding get() = _binding!!

    private val cameraViewModel : CameraSharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialogTheme
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()

        binding.cameraSwitchButton.setOnClickListener {
            if (lensFacing== CameraSelector.LENS_FACING_BACK){
                lensFacing = CameraSelector.LENS_FACING_FRONT
                startCamera()
            }
            else{
                lensFacing = CameraSelector.LENS_FACING_BACK
                startCamera()
            }
        }
        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider : ProcessCameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = binding.preview.surfaceProvider
            }

            imageCapture = ImageCapture.Builder().setTargetResolution(Size(720, 720)).build()

            try {
                cameraProvider.unbindAll()
                // IMPORTANT: Use viewLifecycleOwner for binding
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
                e("CameraDialog", "Use case binding failed", exc)
            }
        },
            ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto(){
        imageCapture.takePicture(cameraExecutor,object : ImageCapture.OnImageCapturedCallback(){
            override fun onCaptureSuccess(image: ImageProxy) {
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)

                val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                cameraViewModel.sendBitmap(bitmap)
                dismiss()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                exception.printStackTrace()
                cameraViewModel.sendBitmap(null)
                dismiss()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

}