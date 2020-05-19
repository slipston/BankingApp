package hu.ait.bankingapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import hu.ait.bankingapp.data.AppDatabase
import hu.ait.bankingapp.data.Item

class GraphActivity : AppCompatActivity() {
    lateinit var allItems: List<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        val barChart = findViewById(R.id.barChart) as BarChart
        addData(barChart)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_graph, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_main){
            var intentDetails = Intent()
            intentDetails.setClass(this, MainActivity::class.java)
            startActivity(intentDetails)
        }
        return super.onOptionsItemSelected(item)
    }

    fun addData(chart: BarChart) {
        Thread {
            allItems = AppDatabase.getInstance(this).itemDao().getAllItems()

            var incomeEntries = arrayListOf<BarEntry>()
            var expenseEntries = arrayListOf<BarEntry>()
            var indexOfItem = 0f
            for (anItem in allItems) {
                if (anItem.type) {
                    incomeEntries.add(BarEntry(indexOfItem, anItem.amount))
                } else {
                    expenseEntries.add(BarEntry(indexOfItem, anItem.amount))
                }
                indexOfItem += 1f
            }

            var barIncomeDataSet = BarDataSet(incomeEntries, getString(R.string.income))
            var barExpenseDataSet = BarDataSet(expenseEntries, getString(R.string.expense))


            barIncomeDataSet.color = Color.parseColor("#388e3c")
            barExpenseDataSet.color = Color.parseColor("#d32f2f")

            var barDataSet = BarData(barExpenseDataSet, barIncomeDataSet)

            chart.data = barDataSet
            chart.xAxis.setDrawLabels(false)
            chart.axisRight.setDrawLabels(false)
            chart.description.isEnabled = false

        }.start()

    }
}