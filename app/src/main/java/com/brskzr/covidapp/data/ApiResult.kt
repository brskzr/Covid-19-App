package com.brskzr.covidapp.data

class ApiResult<T>(val status: Status, val data:T)

enum class Status {
    WAITING,
    SUCCESS,
    ERROR
}