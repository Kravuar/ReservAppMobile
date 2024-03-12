package net.kravuar.reservapp.schedule.services.rest

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import net.kravuar.reservapp.schedule.domain.ReservationSlot
import net.kravuar.reservapp.schedule.domain.ScheduleStaff
import net.kravuar.reservapp.schedule.services.ScheduleRetrievalService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException
import java.util.NavigableMap


class RetrofitScheduleRetrievalClient : ScheduleRetrievalService {
    private val client = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
                    .registerTypeAdapter(LocalTime::class.java, LocalTimeDeserializer())
                    .create()
            )
        )
        .build()
        .create(ScheduleTemplate::class.java)

    override suspend fun getScheduleByService(
        serviceId: Long,
        from: LocalDate,
        to: LocalDate
    ): Map<ScheduleStaff, NavigableMap<LocalDate, Set<ReservationSlot>>> {
        return client.getScheduleByService(serviceId, from, to)
            .associateBy({ it.staff }, { it.schedule })
    }

    override suspend fun getScheduleByServiceAndStaff(
        serviceId: Long,
        staffId: Long,
        from: LocalDate,
        to: LocalDate
    ): NavigableMap<LocalDate, Set<ReservationSlot>> {
        return client.getScheduleByServiceAndStaff(serviceId, staffId, from, to)
    }

    companion object {
        private const val BASE_URL = "http://localhost:8080/schedule/api-v1/"
    }
}


class LocalDateDeserializer : JsonDeserializer<LocalDate> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        try {
            if (typeOfT === LocalDate::class.java)
                return LocalDate.parse(json!!.getAsJsonPrimitive().asString)
        } catch (e: DateTimeParseException) {
            throw JsonParseException(e)
        }
        throw IllegalArgumentException("Unknown type: $typeOfT")
    }
}

class LocalTimeDeserializer : JsonDeserializer<LocalTime> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalTime {
        try {
            if (typeOfT === LocalTime::class.java)
                return LocalTime.parse(json!!.getAsJsonPrimitive().asString)
        } catch (e: DateTimeParseException) {
            throw JsonParseException(e)
        }
        throw IllegalArgumentException("Unknown type: $typeOfT")
    }
}