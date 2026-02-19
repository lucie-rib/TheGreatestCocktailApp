package fr.isen.ribero.thegreatestcocktailapp.dataClasses

import fr.isen.ribero.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.DrinkPreview
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.DrinkCategory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // 1. Random : random.php
    @GET("random.php")
    suspend fun getRandomCocktail(): Drink

    // 2. List of categories : list.php?c=list
    @GET("list.php?c=list")
    suspend fun getCategories(): DrinkCategory

    // 3. List of drinks for a category : filter.php?c=YOUR_CATEGORY
    @GET("filter.php?c=YOUR_CATEGORY")
    suspend fun getDrinksByCategory(@Query("c") category: String): DrinkPreview

    // 4. Detail of a drink : lookup.php?i=YOUR_DRINK_IDENTIFIER
    @GET("lookup.php?i=YOUR_DRINK_IDENTIFIER")
    suspend fun getDrinkDetail(@Query("i") id: String): CocktailResponse
}