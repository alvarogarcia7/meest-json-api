package com.gmaur.meest

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

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