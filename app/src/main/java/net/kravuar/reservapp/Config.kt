package net.kravuar.reservapp

import net.kravuar.reservapp.business.services.BusinessRetrievalService
import net.kravuar.reservapp.business.services.rest.RetrofitBusinessRetrievalClient
import net.kravuar.reservapp.schedule.services.ScheduleRetrievalService
import net.kravuar.reservapp.schedule.services.rest.RetrofitScheduleRetrievalClient
import net.kravuar.reservapp.services.services.ServiceRetrievalService
import net.kravuar.reservapp.services.services.rest.RetrofitServiceRetrievalClient
import net.kravuar.reservapp.staff.services.StaffRetrievalService
import net.kravuar.reservapp.staff.services.rest.RetrofitStaffRetrievalClient

object Services {
    val SERVICE_RETRIEVAL_SERVICE: ServiceRetrievalService = RetrofitServiceRetrievalClient()
    val BUSINESS_RETRIEVAL_SERVICE: BusinessRetrievalService = RetrofitBusinessRetrievalClient()
    val SCHEDULE_RETRIEVAL_SERVICE: ScheduleRetrievalService = RetrofitScheduleRetrievalClient()
    val STAFF_RETRIEVAL_SERVICE: StaffRetrievalService = RetrofitStaffRetrievalClient()
}