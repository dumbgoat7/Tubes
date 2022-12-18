package com.example.tubespbp.api

class VaccinationAPI {
    companion object{
        val  BASE_URl = "http://192.168.99.221:8080/healthy-U/public/"

        val GET_ALL_URL = BASE_URl+ "vaccination/"
        val GET_BY_ID_URL = BASE_URl+ "vaccination/"
        val ADD_URL = BASE_URl+ "vaccination"
        val UPDATE_URL = BASE_URl+ "vaccination/"
        val DELETE_URL = NewsAPI.BASE_URL + "vaccination/"
    }
}