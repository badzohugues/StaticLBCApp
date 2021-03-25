package com.badzohugues.staticlbcapp.misc

import androidx.annotation.StringRes
import com.badzohugues.staticlbcapp.R

enum class ErrorMessage(@StringRes val resId: Int) {
    SERVER_ERROR(R.string.error_server),
    NETWORK_ERROR(R.string.error_no_network),
    DATABASE_ERROR(R.string.error_database),
    UNKNOWN_ERROR(R.string.error_unknown)
}