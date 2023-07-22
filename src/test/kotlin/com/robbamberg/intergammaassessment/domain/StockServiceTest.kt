package com.robbamberg.intergammaassessment.domain

import com.robbamberg.intergammaassessment.openapi.CreateStockRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.lang.IllegalStateException
import java.util.*

@ExtendWith(MockitoExtension::class)
class StockServiceTest {

    @Mock
    private lateinit var repository: StockRepository
    @InjectMocks
    private lateinit var stockService: StockService
    @Captor
    private lateinit var stockCaptor: ArgumentCaptor<Stock>

    private val productCode = "example-product-code"

    @Test
    fun `should get all stock`() {
        val stockItem1 = testData()
        val stockItem2 = testData("item-2")

        `when`(repository.findAll()).thenReturn(listOf(stockItem1, stockItem2))

        val stockList = stockService.getAllStock()

        assertThat(stockList.size).isEqualTo(2)
        assertThat(stockList[0]).isEqualTo(stockItem1.toDTO())
        assertThat(stockList[1]).isEqualTo(stockItem2.toDTO())
    }

    @Test
    fun `should get stock`() {
        val stock = testData()

        `when`(repository.findById(productCode)).thenReturn(Optional.of(stock))

        val stockResult = stockService.getStock(productCode)

        assertThat(stockResult).isEqualTo(stock.toDTO())
    }

    @Test
    fun `should throw error if stock not found`() {
        `when`(repository.findById(productCode)).thenReturn(Optional.empty())

        assertThrows<IllegalStateException> {stockService.getStock(productCode)}
    }


    @Test
    fun `should delete stock`() {
        stockService.deleteStock(productCode)

        verify(repository).deleteById(productCode)
    }

    @Test
    fun `should store stock from request`() {
        val name = "some name"
        val store = "some store"
        `when`(repository.save(any())).thenReturn(testData())

        stockService.saveStock(CreateStockRequest(
            productCode = productCode,
            productName = name,
            store = store,
            )
        )

        verify(repository).save(stockCaptor.capture())
        val stock = stockCaptor.value
        assertThat(stock.productName).isEqualTo(name)
        assertThat(stock.productCode).isEqualTo(productCode)
        assertThat(stock.store).isEqualTo(store)
        assertThat(stock.sold).isFalse()
        assertThat(stock.reserved).isFalse()
    }

    @Test
    fun `should update stock from request`() {
        val stockDTO = testData().toDTO()
        `when`(repository.findById(productCode)).thenReturn(Optional.of(stockDTO.toEntity()))
        `when`(repository.save(stockDTO.toEntity())).thenReturn(stockDTO.toEntity())

        stockService.updateStock(productCode, stockDTO)

        verify(repository).save(stockCaptor.capture())
        assertThat(stockCaptor.value).isEqualTo(stockDTO.toEntity())
    }

    @Test
    fun `should throw exception if item not found`() {
        val stockDTO = testData().toDTO()
        `when`(repository.findById(productCode)).thenReturn(Optional.empty())

        assertThrows<IllegalStateException> {stockService.updateStock(productCode, stockDTO)}
    }

    @Test
    fun `should throw exception if item already reserved`() {
        val stock = testData()
        stock.reserved = true
        `when`(repository.findById(productCode)).thenReturn(Optional.of(stock))

        assertThrows<IllegalStateException> {stockService.reserveItem(productCode)}
    }

    @Test
    fun `should reserve item`() {
        val stock = testData()
        `when`(repository.findById(productCode)).thenReturn(Optional.of(stock))

        stockService.reserveItem(productCode)

        verify(repository).save(stockCaptor.capture())
        val storedStock = stockCaptor.value
        assertThat(storedStock.reserved).isTrue()
    }

    @Test
    fun `should throw error if item not found in cancel reservation`() {
        `when`(repository.findById(productCode)).thenReturn(Optional.empty())

        assertThrows<IllegalStateException> {stockService.cancelReservation(productCode)}
    }

    @Test
    fun `should do nothing if item sold in cancel reservation`() {
        val stock = testData()
        stock.sold = true
        `when`(repository.findById(productCode)).thenReturn(Optional.of(stock))

        stockService.cancelReservation(productCode)

        verifyNoMoreInteractions(repository)
    }
    @Test
    fun `should do nothing if item already not reserved anymore`() {
        val stock = testData()
        stock.sold = true
        stock.reserved = false
        `when`(repository.findById(productCode)).thenReturn(Optional.of(stock))

        stockService.cancelReservation(productCode)

        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `should cancel reservation`() {
        val stock = testData()
        stock.sold = false
        stock.reserved = true
        `when`(repository.findById(productCode)).thenReturn(Optional.of(stock))

        stockService.cancelReservation(productCode)

        verify(repository).save(stockCaptor.capture())
        val storedStock = stockCaptor.value
        assertThat(storedStock.reserved).isFalse()    }

    private fun testData(productCode: String = "example-product-code") : Stock = Stock(
        productCode = productCode,
        productName = "Big thing",
        store = "Store",
        reserved = false,
        sold = false,
    )
}