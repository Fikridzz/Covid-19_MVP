package co.id.fikridzakwan.covid_19mvp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import co.id.fikridzakwan.covid_19mvp.R
import co.id.fikridzakwan.covid_19mvp.adapter.CountryAdapter
import co.id.fikridzakwan.covid_19mvp.model.Countries
import co.id.fikridzakwan.covid_19mvp.model.World
import co.id.fikridzakwan.covid_19mvp.ui.chartcountry.ChartCountryActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat
import java.text.NumberFormat

class MainActivity : AppCompatActivity(), MainService {

    private val mainPresenter = MainPresenter(this)
    private lateinit var countryAdapter: CountryAdapter
    private var ascending = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainPresenter.getCountry()
        btn_sequence.setOnClickListener {
            sequenceListener(ascending)
            ascending = !ascending
        }

        search_view.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countryAdapter.filter.filter(newText)
                return false
            }
        })

        swipe_refresh.setOnRefreshListener {
            mainPresenter.getCountry()
            swipe_refresh.isRefreshing = false
        }
    }

    override fun sequenceListener(ascending: Boolean) {
        rv_country.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)

            if (ascending) {
                (layoutManager as LinearLayoutManager).reverseLayout = true
                (layoutManager as LinearLayoutManager).stackFromEnd = true
                Toast.makeText(this@MainActivity, "Z-A", Toast.LENGTH_SHORT).show()
            } else {
                (layoutManager as LinearLayoutManager).reverseLayout = true
                (layoutManager as LinearLayoutManager).stackFromEnd = true
                Toast.makeText(this@MainActivity, "A-Z", Toast.LENGTH_SHORT).show()
            }
            adapter = countryAdapter
        }
    }

    override fun itemCliked(country: Countries) {
        val intent = Intent(this, ChartCountryActivity::class.java)
        intent.putExtra(ChartCountryActivity.EXTRA_COUNTRY, country.Country)
        intent.putExtra(ChartCountryActivity.EXTRA_LASTEST_UPDATE, country.Date)
        intent.putExtra(ChartCountryActivity.EXTRA_NEW_DEATH, country.NewDeaths)
        intent.putExtra(ChartCountryActivity.EXTRA_NEW_CONFIRMED, country.NewConfirmed)
        intent.putExtra(ChartCountryActivity.EXTRA_NEW_RECOVERED, country.NewRecovered)
        intent.putExtra(ChartCountryActivity.EXTRA_TOTAL_CONFIRMED, country.TotalConfirmed)
        intent.putExtra(ChartCountryActivity.EXTRA_TOTAL_DEATH, country.TotalDeaths)
        intent.putExtra(ChartCountryActivity.EXTRA_TOTAL_RECOVERED, country.TotalRecovered)
        intent.putExtra(ChartCountryActivity.EXTRA_COUNTRY_CODE, country.CountryCode)
        startActivity(intent)
    }

    override fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    override fun showCountry(listCountries: ArrayList<Countries>) {
        rv_country.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            countryAdapter = CountryAdapter(listCountries) {
                itemCliked(it)
            }
            adapter = countryAdapter
        }
    }

    override fun showWorld(dataCovid: World) {
        val formatter: NumberFormat = DecimalFormat("#,##")
        tv_confirmed_globe.text = formatter.format(dataCovid.TotalConfirmed.toDouble())
        tv_deaths_globe.text = formatter.format(dataCovid.TotalDeaths.toDouble())
        tv_recovered_globe.text = formatter.format(dataCovid.TotalRecovered.toDouble())
    }
}
