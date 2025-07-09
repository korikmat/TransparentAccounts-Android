package com.korikmat.data.source

import com.korikmat.data.source.dto.AccountTransactionsResponseDto
import com.korikmat.data.source.dto.AccountsResponseDto
import com.korikmat.data.source.dto.TransparentAccountDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface EDPApiService {
    @GET("transparentAccounts")
    suspend fun getAccounts(
        @Header("WEB-API-key") apiKey: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("filter") filter: String? = null
    ): Response<AccountsResponseDto>

    @GET("transparentAccounts/{id}")
    suspend fun getAccountById(
        @Header("WEB-API-key") apiKey: String,
        @Path("id") id: String
    ): Response<TransparentAccountDto>

    @GET("transparentAccounts/{id}/transactions")
    suspend fun getAccountTransactions(
        @Header("WEB-API-key") apiKey: String,
        @Path("id") id: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
        @Query("dateFrom") dateFrom: String? = null,
        @Query("dateTo") dateTo: String? = null,
        @Query("filter") filter: String? = null

    ): Response<AccountTransactionsResponseDto>
}