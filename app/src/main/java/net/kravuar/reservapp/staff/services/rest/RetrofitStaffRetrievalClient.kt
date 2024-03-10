package net.kravuar.reservapp.staff.services.rest

import net.kravuar.reservapp.staff.domain.Staff
import net.kravuar.reservapp.staff.services.StaffRetrievalService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitStaffRetrievalClient : StaffRetrievalService {
    private val client = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(StaffTemplate::class.java)

    override suspend fun getStaffById(staffId: Long): Staff {
        return client.getStaffById(staffId)
    }

    companion object {
        private const val BASE_URL = "http://localhost:8080/staff/api-v1/"
    }
}
