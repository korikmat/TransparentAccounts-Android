package com.korikmat.domain.models

data class AccountTransaction(
    val value: Number?,
    val precision: Number?,
    val currency: String?,
    val type: String?,
    val dueDate: String?,
    val processedDate: String?,
    val description: String?,
    val typeDescription: String,
    val senderAccountNum: String?,
    val receiverAccountNum: String?,
    val variableSymbol: String?,
    val bankCode: String?,
    val constantSymbol: String?,
    val specificSymbol: String?,
)
