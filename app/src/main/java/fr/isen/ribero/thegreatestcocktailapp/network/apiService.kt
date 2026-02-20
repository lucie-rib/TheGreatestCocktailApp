package fr.isen.ribero.thegreatestcocktailapp.network

import fr.isen.ribero.thegreatestcocktailapp.dataClasses.CategoryListResponse
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.DrinkFilterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
    fun getRandomCocktail(): retrofit2.Call<CocktailResponse> //but manage errors so this one !

    //@GET("random.php")
    //suspend fun getRandomCocktail(): CocktailResponse //easier way

    @GET("list.php?c=list")
    fun getCategories(): retrofit2.Call<CategoryListResponse>

    @GET("filter.php?")
    fun getDrinksPreview(@Query(value="c")categoryID: String): retrofit2.Call<DrinkFilterResponse>

    @GET("lookup.php?")
    fun getDetailCocktail(@Query("i") drinkID: String): retrofit2.Call<CocktailResponse>
}