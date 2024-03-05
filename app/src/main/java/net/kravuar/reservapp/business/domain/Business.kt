package net.kravuar.reservapp.business.domain

data class Business(
    val id: Long,
    val ownerSub: String,
    val name: String,
    val active: Boolean,
    val description: String
)
