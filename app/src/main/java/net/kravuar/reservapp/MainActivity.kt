package net.kravuar.reservapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import net.kravuar.reservapp.business.BusinessModuleConstants
import net.kravuar.reservapp.business.businessModule
import net.kravuar.reservapp.services.ServicesModuleConstants
import net.kravuar.reservapp.services.servicesModule
import net.kravuar.reservapp.ui.theme.ReservAppMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ReservAppMobileTheme(dynamicColor = false) {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val startDestinationModuleIndex = 0
    val navController = rememberNavController()

    val modules = listOf(
        Pair(
            "Business",
            BusinessModuleConstants.START_DESTINATION
        ),
        Pair(
            "Services",
            ServicesModuleConstants.START_DESTINATION
        )
    )

    Column {
        TopAppBarContent(navController, modules, startDestinationModuleIndex)
        NavHost(
            navController = navController,
            startDestination = modules[startDestinationModuleIndex].second
        ) {
            businessModule(
                navController,
                Services.BUSINESS_RETRIEVAL_SERVICE,
                Services.SERVICE_RETRIEVAL_SERVICE
            )
            servicesModule(
                navController,
                Services.SERVICE_RETRIEVAL_SERVICE,
                Services.BUSINESS_RETRIEVAL_SERVICE,
                Services.SCHEDULE_RETRIEVAL_SERVICE,
                Services.STAFF_RETRIEVAL_SERVICE
            )
        }
    }
}

@Composable
fun TopAppBarContent(
    navController: NavController,
    modules: List<Pair<String, String>>,
    startDestinationModuleIndex: Int
) {
    var expanded by remember { mutableStateOf(false) }
    var currentModule by remember { mutableIntStateOf(startDestinationModuleIndex) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                text = modules[currentModule].first
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = "ReservApp",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clickable { navController.navigate(modules[currentModule].second) }
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(8.dp)
        ) {
            modules.forEach { module ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = module.first,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    onClick = {
                        currentModule = modules.indexOf(module)
                        navController.navigate(module.second)
                        expanded = false
                    })
            }
        }
    }
}