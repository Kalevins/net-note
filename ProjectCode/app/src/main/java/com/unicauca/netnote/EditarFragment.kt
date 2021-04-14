package com.unicauca.netnote

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditarFragment : Fragment() {

    val CAMERA_REQUEST_CODE = 102
    val GALLERY_REQUEST_CODE = 105
    lateinit var selectedImage: ImageView
    lateinit var cam_buttom: ImageButton
    lateinit var gallery_buttom: ImageButton
    lateinit var mic_buttom: ImageButton
    lateinit var imageView: ImageView
    lateinit var currentPhotoPath: String
    lateinit var storageReference: StorageReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vi: View = inflater.inflate(R.layout.fragment_editar, container, false)
        return vi
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cam_buttom = view.findViewById(R.id.cam_image)
        gallery_buttom = view.findViewById(R.id.gallery_image)
        mic_buttom = view.findViewById(R.id.mic_image)
        imageView =  view.findViewById(R.id.Contenido_notas_Image)

        storageReference = FirebaseStorage.getInstance().reference

        gallery_buttom.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery, GALLERY_REQUEST_CODE)
        }

        cam_buttom.setOnClickListener {
            takePicture()
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        }catch (e: IOException){}
        if (photoFile != null){
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                "com.unicauca.android.fileprovider",
                photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
            startActivityForResult(intent,CAMERA_REQUEST_CODE)
        }
    }

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getActivity()?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            "JPEG_${timeStamp}",
            ".jpg",
            storageDir
        )

        currentPhotoPath = image.absolutePath

        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val contentUri = Uri.fromFile(File(currentPhotoPath));
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "JPEG_" + timeStamp + ".jpg"
                imageView.setImageURI(Uri.parse(currentPhotoPath))
                uploadImageToFirebase(imageFileName, contentUri)
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val contentUri = data!!.data
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri)
                imageView.setImageURI(contentUri)
                uploadImageToFirebase(imageFileName, contentUri)
            }
        }
    }

    private fun uploadImageToFirebase(name: String, contentUri: Uri?) {
        val image = storageReference.child("pictures/$name")
        image.putFile(contentUri!!).addOnSuccessListener {
            image.downloadUrl.addOnSuccessListener { uri ->
                Log.d("tag", "onSuccess: Uploaded Image URl is $uri")
            }
            Toast.makeText(requireActivity(), "Image Is Uploaded.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(requireActivity(), "Upload Failled.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExt(contentUri: Uri?): String? {
        val c: ContentResolver? = activity?.contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(c?.getType(contentUri!!))
    }
}
