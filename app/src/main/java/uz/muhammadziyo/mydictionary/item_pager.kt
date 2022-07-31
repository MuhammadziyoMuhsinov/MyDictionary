package uz.muhammadziyo.mydictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.newFixedThreadPoolContext
import uz.muhammadziyo.mydictionary.Adapters.RvAdapter
import uz.muhammadziyo.mydictionary.Adapters.RvClick
import uz.muhammadziyo.mydictionary.databinding.FragmentItemPagerBinding
import uz.muhammadziyo.mydictionary.db.MyDbHelper
import uz.muhammadziyo.mydictionary.entity.MyCategory
import uz.muhammadziyo.mydictionary.entity.Word
import uz.muhammadziyo.mydictionary.utils.Mydata




class item_pager : Fragment() {

    var category:MyCategory?=null
    private lateinit var binding:FragmentItemPagerBinding
    lateinit var adapter:RvAdapter
    lateinit var list:ArrayList<Word>
    lateinit var listadapter:ArrayList<Word>

    private lateinit var myDbHelper: MyDbHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemPagerBinding.inflate(layoutInflater)
         list = ArrayList()
         listadapter = ArrayList()
         myDbHelper = MyDbHelper(binding.root.context)
         list.addAll(myDbHelper.getAllWord())
        list.forEach {
            if (it.category?.id==category?.id){
                listadapter.add(it)
            }
        }

        adapter = RvAdapter(listadapter,object : RvClick{


            override fun onClick(word: Word, position: Int, imageView: ImageView) {
                findNavController().navigate(R.id.infoFragment)
                Mydata.word = word
            }
        })
        binding.rv.adapter = adapter

        return binding.root
    }

    companion object {
        fun newInstance(category: MyCategory):Fragment {
            val fragment = item_pager()
            fragment.category=category
            return fragment
        }

    }
}