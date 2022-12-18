package com.example.tubespbp.api

class NewsAPI {
    companion object {
        val  BASE_URL = "http://192.168.1.5:8080/healthy-U/public/"
        val GET_ALL_URL = BASE_URL + "news/"
        val GET_BY_ID_URL = BASE_URL + "news/"
        val ADD_URL = BASE_URL + "news"
        val UPDATE_URL = BASE_URL + "news/"
        val DELETE_URL = BASE_URL + "news/"
    }
}