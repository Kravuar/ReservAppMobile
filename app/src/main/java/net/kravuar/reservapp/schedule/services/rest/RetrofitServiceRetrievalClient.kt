package net.kravuar.reservapp.schedule.services.rest

import net.kravuar.reservapp.schedule.domain.ReservationSlot
import net.kravuar.reservapp.schedule.domain.ScheduleStaff
import net.kravuar.reservapp.schedule.services.ScheduleRetrievalService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.SortedMap

class RetrofitScheduleRetrievalClient : ScheduleRetrievalService {
    private val client = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ScheduleTemplate::class.java)

    override suspend fun getScheduleByService(serviceId: Long, from: LocalDate, to: LocalDate): Map<ScheduleStaff, SortedMap<LocalDate, Set<ReservationSlot>>> {
        return client.getScheduleByService(serviceId, from, to)
    }
    override suspend fun getScheduleByServiceAndStaff(serviceId: Long, staffId: Long, from: LocalDate, to: LocalDate): SortedMap<LocalDate, Set<ReservationSlot>> {
        return client.getScheduleByServiceAndStaff(serviceId, staffId, from, to)
    }

    companion object {
        private const val BASE_URL = "http://localhost:8080/schedule/api-v1/"
    }
}
