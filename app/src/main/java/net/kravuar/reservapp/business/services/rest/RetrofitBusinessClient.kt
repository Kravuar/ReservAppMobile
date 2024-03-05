package net.kravuar.reservapp.business.services.rest

import net.kravuar.reservapp.business.domain.Business
import net.kravuar.reservapp.business.services.BusinessService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBusinessClient : BusinessService {
    private val client = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BusinessTemplate::class.java)

    override suspend fun getActiveBusinesses(): List<Business> {
        return client.getActiveBusinesses()
    }

    override suspend fun getBusinessById(id: Long): Business {
        return client.getBusinessById(id)
    }

    override suspend fun getBusinessesByOwner(ownerSub: String): List<Business> {
        return client.getBusinessesByOwner(ownerSub)
    }

    companion object {
        private const val BASE_URL = "http://localhost:8081/business/api-v1/"
    }
}
