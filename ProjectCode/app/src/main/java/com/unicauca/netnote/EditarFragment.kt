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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditarFragment : Fragment() {

    //Botones
    private lateinit var cam_buttom: ImageButton
    private lateinit var gallery_buttom: ImageButton
    private lateinit var mic_buttom: ImageButton
    private lateinit var text_buttom: ImageButton
    private lateinit var currentPhotoPath: String //Direccion de la foto
    private lateinit var storageReference: StorageReference //Base de datos (Storage)
    private lateinit var auth: FirebaseAuth //Autentificación
    private lateinit var documentID: String //Identificador del documento (Nota) actual
    private lateinit var contenido: ArrayList<String>
    private lateinit var titulo: String
    private lateinit var actividad: EditarNotasActivity

    val CAMERA_REQUEST_CODE = 102 //Identificador para la camara
    val GALLERY_REQUEST_CODE = 105 //Identificador para la galeria

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        actividad = activity as EditarNotasActivity //Carga la actividad para poder obtener los valores de esta
        documentID = actividad.obtenerDocumentID() //Carga y asigna el "documentID"


        val vi: View = inflater.inflate(R.layout.fragment_editar, container, false) //Asigna el layout
        return vi
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Asignacion de botones
        cam_buttom = view.findViewById(R.id.cam_image)
        gallery_buttom = view.findViewById(R.id.gallery_image)
        mic_buttom = view.findViewById(R.id.mic_image)
        text_buttom = view.findViewById(R.id.text_image)

        //Base de datos (Storage)
        storageReference = FirebaseStorage.getInstance().reference

        //Listener botones
        gallery_buttom.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) //Carga el gestor de almacenamiento multimedia
            startActivityForResult(gallery, GALLERY_REQUEST_CODE) //Asigna el identificador
        }
        cam_buttom.setOnClickListener {
            takePicture()
        }
        text_buttom.setOnClickListener {
            Log.d("Info","-----------------SIN IMPLEMENTAR-----------------")
            contenido = actividad.obtenerContent()
            titulo = actividad.obtenerTitulo()
            documentID = actividad.obtenerDocuID()
            Log.d("contenido activi", "$contenido")
            var text = Intent(activity, Add_text_to_notes::class.java)
            text.putStringArrayListExtra("contentdocument", contenido)
            text.putExtra("Titulo",titulo)
            text.putExtra("IDdocument",documentID)
            startActivity(text)
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) //Carga la camara
        var photoFile: File? = null //Inicializa la foto
        try {
            photoFile = createImageFile()
        }catch (e: IOException){} //Si falla, no se detenga la app
        if (photoFile != null){
            val photoUri = FileProvider.getUriForFile(requireContext(), "com.unicauca.android.fileprovider", photoFile) //Obtiene el URI de la foto
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri) //Agrega informacion extra al URI
            startActivityForResult(intent,CAMERA_REQUEST_CODE) //Asigna el identificador
        }
    }

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) //Formato unico (tiempo) de la foto
        val storageDir = getActivity()?.getExternalFilesDir(Environment.DIRECTORY_PICTURES) //Directorio almacenamiento local
        val image = File.createTempFile( //Crea la imagen
            "JPEG_${timeStamp}",
            ".jpg",
            storageDir
        )

        currentPhotoPath = image.absolutePath //Directorio final
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Segun el identificador
        if (requestCode == CAMERA_REQUEST_CODE) { //CAMARA
            if (resultCode == RESULT_OK) {
                val contentUri = Uri.fromFile(File(currentPhotoPath)) //Uri de la foto
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) //Formato unico (tiempo)
                val imageFileName = "JPEG_" + timeStamp + ".jpg" //Asignacion de formato
                uploadImageToFirebase(imageFileName, contentUri)
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) { //GALERIA
            if (resultCode == RESULT_OK) {
                val contentUri = data!!.data //Uri de la foto
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) //Formato unico (tiempo)
                val imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri) //Asignacion de formato
                uploadImageToFirebase(imageFileName, contentUri)
            }
        }

    }

    private fun uploadImageToFirebase(name: String, contentUri: Uri?) {
        //Base de datos (Realtime Database)
        val database = Firebase.database

        //Autenticacion
        auth = FirebaseAuth.getInstance()
        val userID = auth.currentUser?.uid
        documentID = actividad.obtenerDocuID()

        val imageName = name.substring(0,20) //Elimina el formato (.jpg)
        val ref = database.getReference("/users/$userID/$documentID/images/$imageName/") //Direccion base de datos (Realtime Database)
        val image = storageReference.child("pictures/$name") //Direccion base de datos (Storage)
        image.putFile(contentUri!!).addOnSuccessListener {
            image.downloadUrl.addOnSuccessListener { uri ->
                //Log.d("Info", "El URI es: $uri")
                ref.setValue("$uri") //Se sube el URI de la imagen
            }
            Toast.makeText(requireActivity(), "Imagen Subida Satisfactorimente.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(requireActivity(), "Error al Subir Imagen.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getFileExt(contentUri: Uri?): String? {
        val c: ContentResolver? = activity?.contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(c?.getType(contentUri!!))
    }
}
