package net.kravuar.reservapp.services.services.rest

import net.kravuar.reservapp.services.domain.Service
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiceTemplate {
    @GET("retrieval/by-id/{serviceId}")
    suspend fun getServiceById(@Path("serviceId") serviceId: Long): Service

    @GET("retrieval/by-business/{businessId}")
    suspend fun getServicesByBusiness(@Path("businessId") businessId: Long): List<Service>

    @GET("retrieval/active")
    suspend fun getActiveServices(): List<Service>
}
