package com.gmaur.meest

import org.springframework.util.DigestUtils
import java.io.StringWriter
import java.math.BigInteger
import java.net.URL
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

class Meest(val configuration: Configuration) {

    fun byCity(value: String): List<Result> {
        val request = createRequest(value)
        val connection = URL(this.configuration.url).openConnection()
        connection.useCaches = false
        connection.doInput = true
        connection.doOutput = true
        val requestAsString = request.toString()
        connection.setRequestProperty("Content-Length", "" + requestAsString.length)
        connection.setRequestProperty("Content-Type", "text/xml")
        connection.setRequestProperty("Cache-Control", "no-cache")
        connection.getOutputStream().use { stream -> stream.write(requestAsString.toByteArray()) }

        val input = connection.getInputStream()

        val jaxbContext = JAXBContext.newInstance(Response::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()
        val unmarshal = unmarshaller.unmarshal(input)
        val cast = Response::class.java.cast(unmarshal)
        return listOf(Result(cast.resultTable?.items?.first()!!.CityDescriptionRU!!))
    }


    @XmlRootElement(name = "return")
    class Response {

        @XmlElement(name = "api")
        internal var api: String? = null

        @XmlElement(name = "apiversion")
        internal var apiVersion: String? = null

        @XmlElement(name = "result_table")
        internal var resultTable: ResultTable? = null

        @XmlElement(name = "errors")
        internal var errors: Errors? = null

        class ResultTable {

            @XmlElement(name = "items")
            internal var items: List<Item>? = null

            class Item {

                @XmlElement(name = "AddressMoreInformation")
                internal var AddressMoreInformation: String? = null

                @XmlElement(name = "B2C")
                internal var B2C: String? = null

                @XmlElement(name = "BranchCode")
                internal var BranchCode: String? = null

                @XmlElement(name = "Branchtype")
                internal var Branchtype: String? = null

                @XmlElement(name = "BranchtypeCode")
                internal var BranchtypeCode: String? = null

                @XmlElement(name = "CityDescriptionRU")
                internal var CityDescriptionRU: String? = null

                @XmlElement(name = "CityDescriptionUA")
                internal var CityDescriptionUA: String? = null

                @XmlElement(name = "CityUUID")
                internal var CityUUID: String? = null

                @XmlElement(name = "DescriptionRU")
                internal var DescriptionRU: String? = null

                @XmlElement(name = "DescriptionUA")
                internal var DescriptionUA: String? = null

                @XmlElement(name = "DistrictDescriptionRU")
                internal var DistrictDescriptionRU: String? = null

                @XmlElement(name = "DistrictDescriptionUA")
                internal var DistrictDescriptionUA: String? = null

                @XmlElement(name = "DistrictUUID")
                internal var DistrictUUID: String? = null

                @XmlElement(name = "House")
                internal var House: String? = null

                @XmlElement(name = "Latitude")
                internal var Latitude: String? = null

                @XmlElement(name = "Limitweight")
                internal var Limitweight: String? = null

                @XmlElement(name = "Longitude")
                internal var Longitude: String? = null

                @XmlElement(name = "MotoBranchDescription")
                internal var MotoBranchDescription: String? = null

                @XmlElement(name = "RegionDescriptionRU")
                internal var RegionDescriptionRU: String? = null

                @XmlElement(name = "RegionDescriptionUA")
                internal var RegionDescriptionUA: String? = null

                @XmlElement(name = "RegionUUID")
                internal var RegionUUID: String? = null

                @XmlElement(name = "StickerCode")
                internal var StickerCode: String? = null

                @XmlElement(name = "StreetDescriptionRU")
                internal var StreetDescriptionRU: String? = null

                @XmlElement(name = "StreetDescriptionUA")
                internal var StreetDescriptionUA: String? = null

                @XmlElement(name = "StreetTypeRU")
                internal var StreetTypeRU: String? = null

                @XmlElement(name = "StreetTypeUA")
                internal var StreetTypeUA: String? = null

                @XmlElement(name = "StreetUUID")
                internal var StreetUUID: String? = null

                @XmlElement(name = "UUID")
                internal var UUID: String? = null

                @XmlElement(name = "WorkingHours")
                internal var WorkingHours: String? = null

                @XmlElement(name = "ZipCode")
                internal var ZipCode: String? = null

            }
        }

        class Errors {

            @XmlElement(name = "code")
            internal var code: String? = null

            @XmlElement(name = "name")
            internal var name: String? = null
        }
    }

    @Throws(JAXBException::class)
    private fun createRequest(cityInRussian: String): StringWriter {
        val login = this.configuration.username
        val password = this.configuration.password
        val function = "Branch"
        val where = "CityDescriptionRU='$cityInRussian'"
        val order = ""

        val payload = MeestRequest(login, password, function, where, order)

        val context = JAXBContext.newInstance(MeestRequest::class.java)
        val stringWriter = StringWriter()
        context.createMarshaller().marshal(payload, stringWriter)
        return stringWriter
    }


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

    class Configuration(val username: String, val password: String, val url: String) {
    }
}