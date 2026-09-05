package com.example.exploreats.data

import com.google.android.gms.maps.model.LatLng

data class InterestCategory(
    val id: String,
    val title: String,
    val iconName: String,
    val selected: Boolean = false
)

data class MenuItem(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val tag: String, // e.g. "Favorito local", "Plato Estrella", "Típico Andaluz"
    val imageUrl: String
)

data class Review(
    val id: String,
    val userName: String,
    val userRole: String, // e.g. "Guía Local · Nivel 6"
    val rating: Double,
    val comment: String,
    val avatarUrl: String
)

data class Restaurant(
    val id: String,
    val name: String,
    val tagline: String,
    val description: String,
    val rating: Double,
    val reviewCount: Int,
    val priceLevel: String, // e.g. "€€"
    val distance: String, // e.g. "450 m"
    val location: LatLng,
    val address: String,
    val hours: String,
    val isOpen: Boolean,
    val matchPercentage: Int, // e.g. 98
    val tags: List<String>,
    val imageUrl: String,
    val galleryImages: List<String>,
    val discountOffer: String?, // e.g. "10% OFF disponible"
    val couponCode: String?, // e.g. "SABOR-MED-10"
    val menuItems: List<MenuItem> = emptyList(),
    val reviews: List<Review> = emptyList()
)

data class Coupon(
    val id: String,
    val restaurantId: String,
    val restaurantName: String,
    val address: String,
    val distance: String,
    val rating: Double,
    val reviewCount: Int,
    val discountTitle: String, // "10% DE DESCUENTO"
    val discountSubtitle: String, // "Válido en el total de tu consumo de alimentos y bebidas"
    val code: String, // "SABOR-MED-10"
    val validUntil: String, // "Válido hoy hasta las 23:59"
    val imageUrl: String
)
