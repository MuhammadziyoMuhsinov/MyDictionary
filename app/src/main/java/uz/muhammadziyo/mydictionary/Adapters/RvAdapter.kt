package uz.muhammadziyo.mydictionary.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.muhammadziyo.mydictionary.R
import uz.muhammadziyo.mydictionary.databinding.ItemRv2Binding
import uz.muhammadziyo.mydictionary.entity.MyCategory
import uz.muhammadziyo.mydictionary.entity.Word
import uz.muhammadziyo.mydictionary.utils.Mydata

class RvAdapter(var list: List<Word>,val rvClick: RvClick) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(var itemRv: ItemRv2Binding) : RecyclerView.ViewHolder(itemRv.root) {

        fun onBind(word: Word, position: Int) {
         itemRv.name.text = word.name
            if (!Mydata.checkAdapter){

            }else{
                itemRv.more.setImageResource(R.drawable.popupmenu)

            }

            var translation=word.translation

            if (word.translation?.length!! > 30 ){
                translation = word.translation.toString().substring(0,30)
            }

            itemRv.tarjima.text = translation
            itemRv.more.setOnClickListener {
                rvClick.onClick(word,position,itemRv.more)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRv2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int = list.size


}

interface RvClick{
    fun onClick(word: Word,position: Int,imageView: ImageView)
}