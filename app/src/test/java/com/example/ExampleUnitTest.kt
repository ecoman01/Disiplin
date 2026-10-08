package com.example

import com.example.data.*
import com.example.ui.theme.AppTheme
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun levelStartsFromZero() {
    val level0 = DisciplineLevelEngine.getLevelInfo(0)
    assertEquals(0, level0.level)
    assertEquals("Acemi Başlangıç", level0.title)
    assertEquals(0, level0.totalXp)
  }

  @Test
  fun themesIncludeNeonAndMonochrome() {
    val themes = AppTheme.entries
    assertTrue(themes.contains(AppTheme.NEON))
    assertTrue(themes.contains(AppTheme.MONOCHROME))
    assertTrue(themes.contains(AppTheme.TACTICAL))
    assertTrue(themes.contains(AppTheme.BLOODLINE))

    assertEquals("neon", AppTheme.NEON.id)
    assertEquals("monochrome", AppTheme.MONOCHROME.id)
  }

  @Test
  fun nutritionEngineHasHealthyDessertsAndRecipes() {
    val recipes = NutritionEngine.recipes
    assertTrue(recipes.isNotEmpty())
    val dessertRecipes = NutritionEngine.getRecipesByCategory("Sağlıklı Tatlılar")
    assertTrue(dessertRecipes.isNotEmpty())
    assertTrue(dessertRecipes.any { it.title.contains("Sufle") || it.title.contains("Mousse") || it.title.contains("Toplar") })
    assertTrue(NutritionEngine.disciplineTips.isNotEmpty())
  }

  @Test
  fun professionCoachProvidesTailoredAdvice() {
    val officeAdvice = ProfessionCoachEngine.getAdviceForProfession("Masa Başı & Ofis / Yazılımcı")
    assertEquals("Masa Başı & Ofis / Yazılımcı", officeAdvice.professionTitle)
    assertTrue(officeAdvice.quickDeskExercise.isNotEmpty())
    assertTrue(officeAdvice.postureAndBodyAdvice.isNotEmpty())

    val standingAdvice = ProfessionCoachEngine.getAdviceForProfession("Ayakta / Saha / Fiziksel İş")
    assertEquals("Ayakta / Saha / Fiziksel İş", standingAdvice.professionTitle)

    val shiftAdvice = ProfessionCoachEngine.getAdviceForProfession("Vardiyalı / Gece Çalışanı")
    assertEquals("Vardiyalı / Gece Çalışanı", shiftAdvice.professionTitle)
  }
}


