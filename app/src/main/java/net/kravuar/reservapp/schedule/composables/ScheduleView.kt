package net.kravuar.reservapp.schedule.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.kravuar.reservapp.schedule.domain.ReservationSlot
import net.kravuar.reservapp.staff.domain.Staff
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.SortedMap

@Composable
fun ScheduleView(
    schedule: SortedMap<LocalDate, List<Pair<Staff, ReservationSlot>>>,
    onReserve: () -> Unit
) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        scrollState.scrollBy(-delta)
                    }
                }
            )
            .fillMaxSize()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(48.dp)
    ) {
        items(schedule.entries.toList()) { (date, reservations) ->
            DaySchedule(date = date, reservations = reservations, onReserve)
        }
    }
}

@Composable
fun DaySchedule(
    date: LocalDate,
    reservations: List<Pair<Staff, ReservationSlot>>,
    onReserve: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "$date - ${DayOfWeek.from(date)}",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            reservations.forEach { (staff, reservation) ->
                ReservationSlotView(staff = staff, reservation = reservation, onReserve)
            }
        }
    }
}

@Composable
fun ReservationSlotView(staff: Staff, reservation: ReservationSlot, onReserve: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${reservation.start} - ${reservation.end}",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "Cost: $${reservation.cost}",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        enabled = reservation.maxReservations > 0,
                        onClick = { onReserve() }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Reserve slot"
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Reserve", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                    Text(
                        text = "Reservations: ${reservation.maxReservations}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Text(
                text = "Staff: ${staff.sub}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
