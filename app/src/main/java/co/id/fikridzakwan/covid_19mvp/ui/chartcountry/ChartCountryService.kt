package co.id.fikridzakwan.covid_19mvp.ui.chartcountry

import co.id.fikridzakwan.covid_19mvp.model.InfoCountry

interface ChartCountryService {
    fun showCountry(dataCovid: List<InfoCountry>)
    fun loadImage()
}