package com.sdevprem.tasksprint.common.ext

import java.time.LocalDate
import java.time.ZoneId
import java.util.Date


fun LocalDate.toDate() =
    Date.from(
        atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant()
    ) ?: Date()