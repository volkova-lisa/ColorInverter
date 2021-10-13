package com.example.colorinverter1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.colorinverter1.databinding.FragmentStartPageBinding

class StartPageFragment : Fragment() {

    lateinit var threadButton: Button
    lateinit var coroutinesButton: Button
    private var _binding : FragmentStartPageBinding? = null
    val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentStartPageBinding.inflate(layoutInflater, container, false)
        mBinding.threadButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_startPage_to_inverterViaThread))
        return mBinding.root
    }

}