package com.gmaur.meest

class Result(val city: String)

class Results(vararg val values: Result) {
    fun first(): Result {
        return values.first()
    }
}