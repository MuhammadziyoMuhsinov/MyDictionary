package uz.muhammadziyo.mydictionary

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.muhammadziyo.mydictionary.databinding.FragmentInfoBinding
import uz.muhammadziyo.mydictionary.db.MyDbHelper
import uz.muhammadziyo.mydictionary.utils.Mydata


class InfoFragment : Fragment() {
    lateinit var binding:FragmentInfoBinding
    lateinit var myDbHelper: MyDbHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(binding.root.context)

        binding.name.text = Mydata.word?.name
        binding.name2.text = Mydata.word?.name
        binding.image.setImageURI(Uri.parse(Mydata.word?.imagePath))
        binding.info.text = Mydata.word?.translation
        if (Mydata.word?.type==0){
            binding.like.setImageResource(R.drawable.heart2)
        }else{
            binding.like.setImageResource(R.drawable.heart3)
        }
        binding.likeBosish.setOnClickListener {
            if (Mydata.word?.type==0){
                binding.like.setImageResource(R.drawable.heart3)
                Mydata.word?.type=1
                myDbHelper.updateWord(Mydata.word!!)
            }else{
                binding.like.setImageResource(R.drawable.heart2)
                Mydata.word?.type=0
                myDbHelper.updateWord(Mydata.word!!)
            }
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }



        return binding.root
    }


}