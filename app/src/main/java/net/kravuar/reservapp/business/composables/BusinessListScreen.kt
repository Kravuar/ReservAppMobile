package net.kravuar.reservapp.business.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import net.kravuar.reservapp.business.domain.Business
import net.kravuar.reservapp.business.services.BusinessRetrievalService
import net.kravuar.reservapp.ui.components.RefreshButton

@Composable
fun BusinessListScreen(
    businessRetrievalService: BusinessRetrievalService,
    onBusinessSelected: (Long) -> Unit
) {
    var businesses by remember { mutableStateOf<List<Business>>(emptyList()) }
    var errorState: String? by remember { mutableStateOf(null) }
    var isRefreshing by remember { mutableStateOf(false) }

    val fetchBusinesses: suspend () -> Unit = {
        try {
            isRefreshing = true
            businesses = businessRetrievalService.getActiveBusinesses()
            errorState = null
        } catch (e: Exception) {
            errorState = "business.list.fetch-failed"
        } finally {
            isRefreshing = false
        }
    }

    LaunchedEffect(key1 = Unit) {
        fetchBusinesses()
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
                RefreshButton(onClick = fetchBusinesses, isRefreshing = isRefreshing)
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(businesses) { business ->
                    BusinessListItem(business = business) {
                        onBusinessSelected(business.id)
                    }
                }
            }
        }
    }
}

@Composable
fun BusinessListItem(business: Business, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = business.name,
            modifier = Modifier.padding(16.dp)
        )
    }
}
