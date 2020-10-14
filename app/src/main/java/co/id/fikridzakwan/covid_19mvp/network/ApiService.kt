package co.id.fikridzakwan.covid_19mvp.network

import co.id.fikridzakwan.covid_19mvp.model.AllCountries
import co.id.fikridzakwan.covid_19mvp.model.InfoCountry
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("summary")
    fun getAllCountry(): Call<AllCountries>

    @GET("dayone/country/{country}")
    fun getInfoService(@Path("country") dataCovid: String): Call<List<InfoCountry>>

}