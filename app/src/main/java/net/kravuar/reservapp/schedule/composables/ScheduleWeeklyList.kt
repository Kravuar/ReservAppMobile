package net.kravuar.reservapp.schedule.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import net.kravuar.reservapp.schedule.domain.ReservationSlot
import net.kravuar.reservapp.schedule.services.ScheduleRetrievalService
import net.kravuar.reservapp.staff.domain.Staff
import net.kravuar.reservapp.staff.services.StaffRetrievalService
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.SortedMap

@Composable
fun ScheduleWeeklyList(
    date: LocalDate,
    serviceId: Long,
    onReserve: () -> Unit,
    scheduleRetrievalService: ScheduleRetrievalService,
    staffRetrievalService: StaffRetrievalService
) {
    val scheduleState =
        remember { mutableStateOf<Map<Staff, SortedMap<LocalDate, Set<ReservationSlot>>>?>(null) }
    var errorState by remember { mutableStateOf<String?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }

    val fetchScheduleAndStaff: suspend () -> Unit = {
        val startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        try {
            isRefreshing = true
            val loadedSchedule =
                scheduleRetrievalService.getScheduleByService(serviceId, startOfWeek, endOfWeek)
            scheduleState.value = loadedSchedule
                .mapKeys { staffRetrievalService.getStaffById(it.key.id) }
                .toMap()
            errorState = null
        } catch (e: Exception) {
            errorState = "schedule.fetch.error"
        } finally {
            isRefreshing = false
        }
    }

    LaunchedEffect(key1 = serviceId) {
        fetchScheduleAndStaff()
    }

    if (errorState != null)
        AlertDialog(
            onDismissRequest = { errorState = null },
            title = { Text(text = "Error") },
            text = { Text(text = errorState!!) },
            confirmButton = {
                Button(
                    onClick = { errorState = null }
                ) {
                    Text(text = "OK")
                }
            }
        )
    else {
        val currentSchedule = scheduleState.value
        if (currentSchedule != null) {
            val regroupedByDate = currentSchedule
                .flatMap { (staff, schedule) ->
                    schedule.flatMap { (date, reservationSlots) ->
                        reservationSlots.map { reservationSlot ->
                            date to Pair(staff, reservationSlot)
                        }
                    }
                }.groupBy({ it.first }, { it.second }).toSortedMap()
            ScheduleView(regroupedByDate, onReserve)
        }
    }
}