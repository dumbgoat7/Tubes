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
import com.example.tubespbp.databinding.FragmentMainmenuBinding
import com.example.tubespbp.databinding.FragmentNewsBinding
import com.example.tubespbp.room.UserDB
import com.example.tubespbp.room.UserDao

class FragmentMainMenu : Fragment(R.layout.fragment_mainmenu) {
    private var _binding : FragmentMainmenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var dao : UserDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {

        dao = UserDB.getDatabase(requireContext())!!.UserDao()
        _binding = FragmentMainmenuBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.logout.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(rootView.context)
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
        binding.readbtn.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction!!.replace(R.id.layout_fragment, FragmentNews())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        binding.scanbtn.setOnClickListener{
            val intent = Intent(getActivity(), CameraActivity::class.java)
            startActivity(intent)
        }
        binding.rsbtn.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction!!.replace(R.id.layout_fragment, FragmentRS())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return rootView
    }

}