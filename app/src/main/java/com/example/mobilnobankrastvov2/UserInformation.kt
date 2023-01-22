package com.example.mobilnobankrastvov2

data class UserInformation(
    var email: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var funds: Int? = 0,
    var iban: String? = ""
)
