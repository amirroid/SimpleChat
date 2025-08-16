package ir.amirroid.simplechat.extensions

import kotlinx.datetime.toKotlinLocalDateTime
import java.time.ZoneId
import java.util.Date

fun Date.toKotlinLocalDateTime() = toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    .toKotlinLocalDateTime()