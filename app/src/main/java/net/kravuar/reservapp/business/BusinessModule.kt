package net.kravuar.reservapp.business

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import net.kravuar.reservapp.business.composables.BusinessDetailScreen
import net.kravuar.reservapp.business.composables.BusinessListScreen
import net.kravuar.reservapp.business.services.BusinessRetrievalService
import net.kravuar.reservapp.services.services.ServiceRetrievalService

fun NavGraphBuilder.businessModule(
    navController: NavHostController,
    businessRetrievalService: BusinessRetrievalService,
    serviceRetrievalService: ServiceRetrievalService,
) {
    composable(BusinessModuleConstants.LIST_PAGE) {
        BusinessListScreen(
            businessRetrievalService = businessRetrievalService,
            onBusinessSelected = { businessId ->
                navController.navigate("businessDetail/$businessId")
            })
    }
    composable(
        BusinessModuleConstants.DETAIL_PAGE,
        arguments = listOf(navArgument("businessId") { type = NavType.LongType })
    ) { backStackEntry ->
        val businessId = backStackEntry.arguments?.getLong("businessId") ?: -1
        BusinessDetailScreen(
            businessId = businessId,
            businessRetrievalService = businessRetrievalService,
            serviceRetrievalService = serviceRetrievalService,
            onBusinessServiceSelected = { serviceId ->
                navController.navigate("serviceDetail/$serviceId")
            })
    }
}

object BusinessModuleConstants {
    const val LIST_PAGE: String = "businessList"
    const val DETAIL_PAGE: String = "businessDetail/{businessId}"
    const val START_DESTINATION: String = LIST_PAGE
}