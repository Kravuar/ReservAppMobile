package net.kravuar.reservapp.services

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import net.kravuar.reservapp.services.composables.ServiceDetailScreen
import net.kravuar.reservapp.services.composables.ServiceListScreen
import net.kravuar.reservapp.services.services.ServiceRetrievalService
import net.kravuar.reservapp.services.services.rest.RetrofitServiceRetrievalClient

fun NavGraphBuilder.servicesModule(navController: NavHostController) {
    composable(ServicesModuleConstants.LIST_PAGE) {
        ServiceListScreen(
            serviceRetrievalService = ServicesModuleConstants.RETRIEVAL_SERVICE,
            onServiceSelected = { serviceId ->
                navController.navigate("serviceDetail/$serviceId")
            })
    }
    composable(
        ServicesModuleConstants.DETAIL_PAGE,
        arguments = listOf(navArgument("serviceId") { type = NavType.LongType })
    ) { backStackEntry ->
        val serviceId = backStackEntry.arguments?.getLong("serviceId") ?: -1
        ServiceDetailScreen(
            serviceId = serviceId,
            serviceRetrievalService = ServicesModuleConstants.RETRIEVAL_SERVICE,
            onServiceBusinessSelected = { businessId ->
                navController.navigate("businessDetail/$businessId")
            })
    }
}

object ServicesModuleConstants {
    val RETRIEVAL_SERVICE: ServiceRetrievalService = RetrofitServiceRetrievalClient()
    const val LIST_PAGE: String = "serviceList"
    const val DETAIL_PAGE: String = "serviceDetail/{serviceId}"
    const val START_DESTINATION: String = LIST_PAGE
}