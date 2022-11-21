package com.example.tubespbp.api

class UserAPI {
    companion object{
        val  BASE_URl = "http://192.168.17.97/healthy-U/public/user"

        val GET_ALL_URL = BASE_URl+ "User/"
        val GET_BY_ID_URL = BASE_URl+ "User/"
        val ADD_URL = BASE_URl+ "User"
        val UPDATE_URL = BASE_URl+ "User/"
        val DELETE_URL = BASE_URl+ "User/"
    }
}