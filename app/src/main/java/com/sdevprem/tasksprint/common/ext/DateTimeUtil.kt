package com.sdevprem.tasksprint.common.ext

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun LocalDate.toDate() =
    Date.from(
        atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant()
    ) ?: Date()

fun Date.getDisplayDate(): String {
    return SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(this)
}

fun Date.getFormattedSrtMthYr(): String {
    return SimpleDateFormat(DATE_SRT_MTH_YR_FORMAT, Locale.ENGLISH).format(this)
}

fun Calendar.setDateToFirstDateOfMonth() = apply {
    set(
        get(Calendar.YEAR),
        get(Calendar.MONTH),
        getActualMinimum(Calendar.DATE),
        getMinimum(Calendar.HOUR_OF_DAY),
        getMinimum(Calendar.MINUTE),
        getMinimum(Calendar.SECOND)
    )
}

fun Calendar.setDateToLastDateOfMonth() = apply {
    set(
        get(Calendar.YEAR),
        get(Calendar.MONTH),
        getActualMaximum(Calendar.DATE),
        getMaximum(Calendar.HOUR_OF_DAY),
        getMaximum(Calendar.MINUTE),
        getMaximum(Calendar.SECOND)
    )
}

fun Date.toCalendar(): Calendar = Calendar.getInstance().also {
    it.time = this
}

private const val DATE_FORMAT = "EEE, d MMM yyyy"
private const val DATE_SRT_MTH_YR_FORMAT = "MMM yyyy"
