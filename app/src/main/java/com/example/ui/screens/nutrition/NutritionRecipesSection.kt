package com.example.ui.screens.nutrition

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NutritionEngine
import com.example.data.NutritionRecipe
import com.example.ui.theme.*
import com.example.util.ShareHelper

@Composable
fun NutritionRecipesSection(
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var selectedCategory by remember { mutableStateOf("Tümü") }
    var searchQuery by remember { mutableStateOf("") }

    val allCategories = remember {
        listOf(
            "Tümü",
            "Sağlıklı Tatlılar",
            "Yüksek Protein",
            "Pratik & Hızlı",
            "Detoks & İçecek"
        )
    }

    val filteredRecipes = remember(selectedCategory, searchQuery) {
        val baseList = if (selectedCategory == "Tümü") {
            NutritionEngine.recipes
        } else {
            NutritionEngine.recipes.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }

        if (searchQuery.isBlank()) {
            baseList
        } else {
            val q = searchQuery.trim().lowercase()
            baseList.filter { recipe ->
                recipe.title.lowercase().contains(q) ||
                        recipe.description.lowercase().contains(q) ||
                        recipe.ingredients.any { it.lowercase().contains(q) }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Sticky Header / Top Bar
        Surface(
            color = DarkSurfaceElevated,
            border = BorderStroke(0.5.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TacticalGreen.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, TacticalGreen.copy(alpha = 0.35f)),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "🥗", fontSize = 20.sp)
                            }
                        }
                        Column {
                            Text(
                                text = "FIT MUTFAK & TARİFLER",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "Şekersiz Tatlılar & Yüksek Protein",
                                style = MaterialTheme.typography.labelSmall.copy(color = DisciplineGreen)
                            )
                        }
                    }

                    if (onClose != null) {
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Kapat",
                                tint = TextSecondary
                            )
                        }
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Tarif, malzeme ara (örn: muz, yulaf, sufle)...",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Ara",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Temizle",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant,
                        focusedBorderColor = TacticalGreen,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("recipe_search_input")
                )

                // Category Filter Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allCategories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        val icon = when (cat) {
                            "Sağlıklı Tatlılar" -> "🍫 "
                            "Yüksek Protein" -> "🍗 "
                            "Pratik & Hızlı" -> "⚡ "
                            "Detoks & İçecek" -> "🥬 "
                            else -> "✨ "
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) TacticalGreenContainer else DarkSurfaceVariant,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) TacticalGreen else DarkBorder
                            ),
                            modifier = Modifier
                                .clickable { selectedCategory = cat }
                                .testTag("recipe_cat_${cat.lowercase().replace(" ", "_")}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = icon + cat,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) TacticalGreenBright else TextSecondary,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Recipes List
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Inspirational Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, TacticalGreen.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "🧁", fontSize = 28.sp)
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = "“Diyet Bir Ceza Değil, Kendine Olan Saygındır.”",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Tatlı krizini hedeflerinden sapmadan atlat. Tüm tarifler rafine şekersiz ve yüksek besleyicidir.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                        )
                    }
                }
            }

            if (filteredRecipes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🔍", fontSize = 36.sp)
                        Text(
                            text = "Aradığın kriterde tarif bulunamadı",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                        Button(
                            onClick = {
                                searchQuery = ""
                                selectedCategory = "Tümü"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant)
                        ) {
                            Text("Filtreleri Sıfırla", color = TacticalGreen)
                        }
                    }
                }
            } else {
                Text(
                    text = "${filteredRecipes.size} TARİF LİSTELENDİ",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = TextSecondary,
                        letterSpacing = 0.5.sp
                    )
                )

                filteredRecipes.forEachIndexed { index, recipe ->
                    StructuredRecipeCard(
                        recipe = recipe,
                        defaultExpanded = index == 0,
                        onShare = {
                            val shareText = """
                                🥗 ${recipe.title} (${recipe.category})
                                ⏱️ Süre: ${recipe.prepMinutes} dk | 🔥 ${recipe.calories} kcal
                                💪 Protein: ${recipe.proteinG}g | 🌾 Karb: ${recipe.carbsG}g | 🥑 Yağ: ${recipe.fatG}g
                                
                                🛒 MALZEMELER:
                                ${recipe.ingredients.joinToString("\n") { "• $it" }}
                                
                                👨‍🍳 HAZIRLANIŞ:
                                ${recipe.instructions.mapIndexed { i, s -> "${i + 1}. $s" }.joinToString("\n")}
                                
                                💡 DİSİPLİN NOTU:
                                ${recipe.disciplineTip}
                            """.trimIndent()
                            ShareHelper.shareText(context, shareText, "Fit Tarif: ${recipe.title}")
                        },
                        onCopyIngredients = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Malzemeler", recipe.ingredients.joinToString("\n") { "• $it" })
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Malzemeler panoya kopyalandı!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StructuredRecipeCard(
    recipe: NutritionRecipe,
    defaultExpanded: Boolean = true,
    onShare: () -> Unit,
    onCopyIngredients: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(defaultExpanded) }
    // Interactive checkboxes for ingredients
    val checkedIngredients = remember { mutableStateMapOf<String, Boolean>() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .testTag("recipe_card_${recipe.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: Emoji, Title, Category and Time/Calorie Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(46.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = DarkSurfaceVariant,
                        border = BorderStroke(0.8.dp, DarkBorder)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = recipe.emoji, fontSize = 24.sp)
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = recipe.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimary,
                                fontSize = 16.sp
                            )
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = TacticalGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = recipe.category,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TacticalGreenBright,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "•  ${recipe.prepMinutes} dk",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Expand/Collapse Toggle Button
                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Daralt" else "Genişlet",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Description
            Text(
                text = recipe.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            )

            // STRUCTURED MACRO PILLARS (Protein, Carbs, Fat, Calories)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DarkSurfaceVariant,
                border = BorderStroke(0.8.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Protein
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${recipe.proteinG}g",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = DisciplineGreen
                            )
                        )
                        Text(
                            text = "Protein",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DisciplineGreen,
                                fontSize = 10.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(DarkBorder)
                    )

                    // Karb
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${recipe.carbsG}g",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = WarningOrange
                            )
                        )
                        Text(
                            text = "Karbonhidrat",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = WarningOrange,
                                fontSize = 10.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(DarkBorder)
                    )

                    // Yağ
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${recipe.fatG}g",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = TextSecondary
                            )
                        )
                        Text(
                            text = "Sağlıklı Yağ",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(DarkBorder)
                    )

                    // Kalori
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${recipe.calories}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "kcal",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(color = DarkBorder, thickness = 0.8.dp)

                    // 1. STRUCTURED INGREDIENTS WITH INTERACTIVE CHECKMARKS
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "🛒", fontSize = 16.sp)
                                Text(
                                    text = "MALZEMELER & ÖLÇÜLER",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TacticalGreenBright,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }

                            TextButton(
                                onClick = onCopyIngredients,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Kopyala",
                                    tint = TacticalGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Listeyi Al",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TacticalGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        recipe.ingredients.forEach { item ->
                            val isChecked = checkedIngredients[item] == true
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isChecked) TacticalGreenContainer.copy(alpha = 0.4f) else DarkSurfaceVariant,
                                border = BorderStroke(
                                    0.6.dp,
                                    if (isChecked) TacticalGreen.copy(alpha = 0.4f) else DarkBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { checkedIngredients[item] = !isChecked }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isChecked) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                                        contentDescription = null,
                                        tint = if (isChecked) DisciplineGreen else TextSecondary.copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = item,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = if (isChecked) TextPrimary else TextBody,
                                            fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // 2. STRUCTURED STEP-BY-STEP INSTRUCTIONS
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "👨‍🍳", fontSize = 16.sp)
                            Text(
                                text = "HAZIRLANIŞ ADIMLARI",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TacticalGreenBright,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }

                        recipe.instructions.forEachIndexed { idx, step ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = DarkSurfaceVariant,
                                border = BorderStroke(0.6.dp, DarkBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = TacticalGreenContainer,
                                        border = BorderStroke(0.8.dp, TacticalGreen),
                                        modifier = Modifier.size(22.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${idx + 1}",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = TacticalGreenBright,
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }
                                    }
                                    Text(
                                        text = step,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextPrimary,
                                            lineHeight = 19.sp
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    // 3. DISCIPLINE & FIT COACH TIP
                    if (recipe.disciplineTip.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TacticalGreen.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, TacticalGreen.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = "💡", fontSize = 18.sp)
                                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text(
                                        text = "FİT PÜF NOKTASI & METABOLİZMA",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = DisciplineGreen,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 0.5.sp
                                        )
                                    )
                                    Text(
                                        text = recipe.disciplineTip,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextBody,
                                            lineHeight = 17.sp,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // 4. ACTION BAR (Share recipe)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        FilledTonalButton(
                            onClick = onShare,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = DarkSurfaceVariant,
                                contentColor = TacticalGreen
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Paylaş",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tarifi Paylaş",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}
