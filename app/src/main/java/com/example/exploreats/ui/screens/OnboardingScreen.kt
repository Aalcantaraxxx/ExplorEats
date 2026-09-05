package com.example.exploreats.ui.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exploreats.data.SampleData
import com.example.exploreats.ui.theme.*
import com.example.exploreats.utils.LocationHelper
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    currentCity: String,
    currentRegion: String,
    onLocationDetected: (cityName: String, regionName: String, latLng: LatLng) -> Unit,
    onTouristToggle: (Boolean) -> Unit,
    onStartExploring: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isTouristSelected by remember { mutableStateOf(true) }
    var selectedCategories by remember {
        mutableStateOf(SampleData.interestCategories.filter { it.selected }.map { it.id }.toSet())
    }

    var isDetectingLocation by remember { mutableStateOf(false) }
    var displayedCityName by remember { mutableStateOf(currentCity) }
    var displayedRegionName by remember { mutableStateOf(currentRegion) }

    // Real Permission Launcher for device location
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (granted) {
            isDetectingLocation = true
            coroutineScope.launch {
                val result = LocationHelper.getCurrentLocation(context)
                isDetectingLocation = false
                if (result != null) {
                    displayedCityName = result.cityName
                    displayedRegionName = result.regionName
                    onLocationDetected(result.cityName, result.regionName, result.latLng)
                    Toast.makeText(context, "📍 Ubicación detectada: ${result.cityName}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No se pudo obtener la ubicación precisa en este momento.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado. Se usará la ubicación por defecto.", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 48.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Top Step Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Progress Indicator Bar
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(TerracottaPrimary)
                        )
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                    }

                    // Step Badge
                    Surface(
                        color = MintGreenLight,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Paso 1 de 3",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = MintGreenDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Title & Subtitle
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RestaurantMenu,
                            contentDescription = null,
                            tint = TerracottaPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RUTA & SABOR",
                            color = TerracottaPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Text(
                        text = "¡Hola, viajero! Personaliza tu experiencia",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        lineHeight = 32.sp
                    )

                    Text(
                        text = "Elige qué te apasiona explorar para recomendarte los mejores rincones y sabores.",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }

            // TU PERSPECTIVA CULINARIA - Toggle Switch
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "TU PERSPECTIVA CULINARIA",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.8.sp
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        color = CardBackground,
                        shape = RoundedCornerShape(26.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        ) {
                            // Soy Turista option
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(if (isTouristSelected) TerracottaPrimary else Color.Transparent)
                                    .clickable {
                                        isTouristSelected = true
                                        onTouristToggle(true)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.FlightTakeoff,
                                        contentDescription = null,
                                        tint = if (isTouristSelected) Color.White else TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Soy Turista",
                                        color = if (isTouristSelected) Color.White else TextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            // Soy Local option
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(if (!isTouristSelected) TerracottaPrimary else Color.Transparent)
                                    .clickable {
                                        isTouristSelected = false
                                        onTouristToggle(false)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        tint = if (!isTouristSelected) Color.White else TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Soy Local",
                                        color = if (!isTouristSelected) Color.White else TextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Location Box & Google Maps detection button
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CardBackground,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TU CIUDAD ACTUAL O DESTINO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                letterSpacing = 0.5.sp
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(MintGreenDark)
                            )
                        }

                        // Selected City Card
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, BorderColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(TerracottaPrimary.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = TerracottaPrimary
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = displayedCityName,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = displayedRegionName,
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = null,
                                    tint = TerracottaPrimary
                                )
                            }
                        }

                        // Detect location button with Google Maps
                        Button(
                            onClick = {
                                if (LocationHelper.hasLocationPermissions(context)) {
                                    isDetectingLocation = true
                                    coroutineScope.launch {
                                        val result = LocationHelper.getCurrentLocation(context)
                                        isDetectingLocation = false
                                        if (result != null) {
                                            displayedCityName = result.cityName
                                            displayedRegionName = result.regionName
                                            onLocationDetected(result.cityName, result.regionName, result.latLng)
                                            Toast.makeText(context, "📍 Ubicación obtenida: ${result.cityName}", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "No se pudo obtener la ubicación GPS actual.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MintGreen,
                                contentColor = MintGreenDark
                            ),
                            shape = RoundedCornerShape(16.dp),
                            enabled = !isDetectingLocation
                        ) {
                            if (isDetectingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MintGreenDark,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Obteniendo GPS...", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Detectar ubicación con Google Maps",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Interest Section Header
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿Qué te abre el apetito?",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Surface(
                            color = MintGreenLight,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Personalizable",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                color = MintGreenDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Text(
                        text = "Toca las etiquetas que despiertan tu curiosidad. Adaptaremos las cartas y rutas a tus gustos.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }

            // Primary Button: "Comenzar a explorar (X seleccionados)"
            item {
                Button(
                    onClick = onStartExploring,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TerracottaPrimary
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Comenzar a explorar (${selectedCategories.size} seleccionados)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }

            // Interest Tags List
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Puedes ajustar estos filtros en cualquier momento desde tu perfil",
                        fontSize = 12.sp,
                        color = TextMuted
                    )

                    SampleData.interestCategories.forEach { category ->
                        val isSelected = selectedCategories.contains(category.id)
                        InterestTagChip(
                            title = category.title,
                            icon = getIconForCategory(category.iconName),
                            isSelected = isSelected,
                            onToggle = {
                                selectedCategories = if (isSelected) {
                                    selectedCategories - category.id
                                } else {
                                    selectedCategories + category.id
                                }
                            }
                        )
                    }
                }
            }

            // Bottom Sabor del Día Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CardBackground,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(60.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = TerracottaPrimary.copy(alpha = 0.2f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = null,
                                    tint = TerracottaPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = MintGreenDark,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "SABOR DEL DÍA",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MintGreenDark,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Text(
                                text = "Mercado de San Miguel",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Text(
                                text = "A 1.4 km · Ruta recomendada para ti",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InterestTagChip(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        color = if (isSelected) TerracottaPrimary else Color.White,
        shape = RoundedCornerShape(20.dp),
        border = if (isSelected) null else BorderStroke(1.dp, BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else TerracottaPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                color = if (isSelected) Color.White else TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

private fun getIconForCategory(iconName: String): ImageVector {
    return when (iconName) {
        "star" -> Icons.Default.Stars
        "sunset" -> Icons.Default.WbSunny
        "museum" -> Icons.Default.AccountBalance
        "cocktail" -> Icons.Default.LocalBar
        "store" -> Icons.Default.Storefront
        "nature" -> Icons.Default.Park
        "tapas" -> Icons.Default.Restaurant
        else -> Icons.Default.LocalCafe
    }
}
