package net.kravuar.reservapp.staff.services.rest

import net.kravuar.reservapp.staff.domain.Staff
import retrofit2.http.GET
import retrofit2.http.Path

interface StaffTemplate {
    @GET("retrieval/by-id/{staffId}")
    suspend fun getStaffById(@Path("staffId") staffId: Long): Staff
}
