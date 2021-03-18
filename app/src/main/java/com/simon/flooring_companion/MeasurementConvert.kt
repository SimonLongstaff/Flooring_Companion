package com.simon.flooring_companion

import java.math.RoundingMode
import java.text.DecimalFormat

object MeasurementConvert{

    fun metersSquared(height: Double, width: Double): Double {return height*width}

    fun feetToMeters(feet: Double): Double{
        return (feet*0.3048)

    }

    fun numPacks(size: Double, packSize: Double): Int {
        return Math.ceil(size / packSize * 1.1).toInt()
    }

}