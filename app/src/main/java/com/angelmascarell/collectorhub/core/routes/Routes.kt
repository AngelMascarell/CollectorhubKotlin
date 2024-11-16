package com.angelmascarell.collectorhub.core.routes

sealed class Routes(val route: String) {
    object SignInScreenRoute : Routes("signInScreen")
    object HomeScreenRoute : Routes("HomeScreen")
    object PremiumScreenRoute : Routes("PremiumScreen")
    object GetMangaScreenRoute : Routes("GetMangaScreen/{mangaId}")



    object SignUpScreenRoute : Routes("signUpScreen")
    object RoomDetailsScreenRoute : Routes("roomDetailsScreen/{number}") {
        fun createRoute(number: String) = "roomDetailsScreen/$number"
    }

    //object BookingSummaryScreenRoute : Routes("bookingSummaryScreen")
    object BookingSummaryScreenRoute : Routes("bookingSummaryScreen/{startDate}/{endDate}") {
        fun createRoute(startDate: String, endDate: String, beds: Int?, price: Double?, roomType:String?, roomImg:String?) =
            "bookingSummaryScreen/$startDate/$endDate"
    }

    object PaymentGatewayScreenRoute : Routes("paymentGatewayScreen")
    object PaidScreenRoute : Routes("paidScreen")
    object ProfileScreenRoute : Routes("profileScreen")
    object MyBookingsScreenRoute : Routes("myBookingsScreen")
    object ForgotPasswordScreenRoute : Routes("forgotPasswordScreen")
}