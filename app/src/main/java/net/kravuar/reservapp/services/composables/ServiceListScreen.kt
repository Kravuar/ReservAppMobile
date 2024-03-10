package net.kravuar.reservapp.services.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.kravuar.reservapp.services.domain.Service
import net.kravuar.reservapp.services.services.ServiceRetrievalService
import net.kravuar.reservapp.ui.components.RefreshButton

@Composable
fun ServiceListScreen(
    serviceRetrievalService: ServiceRetrievalService,
    onServiceSelected: (Long) -> Unit
) {
    var services by remember { mutableStateOf<List<Service>>(emptyList()) }
    var errorState: String? by remember { mutableStateOf(null) }
    var isRefreshing by remember { mutableStateOf(false) }

    val fetchServicees: suspend () -> Unit = {
        try {
            isRefreshing = true
            services = serviceRetrievalService.getActiveServices()
            errorState = null
        } catch (e: Exception) {
            errorState = "business.list.fetch-failed"
        } finally {
            isRefreshing = false
        }
    }

    LaunchedEffect(key1 = Unit) {
        fetchServicees()
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
        Scaffold(
            floatingActionButton = {
                RefreshButton(onClick = fetchServicees, isRefreshing = isRefreshing)
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(services) { service ->
                    ServiceListItem(service = service) {
                        onServiceSelected(service.id)
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceListItem(service: Service, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = service.name,
                modifier = Modifier.weight(0.5f)
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Box(
                modifier = Modifier
                    .weight(0.3f)
            ) {
                Text(
                    text = "Owner: ${service.business.ownerSub}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
