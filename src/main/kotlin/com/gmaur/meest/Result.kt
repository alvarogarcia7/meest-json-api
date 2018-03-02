package com.gmaur.meest

data class Triad(val id: String, val valueRU: String, val valueUA: String)
data class Biad(val valueRU: String, val valueUA: String)
data class Branch(val type: String?, val code: String?, val typeCode: String?) {

}

data class Result(val city: Triad, val branch: Branch, val district: Triad, val addressMoreInformation: String?, val b2c: String?, val longitude: String?, val latitude: String?,
                  val house: String?, val description: Biad, val limitWeight: String?, val region: Triad, val streetType: Biad, val street: Triad, val id: String?, val stickerCode: String?, val workingHours: String?, val zipCode: String?)

data class Results(val droppoints: List<Result>) {
    fun first(): Result {
        return droppoints.first()
    }
}