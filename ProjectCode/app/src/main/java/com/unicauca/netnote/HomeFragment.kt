package com.unicauca.netnote

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import models.Document


class HomeFragment : Fragment() , RecyclerAdapter.onDocumentClickListener{

    private var auth: FirebaseAuth = FirebaseAuth.getInstance() //Aunteticacion
    private var database: FirebaseDatabase = Firebase.database //Base de datos (Realtime Database)
    private lateinit var globalKeyNameIm: Set<String>
    private lateinit var globalKeyURLIm: Collection<Any>
    private var documentLength : Int = 0
    private var audioInDocument: Boolean = false
    private var scansInDocument: Boolean = false
    private var textsInDocument: Boolean = false
    private var listOfDocuments: MutableList<Document> = mutableListOf<Document>()
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myview: View = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = myview.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(myview.context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                myview.context,
                DividerItemDecoration.VERTICAL
            )
        )

        val userID = auth.currentUser?.uid //Obtiene ID usuario actual
        val path = database.getReference("/users/$userID/")
        addPostEventListener(path)//Obtencion valores de la base de datos (Realtime DataBase)
        recyclerView.adapter = RecyclerAdapter(myview.context, listOfDocuments, this)

        return myview

    }

    /*override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {


        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val userID = auth.currentUser?.uid //Obtiene ID usuario actual
        val path = database.getReference("/users/$userID/")
        addPostEventListener(path)//Obtencion valores de la base de datos (Realtime DataBase)
        Log.d("Info","*******************Esta creando el listDocument")
        recyclerView.adapter = RecyclerAdapter(itemView.context, listOfDocuments, this)

    }*/

    override fun onItemClick(title: String, contentDocument: MutableList<String>, ID: String) {
        val intent = Intent(context, EditarNotasActivity::class.java)
        intent.putExtra("Titulo", title)
        intent.putExtra("ID", ID)
        intent.putStringArrayListExtra("Contenido", ArrayList(contentDocument))
        startActivity(intent)
    }

    private fun addPostEventListener(postReference: DatabaseReference) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if (dataSnapshot.getValue()!=null) {
                    val map: Map<String, Any> = dataSnapshot.getValue() as Map<String, Any>
                    val documentID = map.keys // Vector con los nombres de las imagenes
                    val documentAtributes = map.values // Vector con las URL de las imagenes
                    //val documento1 = documentID.get(0)
                    val documentArrayLength = documentID.toTypedArray().size
                    val documentArrayAtributes = documentAtributes.toTypedArray()
                    var atributesKey: Set<String>
                    lateinit var mapAtributes : Map<String, Any>
                    var dictionaryImages: Map<String, String>
                    var dictionaryTexts: Map<String, String>
                    var dictionaryAudios: Map<String, String>
                    var titleDocument: String
                    var contentDocument : MutableList<String>
                    var arrayLinksImages: Collection<String>
                    var idImages = mutableListOf<String>()
                    var arrayKeyImages: Set<String>
                    var arrayLinksTexts: Collection<String>
                    var arrayKeyTexts: Set<String>
                    var idTexts = mutableListOf<String>()
                    var arrayLinksAudios: Collection<String>
                    var arrayKeyAudios: Set<String>
                    var idAudios = mutableListOf<String>()
                    var timeStamp: String
                    var linksContent: String
                    var mapContent = mutableMapOf<Long, String>()
                    var contentDocumentLinks : MutableList<String>

                    for ((atributes, IDdocuments) in documentArrayAtributes zip documentID){
                        contentDocument = mutableListOf<String>()
                        idImages = mutableListOf<String>()
                        idTexts = mutableListOf<String>()
                        idAudios = mutableListOf<String>()
                        mapContent = mutableMapOf()

                        //contentDocumentLinks = mutableListOf<String>()

                        mapAtributes = atributes as Map<String, Any>

                        atributesKey = mapAtributes.keys

                        if("images" in atributesKey){

                            scansInDocument =  true
                            dictionaryImages = mapAtributes["images"] as Map<String, String>


                            arrayLinksImages = dictionaryImages.values
                            arrayKeyImages = dictionaryImages.keys

                            for ((id, link) in arrayKeyImages zip arrayLinksImages){

                                idImages.add(id.replace("_", "") + link)

                            }

                            contentDocument.addAll(idImages)

                        }
                        else{

                            scansInDocument = false

                        }

                        if("texts" in atributesKey){

                            textsInDocument =  true
                            dictionaryTexts = mapAtributes["texts"] as Map<String, String>

                            arrayLinksTexts = dictionaryTexts.values
                            arrayKeyTexts = dictionaryTexts.keys

                            for ((id, link) in arrayKeyTexts zip arrayLinksTexts){


                                idTexts.add(id.replace("_", "") + link)

                            }


                            contentDocument.addAll(idTexts)



                        }
                        else{

                            textsInDocument =  false

                        }

                        if("audios" in atributesKey){

                            audioInDocument =  true
                            dictionaryAudios = mapAtributes["audios"] as Map<String, String>

                            arrayLinksAudios = dictionaryAudios.values
                            arrayKeyAudios = dictionaryAudios.keys

                            for ((id, link) in arrayKeyAudios zip arrayLinksAudios){

                                idAudios.add(id.replace("_", "") + link)

                            }


                            contentDocument.addAll(idAudios)

                        }
                        else{

                            audioInDocument =  false

                        }



                        for (content in contentDocument){
                            if (content != "") {
                                timeStamp = content.substring(4, 18)
                                linksContent = content.substring(0, 4)+content.substring(18)

                                mapContent[timeStamp.toLong()] = linksContent
                            }


                        }

                        mapContent = mapContent.toSortedMap()
                        contentDocumentLinks = mapContent.values.toMutableList()
                        titleDocument = mapAtributes["title"].toString()

                        listOfDocuments.add(
                            Document(
                                IDdocuments,
                                titleDocument,
                                3,
                                audioInDocument,
                                scansInDocument,
                                textsInDocument,
                                contentDocumentLinks
                            )
                        )

                    }
                    Log.d("Lista de Doc","$listOfDocuments")



                    globalKeyNameIm = documentID
                    globalKeyURLIm = documentAtributes
                    documentLength = documentArrayLength

                }
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Log.w("Error", "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(postListener)
    }
}