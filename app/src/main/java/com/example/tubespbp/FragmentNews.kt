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
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.api.NewsAPI
import com.example.tubespbp.databinding.FragmentNewsBinding
import com.example.tubespbp.room.Constant
import com.example.tubespbp.Models.News
import com.example.tubespbp.Models.User
import com.example.tubespbp.room.NewsDao
import com.example.tubespbp.room.UserDB
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.HashMap

class FragmentNews : Fragment() {
    val db by lazy{activity?.let { UserDB(it)}  }
    lateinit var newsAdapter: NewsAdapter

    private var queue: RequestQueue? = null
    var sharedPreferences: SharedPreferences? = null
    private var _binding : FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?) : View? {

        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val rootView = binding.root


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queue = Volley.newRequestQueue(requireContext())
        newsAdapter = NewsAdapter(arrayListOf(), object : NewsAdapter.OnAdapterListener {
            override fun onClick(news: News) {
                news.id?.let { it-> intentEdit(it,Constant.TYPE_READ) }
            }

            override fun onUpdate(news: News) {
                news.id?.let { it-> intentEdit(it,Constant.TYPE_UPDATE) }
            }

            override fun onDelete(news: News) {
                deleteDialog(news)
            }
        })
        list_news.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
        loadData()
        setupListener()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteDialog(news: News){
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        materialAlertDialogBuilder.setTitle("Konfirmasi")
            .setMessage("Are You Sure to delete this data From ${news.judul}?")
            .setNegativeButton("Batal", null)
            .setPositiveButton("Hapus") {_, _ ->
                 news.id?.let { it1 ->
                    deleteNews(
                        it1
                    )
                     allNews()
                }
            }
            .show()
    }

    fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            allNews()
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

    private fun allNews() {
        val stringRequest : StringRequest = object :
            StringRequest(Method.GET, NewsAPI.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                var news : Array<News> = gson.fromJson(jsonObject.getJSONArray("data").toString(), Array<News>::class.java)
                newsAdapter.setData(news.toList())
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        requireContext(),
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun deleteNews(id: Int) {
        val stringRequest : StringRequest = object :
            StringRequest(Method.DELETE, NewsAPI.DELETE_URL + id, Response.Listener {response ->
                val gson = Gson()
                var news = gson.fromJson(response, News::class.java)
                if (news != null)
                    Toast.makeText(requireContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                allNews()
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        requireContext(),
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }
    private fun getNewsById(id: Int) {
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, NewsAPI.GET_BY_ID_URL + id,
                { response ->
                    val user = Gson().fromJson(response, User::class.java)
                    if( user.username != "admin" ) {
                        addbtn.visibility = GONE
                    } else {
                        setupListener()
                    }
                },
                Response.ErrorListener { error ->
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            requireContext(),
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)

    }

}