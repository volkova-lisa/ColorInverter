package com.example.colorinverter1

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.graphics.set
import com.example.colorinverter1.databinding.FragmentInverterViaCoroutinesBinding
import com.example.colorinverter1.databinding.FragmentInverterViaThreadBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InverterViaCoroutinesFragment : Fragment() {

    lateinit var progressBar: ProgressBar
    lateinit var uploadButton : Button
    lateinit var photoView : ImageView
    lateinit var imageBitmap : Bitmap
    private var _binding : FragmentInverterViaCoroutinesBinding? = null
    val mBinding get() = _binding!!

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInverterViaCoroutinesBinding.inflate(layoutInflater, container, false)
        mBinding.uploadButton.setOnClickListener{uploadImageGallery()}

        return mBinding.root
    }

    private fun uploadImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, data?.data!!)).copy(
                    Bitmap.Config.RGBA_F16, true)
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data?.data).copy(
                    Bitmap.Config.RGBA_F16, true)
            }
            GlobalScope.launch {
                processBitmap()
            }
//            mBinding.photoView.setImageURI(data?.data)
        }
    }

    private suspend fun processBitmap() {
            for (i in 0 until imageBitmap.width) {
                for (j in 0 until imageBitmap.height) {
                    val red = 255 - Color.red(imageBitmap.getPixel(i, j))
                    val green = 255 - Color.green(imageBitmap.getPixel(i, j))
                    val blue = 255 - Color.blue(imageBitmap.getPixel(i, j))

                    imageBitmap[i, j] = Color.rgb(red, green, blue)
                }
            }
            activity?.runOnUiThread {
                mBinding.photoView.setImageBitmap(imageBitmap)
            }
    }
}