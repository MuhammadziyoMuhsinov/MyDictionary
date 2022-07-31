package uz.muhammadziyo.mydictionary.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.muhammadziyo.mydictionary.databinding.ItemRv2Binding
import uz.muhammadziyo.mydictionary.databinding.ItemRvBinding
import uz.muhammadziyo.mydictionary.entity.MyCategory

class RvAdapter2(var list: List<MyCategory>,val rvClick2: RvClick2) :
    RecyclerView.Adapter<RvAdapter2.Vh>() {

    inner class Vh(var itemRv: ItemRvBinding) : RecyclerView.ViewHolder(itemRv.root) {

        fun onBind(category:MyCategory) {
         itemRv.name.text = category.name
            itemRv.more.setOnClickListener {
                rvClick2.onClick2(category,itemRv.more)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size


}
interface RvClick2{
    fun onClick2(category: MyCategory,imageView: ImageView)
}