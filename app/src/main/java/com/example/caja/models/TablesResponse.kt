package com.example.caja.models

data class TablesResponse(
    val countries: List<Country>?,
    val departments: List<Department>?,
    val provinces: List<Province>?,
    val districts: List<District>?,
    val identityDocumentTypes: List<IdentityDocumentType>?
)

//data class Tables(
 //   val countries: List<Country>,
 //   val provinces: List<Province>,
 //   val districts: List<District>,
 //   val identityDocumentTypes: List<IdentityDocumentType>,
 //   val departments: List<Department>
//)//

data class Country(
    val id: String,
    val description: String
)

data class Province(
    val id: String,
    val description: String
)

data class District(
    val id: String,
    val description: String
)

data class Department(
    val id: String,
    val description: String
)

data class IdentityDocumentType(
    val id: Int,
    val description: String
)