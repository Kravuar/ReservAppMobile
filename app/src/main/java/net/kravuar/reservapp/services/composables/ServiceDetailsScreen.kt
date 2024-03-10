package net.kravuar.reservapp.services.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import net.kravuar.reservapp.business.domain.Business
import net.kravuar.reservapp.business.services.BusinessRetrievalService
import net.kravuar.reservapp.services.domain.Service
import net.kravuar.reservapp.services.services.ServiceRetrievalService
import net.kravuar.reservapp.ui.components.RefreshButton

@Composable
fun ServiceDetailScreen(
    serviceId: Long,
    serviceRetrievalService: ServiceRetrievalService,
    businessRetrievalService: BusinessRetrievalService,
    onServiceBusinessSelected: (Long) -> Unit
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
        val currentBusiness = businessState.value
        if (currentService != null && currentBusiness != null) {
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

                                Column(
                                    modifier = Modifier.weight(0.3f),
                                    horizontalAlignment = Alignment.End
                                ) {
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
                        }
                    }
                }
            }
        }
    }
}