package com.example.tubespbp.api

class VaccinationAPI {
    companion object{
        val  BASE_URl = "http://192.168.1.5:8080/healthy-U/public/"

        val GET_ALL_URL = BASE_URl+ "Vaccination/"
        val GET_BY_ID_URL = BASE_URl+ "Vaccination/"
        val ADD_URL = BASE_URl+ "Vaccination"
        val UPDATE_URL = BASE_URl+ "Vaccination/"
        val DELETE_URL = BASE_URl + "Vaccination/"
    }
}