package net.kravuar.reservapp.staff.domain

data class Staff(
    val id: Long,
    val sub: String,
    val business: StaffBusiness
)

data class StaffBusiness(
    val id: Long,
    val ownerSub: String
)