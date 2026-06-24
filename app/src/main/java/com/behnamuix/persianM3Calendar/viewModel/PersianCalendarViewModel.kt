package com.behnamuix.persianM3Calendar.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.behnamuix.persianM3Calendar.model.Reminder
import saman.zamani.persiandate.PersianDate

class PersianCalendarViewModel() : ViewModel() {

    val monthNames = listOf(
        "فروردین",
        "اردیبهشت",
        "خرداد",
        "تیر",
        "مرداد",
        "شهریور",
        "مهر",
        "آبان",
        "آذر",
        "دی",
        "بهمن",
        "اسفند"
    )
    private val today = PersianDate()

    var currentYear by mutableIntStateOf(today.shYear)
        private set

    var currentMonth by mutableIntStateOf(today.shMonth)
        private set

    var selectedDay by mutableStateOf<Int?>(null)
        private set

    private val _reminders = mutableStateListOf<Reminder>()
    val reminders: List<Reminder> = _reminders

    fun addReminder(reminder: Reminder) {
        _reminders.add(reminder)
    }

    fun getReminderForDay(year: Int, month: Int, day: Int): List<Reminder> {
        return reminders.filter {
            it.year == year &&
                    it.month == month &&
                    it.day == day
        }
    }
    fun removeReminder(reminder: Reminder) {
        _reminders.remove(reminder)
    }

    fun selectDay(day: Int) {
        selectedDay = day
    }

    fun nextMonth() {
        if (currentMonth == 12) {
            currentMonth = 1
            currentYear++
        } else {
            currentMonth++
        }
        selectedDay = null
    }

    fun prevMonth() {
        if (currentMonth == 1) {
            currentMonth = 12
            currentYear--
        } else {
            currentMonth--
        }
        selectedDay = null
    }

    fun getFirstDayInMonth(): Int {
        val tempDate = PersianDate()
        tempDate.shYear = currentYear
        tempDate.shMonth = currentMonth
        tempDate.shDay = 1

        return tempDate.dayOfWeek()
    }

}
