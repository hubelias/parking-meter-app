package com.hubelias.parkingmeter.parkingmeterapp.domain.fees

import com.hubelias.parkingmeter.parkingmeterapp.domain.money.PLN
import com.hubelias.parkingmeter.parkingmeterapp.domain.money.times
import org.joda.money.CurrencyMismatchException
import org.joda.money.CurrencyUnit.USD
import org.joda.money.Money
import org.junit.Assert.assertEquals
import org.junit.Test


class MoneyUtilsTest {
    @Test(expected = ArithmeticException::class)
    fun testPLN_tooMuchDecimalPlaces() {
        Money.of(PLN, 2.005)
    }

    @Test(expected = CurrencyMismatchException::class)
    fun testPlus_currencyMismatch() {
        Money.of(USD, 3.43) + Money.of(PLN, 2.12)
    }

    @Test
    fun testPlus() {
        assertEquals(
                Money.of(PLN, 4.50),
                Money.of(PLN, 2.31) + Money.of(PLN, 2.19)
        )
    }

    @Test(expected = CurrencyMismatchException::class)
    fun testMinus_currencyMismatch() {
        Money.of(USD, 3.43) - Money.of(PLN, 2.12)
    }

    @Test
    fun testMinus() {
        assertEquals(
                Money.of(PLN, 2.31),
                Money.of(PLN, 4.50) - Money.of(PLN, 2.19)
        )
    }

    @Test
    fun testTimes_withoutRounding() {
        assertEquals(Money.of(PLN, 6.86), Money.of(PLN, 3.43) * 2.0)
    }

    @Test
    fun testTimes_halfEvenRounding() {
        assertEquals(Money.of(PLN, 3.02), Money.of(PLN, 2.01) * 1.5)
        assertEquals(Money.of(PLN, 3.04), Money.of(PLN, 2.03) * 1.5)
        assertEquals(Money.of(PLN, 3.08), Money.of(PLN, 2.05) * 1.5)
    }

}