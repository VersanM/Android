package com.mihaiv.stockmarketapp.data.repository

import com.mihaiv.stockmarketapp.data.csv.CSVParser
import com.mihaiv.stockmarketapp.data.csv.CompanyListingsParser
import com.mihaiv.stockmarketapp.data.local.StockDatabase
import com.mihaiv.stockmarketapp.data.mappger.toCompanyListing
import com.mihaiv.stockmarketapp.data.mappger.toCompanyListingEntity
import com.mihaiv.stockmarketapp.data.remote.StockApi
import com.mihaiv.stockmarketapp.domain.model.CompanyListing
import com.mihaiv.stockmarketapp.domain.repository.StockRepository
import com.mihaiv.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api: StockApi,
    val db: StockDatabase,
    val companyListingsParser: CSVParser<CompanyListing>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query = query)
            emit(Resource.Success(data = localListings.map { it.toCompanyListing() }))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Something went wrong while fetching data"))
                null
            }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )

                emit(Resource.Loading(false))
                emit(Resource.Success(data = dao.searchCompanyListing("").map { it.toCompanyListing() }))
            }
        }
    }

}