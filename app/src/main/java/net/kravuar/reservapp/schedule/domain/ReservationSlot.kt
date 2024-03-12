package net.kravuar.reservapp.schedule.domain

import java.time.LocalTime

data class ReservationSlot(
    val start: LocalTime,
    val end: LocalTime,
    val cost: Double,
    val maxReservations: Int
)