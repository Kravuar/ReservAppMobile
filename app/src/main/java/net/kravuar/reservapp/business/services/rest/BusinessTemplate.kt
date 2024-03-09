package net.kravuar.reservapp.business.services.rest

import net.kravuar.reservapp.business.domain.Business
import retrofit2.http.GET
import retrofit2.http.Path

interface BusinessTemplate {
    @GET("retrieval/by-id/{businessId}")
    suspend fun getBusinessById(@Path("businessId") businessId: Long): Business

    @GET("retrieval/byOwner/{ownerSub}")
    suspend fun getBusinessesByOwner(@Path("ownerSub") ownerSub: String): List<Business>

    @GET("retrieval/active")
    suspend fun getActiveBusinesses(): List<Business>
}
