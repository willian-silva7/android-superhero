package com.stellantis.espw.api

import com.google.gson.annotations.SerializedName

data class AuthTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("Roles") val roles: String,  // ou uma lista de roles, dependendo da estrutura real
    @SerializedName("Id") val userId: String,
    @SerializedName("UserName") val userName: String,
    @SerializedName("CompanyID") val companyId: String,
    @SerializedName("CompanyCurrency") val companyCurrency: String,
    @SerializedName("CompanyLocalLanguage") val companyLocalLanguage: String,
    @SerializedName(".issued") val issued: String,
    @SerializedName(".expires") val expires: String
)