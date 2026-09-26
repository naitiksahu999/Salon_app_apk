package com.example

import org.junit.Assert.*
import org.junit.Test
import java.util.Locale

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testCustomTimeFormatValidation() {
    val regex = Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
    assertTrue(regex.matches("11:30"))
    assertTrue(regex.matches("14:45"))
    assertTrue(regex.matches("09:15"))
    assertTrue(regex.matches("23:59"))
    assertFalse(regex.matches("24:00"))
    assertFalse(regex.matches("12:60"))
    assertFalse(regex.matches("invalid"))
  }

  @Test
  fun testFormattedTimePadding() {
    val parts = "9:5".split(":")
    val formatted = String.format(Locale.US, "%02d:%02d", parts[0].toInt(), parts[1].toInt())
    assertEquals("09:05", formatted)
  }
}
