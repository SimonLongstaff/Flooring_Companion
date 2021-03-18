package com.simon.flooring_companion.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.simon.flooring_companion.MeasurementConvert
import com.simon.flooring_companion.R
import java.text.DecimalFormat

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var feet: SwitchCompat
    private lateinit var height: EditText
    private lateinit var width: EditText
    private lateinit var m2: EditText
    private lateinit var size: EditText
    private lateinit var price: EditText
    private lateinit var additionalInfo: TextView

    private var usingFeet: Boolean = false


    /**
     * View creation
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        inputSetup(root)
        setupCalculation(root)

        return root
    }

    /**
     * Sets up the button onClick listeners
     */
    private fun setupCalculation(root: View) {
        val calculatedPacks: TextView = root.findViewById(R.id.pack_calculated)
        val totalPrice: TextView = root.findViewById(R.id.price)
        val calculate: Button = root.findViewById(R.id.Calculate)

        feet.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            usingFeet = isChecked
        }

        calculate.setOnClickListener {
            val packsCal: Int = calculate()
            calculatedPacks.text = String.format("%d Packs", packsCal)
            if (price.text.toString() != "") {
                val pricePack = price.text.toString().toDouble()
                val price = pricePack * packsCal
                val formatPrice = run { price.format(2) }
                val finPrice = "£$formatPrice"
                totalPrice.text = finPrice
            }
        }
    }

    /**
     * Assigns the UI elements to variables
     */
    private fun inputSetup(root: View) {
        size = root.findViewById(R.id.SizePerPack)
        price = root.findViewById(R.id.PricePerPack)
        height = root.findViewById(R.id.Height)
        width = root.findViewById(R.id.Width)
        m2 = root.findViewById(R.id.M2)
        additionalInfo = root.findViewById(R.id.other_stats)
        feet = root.findViewById(R.id.Feet)
    }

    /**
     * Calculates the number of packs needed
     * Also sets the additional information if any exists and the price of packs if a price was entered
     */
    private fun calculate(): Int {
        var additionalInformation = ""
        val format = DecimalFormat("#0.00")
        var floorSpace = 0.0
        val packSize = size.text.toString().toDouble()

        if (usingFeet) {
            if (height.text.toString() != "" && width.text.toString() != "") {
                val pair = feetHxW(additionalInformation, format)
                additionalInformation = pair.first
                floorSpace = pair.second
            }
            if (m2.text.toString() != "") {
                val pair = feetSquare(additionalInformation)
                additionalInformation = pair.first
                floorSpace = pair.second
            }

        } else {
            if (height.text.toString() != "" && width.text.toString() != "") {
                val pair = metersHxW(additionalInformation)
                additionalInformation = pair.first
                floorSpace = pair.second
            }
            if (m2.text.toString() != "") {
                floorSpace = m2.text.toString().toDouble()
            }
        }

        additionalInfo.text = additionalInformation
        return MeasurementConvert.numPacks(floorSpace, packSize)
    }

    /**
     * Calculation for when Height and Width is provided in meters
     */
    private fun metersHxW(
        additionalInformation: String
    ): Pair<String, Double> {
        val calFloorSpace: Double
        var additionalInformation1 = additionalInformation
        val h = height.text.toString().toDouble()
        val w = width.text.toString().toDouble()
        calFloorSpace = MeasurementConvert.metersSquared(h, w)
        additionalInformation1 = "$additionalInformation1$h x $w Meters \n"
        additionalInformation1 = "$additionalInformation1$calFloorSpace Meters Squared \n "
        return Pair(additionalInformation1, calFloorSpace)
    }

    /**
     * Calculation for when measurement is entered in Feet squared
     */
    private fun feetSquare(
        additionalInformation: String
    ): Pair<String, Double> {
        var additionalInformation1 = additionalInformation
        additionalInformation1 =
            """$additionalInformation1${m2.text.toString().toDouble()} feet squared 
    """
        val calFloorSpace: Double = MeasurementConvert.feetToMeters(m2.text.toString().toDouble())
        additionalInformation1 = "$additionalInformation1$calFloorSpace Meters Squared \n"
        return Pair(additionalInformation1, calFloorSpace)
    }

    /**
     * Calculation for when Height and Width is provided in Feet
     */
    private fun feetHxW(
        additionalInformation: String,
        format: DecimalFormat
    ): Pair<String, Double> {
        var additionalInformation1 = additionalInformation
        val calFloorSpace: Double
        val h = height.text.toString().toDouble()
        val w = width.text.toString().toDouble()
        additionalInformation1 = "$additionalInformation1$h x $w Feet \n"
        additionalInformation1 =
            additionalInformation1 + format.format(MeasurementConvert.feetToMeters(h)) + " x " + format.format(
                MeasurementConvert.feetToMeters(w)
            ) + " Meters \n"
        additionalInformation1 = """$additionalInformation1${h * w} Feet Squared 
    """
        calFloorSpace = MeasurementConvert.feetToMeters(MeasurementConvert.metersSquared(h, w))
        additionalInformation1 = "$additionalInformation1$calFloorSpace Meters Squared \n "
        return Pair(additionalInformation1, calFloorSpace)
    }

    /**
     * Formats the price
     */
    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
}