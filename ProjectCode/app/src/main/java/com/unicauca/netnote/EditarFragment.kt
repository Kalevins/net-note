package com.unicauca.netnote

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
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
import android.content.pm.PackageManager


/*class EditarFragment : Fragment() {

    private lateinit var cam_buttom: ImageButton
    private lateinit var gallery_buttom: ImageButton
    private lateinit var mic_buttom: ImageButton
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var vi: View = inflater.inflate(R.layout.fragment_editar, container, false)
        return vi
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cam_buttom = view.findViewById(R.id.cam_image)
        gallery_buttom = view.findViewById(R.id.gallery_image)
        mic_buttom = view.findViewById(R.id.mic_image)
        imageView = view.findViewById(R.id.Contenido_notas)

        cam_buttom.setOnClickListener {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if (requestCode == 100){
            val bitmap: Bitmap = data?.extras?.get("data") as Bitmap
            Log.d(
                "Info",
                "-----------------------------------MOSTRAR FOTO---------------------------------"
            )
            imageView.setImageBitmap(bitmap)
        }*/
    }

}*/


class EditarFragment : Fragment() {

    val CAMERA_PERM_CODE = 101
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
            //startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE)
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

    /*private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }*/

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


    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if (requestCode == 100){
            val bitmap: Bitmap = data?.extras?.get("data") as Bitmap
            Log.d(
                "Info",
                "-----------------------------------MOSTRAR FOTO---------------------------------"
            )
            imageView.setImageBitmap(bitmap)
        }*/
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                /*val f = File(currentPhotoPath)
                imageView.setImageURI(Uri.fromFile(f))
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f))
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val contentUri = Uri.fromFile(f)
                mediaScanIntent.data = contentUri
                activity?.sendBroadcast(mediaScanIntent)*/
                val contentUri = Uri.parse(currentPhotoPath)
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri)
                imageView.setImageURI(contentUri)
                //uploadImageToFirebase(imageFileName, contentUri)
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val contentUri = data!!.data
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri)
                Log.d("tag", "onActivityResult: Gallery Image Uri:  $imageFileName")
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

    /*private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp
        //        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //val storageDir = Environment.DIRECTORY_PICTURES
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        val image = File(storageDir, "$imageFileName.jpg")

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image

    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            // Create the File where the photo should go
            val photoFile: File = createImageFile()
            // Continue only if the File was successfully created
            val photoURI = FileProvider.getUriForFile(
                requireActivity(),
                "com.unicauca.android.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
        }
    }*/
}
