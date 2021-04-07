package base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder <T>(itemmView: View):RecyclerView.ViewHolder(itemmView){
    abstract fun bind(item: T, position:Int)
}