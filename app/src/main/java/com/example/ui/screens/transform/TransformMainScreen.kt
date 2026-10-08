package com.example.ui.screens.transform

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.billing.BillingManager
import com.example.billing.GooglePlayBillingSheet
import com.example.data.TransformRepository
import kotlinx.coroutines.launch

@Composable
fun TransformMainScreen(
    repository: TransformRepository,
    userName: String = "Savaşçı",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val isUnlocked by BillingManager.isTransformUnlocked.collectAsState()
    var showBillingSheet by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = isUnlocked,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "transform_unlock_transition"
        ) { unlocked ->
            if (unlocked) {
                TransformDashboardView(
                    repository = repository,
                    userName = userName
                )
            } else {
                TransformPaywallView(
                    onOpenGooglePlayBilling = { showBillingSheet = true },
                    onRestorePurchases = {
                        val restored = BillingManager.restorePurchases(context)
                        if (restored) {
                            Toast.makeText(context, "Google Play satın alımı doğrulandı! TRANSFORM açıldı.", Toast.LENGTH_LONG).show()
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Bu Google Play hesabına ait kayıtlı TRANSFORM lisansı bulunamadı.")
                            }
                        }
                    }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // GOOGLE PLAY IN-APP BILLING SHEET
        if (showBillingSheet) {
            GooglePlayBillingSheet(
                onDismiss = { showBillingSheet = false },
                onPurchaseSuccess = {
                    showBillingSheet = false
                    Toast.makeText(context, "🎉 Tebrikler! TRANSFORM 90 Günlük Sistem kilidi açıldı!", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}
