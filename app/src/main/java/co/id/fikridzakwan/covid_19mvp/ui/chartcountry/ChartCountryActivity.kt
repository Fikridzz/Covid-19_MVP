package co.id.fikridzakwan.covid_19mvp.ui.chartcountry

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import co.id.fikridzakwan.covid_19mvp.R
import co.id.fikridzakwan.covid_19mvp.model.InfoCountry
import co.id.fikridzakwan.covid_19mvp.ui.main.MainPresenter
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.activity_chart_country.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChartCountryActivity : AppCompatActivity(), ChartCountryService {

    private lateinit var sharedPreferences: SharedPreferences
    private var sharedPrefFile = "kotlinsharedpreferences"
    private var dayCases = ArrayList<String>()
    private val chartCountryPresenter = ChartCountryPresenter(this)
    private lateinit var dataFlag: String
    private lateinit var dataCountry: String

    companion object {
        const val EXTRA_COUNTRY = "country"
        const val EXTRA_LASTEST_UPDATE = "date"
        const val EXTRA_NEW_DEATH = "new_death"
        const val EXTRA_NEW_CONFIRMED = "new_confirmed"
        const val EXTRA_NEW_RECOVERED = "new_recovered"
        const val EXTRA_TOTAL_CONFIRMED = "total_confirmed"
        const val EXTRA_TOTAL_DEATH = "total_death"
        const val EXTRA_TOTAL_RECOVERED = "total_recovered"
        const val EXTRA_COUNTRY_CODE = "code"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart_country)

        sharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val country = intent.getStringExtra(EXTRA_COUNTRY)
        val date = intent.getStringExtra(EXTRA_LASTEST_UPDATE)
        val newDeath = intent.getStringExtra(EXTRA_NEW_DEATH)
        val newConfirmed = intent.getStringExtra(EXTRA_NEW_CONFIRMED)
        val newReovered = intent.getStringExtra(EXTRA_NEW_RECOVERED)
        val totalConfirmed = intent.getStringExtra(EXTRA_TOTAL_CONFIRMED)
        val totalDeath = intent.getStringExtra(EXTRA_TOTAL_DEATH)
        val totalRecovered = intent.getStringExtra(EXTRA_TOTAL_RECOVERED)
        val countryCode = intent.getStringExtra(EXTRA_COUNTRY_CODE)

        val formatter: NumberFormat = DecimalFormat("#,###")
        tv_country_chart.text = country
        tv_curent.text = date
        tv_total_confirmed_current.text = formatter.format(totalConfirmed.toDouble())
        tv_new_confirmed_current.text = formatter.format(newConfirmed.toDouble())
        tv_total_recovered_current.text = formatter.format(totalRecovered.toDouble())
        tv_new_recovered_current.text = formatter.format(newReovered.toDouble())
        tv_total_deaths_current.text = formatter.format(totalDeath.toDouble())
        tv_new_deaths_current.text = formatter.format(newDeath.toDouble())

        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(country, country)
        editor.apply()
        editor.commit()

        val saveDataCountry = sharedPreferences.getString(country, country)
        val saveCountryFlag = sharedPreferences.getString(countryCode, countryCode)
        dataCountry = saveDataCountry.toString()
        dataFlag = saveCountryFlag.toString() + "/flat/64.png"

        chartCountryPresenter.getCountry(dataCountry)

        if (saveCountryFlag != null) {
            loadImage()
        } else {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showCountry(dataCovid: List<InfoCountry>) {
        val barEntries1: ArrayList<BarEntry> = ArrayList()
        val barEntries2: ArrayList<BarEntry> = ArrayList()
        val barEntries3: ArrayList<BarEntry> = ArrayList()
        val barEntries4: ArrayList<BarEntry> = ArrayList()
        var i = 0

        while (i < dataCovid.size ?: 0) {
            for (s in dataCovid) {
                val barEntry1 = BarEntry(i.toFloat(), s.Confirmed.toFloat())
                val barEntry2 = BarEntry(i.toFloat(), s.Deaths.toFloat())
                val barEntry3 = BarEntry(i.toFloat(), s.Recovered.toFloat())
                val barEntry4 = BarEntry(i.toFloat(), s.Active.toFloat())

                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS'Z'")
                val outputFormat = SimpleDateFormat("dd-MM-yyyy")
                val date: Date? = inputFormat.parse(s.Date)
                val formattedDate: String = outputFormat.format(date!!)
                dayCases.add(formattedDate)

                barEntries1.add(barEntry1)
                barEntries2.add(barEntry2)
                barEntries3.add(barEntry3)
                barEntries4.add(barEntry4)

                i++
            }
        }

        val xAxis: XAxis = chart_view.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(dayCases)
        chart_view.axisLeft.axisMinimum = 0f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.isGranularityEnabled = true

        val barDataSet1 = BarDataSet(barEntries1, "Confirmed")
        val barDataSet2 = BarDataSet(barEntries2, "Deaths")
        val barDataSet3 = BarDataSet(barEntries3, "Recovered")
        val barDataSet4 = BarDataSet(barEntries4, "Active")

        barDataSet1.setColor(Color.parseColor("#F44336"))
        barDataSet2.setColor(Color.parseColor("#FFEB3B"))
        barDataSet3.setColor(Color.parseColor("#03DAC5"))
        barDataSet4.setColor(Color.parseColor("#2196F3"))

        val data = BarData(barDataSet1, barDataSet2, barDataSet3, barDataSet4)
        chart_view.data = data

        val barSpace = 0.02f
        val groupSpace = 0.3f
        val groupCount = 4f

        data.barWidth = 0.15f
        chart_view.invalidate()
        chart_view.setNoDataTextColor(android.R.color.black)
        chart_view.setTouchEnabled(true)
        chart_view.description.isEnabled = false

        chart_view.xAxis.axisMinimum = 0f
        chart_view.setVisibleXRangeMaximum(0f + chart_view.barData.getGroupWidth(groupSpace, barSpace) * groupCount)
        chart_view.groupBars(0f, groupSpace, barSpace)
    }

    override fun loadImage() {
        Glide.with(this).load("https://www.countryflags.io/$dataFlag").into(img_flag_chart)
    }
}
