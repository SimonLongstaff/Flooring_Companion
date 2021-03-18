package com.simon.flooring_companion

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTests {
    @Test
    fun feetToMeters() {
        assertEquals(1.0, MeasurementConvert.feetToMeters(3.28084), 0.01)
        assertEquals(2.0, MeasurementConvert.feetToMeters(6.56168), 0.01)
        assertEquals(4.0, MeasurementConvert.feetToMeters(13.1234), 0.01)
        assertEquals(13.716, MeasurementConvert.feetToMeters(45.0), 0.01)
        assertEquals(304.8, MeasurementConvert.feetToMeters(1000.0), 0.01)
        assertEquals(563.998, MeasurementConvert.feetToMeters(1850.39), 0.01)
        assertEquals(6063.999, MeasurementConvert.feetToMeters(19895.01), 0.01)

    }
}