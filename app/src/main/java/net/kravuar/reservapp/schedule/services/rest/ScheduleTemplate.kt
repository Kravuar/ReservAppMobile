package net.kravuar.reservapp.schedule.services.rest

import net.kravuar.reservapp.schedule.domain.ReservationSlot
import net.kravuar.reservapp.schedule.domain.ScheduleStaff
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDate
import java.util.SortedMap

interface ScheduleTemplate {
    @GET("retrieval/by-service/{serviceId}/{from}/{to}")
    suspend fun getScheduleByService(@Path("serviceId") serviceId: Long, @Path("from") from: LocalDate, @Path("to") to: LocalDate): Map<ScheduleStaff, SortedMap<LocalDate, Set<ReservationSlot>>>

    @GET("retrieval/by-service-and-staff/{serviceId}/{staffId}/{from}/{to}")
    suspend fun getScheduleByServiceAndStaff(@Path("serviceId") serviceId: Long, @Path("staffId") staffId: Long, @Path("from") from: LocalDate, @Path("to") to: LocalDate): SortedMap<LocalDate, Set<ReservationSlot>>
}
