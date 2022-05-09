package com.mihaiv.stockmarketapp.data.mappger

import com.mihaiv.stockmarketapp.data.local.CompanyListingEntity
import com.mihaiv.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(name = name, symbol = symbol, exchange = exchange)
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity (name = name, symbol = symbol, exchange = exchange)
}