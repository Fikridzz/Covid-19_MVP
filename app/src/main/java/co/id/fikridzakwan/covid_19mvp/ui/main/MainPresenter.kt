package co.id.fikridzakwan.covid_19mvp.ui.main

import co.id.fikridzakwan.covid_19mvp.model.AllCountries
import co.id.fikridzakwan.covid_19mvp.model.Countries
import co.id.fikridzakwan.covid_19mvp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(private val view: MainService) {

    fun getCountry() {
        val apiService = ApiClient.getClient()
        apiService.getAllCountry().enqueue(object : Callback<AllCountries> {
            override fun onResponse(call: Call<AllCountries>, response: Response<AllCountries>) {
                view.hideProgressBar()
                if (response.body() != null) {
                    response.body()!!.Global.let { view.showWorld(it) }
                    response.body()!!.Countries.let { view.showCountry(it) }
                } else {
                    view.hideProgressBar()
                }
            }

            override fun onFailure(call: Call<AllCountries>, t: Throwable) {
                t.message
            }
        })
    }
}