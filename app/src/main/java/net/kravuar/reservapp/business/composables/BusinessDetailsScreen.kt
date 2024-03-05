package net.kravuar.reservapp.business.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.kravuar.reservapp.business.domain.Business
import net.kravuar.reservapp.business.services.BusinessService

@Composable
fun BusinessDetailScreen(
    businessId: Long,
    businessService: BusinessService
) {
    val business = remember { mutableStateOf<Business?>(null) }
    var errorState: String? by remember { mutableStateOf(null) }
    var isRefreshing by remember { mutableStateOf(false) }

    val fetchBusiness: suspend () -> Unit = {
        try {
            isRefreshing = true
            business.value = businessService.getBusinessById(businessId)
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
        if (business.value != null)
            Scaffold(
                floatingActionButton = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(56.dp)
                            .padding(16.dp)
                    ) {
                        FloatingActionButton(
                            modifier = Modifier
                                .size(56.dp),
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    fetchBusiness()
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                        }
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(36.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            ) { padding ->
                Text(
                    text = business.value?.description?: "",
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                )
            }
    }
}
