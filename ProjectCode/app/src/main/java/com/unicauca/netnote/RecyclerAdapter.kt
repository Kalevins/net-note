package com.unicauca.netnote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import base.BaseViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.documents_row.view.*
import models.Document
import java.lang.IllegalArgumentException

class RecyclerAdapter (private val context: Context, val listDocuments:List<Document>):RecyclerView.Adapter<BaseViewHolder<*>>(){

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
            Glide.with(context).load(item.scans).into(itemView.image_document)
            itemView.txt_title_document.text = item.title
        }

    }
}