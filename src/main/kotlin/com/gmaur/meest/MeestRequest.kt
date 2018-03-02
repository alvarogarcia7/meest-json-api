package com.gmaur.meest

import arrow.core.Either
import org.springframework.util.DigestUtils
import java.math.BigInteger
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class MeestRequest {
    private var login: String? = null
    private var password: String? = null
    private var function: String? = null
    private var where: String? = null
    private var order: String? = null

    constructor(login: String, password: String, meestR: MeestR) {
        this.login = login
        this.password = password
        this.function = meestR.function
        this.where = meestR.where
        this.order = meestR.order
    }

    @Deprecated("just for serialization")
    constructor()

    @XmlElement(name = "login")
    fun getLogin(): String {
        return this.login!!
    }

    @XmlElement(name = "function")
    fun getFunction(): String {
        return this.function!!
    }

    @XmlElement(name = "where")
    fun getWhere(): String {
        return this.where!!
    }

    @XmlElement(name = "order")
    fun getOrder(): String {
        return this.order!!
    }

    @XmlElement(name = "sign")
    fun getSign(): String {
        return md5(login + password + getFunction() + getWhere() + getOrder())
    }

    private fun md5(query: String): String {
        return String.format("%032x", BigInteger(1, DigestUtils.md5Digest(query.toByteArray())))
    }

}

data class MeestR(val function: String, val where: String, val order: String) {
    companion object {
        fun byCity(valueInRussian: String): MeestR {
            val function = "Branch"
            val where = "CityDescriptionRU='$valueInRussian'"
            val order = ""

            val request = MeestR(function, where, order)
            return request
        }

        fun parse(values: Map<String, String>): Either<List<BusinessError>, MeestR> {
            if (values.entries.isEmpty()) {
                return notEnoughCriterias()
            }
            return Either.right(MeestR("Branch", createWhere(values), ""))
        }

        private fun createWhere(values: Map<String, String>): String {
            val where = values.entries.map { (key, value) ->
                "$key='$value'"
            }.joinToString(" OR ")
            return where
        }

        private fun notEnoughCriterias(): Either<List<BusinessError>, Nothing> {
            val values = "Received none"
            return Either.left(listOf(BusinessError.aNew("Need a criteria to filter on", values, 400)))
        }

        private fun tooManyCriterias(values: Map<String, String>): Either<List<BusinessError>, Nothing> {
            val values = "Received: " + values.entries.joinToString(", ") { (key, value) -> "$key=$value" }
            return Either.left(listOf(BusinessError.aNew("Cannot parse two criterias at the same time", values, 400)))
        }
    }
}