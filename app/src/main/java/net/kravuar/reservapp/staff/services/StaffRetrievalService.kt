package net.kravuar.reservapp.staff.services

import net.kravuar.reservapp.staff.domain.Staff

interface StaffRetrievalService {
    suspend fun getStaffById(staffId: Long): Staff
}
