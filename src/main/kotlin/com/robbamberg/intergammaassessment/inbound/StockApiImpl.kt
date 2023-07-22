package com.robbamberg.intergammaassessment.inbound

import com.robbamberg.intergammaassessment.domain.StockService
import com.robbamberg.intergammaassessment.openapi.CreateStockRequest
import com.robbamberg.intergammaassessment.openapi.StockApi
import com.robbamberg.intergammaassessment.openapi.StockDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class StockApiImpl(private val service: StockService) : StockApi {

    override fun getAllStock(): ResponseEntity<List<StockDTO>> {
        return service.getAllStock().let { ResponseEntity.ok(it) }
    }

    override fun getStock(productCode: String): ResponseEntity<StockDTO> {
        return service.getStock(productCode).let { ResponseEntity.ok(it) }
    }

    override fun saveStock(createStockRequest: CreateStockRequest): ResponseEntity<StockDTO> {
        return service.saveStock(createStockRequest).let { ResponseEntity.ok(it) }
    }

    override fun updateStock(productCode: String, stockDTO: StockDTO): ResponseEntity<StockDTO> {
        return service.updateStock(productCode, stockDTO).let { ResponseEntity.ok(it) }
    }

    override fun deleteStock(productCode: String): ResponseEntity<Unit> {
       service.deleteStock(productCode)
       return ResponseEntity.ok().build()
    }

    override fun reserveStock(productCode: String): ResponseEntity<StockDTO> {
        return service.reserveItem(productCode).let { ResponseEntity.ok(it) }
    }
}