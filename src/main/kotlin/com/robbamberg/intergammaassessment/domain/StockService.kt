package com.robbamberg.intergammaassessment.domain

import com.robbamberg.intergammaassessment.openapi.CreateStockRequest
import com.robbamberg.intergammaassessment.openapi.StockDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import kotlin.concurrent.schedule

@Service
class StockService(private val repository: StockRepository) {

    private val logger = LoggerFactory.getLogger(StockService::class.java)

    @Value("\${app.reservation.timeout}")
    var reservationTimeout: Long = 1000

    fun getAllStock(): List<StockDTO> {
        logger.info("Fetching all stock")
        return repository.findAll().map{ it.toDTO() }
    }

    fun getStock(productCode: String): StockDTO {
        logger.info("Fetching stock")
        return repository.findById(productCode)
            .orElseThrow{ throw IllegalStateException("Product not found") }
            .toDTO()
    }

    fun saveStock(stockRequest: CreateStockRequest): StockDTO {
        logger.info("Saving stock")
        if(repository.findById(stockRequest.productCode).isPresent) {
            throw IllegalStateException("Product already exists")
        }

        return repository.save(stockRequest.toEntity()).toDTO()
    }

    fun updateStock(productCode: String, stockDTO: StockDTO): StockDTO {
        logger.info("Updating stock")
        repository.findById(productCode).orElseThrow{ throw IllegalStateException("Product not found") }

        return repository.save(stockDTO.toEntity()).toDTO()
    }

    fun deleteStock(productCode: String) {
        repository.deleteById(productCode)
    }

    fun reserveItem(productCode: String) : StockDTO {
        logger.info("Reserving stock")
        val stock = repository.findById(productCode).orElseThrow{ IllegalStateException("Product not found") }
        if (stock.reserved) {
            throw IllegalStateException("Product already reserved")
        }

        stock.reserved = true
        repository.save(stock)

        // After 5 minutes the reservation is cancelled
        Timer("CancelReservation", false).schedule(reservationTimeout) {
            cancelReservation(productCode)
        }

        return stock.toDTO()
    }

    fun cancelReservation(productCode: String) {
        val stock = repository.findById(productCode).orElseThrow{ IllegalStateException("Product not found") }
        if (stock.sold) {
            // no need to cancel reservation if product is sold
            return
        }

        if (stock.reserved) {
            stock.reserved = false
            repository.save(stock)
            logger.info("Stock reservation cancelled")
        }
    }
}