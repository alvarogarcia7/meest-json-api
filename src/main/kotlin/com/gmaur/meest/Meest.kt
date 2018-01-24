package com.gmaur.meest

import java.io.StringWriter
import java.net.URL
import java.net.URLConnection
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException

class Meest(private val configuration: Configuration) {

    fun byCity(value: String): List<Result> {
        val request = createRequest(value)
        val connection = sendRequest(request)
        val result = readResponse(connection)
        val element = Result(result.resultTable?.items?.first()!!.CityDescriptionRU!!)
        return listOf(element)
    }

    private fun readResponse(connection: URLConnection): Response {
        val input = connection.getInputStream()
        val jaxbContext = JAXBContext.newInstance(Response::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()
        val unmarshal = unmarshaller.unmarshal(input)
        val cast = Response::class.java.cast(unmarshal)
        return cast
    }

    private fun sendRequest(request: StringWriter): URLConnection {
        val connection = URL(this.configuration.url).openConnection()
        connection.useCaches = false
        connection.doInput = true
        connection.doOutput = true
        val requestAsString = request.toString()
        connection.setRequestProperty("Content-Length", "" + requestAsString.length)
        connection.setRequestProperty("Content-Type", "text/xml")
        connection.setRequestProperty("Cache-Control", "no-cache")
        connection.getOutputStream().use { stream -> stream.write(requestAsString.toByteArray()) }
        return connection
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


}