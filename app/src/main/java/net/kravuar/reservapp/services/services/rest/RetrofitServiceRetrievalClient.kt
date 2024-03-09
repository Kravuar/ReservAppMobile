package net.kravuar.reservapp.services.services.rest

import net.kravuar.reservapp.services.domain.Service
import net.kravuar.reservapp.services.services.ServiceRetrievalService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitServiceRetrievalClient : ServiceRetrievalService {
    private val client = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ServiceTemplate::class.java)

    override suspend fun getServiceById(serviceId: Long): Service {
        return client.getServiceById(serviceId)
    }

    override suspend fun getServicesByBusiness(businessId: Long): List<Service> {
        return client.getServicesByBusiness(businessId)
    }

    override suspend fun getActiveServices(): List<Service> {
        return client.getActiveServices()
    }

    companion object {
        private const val BASE_URL = "http://localhost:8080/services/api-v1/"
    }
}
