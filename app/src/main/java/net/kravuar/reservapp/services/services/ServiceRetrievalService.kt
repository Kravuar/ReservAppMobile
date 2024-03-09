package net.kravuar.reservapp.services.services

import net.kravuar.reservapp.services.domain.Service

interface ServiceRetrievalService {
    suspend fun getServiceById(serviceId: Long): Service
    suspend fun getServicesByBusiness(businessId: Long): List<Service>
    suspend fun getActiveServices(): List<Service>
}
