package com.example.exploreats.data

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONObject

data class UserProfile(
    val email: String,
    val name: String,
    val isLoggedIn: Boolean,
    val isTourist: Boolean = true,
    val cityName: String = "Madrid, España",
    val regionName: String = "Comunidad de Madrid · Zona activa",
    val latLng: LatLng = LatLng(40.4168, -3.7038)
) {
    fun getInitials(): String {
        val parts = name.trim().split(" ").filter { it.isNotEmpty() }
        return when {
            parts.isEmpty() -> "VE"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts[0].first()}${parts[1].first()}".uppercase()
        }
    }
}

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("exploreats_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LOGGED_IN = "is_logged_in"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_NAME = "user_name"
        private const val KEY_IS_TOURIST = "is_tourist"
        private const val KEY_CITY_NAME = "city_name"
        private const val KEY_REGION_NAME = "region_name"
        private const val KEY_LAT = "lat"
        private const val KEY_LNG = "lng"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_SAVED_IDS = "saved_restaurant_ids"
        private const val KEY_SAVED_RESTAURANTS = "saved_restaurants_json"
        private const val KEY_CLAIMED_COUPONS = "claimed_coupons_json"
    }

    fun saveUserSession(email: String, name: String) {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_EMAIL, email)
            .putString(KEY_NAME, name)
            .apply()
    }

    fun updateProfile(name: String, email: String) {
        prefs.edit()
            .putString(KEY_NAME, name)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    fun saveLocationPreference(cityName: String, regionName: String, latLng: LatLng) {
        prefs.edit()
            .putString(KEY_CITY_NAME, cityName)
            .putString(KEY_REGION_NAME, regionName)
            .putFloat(KEY_LAT, latLng.latitude.toFloat())
            .putFloat(KEY_LNG, latLng.longitude.toFloat())
            .apply()
    }

    fun saveTouristPreference(isTourist: Boolean) {
        prefs.edit().putBoolean(KEY_IS_TOURIST, isTourist).apply()
    }

    fun setOnboardingCompleted(completed: Boolean = true) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }

    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun getUserProfile(): UserProfile {
        val isLoggedIn = prefs.getBoolean(KEY_LOGGED_IN, false)
        val email = prefs.getString(KEY_EMAIL, "viajero@exploreats.com") ?: "viajero@exploreats.com"
        val name = prefs.getString(KEY_NAME, "Viajero Explorador") ?: "Viajero Explorador"
        val isTourist = prefs.getBoolean(KEY_IS_TOURIST, true)
        val city = prefs.getString(KEY_CITY_NAME, "Madrid, España") ?: "Madrid, España"
        val region = prefs.getString(KEY_REGION_NAME, "Comunidad de Madrid · Zona activa") ?: "Comunidad de Madrid · Zona activa"
        val lat = prefs.getFloat(KEY_LAT, 40.4168f).toDouble()
        val lng = prefs.getFloat(KEY_LNG, -3.7038f).toDouble()

        return UserProfile(
            email = email,
            name = name,
            isLoggedIn = isLoggedIn,
            isTourist = isTourist,
            cityName = city,
            regionName = region,
            latLng = LatLng(lat, lng)
        )
    }

    // SAVED RESTAURANTS
    fun getSavedRestaurantIds(): Set<String> {
        return prefs.getStringSet(KEY_SAVED_IDS, emptySet()) ?: emptySet()
    }

    fun getSavedRestaurants(): List<Restaurant> {
        val savedJson = prefs.getString(KEY_SAVED_RESTAURANTS, null) ?: return emptyList()
        val list = mutableListOf<Restaurant>()
        try {
            val array = JSONArray(savedJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    Restaurant(
                        id = obj.getString("id"),
                        name = obj.getString("name"),
                        tagline = obj.optString("tagline", ""),
                        description = obj.optString("description", ""),
                        rating = obj.optDouble("rating", 4.5),
                        reviewCount = obj.optInt("reviewCount", 100),
                        priceLevel = obj.optString("priceLevel", "€€"),
                        distance = obj.optString("distance", "500 m"),
                        location = LatLng(obj.optDouble("lat", 0.0), obj.optDouble("lng", 0.0)),
                        address = obj.optString("address", ""),
                        hours = obj.optString("hours", "12:00 - 23:00"),
                        isOpen = obj.optBoolean("isOpen", true),
                        matchPercentage = obj.optInt("matchPercentage", 95),
                        tags = listOf("Guardado"),
                        imageUrl = obj.optString("imageUrl", ""),
                        galleryImages = listOf(obj.optString("imageUrl", "")),
                        discountOffer = if (obj.has("discountOffer")) obj.getString("discountOffer") else null,
                        couponCode = if (obj.has("couponCode")) obj.getString("couponCode") else null
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }

    fun toggleSaveRestaurant(restaurant: Restaurant): Boolean {
        val currentIds = getSavedRestaurantIds().toMutableSet()
        val currentList = getSavedRestaurants().toMutableList()

        val isSaved = if (currentIds.contains(restaurant.id)) {
            currentIds.remove(restaurant.id)
            currentList.removeAll { it.id == restaurant.id }
            false
        } else {
            currentIds.add(restaurant.id)
            currentList.removeAll { it.id == restaurant.id }
            currentList.add(restaurant)
            true
        }

        val jsonArray = JSONArray()
        currentList.forEach { r ->
            val obj = JSONObject()
            obj.put("id", r.id)
            obj.put("name", r.name)
            obj.put("tagline", r.tagline)
            obj.put("description", r.description)
            obj.put("rating", r.rating)
            obj.put("reviewCount", r.reviewCount)
            obj.put("priceLevel", r.priceLevel)
            obj.put("distance", r.distance)
            obj.put("lat", r.location.latitude)
            obj.put("lng", r.location.longitude)
            obj.put("address", r.address)
            obj.put("hours", r.hours)
            obj.put("isOpen", r.isOpen)
            obj.put("matchPercentage", r.matchPercentage)
            obj.put("imageUrl", r.imageUrl)
            obj.put("discountOffer", r.discountOffer)
            obj.put("couponCode", r.couponCode)
            jsonArray.put(obj)
        }

        prefs.edit()
            .putStringSet(KEY_SAVED_IDS, currentIds)
            .putString(KEY_SAVED_RESTAURANTS, jsonArray.toString())
            .apply()

        return isSaved
    }

    // CLAIMED COUPONS
    fun getClaimedCoupons(): List<Coupon> {
        val json = prefs.getString(KEY_CLAIMED_COUPONS, null) ?: return emptyList()
        val list = mutableListOf<Coupon>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    Coupon(
                        id = obj.getString("id"),
                        restaurantId = obj.getString("restaurantId"),
                        restaurantName = obj.getString("restaurantName"),
                        address = obj.getString("address"),
                        distance = obj.getString("distance"),
                        rating = obj.optDouble("rating", 4.8),
                        reviewCount = obj.optInt("reviewCount", 200),
                        discountTitle = obj.getString("discountTitle"),
                        discountSubtitle = obj.getString("discountSubtitle"),
                        code = obj.getString("code"),
                        validUntil = obj.getString("validUntil"),
                        imageUrl = obj.getString("imageUrl")
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }

    fun addClaimedCoupon(coupon: Coupon) {
        val current = getClaimedCoupons().toMutableList()
        current.removeAll { it.id == coupon.id || it.code == coupon.code }
        current.add(0, coupon)

        val array = JSONArray()
        current.forEach { c ->
            val obj = JSONObject()
            obj.put("id", c.id)
            obj.put("restaurantId", c.restaurantId)
            obj.put("restaurantName", c.restaurantName)
            obj.put("address", c.address)
            obj.put("distance", c.distance)
            obj.put("rating", c.rating)
            obj.put("reviewCount", c.reviewCount)
            obj.put("discountTitle", c.discountTitle)
            obj.put("discountSubtitle", c.discountSubtitle)
            obj.put("code", c.code)
            obj.put("validUntil", c.validUntil)
            obj.put("imageUrl", c.imageUrl)
            array.put(obj)
        }

        prefs.edit().putString(KEY_CLAIMED_COUPONS, array.toString()).apply()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
