package com.example.tubespbp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tubespbp.room.UserDB

class FragmentProfile : Fragment() {
    val db by lazy{activity?.let { UserDB(it)}  }

    var sharedPreferences: SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val nameTxt :TextView =  view.findViewById(R.id.etUsername)
        val emailTxt :TextView =  view.findViewById(R.id.etEmail)
        val tanggalLahirTxt :TextView =  view.findViewById(R.id.etTglLahir)
        val noHpTxt :TextView =  view.findViewById(R.id.etNoHp)
        val btnEdit : Button = view.findViewById(R.id.btnEdit)
        val imageButton : ImageView = view.findViewById(R.id.user)
        val id = sharedPreferences?.getString("id", "-1")

        val user = db?.UserDao()?.getUser(id!!.toInt())
        nameTxt.setText(user!!.username)
        emailTxt.setText(user.email)
        tanggalLahirTxt.setText(user.tanggallahir)
        noHpTxt.setText(user.noHp)

        btnEdit.setOnClickListener(){
            (activity as HomeActivity).setActivity(EditProfileActivity())
        }

        imageButton.setOnClickListener(){
            println("hello")
            val intent = Intent(requireActivity(), CameraActivity::class.java)

            startActivity(intent)
        }

    }
}