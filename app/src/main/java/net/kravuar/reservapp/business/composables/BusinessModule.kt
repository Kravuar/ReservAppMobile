package net.kravuar.reservapp.business.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import net.kravuar.reservapp.business.services.rest.RetrofitBusinessClient

@Composable
fun BusinessModule(navController: NavHostController) {
    val apiService = RetrofitBusinessClient()

    NavHost(navController = navController, startDestination = "businessList") {
        composable(BusinessModuleConstants.LIST_PAGE) {
            BusinessListScreen(businessService = apiService, onBusinessSelected = { businessId ->
                navController.navigate("businessDetail/$businessId")
            })
        }
        composable(
            BusinessModuleConstants.DETAIL_PAGE,
            arguments = listOf(navArgument("businessId") { type = NavType.LongType })
        ) { backStackEntry ->
            val businessId = backStackEntry.arguments?.getLong("businessId") ?: -1
            BusinessDetailScreen(businessId = businessId, businessService = apiService)
        }
    }
}

object BusinessModuleConstants {
    const val LIST_PAGE: String = "businessList"
    const val DETAIL_PAGE: String = "businessDetail/{businessId}"
    const val START_DESTINATION: String = LIST_PAGE
}