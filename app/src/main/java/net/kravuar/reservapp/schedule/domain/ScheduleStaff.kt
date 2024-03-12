package net.kravuar.reservapp.schedule.domain

import java.time.LocalDate
import java.util.NavigableMap

data class ScheduleStaff(
    val id: Long,
    val business: ScheduleStaffBusiness
)

data class ScheduleStaffBusiness(
    val id: Long,
    val ownerSub: String
)

data class ScheduleByStaff(
    val staff: ScheduleStaff,
    val schedule: NavigableMap<LocalDate, Set<ReservationSlot>>
)