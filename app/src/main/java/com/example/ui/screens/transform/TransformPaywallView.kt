package com.example.ui.screens.transform

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billing.BillingManager
import com.example.ui.theme.*

@Composable
fun TransformPaywallView(
    onOpenGooglePlayBilling: () -> Unit,
    onRestorePurchases: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .testTag("transform_paywall_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TOP HERO BADGE
        Surface(
            color = TacticalGreenBright.copy(alpha = 0.12f),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "🔒", fontSize = 14.sp)
                Text(
                    text = "PREMIUM DÖNÜŞÜM MODÜLÜ",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = TacticalGreenBright,
                        letterSpacing = 1.sp
                    )
                )
            }
        }

        // HERO TITLE & LOGO
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            TacticalGreenBright.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )
                .border(2.dp, TacticalGreenBright, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Kilitli TRANSFORM",
                tint = TacticalGreenBright,
                modifier = Modifier.size(36.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "🔒 TRANSFORM",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 1.sp
                )
            )
            Text(
                text = "90 günlük kişisel dönüşüm programı",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TacticalGreenBright
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Ücretsiz uygulama: Disiplinini takip et.\nTRANSFORM: Disiplinini baştan aşağı geliştir.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    lineHeight = 18.sp
                ),
                textAlign = TextAlign.Center
            )
        }

        // 5 CORE HIGHLIGHT CARDS (AS EXPLICITLY REQUESTED)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FeatureHighlightItem(
                icon = Icons.Default.Psychology,
                title = "🧠 Kişisel Hayat Analizi",
                desc = "Disiplin, erteleme, uyku, spor ve telefon kullanımı dahil 8 boyutlu derin kişisel profil ve problem tespiti."
            )
            FeatureHighlightItem(
                icon = Icons.Default.SmartToy,
                title = "🤖 Karar Veren AI Koç",
                desc = "Sadece konuşan değil, karar veren yapay zeka: 4 gün aksatırsan yoğunluğu düşürür, formundaysan zorlaştırır."
            )
            FeatureHighlightItem(
                icon = Icons.Default.Tune,
                title = "🎯 30 / 60 / 90 Günlük Adaptif Program",
                desc = "Her gün hedefinize özel görevler gelir ve performansınıza göre program otomatik olarak yeniden şekillenir."
            )
            FeatureHighlightItem(
                icon = Icons.Default.Insights,
                title = "📊 Gelişmiş İstatistikler",
                desc = "Disiplin skoru eğrisi, en başarılı günler, zayıf alışkanlıklar ve 'Geçen aya göre %18 daha istikrarlısın' analitiği."
            )
            FeatureHighlightItem(
                icon = Icons.Default.Assignment,
                title = "📝 Haftalık AI Raporları",
                desc = "Bu hafta tamamlanan görev oranı, en güçlü ve en zayıf alanların, gelecek hafta için özel stratejik odak noktan."
            )
            FeatureHighlightItem(
                icon = Icons.Default.FitnessCenter,
                title = "🏋️ Kişisel Spor & 🧨 Challenge Sistemi",
                desc = "Kas / Yağ yakma / Kondisyon hedefine özel antrenmanlar ve 7, 30, 90 günlük seviye rozetleri."
            )
        }

        // PRIMARY ACTION: "Premium'u Aç – 199,99 TL"
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onOpenGooglePlayBilling,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("open_premium_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TacticalGreenBright,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                    Text(
                        text = "Premium'u Aç – ${BillingManager.PRODUCT_PRICE}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            // Secondary: Restore Purchases
            TextButton(
                onClick = onRestorePurchases,
                modifier = Modifier.testTag("restore_purchases_btn")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Daha Önce Satın Aldıysan: Satın Alımları Geri Yükle",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // Test Modu: Transform'a Direkt Gir
            val context = LocalContext.current
            OutlinedButton(
                onClick = {
                    BillingManager.unlockForTesting(context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("test_unlock_transform_btn"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TacticalGreenBright),
                border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = null,
                        tint = TacticalGreenBright,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Test Modu: Transform'a Direkt Gir",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TacticalGreenBright
                        )
                    )
                }
            }
        }

        // GOOGLE PLAY SECURITY GUARANTEE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = Color(0xFF34A853),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Google Play Güvenli Ödeme Sistemi • Tek Seferlik Ömür Boyu Lisans",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary.copy(alpha = 0.8f),
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
private fun FeatureHighlightItem(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(TacticalGreenBright.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TacticalGreenBright,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                )
            }
        }
    }
}
