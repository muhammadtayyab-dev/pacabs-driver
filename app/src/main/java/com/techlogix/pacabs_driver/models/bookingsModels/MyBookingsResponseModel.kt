package com.techlogix.pacabs_driver.models.bookingsModels

data class MyBookingsResponseModel(
    var userImge: Int,
    var bookingUserName: String,
    var bookingId: String,
    var bookingHrs: String,
    var bookingDate: String,
    var bookingsEST: String,
    var cancelledBy:String,
    var cancellationCharges:String
)