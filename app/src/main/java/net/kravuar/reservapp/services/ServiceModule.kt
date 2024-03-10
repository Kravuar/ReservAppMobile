package net.kravuar.reservapp.services

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import net.kravuar.reservapp.business.services.BusinessRetrievalService
import net.kravuar.reservapp.schedule.services.ScheduleRetrievalService
import net.kravuar.reservapp.services.composables.ServiceDetailScreen
import net.kravuar.reservapp.services.composables.ServiceListScreen
import net.kravuar.reservapp.services.services.ServiceRetrievalService
import net.kravuar.reservapp.staff.services.StaffRetrievalService

fun NavGraphBuilder.servicesModule(
    navController: NavHostController,
    serviceRetrievalService: ServiceRetrievalService,
    businessRetrievalService: BusinessRetrievalService,
    scheduleRetrievalService: ScheduleRetrievalService,
    staffRetrievalService: StaffRetrievalService
) {
    composable(ServicesModuleConstants.LIST_PAGE) {
        ServiceListScreen(
            serviceRetrievalService = serviceRetrievalService,
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
            serviceRetrievalService = serviceRetrievalService,
            businessRetrievalService = businessRetrievalService,
            onServiceBusinessSelected = { businessId ->
                navController.navigate("businessDetail/$businessId")
            },
            onReserve = { },
            scheduleRetrievalService,
            staffRetrievalService
        )
    }
}

object ServicesModuleConstants {
    const val LIST_PAGE: String = "serviceList"
    const val DETAIL_PAGE: String = "serviceDetail/{serviceId}"
    const val START_DESTINATION: String = LIST_PAGE
}