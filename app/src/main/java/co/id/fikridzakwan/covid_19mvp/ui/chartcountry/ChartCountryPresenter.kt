package co.id.fikridzakwan.covid_19mvp.ui.chartcountry

import co.id.fikridzakwan.covid_19mvp.BuildConfig
import co.id.fikridzakwan.covid_19mvp.model.AllCountries
import co.id.fikridzakwan.covid_19mvp.model.InfoCountry
import co.id.fikridzakwan.covid_19mvp.network.ApiClient
import co.id.fikridzakwan.covid_19mvp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChartCountryPresenter(val view: ChartCountryService) {

    fun getCountry(dataCountry: String) {
        val apiService = ApiClient.getClient()
        apiService.getInfoService(dataCountry).enqueue(object : Callback<List<InfoCountry>> {
            override fun onResponse(call: Call<List<InfoCountry>>, response: Response<List<InfoCountry>>) {
                if (response.body() != null) {
                    response.body()!!.let { view.showCountry(it) }
                }
            }

            override fun onFailure(call: Call<List<InfoCountry>>, t: Throwable) {
                t.message
            }
        })
    }
}