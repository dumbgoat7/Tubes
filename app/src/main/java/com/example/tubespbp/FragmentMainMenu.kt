package com.example.tubespbp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class FragmentMainMenu : Fragment() {
    private lateinit var logOut : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {
        val view : View = inflater.inflate(R.layout.fragment_mainmenu, container, false)

        logOut = view.findViewById(R.id.logout)

        logOut.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            builder.setMessage("Do you want to exit?")
            builder.setPositiveButton("OK", object:DialogInterface.OnClickListener{
                override fun onClick(dialogInterface: DialogInterface, i: Int) {
                    val intent = Intent ( getActivity(), LoginActivity::class.java)
                    getActivity()?.onBackPressed()
                    startActivity(intent)
                }
            })
                .setNegativeButton("NO",DialogInterface.OnClickListener{
                    dialog, id -> dialog.cancel()
                })
            builder.show()

        }
        return view
    }

}