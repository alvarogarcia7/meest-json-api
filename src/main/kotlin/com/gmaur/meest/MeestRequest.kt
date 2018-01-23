package com.gmaur.meest

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

    constructor(login: String, password: String, function: String, where: String, order: String) {
        this.login = login
        this.password = password
        this.function = function
        this.where = where
        this.order = order
    }

    @Deprecated("just for serialization")
    constructor()

    @XmlElement(name = "login")
    fun getLogin(): String {
        return this.login!!
    }

    fun getPassword(): String {
        return this.password!!
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
        return md5(getLogin() + getPassword() + getFunction() + getWhere() + getOrder())
    }

    private fun md5(query: String): String {
        return String.format("%032x", BigInteger(1, DigestUtils.md5Digest(query.toByteArray())))
    }

}