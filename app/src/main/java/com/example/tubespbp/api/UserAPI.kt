package com.example.tubespbp.api

class UserAPI {
    companion object{
        val  BASE_URl = "http://192.168.1.5:8080/healthy-U/public/"

        val GET_ALL_URL = BASE_URl+ "user/"
        val GET_BY_ID_URL = BASE_URl+ "user/"
        val ADD_URL = BASE_URl+ "user"
        val UPDATE_URL = BASE_URl+ "user/"
    }
}