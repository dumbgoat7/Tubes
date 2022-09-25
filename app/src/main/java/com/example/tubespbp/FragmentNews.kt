package com.example.tubespbp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubespbp.databinding.FragmentNewsBinding
import com.example.tubespbp.room.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.rv_item_news.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentNews : Fragment(R.layout.fragment_rs) {
    private lateinit var dao : NewsDao
    lateinit var newsAdapter: NewsAdapter
    var sharedPreferences: SharedPreferences? = null
    private var _binding : FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?) : View? {

        dao = UserDB.getDatabase(requireContext())!!.NewsDao()
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val rootView = binding.root


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        sharedPreferences = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
//        if( sharedPreferences!!.contains("idAdmin") ) {
//
//        } else {
//            addbtn.visibility = GONE
//            list_news.icon_edit.visibility = GONE
//            list_news.icon_delete.visibility = GONE
//        }
        setupListener()
        newsAdapter = NewsAdapter(arrayListOf(), object : NewsAdapter.OnAdapterListener {
            override fun onClick(news: News) {
                intentEdit(news.id,Constant.TYPE_READ)
            }

            override fun onUpdate(news: News) {
                intentEdit(news.id,Constant.TYPE_UPDATE)
            }

            override fun onDelete(news: News) {
                deleteDialog(news, view)
            }
        })
        list_news.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteDialog(news: News,view: View){
        val alertDialog = AlertDialog.Builder(view.context)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From ${news.judul}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    dao.deleteNews(news)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            val notes = dao.getAllNews()
            Log.d("NewsFragment","dbResponse: $notes")
            withContext(Dispatchers.Main){
                newsAdapter.setData( notes )
            }
        }
    }

    fun setupListener() {
        addbtn.setOnClickListener{
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(newsId : Int, intentType: Int){
        startActivity(
            Intent(activity?.applicationContext, EditNewsActivity::class.java)
                .putExtra("intent_id", newsId)
                .putExtra("intent_type", intentType)
        )
    }

}