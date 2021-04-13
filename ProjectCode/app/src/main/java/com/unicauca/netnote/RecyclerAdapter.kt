package com.unicauca.netnote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import base.BaseViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.documents_row.view.*
import models.Document
import java.lang.IllegalArgumentException

class RecyclerAdapter ( private val context: Context,
                        val listDocuments:List<Document>,
                        private val itemClickListener:onDocumentClickListener):RecyclerView.Adapter<BaseViewHolder<*>>(){
    interface onDocumentClickListener{
        fun onItemClick(title: String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
       return DocumentsViewHolder(LayoutInflater.from(context).inflate(R.layout.documents_row, parent,false))
    }

    override fun getItemCount(): Int {
        return listDocuments.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {

        when(holder){
            is DocumentsViewHolder -> holder.bind(listDocuments[position],position)
            else -> throw  IllegalArgumentException("Se olvido de pasa el viewholder en el bind")
        }

    }

    inner class  DocumentsViewHolder(itemView: View):BaseViewHolder<Document>(itemView){

        override fun bind(item: Document, position: Int) {

            itemView.setOnClickListener{
                itemClickListener.onItemClick(item.title)
            }

            val scanImage: String = if(item.scans) {
                "https://raw.githubusercontent.com/kevinmuz55/NetNote/Kevin/Imagenes/Imagen.png"

            } else{
                "https://raw.githubusercontent.com/kevinmuz55/NetNote/main/Imagenes/Imagen_Gris.png"
            }

            val textImage: String = if(item.texts) {
                "https://raw.githubusercontent.com/kevinmuz55/NetNote/main/Imagenes/Docs.png"

            } else{
                "https://raw.githubusercontent.com/kevinmuz55/NetNote/main/Imagenes/Docs_Gris.png"
            }

            val audioImage: String = if(item.audio) {
                "https://raw.githubusercontent.com/kevinmuz55/NetNote/main/Imagenes/Micro.png"

            } else{
                "https://raw.githubusercontent.com/kevinmuz55/NetNote/main/Imagenes/Micro_Gris.png"
            }

            Glide.with(context).load(scanImage).into(itemView.image_document)
            Glide.with(context).load(textImage).into(itemView.image_audio)
            Glide.with(context).load(audioImage).into(itemView.image_scan)
            itemView.txt_title_document.text = item.title
            itemView.number_Chapters.text = item.chapters.toString() +" capítulos"
        }

    }
}