package com.example.robbie.data.model

import java.io.Serializable
import java.util.*

data class User(var userId: String = "",
                var employeeId: String = "",
                var registerTime: Date = Date()) : Serializable