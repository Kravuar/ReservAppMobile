package net.kravuar.reservapp.schedule.services

import net.kravuar.reservapp.schedule.domain.ReservationSlot
import net.kravuar.reservapp.schedule.domain.ScheduleStaff
import java.time.LocalDate
import java.util.NavigableMap
import java.util.SortedMap

interface ScheduleRetrievalService {
    suspend fun getScheduleByService(serviceId: Long, from: LocalDate, to: LocalDate): Map<ScheduleStaff, NavigableMap<LocalDate, Set<ReservationSlot>>>
    suspend fun getScheduleByServiceAndStaff(serviceId: Long, staffId: Long, from: LocalDate, to: LocalDate): NavigableMap<LocalDate, Set<ReservationSlot>>
}
