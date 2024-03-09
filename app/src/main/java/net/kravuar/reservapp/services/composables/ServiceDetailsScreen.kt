package net.kravuar.reservapp.services.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.kravuar.reservapp.services.domain.Service
import net.kravuar.reservapp.services.services.ServiceRetrievalService
import net.kravuar.reservapp.ui.components.RefreshButton

@Composable
fun ServiceDetailScreen(
    serviceId: Long,
    serviceRetrievalService: ServiceRetrievalService,
    onServiceBusinessSelected: (Long) -> Unit
) {
    val serviceMutableState = remember { mutableStateOf<Service?>(null) }
    var errorState: String? by remember { mutableStateOf(null) }
    var isRefreshing by remember { mutableStateOf(false) }

    val fetchService: suspend () -> Unit = {
        try {
            isRefreshing = true
            serviceMutableState.value = serviceRetrievalService.getServiceById(serviceId)
            errorState = null
        } catch (e: Exception) {
            errorState = "service.fetch-failed"
        } finally {
            isRefreshing = false
        }
    }

    LaunchedEffect(key1 = serviceId) {
        fetchService()
    }

    if (errorState != null) {
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
    } else {
        val currentService = serviceMutableState.value
        if (currentService != null) {
            Scaffold(
                floatingActionButton = {
                    RefreshButton(onClick = fetchService, isRefreshing = isRefreshing)
                }
            ) { padding ->
                Row(
                    modifier = Modifier.padding(padding)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(end = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = currentService.name,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "Owner: ${currentService.business.ownerSub}"
                            )
                        }
                    }
                    Button(onClick = { onServiceBusinessSelected(currentService.business.id) }) {
                        Card(
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Business Info",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Owner: ${currentService.business.ownerSub}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
