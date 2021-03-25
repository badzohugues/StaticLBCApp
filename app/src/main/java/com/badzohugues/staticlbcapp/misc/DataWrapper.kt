package com.badzohugues.staticlbcapp.misc

data class DataWrapper<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): DataWrapper<T> {
            return DataWrapper(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String?, data: T?): DataWrapper<T> {
            return DataWrapper(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): DataWrapper<T> {
            return DataWrapper(Status.LOADING, data, null)
        }
    }
}