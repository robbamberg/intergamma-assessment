package com.robbamberg.intergammaassessment.inbound

import com.robbamberg.intergammaassessment.openapi.CreateStockRequest
import com.robbamberg.intergammaassessment.openapi.StockDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@SpringBootTest(properties = ["app.reservation.timeout = 1000"])
class StockHttpApiIntegrationTest(
    @Autowired
    private val stockHttpApi: StockApiImpl
) {

    @Test
    fun `save and fetch stock`() {
        val stock = saveStock()

        val stockList = stockHttpApi.getAllStock().body
        assertThat(stockList.size).isEqualTo(1)
        assertThat(stockList[0]).isEqualTo(stock)

        cleanUpProduct(stock.productCode)
    }

    @Test
    fun `save and update stock`() {
        val stock = saveStock()
        val newName = "new-name"
        val newStore = "new-store"

        stockHttpApi.updateStock(stock.productCode, stock.copy(
            productName = newName,
            store = newStore
        ))

        val updatedStock = stockHttpApi.getStock("product-code").body

        assertThat(updatedStock.productName).isEqualTo(newName)
        assertThat(updatedStock.store).isEqualTo(newStore)

        cleanUpProduct(stock.productCode)
    }

    @Test
    fun `reserve product`() {
        val stock = saveStock()

        stockHttpApi.reserveStock(stock.productCode)

        val updatedStock = stockHttpApi.getStock("product-code").body
        assertThat(updatedStock.reserved).isTrue()

        Thread.sleep(2000)

        val updatedStockAfterTimeout = stockHttpApi.getStock("product-code").body
        assertThat(updatedStockAfterTimeout.reserved).isFalse()

        cleanUpProduct(stock.productCode)
    }

    fun saveStock(): StockDTO {
        stockHttpApi.saveStock(CreateStockRequest(
            productName = "product-name",
            productCode = "product-code",
            store = "store"
            )
        )

        return stockHttpApi.getStock("product-code").body!!
    }

    fun cleanUpProduct(productCode: String) {
        stockHttpApi.deleteStock(productCode)
    }
}
