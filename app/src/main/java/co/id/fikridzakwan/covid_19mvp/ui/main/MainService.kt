package co.id.fikridzakwan.covid_19mvp.ui.main

import co.id.fikridzakwan.covid_19mvp.model.Countries
import co.id.fikridzakwan.covid_19mvp.model.World

interface MainService {
    fun sequenceListener(ascending: Boolean)
    fun itemCliked(country: Countries)
    fun hideProgressBar()
    fun showCountry(listCountries: ArrayList<Countries>)
    fun showWorld(dataCovid: World)
}