package com.mihaiv.stockmarketapp.domain.repository

import com.mihaiv.stockmarketapp.domain.model.CompanyListing
import com.mihaiv.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}