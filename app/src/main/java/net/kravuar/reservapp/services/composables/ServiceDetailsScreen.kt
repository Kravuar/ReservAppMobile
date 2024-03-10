package net.kravuar.reservapp.services.composables

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import net.kravuar.reservapp.business.domain.Business
import net.kravuar.reservapp.business.services.BusinessRetrievalService
import net.kravuar.reservapp.schedule.composables.ScheduleWeeklyList
import net.kravuar.reservapp.schedule.services.ScheduleRetrievalService
import net.kravuar.reservapp.services.domain.Service
import net.kravuar.reservapp.services.services.ServiceRetrievalService
import net.kravuar.reservapp.staff.services.StaffRetrievalService
import net.kravuar.reservapp.ui.components.RefreshButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ServiceDetailScreen(
    serviceId: Long,
    serviceRetrievalService: ServiceRetrievalService,
    businessRetrievalService: BusinessRetrievalService,
    onServiceBusinessSelected: (Long) -> Unit,
    onReserve: () -> Unit,
    scheduleRetrievalService: ScheduleRetrievalService,
    staffRetrievalService: StaffRetrievalService
) {
    val serviceState = remember { mutableStateOf<Service?>(null) }
    val businessState = remember { mutableStateOf<Business?>(null) }
    var errorState by remember { mutableStateOf<String?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }

    val fetchService: suspend () -> Unit = {
        try {
            isRefreshing = true
            val service = serviceRetrievalService.getServiceById(serviceId)
            serviceState.value = service
            try {
                val business = businessRetrievalService.getBusinessById(service.business.id)
                businessState.value = business
            } catch (e: Exception) {
                errorState = "business.fetch.error"
            }
            errorState = null
        } catch (e: Exception) {
            errorState = "service.fetch.error"
        } finally {
            isRefreshing = false
        }
    }

    LaunchedEffect(key1 = serviceId) {
        fetchService()
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
        val currentService = serviceState.value
        if (currentService != null) {
            Scaffold(
                floatingActionButton = {
                    RefreshButton(onClick = fetchService, isRefreshing = isRefreshing)
                }
            ) { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier.weight(0.5f),
                                    text = currentService.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                )

                                Spacer(modifier = Modifier.weight(0.2f))

                                val currentBusiness = businessState.value
                                Column(
                                    modifier = Modifier.weight(0.3f),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    if (currentBusiness != null) {
                                        Text(
                                            text = currentBusiness.name,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                        IconButton(
                                            onClick = { onServiceBusinessSelected(currentBusiness.id) },
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.AccountBox,
                                                contentDescription = "To Business"
                                            )
                                        }
                                    } else
                                        Icon(
                                            imageVector = Icons.Filled.Info,
                                            contentDescription = "No Business Info",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp)),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val descriptionText =
                                    currentService.description ?: "Description not available"
                                if (currentService.description == null)
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "No Description",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                Text(
                                    text = descriptionText,
                                    modifier = Modifier.padding(8.dp),
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                ScheduleList(
                                    serviceId,
                                    onReserve,
                                    scheduleRetrievalService,
                                    staffRetrievalService
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleList(
    serviceId: Long,
    onReserve: () -> Unit,
    scheduleRetrievalService: ScheduleRetrievalService,
    staffRetrievalService: StaffRetrievalService
) {
    var dateState by remember { mutableStateOf<LocalDate>(LocalDate.now()) }

    Column (
    ) {
        DatePicker(initialDate = dateState) {
            dateState = it
        }
        ScheduleWeeklyList(
            date = dateState,
            serviceId = serviceId,
            onReserve = onReserve,
            scheduleRetrievalService = scheduleRetrievalService,
            staffRetrievalService = staffRetrievalService
        )
    }
}

@Composable
fun DatePicker(
    initialDate: LocalDate?,
    onDateChanged: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(initialDate ?: LocalDate.now()) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { showDialog = true }
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-d")),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "datePicker"
                )
            }
        }

        if (showDialog) {
            val initYear = selectedDate.year
            val initMonth = selectedDate.monthValue - 1
            val initDayOfMonth = selectedDate.dayOfMonth

            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val pickedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    selectedDate = pickedDate
                    onDateChanged(pickedDate)
                    showDialog = false
                },
                initYear,
                initMonth,
                initDayOfMonth
            ).show()
        }
    }
}

