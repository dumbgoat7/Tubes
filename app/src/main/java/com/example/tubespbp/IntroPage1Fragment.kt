package com.example.tubespbp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class IntroPage1Fragment : Fragment() {
    private lateinit var start : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intropage1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start = view.findViewById(R.id.btn1)

        start.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction!!.replace(R.id.layout_introfragment, IntroPage2Fragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}