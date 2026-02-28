package fr.isen.ribero.thegreatestcocktailapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import fr.isen.ribero.thegreatestcocktailapp.R
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.ribero.thegreatestcocktailapp.managers.FavoritesManager
import fr.isen.ribero.thegreatestcocktailapp.models.AppBarState
import fr.isen.ribero.thegreatestcocktailapp.models.Category
import fr.isen.ribero.thegreatestcocktailapp.network.ApiClient
import retrofit2.Call
import retrofit2.Response

@Composable
fun RandomCocktailScreen(modifier: Modifier, onComposing: (AppBarState) -> Unit) {
    var drink = remember { mutableStateOf<Drink?>(null) }

    LaunchedEffect(Unit) {
        onComposing (
            AppBarState("Random Cocktail",
                actions = { DetailCocktailTopButton(drink.value) })
        )
        val call = ApiClient.retrofit.getRandomCocktail()
        call.enqueue(object : retrofit2.Callback<CocktailResponse> {
            override fun onResponse(
                call: Call<CocktailResponse?>?,
                response: Response<CocktailResponse?>?
            ) {
                drink.value = response?.body()?.drinks?.first()
            }
            override fun onFailure(
                call: Call<CocktailResponse?>?,
                t: Throwable?
            ) {
                Log.e("request", "getrandom failed ${t?.message}")
            }
        })
    }

    drink.value?.let { currentDrink ->
        DetailCocktailScreen(modifier, currentDrink)
    } ?: run {
        Text("Loading")
    }
}

@Composable
fun DetailCocktailScreen(drinkId: String,
                         onComposing: (AppBarState) -> Unit,
                         modifier: Modifier) {
    var drink = remember { mutableStateOf<Drink?>(null) }

    LaunchedEffect(Unit) {

        onComposing (
            AppBarState("Random Cocktail",
                actions = { DetailCocktailTopButton(drink.value) })
        )
        val call = ApiClient.retrofit.getDetailCocktail(drinkId)
        call.enqueue(object : retrofit2.Callback<CocktailResponse> {
            override fun onResponse(
                call: Call<CocktailResponse?>?,
                response: Response<CocktailResponse?>?
            ) {
                drink.value = response?.body()?.drinks?.first()
            }
            override fun onFailure(
                call: Call<CocktailResponse?>?,
                t: Throwable?
            ) {
                Log.e("request", "getrandom failed ${t?.message}")
            }
        })
    }

    drink.value?.let { drink ->
        DetailCocktailScreen(modifier, drink)
    } ?: run {
        Text("Loading")
    }
}

@Composable
fun DetailCocktailScreen(modifier: Modifier, drink: Drink) {
    Box(
        Modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        colorResource(R.color.purple_500),
                        colorResource(R.color.purple_700)
                    )
                )
            )
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                model = drink.strDrinkThumb,
                contentDescription = drink.strDrink,
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .clip(CircleShape)
                    .border(
                        1.dp,
                        colorResource(R.color.teal_200),
                        CircleShape
                    )
            )

            Text(
                drink.strDrink ?: "",
                fontSize = 40.sp,
                color = colorResource(R.color.white)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                drink.strCategory?.let { InfoBadge(it) }
                drink.strAlcoholic?.let { InfoBadge(it) }
            }

            Text(
                drink.strGlass ?: "",
                color = colorResource(R.color.grey)
            )

            Card {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.ingredient),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    val ingredients = drink.ingredientList()
                    if (ingredients.isEmpty()) {
                        Text("Aucun ingrédient spécifié.")
                    } else {
                        ingredients.forEach { (ingredient, measure) ->
                            val textToDisplay = if (measure.isNotBlank()) "$measure $ingredient" else ingredient
                            Text(textToDisplay)
                        }
                    }
                }
            }

            Card {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.preparation),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(drink.strInstructions ?: "")
                }
            }
        }
    }
}

@Composable
fun InfoBadge(text: String) {
    Box(
        Modifier
            .clip(CircleShape)
            .background(
                Brush.horizontalGradient(
                    listOf(colorResource(R.color.teal_200), colorResource(R.color.teal_700))
                )
            )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = colorResource(R.color.white),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun DetailCocktailTopButton(drink: Drink?) {
    val context = LocalContext.current
    val favoritesManager = FavoritesManager()
    var isFavorite by remember(drink?.idDrink) {
        mutableStateOf(drink?.let { favoritesManager.isFavorite(it, context) } ?: false)
    }

    drink?.let { drink ->
        IconButton({
            favoritesManager.toggleFavorite(drink, context)
            isFavorite = favoritesManager.isFavorite(drink, context)
        }) {
            Icon(
                imageVector = if (isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Filled.FavoriteBorder
                },
                contentDescription = "Localized description"
            )
        }
    }
}

@Composable
fun CategoryView(category: Category) {
    Box(Modifier
        .clip(CircleShape)
        .background(
            Brush.horizontalGradient(
                Category.colors(category)
            )
        )
    ) {
        Text(
            Category.toString(category),
            fontSize = 20.sp,
            color = colorResource(R.color.white),
            modifier = Modifier.padding(8.dp)
        )
    }
}