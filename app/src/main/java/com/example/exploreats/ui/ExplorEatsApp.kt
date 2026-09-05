package com.example.exploreats.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.exploreats.data.Coupon
import com.example.exploreats.data.Restaurant
import com.example.exploreats.data.UserSessionManager
import com.example.exploreats.ui.screens.*
import com.example.exploreats.ui.theme.ExplorEatsTheme

sealed class Screen {
    object Auth : Screen()
    object Onboarding : Screen()
    object Home : Screen()
    object Saved : Screen()
    object CouponsList : Screen()
    data class CouponDetail(val coupon: Coupon) : Screen()
    object Profile : Screen()
    data class Detail(val restaurant: Restaurant) : Screen()
}

@Composable
fun ExplorEatsApp() {
    val context = LocalContext.current
    val sessionManager = remember { UserSessionManager(context) }

    var userProfile by remember { mutableStateOf(sessionManager.getUserProfile()) }
    var savedIds by remember { mutableStateOf(sessionManager.getSavedRestaurantIds()) }
    var savedRestaurants by remember { mutableStateOf(sessionManager.getSavedRestaurants()) }
    var claimedCoupons by remember { mutableStateOf(sessionManager.getClaimedCoupons()) }

    var currentScreen by remember {
        mutableStateOf<Screen>(
            if (!userProfile.isLoggedIn) {
                Screen.Auth
            } else if (!sessionManager.isOnboardingCompleted()) {
                Screen.Onboarding
            } else {
                Screen.Home
            }
        )
    }

    var activeTab by remember { mutableStateOf("Explorar") }

    val handleTabNavigation: (String) -> Unit = { tab ->
        activeTab = tab
        when (tab) {
            "Explorar" -> currentScreen = Screen.Home
            "Guardados" -> {
                savedRestaurants = sessionManager.getSavedRestaurants()
                currentScreen = Screen.Saved
            }
            "Cupones" -> {
                claimedCoupons = sessionManager.getClaimedCoupons()
                currentScreen = Screen.CouponsList
            }
            "Perfil" -> currentScreen = Screen.Profile
        }
    }

    val toggleSave: (Restaurant) -> Unit = { restaurant ->
        sessionManager.toggleSaveRestaurant(restaurant)
        savedIds = sessionManager.getSavedRestaurantIds()
        savedRestaurants = sessionManager.getSavedRestaurants()
    }

    ExplorEatsTheme {
        when (val screen = currentScreen) {
            is Screen.Auth -> {
                AuthScreen(
                    onAuthSuccess = { email, name ->
                        sessionManager.saveUserSession(email, name)
                        userProfile = sessionManager.getUserProfile()
                        currentScreen = Screen.Onboarding
                    },
                    onGuestMode = {
                        currentScreen = Screen.Onboarding
                    }
                )
            }

            is Screen.Onboarding -> {
                OnboardingScreen(
                    currentCity = userProfile.cityName,
                    currentRegion = userProfile.regionName,
                    onLocationDetected = { city, region, latLng ->
                        sessionManager.saveLocationPreference(city, region, latLng)
                        userProfile = sessionManager.getUserProfile()
                    },
                    onTouristToggle = { isTourist ->
                        sessionManager.saveTouristPreference(isTourist)
                        userProfile = sessionManager.getUserProfile()
                    },
                    onStartExploring = {
                        sessionManager.setOnboardingCompleted(true)
                        currentScreen = Screen.Home
                        activeTab = "Explorar"
                    }
                )
            }

            is Screen.Home -> {
                HomeScreen(
                    userLocation = userProfile.latLng,
                    cityName = userProfile.cityName,
                    userProfile = userProfile,
                    savedIds = savedIds,
                    onToggleSave = toggleSave,
                    currentTab = activeTab,
                    onSelectRestaurant = { restaurant ->
                        currentScreen = Screen.Detail(restaurant)
                    },
                    onNavigateTab = handleTabNavigation
                )
            }

            is Screen.Saved -> {
                SavedScreen(
                    savedRestaurants = savedRestaurants,
                    userProfile = userProfile,
                    onSelectRestaurant = { restaurant ->
                        currentScreen = Screen.Detail(restaurant)
                    },
                    onToggleSave = toggleSave,
                    onNavigateTab = handleTabNavigation,
                    currentTab = activeTab
                )
            }

            is Screen.CouponsList -> {
                CouponsScreen(
                    claimedCoupons = claimedCoupons,
                    userProfile = userProfile,
                    onSelectCoupon = { coupon ->
                        currentScreen = Screen.CouponDetail(coupon)
                    },
                    onNavigateTab = handleTabNavigation,
                    currentTab = activeTab
                )
            }

            is Screen.CouponDetail -> {
                CouponDetailScreen(
                    coupon = screen.coupon,
                    userProfile = userProfile,
                    onBackToCoupons = {
                        claimedCoupons = sessionManager.getClaimedCoupons()
                        currentScreen = Screen.CouponsList
                    },
                    onNavigateTab = handleTabNavigation,
                    currentTab = activeTab
                )
            }

            is Screen.Profile -> {
                ProfileScreen(
                    userProfile = userProfile,
                    onUpdateProfile = { name, email ->
                        sessionManager.updateProfile(name, email)
                        userProfile = sessionManager.getUserProfile()
                    },
                    onTouristToggle = { isTourist ->
                        sessionManager.saveTouristPreference(isTourist)
                        userProfile = sessionManager.getUserProfile()
                    },
                    onLogout = {
                        sessionManager.logout()
                        userProfile = sessionManager.getUserProfile()
                        savedIds = emptySet()
                        savedRestaurants = emptyList()
                        claimedCoupons = emptyList()
                        activeTab = "Explorar"
                        currentScreen = Screen.Auth
                    },
                    onNavigateTab = handleTabNavigation,
                    currentTab = activeTab
                )
            }

            is Screen.Detail -> {
                DetailScreen(
                    restaurant = screen.restaurant,
                    userProfile = userProfile,
                    isSaved = savedIds.contains(screen.restaurant.id),
                    onToggleSave = { toggleSave(screen.restaurant) },
                    onBackClick = {
                        currentScreen = Screen.Home
                        activeTab = "Explorar"
                    },
                    onClaimDiscount = {
                        val couponToUse = Coupon(
                            id = "c_${screen.restaurant.id}",
                            restaurantId = screen.restaurant.id,
                            restaurantName = screen.restaurant.name,
                            address = screen.restaurant.address,
                            distance = screen.restaurant.distance,
                            rating = screen.restaurant.rating,
                            reviewCount = screen.restaurant.reviewCount,
                            discountTitle = "10% DE DESCUENTO",
                            discountSubtitle = "Válido en el total de tu consumo de alimentos y bebidas",
                            code = screen.restaurant.couponCode ?: "SABOR-MED-10",
                            validUntil = "Válido hoy hasta las 23:59",
                            imageUrl = screen.restaurant.imageUrl
                        )
                        sessionManager.addClaimedCoupon(couponToUse)
                        claimedCoupons = sessionManager.getClaimedCoupons()
                        currentScreen = Screen.CouponDetail(couponToUse)
                        activeTab = "Cupones"
                    }
                )
            }
        }
    }
}
