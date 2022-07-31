package uz.muhammadziyo.mydictionary.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uz.muhammadziyo.mydictionary.entity.MyCategory
import uz.muhammadziyo.mydictionary.entity.Word
import uz.muhammadziyo.mydictionary.item_pager

class PagerAdapter(val list: List<MyCategory>, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int = list.size



    override fun getPageTitle(position: Int): CharSequence? {

        return list[position].name
    }

    override fun getItem(position: Int): Fragment {
        return item_pager.newInstance(list[position])
    }

}