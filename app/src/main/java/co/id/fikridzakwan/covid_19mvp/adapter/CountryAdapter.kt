package co.id.fikridzakwan.covid_19mvp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.id.fikridzakwan.covid_19mvp.BuildConfig
import co.id.fikridzakwan.covid_19mvp.R
import co.id.fikridzakwan.covid_19mvp.model.Countries
import co.id.fikridzakwan.covid_19mvp.ui.chartcountry.ChartCountryActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_country.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CountryAdapter(val country: ArrayList<Countries>, val clickListener: (Countries) -> Unit):
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(), Filterable {

    var countryFilterList = ArrayList<Countries>()
    init {
        countryFilterList = country
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_country, parent, false))
    }

    override fun getItemCount(): Int = countryFilterList.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bindItem(countryFilterList[position], clickListener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charSearch = constraint.toString()
                countryFilterList = if (charSearch.isEmpty()) {
                    country
                } else {
                    val resultList = ArrayList<Countries>()
                    for (row in country) {
                        if (row.Country.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResult = FilterResults()
                filterResult.values = countryFilterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<Countries>
                notifyDataSetChanged()
            }
        }
    }

    class CountryViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val tvCountry = itemView.findViewById<TextView>(R.id.tv_country_name)
        val tvTotalCase = itemView.findViewById<TextView>(R.id.tv_country_total_case)
        val tvTotalRecoverd = itemView.findViewById<TextView>(R.id.tv_country_total_recovered)
        val tvTotalDeath = itemView.findViewById<TextView>(R.id.tv_country_total_deaths)
        val imgFlag = itemView.findViewById<ImageView>(R.id.img_flag_country)

        fun bindItem(countries: Countries, clickListener: (Countries) -> Unit) {
            tvCountry.text = countries.Country
            val formatter: NumberFormat = DecimalFormat("#,###")
            tvTotalCase.text = formatter.format(countries.TotalConfirmed.toDouble())
            tvTotalRecoverd.text = formatter.format(countries.TotalRecovered.toDouble())
            tvTotalDeath.text = formatter.format(countries.TotalRecovered.toDouble())
            Glide.with(itemView.context).load("https://www.countryflags.io/" + countries.CountryCode + "/flat/16.png").into(imgFlag)

            itemView.setOnClickListener {
                clickListener(countries)
            }
        }
    }
}