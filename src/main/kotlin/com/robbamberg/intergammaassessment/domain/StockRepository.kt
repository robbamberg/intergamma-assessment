package com.robbamberg.intergammaassessment.domain

import org.springframework.data.jpa.repository.JpaRepository

interface StockRepository : JpaRepository<Stock, String>