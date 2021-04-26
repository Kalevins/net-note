package com.unicauca.netnote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import base.BaseViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.notas_row.view.*

class RecyAdapterEditNotas (private val context: Context,
                            val contentDocument:MutableList<String>
                            ):
    RecyclerView.Adapter<BaseViewHolder<*>>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return ContentNotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notas_row, parent,false))
    }

    override fun getItemCount(): Int {
        return contentDocument.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {

        when(holder){
            is ContentNotesViewHolder -> holder.bind(contentDocument[position],position)
            else -> throw  IllegalArgumentException("Se olvido de pasa el viewholder en el bind")
        }

    }

    inner class  ContentNotesViewHolder(itemView: View): BaseViewHolder<String>(itemView){

        override fun bind(item: String, position: Int) {

            var formato: String = item.substring(0,4)
            var link: String = item.substring(4)

            if( formato == "JPEG"){

                Glide.with(context).load(link).into(itemView.imageDocs)
                //itemView.ContentDoc.setCompoundDrawablesWithIntrinsicBounds(null, itemView.imageDocs.drawable, null, null)

            }
            else if(formato == "TEXT") {

                itemView.ContentDoc.text = link
            }
        }

    }
}