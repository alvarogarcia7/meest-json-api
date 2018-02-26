package com.gmaur.meest

import arrow.core.Either
import org.springframework.stereotype.Component
import java.io.StringWriter
import java.net.URL
import java.net.URLConnection
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException

@Component
class Meest(private val configuration: Configuration, private val mapper: Mapper) {

    fun request(value: MeestRequest): Either<List<Error>, Results> {
        return createRequest(value)
                .let {
                    sendRequest(it)
                }.let {
                    val result = readResponse(it)
                    println(result)
                    result
                }.let {
                    mapper.map(it.toEither())
                }
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
        fun newConnection(url: String): URLConnection {
            val connection = URL(url).openConnection()
            connection.useCaches = false
            connection.doInput = true
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "text/xml")
            connection.setRequestProperty("Cache-Control", "no-cache")
            return connection
        }

        fun addPayload(connection: URLConnection, request: StringWriter): URLConnection {
            val requestAsString = request.toString()
            connection.setRequestProperty("Content-Length", "" + requestAsString.length)
            connection.getOutputStream().use { stream -> stream.write(requestAsString.toByteArray()) }
            return connection
        }

        val connection = newConnection(configuration.url)
        return addPayload(connection, request)
    }


    @Throws(JAXBException::class)
    private fun createRequest(payload: MeestRequest): StringWriter {
        payload.setLogin(this.configuration.username)
        payload.password = this.configuration.password

        val context = JAXBContext.newInstance(MeestRequest::class.java)
        val stringWriter = StringWriter()
        context.createMarshaller().marshal(payload, stringWriter)
        return stringWriter
    }

    companion object {
        fun parseByCity(city: String): Either<List<Error>, MeestRequest> {
            return Either.right(MeestRequest.byCity(city))
        }
    }

    @Component
    class Mapper {
        fun map(result: Either<Response, Response>): Either<List<Error>, Results> {
            return result.bimap(
                    {
                        listOf(BusinessError.x("Authentication Error", BusinessError.leaf(it.errors?.name!!)))
                    },
                    {
                        Results(it.resultTable?.items?.map {
                            Result(
                                    city = Triad(id = it.CityUUID!!, valueUA = it.CityDescriptionUA!!, valueRU = it.CityDescriptionRU!!),
                                    branch = Branch(type = it.Branchtype, code = it.BranchCode, typeCode = it.BranchtypeCode),
                                    district = Triad(id = it.DistrictUUID!!, valueRU = it.DistrictDescriptionRU!!, valueUA = it.DistrictDescriptionUA!!),
                                    addressMoreInformation = it.AddressMoreInformation,
                                    b2c = it.B2C,
                                    description = Biad(valueRU = it.DescriptionRU!!, valueUA = it.DescriptionRU!!),
                                    house = it.House,
                                    latitude = it.Latitude,
                                    longitude = it.Longitude,
                                    limitWeight = it.Limitweight,
                                    region = Triad(id = it.RegionUUID!!, valueRU = it.RegionDescriptionRU!!, valueUA = it.RegionDescriptionUA!!),
                                    streetType = Biad(valueRU = it.StreetTypeRU!!, valueUA = it.StreetTypeUA!!),
                                    street = Triad(id = it.StreetUUID!!, valueRU = it.StreetDescriptionRU!!, valueUA = it.StreetTypeUA!!),
                                    id = it.UUID,
                                    stickerCode = it.StickerCode,
                                    workingHours = it.WorkingHours,
                                    zipCode = it.ZipCode)
                        }!!)
                    })
        }

    }
}


data class BusinessError private constructor(override val message: String, val next: BusinessError?) : Error(message) {
    companion object {
        fun leaf(message: String): BusinessError {
            return BusinessError(message, null)
        }

        fun x(message: String, next: BusinessError): BusinessError {
            return BusinessError(message, next)
        }
    }
}
