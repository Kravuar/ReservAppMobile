package net.kravuar.reservapp.business.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import net.kravuar.reservapp.business.domain.Business
import net.kravuar.reservapp.business.services.BusinessRetrievalService
import net.kravuar.reservapp.services.domain.Service
import net.kravuar.reservapp.services.services.ServiceRetrievalService
import net.kravuar.reservapp.ui.components.RefreshButton

@Composable
fun BusinessDetailScreen(
    businessId: Long,
    businessRetrievalService: BusinessRetrievalService,
    serviceRetrievalService: ServiceRetrievalService,
    onBusinessServiceSelected: (Long) -> Unit
) {
    val business = remember { mutableStateOf<Business?>(null) }
    val services = remember { mutableStateOf<List<Service>?>(null) }
    var errorState: String? by remember { mutableStateOf(null) }
    var isRefreshing by remember { mutableStateOf(false) }

    val fetchBusiness: suspend () -> Unit = {
        try {
            isRefreshing = true
            business.value = businessRetrievalService.getBusinessById(businessId)
            services.value = serviceRetrievalService.getServicesByBusiness(businessId)
            errorState = null
        } catch (e: Exception) {
            errorState = "business.fetch-failed"
        } finally {
            isRefreshing = false
        }
    }

    LaunchedEffect(key1 = businessId) {
        fetchBusiness()
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
        val currentBusiness = business.value
        if (currentBusiness != null) {
            Scaffold(
                floatingActionButton = {
                    RefreshButton(onClick = fetchBusiness, isRefreshing = isRefreshing)
                },
                modifier = Modifier.background(Color.Blue)
            ) { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.End)
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = currentBusiness.name,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "Owner: ${currentBusiness.ownerSub}"
                            )

                            val descriptionText =
                                currentBusiness.description ?: "Description not available"
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp)),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (currentBusiness.description == null)
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "No Description",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                Text(
                                    text = descriptionText,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Business Services",
                            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        val currentBusinessServices = services.value
                        val scrollState = rememberLazyListState()
                        val coroutineScope = rememberCoroutineScope()
                        if (currentBusinessServices != null) {
                            LazyColumn(
                                state = scrollState,
                                modifier = Modifier
                                    .draggable(
                                        orientation = Orientation.Vertical,
                                        state = rememberDraggableState { delta ->
                                            coroutineScope.launch {
                                                scrollState.scrollBy(-delta)
                                            }
                                        }
                                    )
                                    .fillMaxHeight()
                                    .background(
                                        color = MaterialTheme.colorScheme.secondary,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .background(Color.Transparent),
                            ) {
                                items(currentBusinessServices) { service ->
                                    BusinessServicesListItem(service, onBusinessServiceSelected)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BusinessServicesListItem(service: Service, onClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { onClick(service.id) },
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = service.name,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}