package com.tuwaiq.value.fitnessCalculator.models

data class MacrosCount(
    val balanced: Balanced,
    val calorie: Int,
    val highprotein: Highprotein,
    val lowcarbs: Lowcarbs,
    val lowfat: Lowfat
)