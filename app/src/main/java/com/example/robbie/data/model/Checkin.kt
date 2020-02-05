package com.example.robbie.data.model

import java.io.Serializable
import java.util.*

data class Checkin(var employeeId: String = "",
                   var employeeName: String = "",
                   var eventId: Int = 0,
                   var eventName: String = "",
                   var eventPoint: Int = 0,
                   var remarks: String = "",
                   var registerTime: Date = Date()) : Serializable