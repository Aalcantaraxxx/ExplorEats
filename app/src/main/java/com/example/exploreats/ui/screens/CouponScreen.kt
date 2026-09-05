package com.example.exploreats.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.exploreats.data.Coupon
import com.example.exploreats.data.UserProfile
import com.example.exploreats.ui.components.UserAvatar
import com.example.exploreats.ui.theme.*

@Composable
fun CouponsScreen(
    claimedCoupons: List<Coupon>,
    userProfile: UserProfile,
    onSelectCoupon: (Coupon) -> Unit,
    onNavigateTab: (String) -> Unit,
    currentTab: String = "Cupones"
) {
    Scaffold(
        bottomBar = {
            ExplorEatsBottomNavigation(
                currentTab = currentTab,
                onTabSelected = onNavigateTab
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(WarmBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = TerracottaPrimary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = null,
                                    tint = TerracottaPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "RUTA & SABOR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TerracottaPrimary,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "Mis Cupones",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }

                    UserAvatar(
                        initials = userProfile.getInitials(),
                        size = 38.dp,
                        onClick = { onNavigateTab("Perfil") }
                    )
                }

                if (claimedCoupons.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(80.dp),
                                color = TerracottaPrimary.copy(alpha = 0.15f),
                                shape = CircleShape
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.ConfirmationNumber,
                                        contentDescription = null,
                                        tint = TerracottaPrimary,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }

                            Text(
                                text = "Aún no tienes cupones canjeados",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Explora restaurantes y reclama tus ofertas exclusivas para ver tus cupones activos aquí.",
                                fontSize = 14.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )

                            Button(
                                onClick = { onNavigateTab("Explorar") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TerracottaPrimary
                                ),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Descubrir Ofertas",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(claimedCoupons) { coupon ->
                            CouponListItem(
                                coupon = coupon,
                                onClick = { onSelectCoupon(coupon) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CouponListItem(
    coupon: Coupon,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = CardBackground,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = coupon.imageUrl,
                contentDescription = coupon.restaurantName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = coupon.restaurantName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Surface(
                        color = MintGreenLight,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "ACTIVO",
                            color = MintGreenDark,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = coupon.discountTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TerracottaPrimary
                )

                Text(
                    text = "Código: ${coupon.code}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun CouponDetailScreen(
    coupon: Coupon,
    userProfile: UserProfile,
    onBackToCoupons: () -> Unit,
    onNavigateTab: (String) -> Unit,
    currentTab: String = "Cupones"
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Surface(
                color = WarmBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackToCoupons) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextPrimary
                        )
                    }

                    Text(
                        text = "Detalle del Cupón",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    UserAvatar(
                        initials = userProfile.getInitials(),
                        size = 36.dp
                    )
                }
            }
        },
        bottomBar = {
            ExplorEatsBottomNavigation(
                currentTab = currentTab,
                onTabSelected = onNavigateTab
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(WarmBackground)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Unlocked Banner
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Gift Icon Circle Badge
                        Box(contentAlignment = Alignment.TopEnd) {
                            Surface(
                                modifier = Modifier.size(80.dp),
                                color = TerracottaPrimary.copy(alpha = 0.2f),
                                shape = CircleShape
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.CardGiftcard,
                                        contentDescription = null,
                                        tint = TerracottaPrimary,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier.size(24.dp),
                                color = MintGreenDark,
                                shape = CircleShape
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Surface(
                            color = CardBackground,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = BadgeOrange,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "BENEFICIO EXCLUSIVO DESBLOQUEADO",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TerracottaDark,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Text(
                            text = "¡Todo listo! Que disfrutes tu visita",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Gracias por explorar con nosotros. Esperamos que tu experiencia en ${coupon.restaurantName} sea inolvidable y llena de buen sabor.",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }

                // Main Coupon Ticket Card
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp),
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Coupon Top Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = TerracottaPrimary.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Restaurant,
                                                contentDescription = null,
                                                tint = TerracottaPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = coupon.restaurantName,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = null,
                                                tint = MintGreenDark,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Sello Ruta & Sabor",
                                                fontSize = 12.sp,
                                                color = TextMuted
                                            )
                                        }
                                    }
                                }

                                Surface(
                                    color = MintGreenLight,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "ACTIVO",
                                        color = MintGreenDark,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            // Big Discount Title
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = coupon.discountTitle,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TerracottaPrimary,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = coupon.discountSubtitle,
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }

                            // Dotted Line
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                            ) {
                                drawLine(
                                    color = BorderColor,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                )
                            }

                            // QR Code Stylized Box
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(190.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    border = BorderStroke(1.dp, BorderColor),
                                    color = Color.White
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        QrCodeCanvas()
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.FilterCenterFocus,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Toca para ampliar brillo",
                                        fontSize = 12.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            // Code for Waiter Copy Box
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = CardBackground,
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "CÓDIGO PARA EL CAMARERO",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMuted,
                                        letterSpacing = 0.5.sp
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = coupon.code,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TerracottaDark,
                                            letterSpacing = 2.sp
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Surface(
                                            color = Color.White,
                                            shape = CircleShape,
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clickable {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    val clip = ClipData.newPlainText("Código Cupón", coupon.code)
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "¡Código copiado!", Toast.LENGTH_SHORT).show()
                                                }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy Code",
                                                    tint = TerracottaPrimary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Time Badge
                            Surface(
                                color = Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = TerracottaPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = coupon.validUntil,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TerracottaPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                // Action Buttons
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                val uri = Uri.parse("google.navigation:q=${coupon.restaurantName}")
                                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                    setPackage("com.google.android.apps.maps")
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TerracottaPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cómo llegar al restaurante (Google Maps)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        TextButton(onClick = onBackToCoupons) {
                            Text(
                                text = "Volver a la lista de cupones",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TerracottaPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QrCodeCanvas() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val sizeCell = w / 12f

        drawRect(Color.Black, topLeft = Offset(0f, 0f), size = Size(sizeCell * 3.5f, sizeCell * 3.5f))
        drawRect(Color.White, topLeft = Offset(sizeCell * 0.5f, sizeCell * 0.5f), size = Size(sizeCell * 2.5f, sizeCell * 2.5f))
        drawRect(Color.Black, topLeft = Offset(sizeCell * 1f, sizeCell * 1f), size = Size(sizeCell * 1.5f, sizeCell * 1.5f))

        drawRect(Color.Black, topLeft = Offset(w - sizeCell * 3.5f, 0f), size = Size(sizeCell * 3.5f, sizeCell * 3.5f))
        drawRect(Color.White, topLeft = Offset(w - sizeCell * 3f, sizeCell * 0.5f), size = Size(sizeCell * 2.5f, sizeCell * 2.5f))
        drawRect(Color.Black, topLeft = Offset(w - sizeCell * 2.5f, sizeCell * 1f), size = Size(sizeCell * 1.5f, sizeCell * 1.5f))

        drawRect(Color.Black, topLeft = Offset(0f, h - sizeCell * 3.5f), size = Size(sizeCell * 3.5f, sizeCell * 3.5f))
        drawRect(Color.White, topLeft = Offset(sizeCell * 0.5f, h - sizeCell * 3f), size = Size(sizeCell * 2.5f, sizeCell * 2.5f))
        drawRect(Color.Black, topLeft = Offset(sizeCell * 1f, h - sizeCell * 2.5f), size = Size(sizeCell * 1.5f, sizeCell * 1.5f))

        for (i in 0..10) {
            for (j in 0..10) {
                if ((i + j * 3) % 2 == 0 && (i > 3 || j > 3) && (i < 8 || j < 8)) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(i * sizeCell, j * sizeCell),
                        size = Size(sizeCell * 0.8f, sizeCell * 0.8f)
                    )
                }
            }
        }

        drawCircle(
            color = TerracottaPrimary,
            radius = sizeCell * 1.2f,
            center = Offset(w / 2f, h / 2f)
        )
    }
}
