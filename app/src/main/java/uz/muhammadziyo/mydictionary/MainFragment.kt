package uz.muhammadziyo.mydictionary

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import uz.muhammadziyo.mydictionary.Adapters.PagerAdapter
import uz.muhammadziyo.mydictionary.Adapters.RvAdapter
import uz.muhammadziyo.mydictionary.Adapters.RvAdapter2
import uz.muhammadziyo.mydictionary.Adapters.RvClick
import uz.muhammadziyo.mydictionary.databinding.FragmentMainBinding
import uz.muhammadziyo.mydictionary.db.MyDbHelper
import uz.muhammadziyo.mydictionary.entity.MyCategory
import uz.muhammadziyo.mydictionary.entity.Word
import uz.muhammadziyo.mydictionary.utils.Mydata
import java.util.concurrent.ThreadPoolExecutor

class MainFragment : Fragment() {
    private lateinit var list: ArrayList<MyCategory>
    lateinit var myDbHelper: MyDbHelper
    lateinit var pagerAdapter: PagerAdapter
    lateinit var rvAdapter:RvAdapter
    private lateinit var listWord:ArrayList<Word>
    private lateinit var listWord2:ArrayList<Word>
    lateinit var binding: FragmentMainBinding
    lateinit var handler: Handler
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(binding.root.context)
        handler = Handler()
        listWord = ArrayList()
        listWord2 = ArrayList()
        listWord.addAll(myDbHelper.getAllWord())
        listWord.forEach {
            if (it.type==1){
                listWord2.add(it)
            }
        }

        rvAdapter = RvAdapter(listWord2, object : RvClick{
            override fun onClick(word: Word, position: Int, imageView: ImageView) {
                Mydata.word = word
                findNavController().navigate(R.id.infoFragment)
            }
        })
        binding.myTab.setupWithViewPager(binding.myViewPager)

        handler.postDelayed(object : Runnable {
            override fun run() {
                binding.start.visibility = View.GONE
            }
        }, 2000)

        list = ArrayList()
        list.addAll(myDbHelper.getAllCategory())
        pagerAdapter = PagerAdapter(list, childFragmentManager)



        if (!Mydata.indicatorCheck2){
            binding.bir.visibility = View.VISIBLE
            binding.ikki.visibility = View.INVISIBLE
            binding.myTab.visibility = View.VISIBLE
            binding.myViewPager.visibility = View.VISIBLE
            binding.rv.visibility = View.GONE
            binding.myViewPager.adapter = pagerAdapter

        }else{
            binding.ikki.visibility = View.VISIBLE
            binding.bir.visibility = View.INVISIBLE
            binding.rv.visibility = View.VISIBLE
            binding.myTab.visibility = View.GONE
            binding.myViewPager.visibility = View.GONE
            binding.rv.adapter = rvAdapter
        }



        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.kategories)

        }


        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_asosiy -> {
                    binding.bir.visibility = View.VISIBLE
                    binding.ikki.visibility = View.INVISIBLE
                    Mydata.indicatorCheck2 = false
                    binding.myTab.visibility = View.VISIBLE
                    binding.myViewPager.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE
                    binding.myViewPager.adapter = pagerAdapter
                }
                R.id.menu_tanlangan -> {
                    Mydata.indicatorCheck2 = true
                    binding.ikki.visibility = View.VISIBLE
                    binding.bir.visibility = View.INVISIBLE
                    binding.rv.visibility = View.VISIBLE
                    binding.myTab.visibility = View.GONE
                    binding.myViewPager.visibility = View.GONE
                    binding.rv.adapter = rvAdapter
                }
            }
            true
        }

        return binding.root
    }


}