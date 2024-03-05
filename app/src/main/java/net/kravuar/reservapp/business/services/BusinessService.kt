package net.kravuar.reservapp.business.services

import net.kravuar.reservapp.business.domain.Business

interface BusinessService {
    suspend fun getActiveBusinesses(): List<Business>
    suspend fun getBusinessById(id: Long): Business
    suspend fun getBusinessesByOwner(ownerSub: String): List<Business>
}
