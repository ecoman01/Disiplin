package com.example.billing

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val GooglePlayGreen = Color(0xFF01875F)
private val GooglePlayDarkBg = Color(0xFF202124)
private val GooglePlayCardBg = Color(0xFF303134)
private val GooglePlayTextSec = Color(0xFF9AA0A6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GooglePlayBillingSheet(
    onDismiss: () -> Unit,
    onPurchaseSuccess: () -> Unit,
    userEmail: String = "baysalemr01@gmail.com",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var purchaseState by remember { mutableStateOf<PaymentStep>(PaymentStep.Ready) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = {
            if (purchaseState !is PaymentStep.Processing) {
                onDismiss()
            }
        },
        sheetState = sheetState,
        containerColor = GooglePlayDarkBg,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF5F6368))
            )
        },
        modifier = modifier.testTag("google_play_billing_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // GOOGLE PLAY HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Google Play Tri-color Bag / Icon representation
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF01875F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Google Play",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "Google Play",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.2.sp
                        )
                    )
                }

                // Account chip
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = GooglePlayCardBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4285F4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userEmail.take(1).uppercase(),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = userEmail,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GooglePlayTextSec,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Divider(color = Color(0xFF3C4043), thickness = 0.8.dp)

            when (val state = purchaseState) {
                PaymentStep.Ready -> {
                    // PRODUCT DETAILS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = BillingManager.PRODUCT_TITLE,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Kişisel Hayat Analizi • AI Koç • 90 Günlük Adaptif Program • Gelişmiş İstatistikler • Challenge'lar",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GooglePlayTextSec,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = BillingManager.PRODUCT_PRICE,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Tek seferlik",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GooglePlayTextSec
                                )
                            )
                        }
                    }

                    // PAYMENT METHOD SELECTION CARD
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = GooglePlayCardBg),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CreditCard,
                                    contentDescription = null,
                                    tint = Color(0xFF8AB4F8),
                                    modifier = Modifier.size(24.dp)
                                )
                                Column {
                                    Text(
                                        text = "Google Pay •••• 4242",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    )
                                    Text(
                                        text = "Varsayılan ödeme yöntemi",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = GooglePlayTextSec
                                        )
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = null,
                                tint = GooglePlayTextSec
                            )
                        }
                    }

                    // SECURITY / PRIVACY ASSURANCE
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Color(0xFF34A853),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Ödeme doğrudan Google Play güvencesindedir. Kart bilgileri uygulama geliştiricisiyle paylaşılmaz.",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GooglePlayTextSec,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        )
                    }

                    // PURCHASE BUTTON
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                purchaseState = PaymentStep.Processing
                                delay(1200) // Realistic Google Play token exchange
                                val details = BillingManager.confirmGooglePlayPurchase(context, userEmail)
                                purchaseState = PaymentStep.Success(details.orderId)
                                delay(1300)
                                onPurchaseSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("google_play_confirm_buy_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GooglePlayGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text(
                            text = "1 Dokunuşla Satın Al – ${BillingManager.PRODUCT_PRICE}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp
                            )
                        )
                    }
                }

                PaymentStep.Processing -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = GooglePlayGreen,
                            strokeWidth = 4.dp
                        )
                        Text(
                            text = "Google Play ile ödeme işleniyor...",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Lütfen bekleyin, işlem Google sunucularında doğrulanıyor.",
                            style = MaterialTheme.typography.bodySmall.copy(color = GooglePlayTextSec)
                        )
                    }
                }

                is PaymentStep.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(GooglePlayGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Başarılı",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Text(
                            text = "Ödeme Başarılı!",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )

                        Text(
                            text = "Google Play siparişiniz onaylandı.\nTRANSFORM (90 Günlük Sistem) kilidi açıldı!",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = GooglePlayTextSec,
                                lineHeight = 20.sp
                            )
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GooglePlayCardBg
                        ) {
                            Text(
                                text = "Sipariş No: ${state.orderId}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GooglePlayTextSec,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class PaymentStep {
    object Ready : PaymentStep()
    object Processing : PaymentStep()
    data class Success(val orderId: String) : PaymentStep()
}
