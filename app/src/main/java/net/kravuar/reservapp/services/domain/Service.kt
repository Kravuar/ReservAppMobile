package net.kravuar.reservapp.services.domain

data class Service(
    val id: Long,
    val business: ServiceBusiness,
    val active: Boolean,
    val name: String,
    val description: String?
)

data class ServiceBusiness(
    val id: Long,
    val ownerSub: String
)