package com.robbamberg.intergammaassessment.domain

import com.robbamberg.intergammaassessment.openapi.CreateStockRequest
import com.robbamberg.intergammaassessment.openapi.StockDTO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Stock(
    @Id
    @Column(name = "ProductCode")
    val productCode: String,
    @Column(name = "ProductName")
    var productName: String,
    @Column(name = "Store")
    var store: String,
    @Column(name = "Reserved")
    var reserved: Boolean = false,
    @Column(name = "Sold")
    var sold: Boolean = false,
)

fun Stock.toDTO(): StockDTO = StockDTO(
    productCode = this.productCode,
    productName = this.productName,
    store = this.store,
    reserved = this.reserved,
    sold = this.sold,
)

fun CreateStockRequest.toEntity(): Stock = Stock(
    productCode = this.productCode,
    productName = this.productName,
    store = this.store,
)

fun StockDTO.toEntity(): Stock = Stock(
    productCode = this.productCode,
    productName = this.productName,
    store = this.store,
    reserved = this.reserved,
    sold = this.sold,
)
