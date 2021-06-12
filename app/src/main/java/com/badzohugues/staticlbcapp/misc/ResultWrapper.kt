package com.badzohugues.staticlbcapp.misc

data class ResultWrapper<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): ResultWrapper<T> {
            return ResultWrapper(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String?, data: T?): ResultWrapper<T> {
            return ResultWrapper(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): ResultWrapper<T> {
            return ResultWrapper(Status.LOADING, data, null)
        }
    }
}
