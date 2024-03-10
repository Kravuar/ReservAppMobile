package net.kravuar.reservapp.schedule.domain

data class ScheduleStaff(
    val id: Long,
    val business: ScheduleStaffBusiness
)

data class ScheduleStaffBusiness(
    val id: Long,
    val ownerSub: String
)