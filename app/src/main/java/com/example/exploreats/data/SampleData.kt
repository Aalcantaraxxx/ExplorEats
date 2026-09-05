package com.example.exploreats.data

import com.google.android.gms.maps.model.LatLng

object SampleData {

    val interestCategories = listOf(
        InterestCategory("1", "Restaurantes con estrella y alta cocina", "star", true),
        InterestCategory("2", "Miradores y atardeceres", "sunset", true),
        InterestCategory("3", "Monumentos y museos históricos", "museum", true),
        InterestCategory("4", "Vida nocturna y coctelerías", "cocktail", true),
        InterestCategory("5", "Mercados locales y comida callejera", "store", true),
        InterestCategory("6", "Rutas al aire libre y naturaleza", "nature", true),
        InterestCategory("7", "Tapas tradicionales y bodegas", "tapas", true),
        InterestCategory("8", "Cafeterías de especialidad", "coffee", true)
    )

    val sampleRestaurants = listOf(
        Restaurant(
            id = "1",
            name = "La Taberna del Califa",
            tagline = "Cocina Tradicional y tapas mediterráneas",
            description = "Cocina Mediterránea Tradicional • Tapas de Autor • Terraza",
            rating = 4.9,
            reviewCount = 342,
            priceLevel = "€€",
            distance = "450 m",
            location = LatLng(37.3886, -5.9953), // Sevilla centro / Madrid centro
            address = "Calle Mayor 14, Centro",
            hours = "Abierto hoy 12:30 - 23:30 (Cocina continua)",
            isOpen = true,
            matchPercentage = 98,
            tags = listOf("Tapas Ibéricas", "Vinos Naturales", "Terraza"),
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80",
            galleryImages = listOf(
                "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1559339352-11d035aa65de?auto=format&fit=crop&w=800&q=80"
            ),
            discountOffer = "10% OFF disponible",
            couponCode = "SABOR-MED-10",
            menuItems = listOf(
                MenuItem(
                    id = "m1",
                    name = "Croquetas de jamón ibérico",
                    description = "Receta centenaria con bechamel extra cremosa y crujiente de bellota.",
                    price = "9,50 €",
                    tag = "Favorito local",
                    imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?auto=format&fit=crop&w=400&q=80"
                ),
                MenuItem(
                    id = "m2",
                    name = "Pulpo a la brasa con puré",
                    description = "Puntual braseado al carbón de encina, aceite virgen extra y pimentón ahumado.",
                    price = "18,00 €",
                    tag = "Plato Estrella",
                    imageUrl = "https://images.unsplash.com/photo-1534422298391-e4f8c172dddb?auto=format&fit=crop&w=400&q=80"
                ),
                MenuItem(
                    id = "m3",
                    name = "Salmorejo cordobés tradicional",
                    description = "Emulsión sedosa de tomates de huerta, virutas de jamón y huevo campero.",
                    price = "7,80 €",
                    tag = "Típico Andaluz",
                    imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=400&q=80"
                )
            ),
            reviews = listOf(
                Review(
                    id = "r1",
                    userName = "Elena Ramos",
                    userRole = "Guía Local • Nivel 6",
                    rating = 5.0,
                    comment = "El pulpo es insuperable y la terraza tiene esa magia que solo encuentras en los rincones auténticos del sur. ¡No olviden pedir el vino de la casa!",
                    avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=200&q=80"
                ),
                Review(
                    id = "r2",
                    userName = "Marcos Valenzuela",
                    userRole = "Viajero Gastronómico",
                    rating = 5.0,
                    comment = "Pedí el salmorejo y las croquetas gracias a la recomendación de la app. El 10% de descuento funcionó de maravilla al pagar.",
                    avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=200&q=80"
                )
            )
        ),
        Restaurant(
            id = "2",
            name = "Café Botánico & Brunch",
            tagline = "Café de especialidad y repostería artesanal",
            description = "Café de finca seleccionada • Tartas caseras • Opciones Veganas",
            rating = 4.7,
            reviewCount = 189,
            priceLevel = "€€",
            distance = "850 m",
            location = LatLng(37.3895, -5.9920),
            address = "Calle Sierpes 22, Centro",
            hours = "Abierto hoy 08:30 - 20:00",
            isOpen = true,
            matchPercentage = 94,
            tags = listOf("Orgánico", "Pet friendly", "Brunch"),
            imageUrl = "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?auto=format&fit=crop&w=800&q=80",
            galleryImages = listOf("https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?auto=format&fit=crop&w=800&q=80"),
            discountOffer = null,
            couponCode = null
        ),
        Restaurant(
            id = "3",
            name = "Mirador & Bodega San Miguel",
            tagline = "Vistas panorámicas de la ciudad y vinos locales",
            description = "Terraza en azotea • Coctelería de autor • Vinos con D.O.",
            rating = 4.8,
            reviewCount = 512,
            priceLevel = "€€€",
            distance = "1,2 km",
            location = LatLng(37.3850, -5.9980),
            address = "Plaza del Salvador 8",
            hours = "Abierto hoy 17:00 - 02:00",
            isOpen = true,
            matchPercentage = 96,
            tags = listOf("Rooftop", "Cata de Vinos", "Vistas"),
            imageUrl = "https://images.unsplash.com/photo-1511018556340-d16986a1c194?auto=format&fit=crop&w=800&q=80",
            galleryImages = listOf("https://images.unsplash.com/photo-1511018556340-d16986a1c194?auto=format&fit=crop&w=800&q=80"),
            discountOffer = "Copita de bienvenida",
            couponCode = "MIRADOR-FREE"
        )
    )

    val defaultCoupon = Coupon(
        id = "c1",
        restaurantId = "1",
        restaurantName = "La Taberna del Califa",
        address = "Plaza de los Naranjos, 4",
        distance = "A 180 m de ti",
        rating = 4.9,
        reviewCount = 428,
        discountTitle = "10% DE DESCUENTO",
        discountSubtitle = "Válido en el total de tu consumo de alimentos y bebidas",
        code = "SABOR-MED-10",
        validUntil = "Válido hoy hasta las 23:59",
        imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80"
    )
}
