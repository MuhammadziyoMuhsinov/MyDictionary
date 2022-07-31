package uz.muhammadziyo.mydictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import uz.muhammadziyo.mydictionary.Adapters.RvAdapter
import uz.muhammadziyo.mydictionary.Adapters.RvAdapter2
import uz.muhammadziyo.mydictionary.Adapters.RvClick
import uz.muhammadziyo.mydictionary.Adapters.RvClick2
import uz.muhammadziyo.mydictionary.databinding.FragmentKategoriesBinding
import uz.muhammadziyo.mydictionary.databinding.ItemDialogBinding
import uz.muhammadziyo.mydictionary.databinding.ItemDialogRemoveBinding
import uz.muhammadziyo.mydictionary.db.MyDbHelper
import uz.muhammadziyo.mydictionary.entity.MyCategory
import uz.muhammadziyo.mydictionary.entity.Word
import uz.muhammadziyo.mydictionary.utils.Mydata


class Kategories : Fragment() {

    private lateinit var binding: FragmentKategoriesBinding
    private lateinit var list: ArrayList<MyCategory>
    private lateinit var listWord: ArrayList<Word>
    private lateinit var adapter: RvAdapter2
    private lateinit var adapter2: RvAdapter
    private lateinit var myDbHelper: MyDbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKategoriesBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(binding.root.context)
        list = ArrayList()
        listWord = ArrayList()

        if (myDbHelper.getAllWord().isEmpty()){

        }else{
            listWord.addAll(myDbHelper.getAllWord())
        }
        list.addAll(myDbHelper.getAllCategory())
        adapter = RvAdapter2(list, object : RvClick2 {

            override fun onClick2(category: MyCategory, imageView: ImageView) {
                clickCategory(category,imageView)

            }
        })

        adapter2 = RvAdapter(listWord, object : RvClick{
            override fun onClick(word: Word, position: Int, imageView: ImageView) {
         clickWord(word,imageView)
            }
        })


        binding.rv.adapter = adapter

        binding.back.setOnClickListener {
            findNavController().popBackStack()
            Mydata.checkAdapter=false
            Mydata.indicatorCheck=false
        }

        binding.add.setOnClickListener {
            if (!Mydata.indicatorCheck){
                val alertDialog = AlertDialog.Builder(binding.root.context).create()
                val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)
                alertDialog.setView(itemDialogBinding.root)
                alertDialog.show()
                itemDialogBinding.close.setOnClickListener {
                    alertDialog.cancel()
                }
                itemDialogBinding.saqlash.setOnClickListener {
                    val category = MyCategory(itemDialogBinding.name.text.toString())
                    myDbHelper.addCategory(category)
                    alertDialog.cancel()
                    Toast.makeText(binding.root.context, "added!", Toast.LENGTH_SHORT).show()
                    list.add(category)
                    adapter.notifyItemInserted(list.size + 1)
                }
            }else{
                Mydata.checkTitle = "So'z qo'shish"
                findNavController().navigate(R.id.addwordFragment)
            }

        }
        binding.myBottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_kategory ->{
                    Mydata.indicatorCheck=false
                    binding.ikki.visibility=View.INVISIBLE
                    binding.bir.visibility = View.VISIBLE
                    binding.rv.adapter=adapter
                }
                R.id.menu_words -> {
                    Mydata.indicatorCheck =true
                    Mydata.checkAdapter=true
                    binding.bir.visibility = View.INVISIBLE
                    binding.ikki.visibility=View.VISIBLE
                    binding.rv.adapter=adapter2
                }
            }


            true
        }


        if (!Mydata.indicatorCheck){
            binding.ikki.visibility=View.INVISIBLE
            binding.bir.visibility = View.VISIBLE
            binding.rv.adapter=adapter
        }else{
            binding.bir.visibility = View.INVISIBLE
            binding.ikki.visibility=View.VISIBLE
            binding.rv.adapter=adapter2
        }

        return binding.root
    }

  private fun clickCategory(category: MyCategory,imageView:ImageView){




       val popupMenu = PopupMenu(binding.root.context, imageView)
       popupMenu.inflate(R.menu.popup_menu)
       popupMenu.show()
       popupMenu.setOnMenuItemClickListener {

           when (it.itemId) {

               R.id.menu_change -> {

                   val alertDialog = AlertDialog.Builder(binding.root.context).create()
                   val itemDialog = ItemDialogBinding.inflate(layoutInflater)
                   alertDialog.setView(itemDialog.root)
                   itemDialog.name.setText(category.name)
                   alertDialog.show()
                   itemDialog.close.setOnClickListener {
                       alertDialog.cancel()
                   }

                   itemDialog.saqlash.setOnClickListener {
                       category.name = itemDialog.name.text.toString()
                       if (itemDialog.name.text.toString() == "") {
                           Toast.makeText(
                               binding.root.context,
                               "empty",
                               Toast.LENGTH_SHORT
                           )
                               .show()
                       } else {
                           list[list.indexOf(category)]=category
                           adapter.notifyItemChanged(list.indexOf(category))
                           myDbHelper.updateCategory(category)
                           Toast.makeText(
                               binding.root.context,
                               "update",
                               Toast.LENGTH_SHORT
                           )
                               .show()
                           alertDialog.cancel()
                       }

                   }


               }
               R.id.menu_delete -> {
                   val alertDialog = AlertDialog.Builder(binding.root.context).create()
                   val itemDialog = ItemDialogRemoveBinding.inflate(layoutInflater)
                   alertDialog.setView(itemDialog.root)
                   itemDialog.no.setOnClickListener {
                       alertDialog.cancel()
                   }
                   itemDialog.yes.setOnClickListener {
                       val position = list.indexOf(category)


                       list.remove(category)
                       adapter.notifyItemRemoved(position)
                       if (myDbHelper.getAllWord().isEmpty()){

                       }else{
                           listWord.forEach { i ->
                               if (i.category?.id == category.id) {
                                   myDbHelper.deleteWord(i)
                               }
                           }
                       }
                       myDbHelper.deleteCategory(category)
                       Toast.makeText(binding.root.context, "deleted", Toast.LENGTH_SHORT)
                           .show()
                       alertDialog.cancel()
                   }
                   alertDialog.show()

               }

           }
           true

       }



    }

    private fun clickWord(word: Word,imageView: ImageView){

        val popupMenu = PopupMenu(binding.root.context, imageView)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {

            when (it.itemId) {

                R.id.menu_change -> {

                 findNavController().navigate(R.id.addwordFragment)

                    Mydata.word=word
                    Mydata.checkTitle = "So'z tahrirlash"


                }
                R.id.menu_delete -> {
                    val alertDialog = AlertDialog.Builder(binding.root.context).create()
                    val itemDialog = ItemDialogRemoveBinding.inflate(layoutInflater)
                    itemDialog.tvSoroq.text = "Bu so’zni o’chirasizmi?"
                    alertDialog.setView(itemDialog.root)
                    itemDialog.no.setOnClickListener {
                        alertDialog.cancel()
                    }
                    itemDialog.yes.setOnClickListener {
                        val position = listWord.indexOf(word)

                        myDbHelper.deleteWord(word)
                        listWord.remove(word)
                        adapter2.notifyItemRemoved(position)

                        Toast.makeText(binding.root.context, "deleted", Toast.LENGTH_SHORT)
                            .show()
                        alertDialog.cancel()
                    }
                    alertDialog.show()

                }

            }
            true

        }

    }


}